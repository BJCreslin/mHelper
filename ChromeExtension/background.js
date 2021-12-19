// const SERVER_URL = "https://zhelper.ru/";
const SERVER_URL = "http://localhost:8080/";
const POST_PROCUREMENT_URL = SERVER_URL + "v1/chrome/"
const TEST_SERVER_URL = SERVER_URL + "v1/auth/";
const CHROME_REGISTRATION = SERVER_URL + "v1/auth/code/"
let connected = false;
let token;
chrome.runtime.onMessage.addListener(
    function (request, sender, sendResponse) {
        console.log("***************");
        console.log(request, sender, sendResponse);
        console.log("***************");
        if (request.destination === "test_connection") {
            fetch(TEST_SERVER_URL, {
                method: 'GET'
            }).then((response) => {
                if (!response.ok) {
                    return Promise.reject(new Error(
                        'Responce failed: ' + response.status + ' (' + response.statusText + ')'
                    ));
                }
            })
            return true;
        }
        if (request.destination === "send_code") {
            let code_from_tg = request.data;
            fetch(CHROME_REGISTRATION + code_from_tg, {
                method: 'GET'
            }).then((response) => {
                if (!response.ok) {
                    return Promise.reject(new Error(
                        'Responce failed: ' + response.status + ' (' + response.statusText + ')'
                    ));
                }
                return response.json();
            })
                .then(function (json) {
                    connected = true;
                    token = json.token;
                    console.log("token: " + token);
                })
            return true;
        }
        if (request.destination === "sender") {
            console.log("token: " + token);
            fetch(POST_PROCUREMENT_URL, {
                method: 'POST',
                headers: {
                    'Accept': 'application/json',
                    'Content-Type': 'application/json; charset=utf-8',
                    'Authorization': token
                },
                body: JSON.stringify(request.data)
            }).then((response) => {
                if (!response.ok) {
                    return Promise.reject(new Error(
                        'Responce failed: ' + response.status + ' (' + response.statusText + ')'
                    ));
                }
            })
            return true;
        }
    }
);
