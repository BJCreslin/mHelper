// const SERVER_URL = "https://zhelper.ru/v1/chrome/";
const SERVER_URL = "https://localhost:8080/v1/chrome/";
chrome.runtime.onMessage.addListener(
    function (request, sender, sendResponse) {
        if (request.destination === "background") {
            fetch(SERVER_URL, {
                method: 'POST',
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
            return true;
        }
    }
);
