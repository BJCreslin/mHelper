const SERVER_URL = "https://zhelper.ru/";
chrome.runtime.onMessage.addListener(
    function (request, sender, sendResponse) {
        if (request.destination === "background") {
            post(request.data)
        }
    }
);

async function post(data) {
    const response = fetch(SERVER_URL, {
        method: 'POST',
        mode: 'cors',
        cache: 'no-cache',
        credentials: 'same-origin',
        headers: {
            'Content-Type': 'application/json'
            // 'Content-Type': 'application/x-www-form-urlencoded',
        },
        redirect: 'follow',
        referrerPolicy: 'no-referrer',
        body: JSON.stringify(data)
    });
    return await response.json();
}

// postData('https://example.com/answer', { answer: 42 })
//     .then((data) => {
//         console.log(data); // JSON data parsed by `response.json()` call
//     });
