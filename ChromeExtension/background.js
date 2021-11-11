// const SERVER_URL = "https://zhelper.ru/chrome/";
const SERVER_URL = "https://localhost:8080/chrome/";
chrome.runtime.onMessage.addListener(
    function (request, sender, sendResponse) {
        if (request.destination === "background") {
            fetch(SERVER_URL, {
                method: 'POST',
                // mode: 'no-cors',
                headers: {
                    'Accept': 'application/json, application/xml, text/plain, text/html, *.*',
                    'Content-Type': 'application/json; charset=utf-8'
                },
                body: JSON.stringify(request.data)
            }).then((responce) => {
                if (!responce.ok) {
                    return Promise.reject(new Error(
                        'Responce failed: ' + responce.status + ' (' + responce.statusText + ')'
                    ));
                }
            })
            // const request = new XMLHttpRequest();
            // request.open("POST", SERVER_URL, true);
            // request.setRequestHeader("Content-Type", "application/json;charset=UTF-8");
            // request.send(JSON.stringify(request.data));
            return true;
        }
    }
);
