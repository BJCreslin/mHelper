const SERVER_URL = "https://zhelper.ru/api/";
chrome.runtime.onMessage.addListener(
    function (request, sender, sendResponse) {
        if (request.destination === "background") {
            fetch(SERVER_URL, {
                method: 'POST',
                mode: 'no-cors',
                headers: {
                    'Accept': 'application/json, text/plain, */*',
                    'Content-Type': 'application/json; charset=utf-8'
                },
                body: JSON.stringify(request.data)
            });
            return true;
        }
    }
);
