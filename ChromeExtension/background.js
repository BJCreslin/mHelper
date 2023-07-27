// const SERVER_URL = "https://mhelper.ru/";
const SERVER_URL = "http://localhost:8080/";
const POST_PROCUREMENT_URL = SERVER_URL + "v1/chrome/";
const TEST_SERVER_URL = SERVER_URL + "v1/auth/";
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
        debugger;
        return response.json();
    }).then((data) => {
        debugger;
        return data;
    })
        .catch((rejected) => {
            return null;
        });
}

function saveTokens(data) {
    connected = true;
    debugger;
    console.log('access ' + data.accessToken);
    accessToken = data.accessToken;
    refreshToken = data.refreshToken;
    saveAccessTokensToLocalStorage();
}

chrome.runtime.onMessage.addListener(
    function (request, sender, sendResponse) {

        if (request.destination === "test_connection") {
            fetch(TEST_SERVER_URL, {
                method: 'GET',
                headers: createHeaders()
            })
                .then((response) => {
                    debugger;
                    sendResponse(response.status);
                })
                .catch((rejected) => {
                    sendResponse(rejected.status);
                });
            return true;
        }

        if (request.destination === "send_code") {
            let registrationCode = request.data;
            let host = CHROME_REGISTRATION + registrationCode;
            let result = sendCode(host);
            result.then(saveTokens)
            // if (result != null) {
            //     console.log(result)
            //     saveTokens(result);
            //     sendResponse(200);
            // }
            // sendResponse(400);
        }

        if (request.destination === "sender") {
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

        if (request.destination === "check_code") {
            if (jwtToken === null) {
                sendResponse(2);
            }
        }
    }
);

function testConnection() {
    fetch(TEST_SERVER_URL, {
        method: 'GET',
        headers: createHeaders()
    })
        .then((response) => {
            if (!response.ok) {
                return Promise.reject(new Error(
                    'Response failed: ' + response.status + ' (' + response.statusText + ')'
                ));
            }
            return response.json();
        }).then((data) => {

        if (data.accessToken != null) {
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
    debugger;
    restoreAccessTokensFromLocalStorage();
    if (accessToken === null) {
        connected = false;
    } else {
        testConnection();
    }
})

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

const readLocalStorage = async (key) => {
    return new Promise((resolve, reject) => {
        chrome.storage.local.get([key], function (result) {
            if (result[key] === undefined) {
                reject();
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

//--------------------------------------------------------------------------