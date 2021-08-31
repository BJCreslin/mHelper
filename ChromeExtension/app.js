const FZ_44 = "https://zakupki.gov.ru/epz/order/notice/ea44/";
const EA_615 = "https://zakupki.gov.ru/epz/order/notice/ea615/";
const EA_615_FULL_WITHOUT_UIN = "https://zakupki.gov.ru/epz/order/notice/ea615/view/common-info.html?regNumber=";
const EA_615_NUMBER = "ea615";
const PO_615 = "https://zakupki.gov.ru/epz/order/notice/po615/";
const PO_615_FULL_WITHOUT_UIN = "https://zakupki.gov.ru/epz/order/notice/po615/view/common-info.html?regNumber=";
const PO_615_NUMBER = "po615";
const ZAKUPKI = "https://zakupki.gov.ru/epz/order/notice/";
const CLASS_SELECTOR_FZ44 = "tabsNav d-flex align-items-end";
const BUTTON_NAME = "To zHelper";
const BUTTON_CLASS = "btn btn-primary";
const BOOTSTRAP_LINK = "https://cdn.jsdelivr.net/npm/bootstrap@5.1.0/dist/css/bootstrap.min.css";
const BOOTSTRAP_INTEGRITY = "sha384-KyZXEAg3QhqLMpG8r+8fhAXLRk2vvoC2f3B09zVXn8CA5QIVfZOJ3BCsw2P0p/We"

const URL = document.documentURI;
const dataAboutProcurement = {
    law: "",
    uin: ""
}


// if (URL.startsWith(FZ_44)) {
//
// }

if (URL.startsWith(EA_615)) {
    dataAboutProcurement.law = EA_615_NUMBER;
    dataAboutProcurement.uin = URL.replace(EA_615_FULL_WITHOUT_UIN, "")
}
if (URL.startsWith(PO_615)) {
    dataAboutProcurement.law = PO_615_NUMBER;
    dataAboutProcurement.uin = URL.replace(PO_615_FULL_WITHOUT_UIN, "")
}
if (URL.startsWith(ZAKUPKI)) {
    addCss(BOOTSTRAP_LINK)
    insertButton();
}

function insertButton() {
    let buttonPlace = document.getElementsByClassName(CLASS_SELECTOR_FZ44)[0];
    let input = document.createElement("input");
    input.type = "button";
    input.setAttribute("class", BUTTON_CLASS);
    input.value = BUTTON_NAME;
    // console.log(dataAboutProcurement);
    input.onclick = function () {
        chrome.runtime.sendMessage(
            {
                destination: "background",
                data: dataAboutProcurement
            },
            function (response) {
                console.log(response);
            })
    }
    ;
    buttonPlace.appendChild(input);
}

function addCss(css) {
    const head = document.getElementsByTagName('head')[0];
    const s = document.createElement('style');
    s.setAttribute('type', 'text/css');
    s.setAttribute('integrity', 'BOOTSTRAP_INTEGRITY');
    s.setAttribute('crossOrigin', 'zakupki.gov.ru');
    if (s.styleSheet) {   // IE
        s.styleSheet.cssText = css;
    } else {                // the world
        s.appendChild(document.createTextNode(css));
    }
    head.appendChild(s);
}
