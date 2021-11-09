// const SERVER_URL = "https://zhelper.ru/chrome/";
const SERVER_URL = "https://localhost:8080/chrome/";
chrome.runtime.onMessage.addListener(
    function (request, sender, sendResponse) {
        if (request.destination === "background") {
            fetch(SERVER_URL, {
                method: 'POST',
                mode: 'no-cors',
                headers: {
                    'Accept': 'application/json, application/xml, text/plain, text/html, *.*',
                    'Content-Type': 'application/x-www-form-urlencoded; charset=utf-8'
                },
                body: JSON.stringify(request.data)
            });
            // const request = new XMLHttpRequest();
            // request.open("POST", SERVER_URL, true);
            // request.setRequestHeader("Content-Type", "application/json;charset=UTF-8");
            // request.send(JSON.stringify(request.data));
            return true;
        }
    }
);
