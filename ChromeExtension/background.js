// const SERVER_URL = "https://mhelper.ru/";
const SERVER_URL = "http://localhost:8080/";
const POST_PROCUREMENT_URL = SERVER_URL + "v1/chrome/";
const TEST_SERVER_URL = SERVER_URL + "v1/auth/test";
const CHROME_REGISTRATION = SERVER_URL + "v1/auth/code/";
const JWT_PREFIX = "Bearer ";
let connected = false;
let jwtToken = null;

let accessToken = null;
let refreshToken = null;

function sendCode(host) {
    return fetch(host, {
        method: 'GET'
    }).then((response) => {
        if (response.status !== 200) {
            return Promise.reject(new Error("Ошибка запроса."));
        }
        return response.json();
    }).then((data) => {
        return data;
    })
        .catch((rejected) => {
            return null;
        });
}

function saveTokens(data) {
    console.log('access ' + data.accessToken);
    accessToken = data.accessToken;
    refreshToken = data.refreshToken;
    saveAccessTokensToLocalStorage();
}

function setConnectedStatus() {
    console.log('Set connected status');
    connected = true;
}

/**
 * Обработчик события, пришедший с request.destination === "test_connection". тестирование доступности сервера
 * @param sendResponse
 * @returns {boolean}
 */
function sendConnectionHandler(sendResponse) {
    fetch(TEST_SERVER_URL, {
        method: 'GET',
        headers: createHeaders()
    })
        .then((response) => {
            console.log('test_connection ' + response);
            debugger;
            sendResponse(response.status);
        })
        .catch((rejected) => {
            sendResponse(rejected.status);
        });
    return true;
}

/**
 * Обработчик события destination === "send_code"   Посылает код, полученный в телеграмм, и сохраняет полученный токен.
 * @param request
 * @param sendResponse
 */
function sendCodeHandler(request, sendResponse) {
    let registrationCode = request.data;
    let host = CHROME_REGISTRATION + registrationCode;
    let result = sendCode(host);
    result.then((data) => {
        setConnectedStatus()
        saveTokens(data)
        if (connected) {
            console.log('connected and send response to popup');
            sendResponse({status: 'ok'});
        } else {
            console.log('not connected and send response to popup');
            sendResponse({staus: 'wrong code'})
        }
    });
}

/**
 * Обработчик события destination === "sender" . Посылает спарсенную закупку на сервер.
 * @param request
 * @param sendResponse
 * @returns {boolean}
 */
function senderHandler(request, sendResponse) {
    fetch(POST_PROCUREMENT_URL, {
        method: 'POST',
        headers: createHeaders(),
        body: JSON.stringify(request.data)
    }).then((response) => {
        if (!response.ok) {
            return Promise.reject(new Error(
                'Response failed: ' + response.status + ' (' + response.statusText + ')'
            ));
        }
        return response.json();
    }).then((data) => {
        connected = true;
        jwtToken = data.token;
        sendResponse(201);
    })
    return true;
}

/**
 * Обработчик события destination === "check_code"
 * @param sendResponse
 */
function checkCodeHandler(sendResponse) {
    if (jwtToken === null) {
        sendResponse(2);
    }
}

/**
 * Слушатель запросов на background.js
 */
chrome.runtime.onMessage.addListener(
    function (request, sender, sendResponse) {

        if (request.destination === "test_connection") {
            return sendConnectionHandler(sendResponse);
        }
        if (request.destination === "send_code") {
            sendCodeHandler(request, sendResponse);
        }
        if (request.destination === "sender") {
            return senderHandler(request, sendResponse);
        }
        if (request.destination === "check_code") {
            checkCodeHandler(sendResponse);
        }
    }
);

//----------------------------------------------------------------------------------------------


// --------------------------------- LocalStorage Api ----------------------
/**
 * Сохраняет токены в локальное хранилище
 */
function saveAccessTokensToLocalStorage() {
    chrome.storage.local.set({
        accessToken: accessToken,
        refreshToken: refreshToken
    }, function () {
        console.log("Saved access tokens to local storage.");
    });
}

/**
 * Восстанавливает токены из локального хранилища
 */
function restoreAccessTokensFromLocalStorage() {
    const accessTokenLocal = readLocalStorage('accessToken')
    const refreshTokenLocal = readLocalStorage('refreshToken')
    Promise.all([accessTokenLocal, refreshTokenLocal]).then(values => {
        accessToken = values[0] === null ? null : values[0];
        refreshToken = values[1] === null ? null : values[1];
        console.log("Restored access tokens from local storage.");
    });
}

/**
 * Получает значение из локального хранилища по ключу
 * @param key
 * @returns {Promise<unknown>}
 */
const readLocalStorage = async (key) => {
    return new Promise((resolve, reject) => {
        chrome.storage.local.get([key], function (result) {
            if (result[key] === undefined) {
                reject(new Error("No local key saved"))
            } else {
                resolve(result[key]);
            }
        });
    });
};
//--------------------------------------------------------------------------

// --------------------------------- Web ----------------------
/**
 * Создает заголовки
 */
function createHeaders() {
    let myHeaders = new Headers();
    myHeaders.append('Authorization', JWT_PREFIX + accessToken);
    myHeaders.append('Content-Type', 'application/json; charset=utf-8');
    myHeaders.append('Accept', 'application/json');
    return myHeaders;
}

/**
 * Проверяет коннект к серверу
 */
function testConnection() {
    fetch(TEST_SERVER_URL, {
        method: 'GET',
        headers: createHeaders()
    })
        .then((response) => {
            console.log('get test ' + response);
            if (!response.ok) {
                return Promise.reject(new Error(
                    'Response failed: ' + response.status + ' (' + response.statusText + ')'
                ));
            }
            return response.json();
        }).then((data) => {
        if (data.accessToken != null) {
            console.log('get json ' + data);
            accessToken = data.accessToken;
            refreshToken = data.refreshToken;
            saveAccessTokensToLocalStorage();
            connected = true;
        } else {
            connected = false;
            // todo: Что если приходит с бэка пустой токен?
        }
    })
        .catch((rejected) => {
            // todo: Что если приходит c бэка пришла ошибка
        });
}

chrome.runtime.onStartup.addListener(() => {
    restoreAccessTokensFromLocalStorage();
    if (accessToken === null) {
        connected = false;
    } else {
        testConnection();
    }
})

//--------------------------------------------------------------------------