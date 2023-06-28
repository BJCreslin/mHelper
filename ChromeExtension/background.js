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

chrome.runtime.onMessage.addListener(
    function (request, sender, sendResponse) {

        if (request.destination === "test_connection") {
            fetch(TEST_SERVER_URL, {
                method: 'GET',
                headers: getHeaders()
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
            let code_from_tg = request.data;
            fetch(CHROME_REGISTRATION + code_from_tg, {
                method: 'GET'
            }).then((response) => {
                if (!response.ok) {
                    return Promise.reject(new Error(
                        'Response failed: ' + response.status + ' (' + response.statusText + ')'
                    ));
                }
                debugger;
                return response.json();
            }).then((data) => {
                connected = true;
                accessToken = data.accessToken;
                refreshToken = data.refreshToken;
                saveAccessToken();
                debugger;
                sendResponse(201);
            })
                .catch((rejected) => {
                    sendResponse(rejected.status);
                });
            return true;
        }

        if (request.destination === "sender") {
            fetch(POST_PROCUREMENT_URL, {
                method: 'POST',
                headers: getHeaders(),
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
        headers: getHeaders()
    })
        .then((response) => {
            if (!response.ok) {
                return Promise.reject(new Error(
                    'Response failed: ' + response.status + ' (' + response.statusText + ')'
                ));
            }
            return response.json();
        }).then((data) => {
        connected = true;
        accessToken = data.accessToken;
        refreshToken = data.refreshToken;
        saveAccessToken();
        sendResponse(201);
    })
        .catch((rejected) => {
            sendResponse(rejected.status);
        });
}

chrome.runtime.onStartup.addListener(() => {
    accessToken = readAccessToken()
    if (accessToken === null) {
        connected = false;
    } else {
        testConnection();
        connected = true;
    }

})

function saveAccessToken() {
    chrome.storage.local.set({
        accessToken: accessToken,
        refreshToken: refreshToken
    })
}

function readAccessToken() {
    return chrome.storage.local.get('accessToken');
}

function getHeaders() {
    let myHeaders = new Headers();
    myHeaders.append('Authorization', JWT_PREFIX + accessToken);
    myHeaders.append('Content-Type', 'application/json; charset=utf-8');
    myHeaders.append('Accept', 'application/json');
    return myHeaders;
}
