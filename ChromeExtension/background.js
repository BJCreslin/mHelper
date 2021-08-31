const SERVER_URL = "https://zhelper.ru/api/";
chrome.runtime.onMessage.addListener(
    function (request, sender, sendResponse) {
        if (request.destination === "background") {
            fetch(SERVER_URL, {
                method: 'POST',
                mode: 'no-cors',
                headers: {
                    'Content-Type': 'application/json'
                },
                body: JSON.stringify(request.data)
            });
            return true;
        }
    }
);
