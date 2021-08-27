const FZ_44 = "https://zakupki.gov.ru/epz/order/notice/ea44/";
const PP_615 = "https://zakupki.gov.ru/epz/order/notice/ea615/";
const CLASS_SELECTOR_FZ44 = "tabsNav d-flex align-items-end";
const BUTTON_NAME = "To zHelper";
const BUTTON_CLASS = "btn btn-primary";
const BOOTSTRAP_LINK = "https://cdn.jsdelivr.net/npm/bootstrap@5.1.0/dist/css/bootstrap.min.css";
const BOOTSTRAP_INTEGRITY = "sha384-KyZXEAg3QhqLMpG8r+8fhAXLRk2vvoC2f3B09zVXn8CA5QIVfZOJ3BCsw2P0p/We"
const SERVER_URL = "https://zhelper.ru/";
const URL = document.documentURI;

if (URL.startsWith(FZ_44)) {
    addCss(BOOTSTRAP_LINK)
    insertButtonFZ44();
}
if (URL.startsWith(PP_615)) {
    addCss(BOOTSTRAP_LINK)
    insertButtonFZ44();
}

function insertButtonFZ44() {
    let buttonPlace = document.getElementsByClassName(CLASS_SELECTOR_FZ44)[0];
    let input = document.createElement("input");
    input.type = "button";
    input.setAttribute("class", BUTTON_CLASS);
    input.value = BUTTON_NAME;
    input.onclick = function () {
        post(URL);
    };
    buttonPlace.appendChild(input);
}

function addCss(css) {
    const head = document.getElementsByTagName('head')[0];
    const s = document.createElement('style');
    s.setAttribute('type', 'text/css');
    s.setAttribute('integrity', 'BOOTSTRAP_INTEGRITY');
    s.setAttribute('crossOrigin', 'anonymous');
    if (s.styleSheet) {   // IE
        s.styleSheet.cssText = css;
    } else {                // the world
        s.appendChild(document.createTextNode(css));
    }
    head.appendChild(s);
}

async function post(url) {
    let procurementAddress = {
        address: url
    }
    let response = await fetch(SERVER_URL,
        {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json;charset=utf-8'
            },
            body: JSON.stringify(procurementAddress)
        });

    if (response.ok) { // если HTTP-статус в диапазоне 200-299
        // получаем тело ответа (см. про этот метод ниже)
        let json = await response.json();
    } else {
        alert("Ошибка HTTP: " + response.status);
    }


}
