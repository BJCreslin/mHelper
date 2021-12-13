// const SERVER_URL = "https://zhelper.ru/";
const SERVER_URL = "https://localhost:8080/";
const POST_PROCUREMENT_URL = SERVER_URL + "v1/chrome/"
const TEST_SERVER_URL = SERVER_URL + "v1/auth/";
chrome.runtime.onMessage.addListener(
    function (request, sender, sendResponse) {
        console.log("***************");
        console.log(request, sender, sendResponse);
        console.log("***************");
        if (request.destination === "test_connection") {
        fetch(TEST_SERVER_URL,{
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
        if (request.destination === "sender") {
            fetch(POST_PROCUREMENT_URL, {
                method: 'POST',
                headers: {
                    'Accept': 'application/json, application/xml, text/plain, text/html, *.*',
                    'Content-Type': 'application/json; charset=utf-8'
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
