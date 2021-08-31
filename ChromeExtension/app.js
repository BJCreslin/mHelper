const FZ_44 = "https://zakupki.gov.ru/epz/order/notice/ea44/";
const EA_615 = "https://zakupki.gov.ru/epz/order/notice/ea615/";
const EA_615_FULL_WITHOUT_UIN = "https://zakupki.gov.ru/epz/order/notice/ea615/view/common-info.html?regNumber=";
const FZ_615_NUMBER = "LAW_615";
const PO_615 = "https://zakupki.gov.ru/epz/order/notice/po615/";
const PO_615_FULL_WITHOUT_UIN = "https://zakupki.gov.ru/epz/order/notice/po615/view/common-info.html?regNumber=";
const PO_615_NUMBER = "po615";
const ZAKUPKI = "https://zakupki.gov.ru/epz/order/notice/";
const CLASS_SELECTOR_FZ44 = "tabsNav d-flex align-items-end";
const BUTTON_NAME = "To zHelper";
const BUTTON_CLASS = "btn btn-primary";
const BOOTSTRAP_LINK = "https://cdn.jsdelivr.net/npm/bootstrap@5.1.0/dist/css/bootstrap.min.css";
const BOOTSTRAP_INTEGRITY = "sha384-KyZXEAg3QhqLMpG8r+8fhAXLRk2vvoC2f3B09zVXn8CA5QIVfZOJ3BCsw2P0p/We"
const OBJECT_OF_SELECTOR = "span.cardMainInfo__content";

const URL = document.documentURI;
const dataAboutProcurement = {
    fzNumber: "",
    uin: "",
    objectOf: "",
    publisherName: "",
    contractPrice: ""
}

if (URL.startsWith(ZAKUPKI)) {
    addCss(BOOTSTRAP_LINK)
    insertButton();
    if (URL.startsWith(EA_615)) {
        dataAboutProcurement.fzNumber = FZ_615_NUMBER;
        dataAboutProcurement.uin = URL.replace(EA_615_FULL_WITHOUT_UIN, "");
        dataAboutProcurement.objectOf = document.body.querySelectorAll(OBJECT_OF_SELECTOR)[0].innerHTML;
        dataAboutProcurement.publisherName = document.body.querySelectorAll(OBJECT_OF_SELECTOR)[1].innerHTML;
        dataAboutProcurement.contractPrice = document.body.querySelectorAll(OBJECT_OF_SELECTOR)[3].outerText;
        console.log(document.body.querySelectorAll(OBJECT_OF_SELECTOR));
        console.log(dataAboutProcurement);
    }
    if (URL.startsWith(PO_615)) {
        dataAboutProcurement.fzNumber = FZ_615_NUMBER;
        dataAboutProcurement.uin = URL.replace(PO_615_FULL_WITHOUT_UIN, "");

    }
}

// if (URL.startsWith(FZ_44)) {
//
// }


function insertButton() {
    let buttonPlace = document.getElementsByClassName(CLASS_SELECTOR_FZ44)[0];
    let buttonToZhelper = document.createElement("input");
    buttonToZhelper.type = "button";
    buttonToZhelper.setAttribute("class", BUTTON_CLASS);
    buttonToZhelper.value = BUTTON_NAME;
    buttonToZhelper.onclick = function () {
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
    buttonPlace.appendChild(buttonToZhelper);
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
