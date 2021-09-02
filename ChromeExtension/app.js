const EA_44 = "https://zakupki.gov.ru/epz/order/notice/ea44/view/common-info.html?regNumber=";
const OK_504 = "https://zakupki.gov.ru/epz/order/notice/ok504/view/common-info.html?regNumber=";
const ZK_20 = "https://zakupki.gov.ru/epz/order/notice/zk20/view/common-info.html?regNumber=";
const EA_615 = "https://zakupki.gov.ru/epz/order/notice/ea615/";
const EA_615_FULL_WITHOUT_UIN = "https://zakupki.gov.ru/epz/order/notice/ea615/view/common-info.html?regNumber=";
const FZ_615_NUMBER = "LAW_615";
const FZ_44_NUMBER = "LAW_44";
const PO_615 = "https://zakupki.gov.ru/epz/order/notice/po615/";
const PO_615_FULL_WITHOUT_UIN = "https://zakupki.gov.ru/epz/order/notice/po615/view/common-info.html?regNumber=";
const CLASS_SELECTOR_FZ44 = "tabsNav d-flex align-items-end";
const BUTTON_NAME = "To zHelper";
const BUTTON_CLASS = "btn btn-primary";
const BOOTSTRAP_LINK = "https://cdn.jsdelivr.net/npm/bootstrap@5.1.0/dist/css/bootstrap.min.css";
const BOOTSTRAP_INTEGRITY = "sha384-KyZXEAg3QhqLMpG8r+8fhAXLRk2vvoC2f3B09zVXn8CA5QIVfZOJ3BCsw2P0p/We"

const RUBLES = " ₽";
const NBSP = /&nbsp;/g;

const OBJECT_SELECTOR_615 = "body > div.cardWrapper.outerWrapper > div > div.cardHeaderBlock > div:nth-child(3) > div.cardMainInfo.row > div.sectionMainInfo.borderRight.col-6 > div.sectionMainInfo__body > div:nth-child(1) > span.cardMainInfo__content";
const PUBLISHER_NAME_SELECTOR_615 = "body > div.cardWrapper.outerWrapper > div > div.cardHeaderBlock > div:nth-child(3) > div.cardMainInfo.row > div.sectionMainInfo.borderRight.col-6 > div.sectionMainInfo__body > div:nth-child(2) > span.cardMainInfo__content > a";
const PRICE_SELECTOR_615 = "body > div.cardWrapper.outerWrapper > div > div.cardHeaderBlock > div:nth-child(3) > div.cardMainInfo.row > div.sectionMainInfo.borderRight.col-3.colSpaceBetween > div.price > span.cardMainInfo__content.cost";
const PROCEDURE_TYPE_SELECTOR_615 = "body > div.cardWrapper.outerWrapper > div > div:nth-child(2) > div > div > section:nth-child(2) > span.section__info";
const STAGE_SELECTOR_615 = "body > div.cardWrapper.outerWrapper > div > div:nth-child(2) > div > div > section:nth-child(3) > span.section__info";
const LINK_OF_PLACEMENT_SELECTOR_EA615 = "body > div.cardWrapper.outerWrapper > div > div:nth-child(2) > div > div > section:nth-child(6) > span.section__info > a";
const LINK_OF_PLACEMENT_SELECTOR_PO615 = "body > div.cardWrapper.outerWrapper > div > div:nth-child(2) > div > div > section:nth-child(7) > span.section__info > a";
const APPLICATION_SELECTOR_615 = "body > div.cardWrapper.outerWrapper > div > div:nth-child(10) > div > div > section:nth-child(3) > span.section__info > span";
const CONTRACT_SELECTOR_615 = "body > div.cardWrapper.outerWrapper > div > div:nth-child(10) > div > div > section:nth-child(4) > span.section__info > span";
const DATE_OF_PLACEMENT_SELECTOR_615 = "body > div.cardWrapper.outerWrapper > div > div.cardHeaderBlock > div:nth-child(3) > div.cardMainInfo.row > div.sectionMainInfo.borderRight.col-3.colSpaceBetween > div.date > div:nth-child(1) > span.cardMainInfo__content";
const DATE_OF_AUCTION_SELECTOR_615 = "body > div.cardWrapper.outerWrapper > div > div:nth-child(8) > div > div > section:nth-child(4) > span.section__info";
const TIME_OF_AUCTION_SELECTOR_615 = "body > div.cardWrapper.outerWrapper > div > div:nth-child(8) > div > div > section:nth-child(5) > span.section__info > span";
const DEADLINE_SELECTOR_615 = "div.date div.cardMainInfo__section:last-child span.cardMainInfo__content";

const URL = document.documentURI;
const dataAboutProcurement = {
    fzNumber: "",
    uin: "",
    objectOf: "",
    publisherName: "",
    contractPrice: "",
    procedureType: "",
    stage: "",
    linkOnPlacement: "",
    applicationDeadline: "",
    applicationSecure: "",
    contractSecure: "",
    restrictions: "",
    lastUpdatedFromEIS: "",
    dateOfPlacement: "",
    dateOfAuction: "",
}

if (URL.startsWith(EA_615)) {
    addCss(BOOTSTRAP_LINK)
    insertButton();
    fillProcurementWith_EA_615();
}
if (URL.startsWith(PO_615)) {
    addCss(BOOTSTRAP_LINK)
    insertButton();
    fillProcurementWith_PO_615();
}
if (URL.startsWith(EA_44)) {
    addCss(BOOTSTRAP_LINK)
    insertButton();
    fillProcurementWithEA44()
}
if (URL.startsWith(OK_504)) {
    addCss(BOOTSTRAP_LINK)
    insertButton();
    fillProcurementWithOK504()
}


if (URL.startsWith(ZK_20)) {
    addCss(BOOTSTRAP_LINK)
    insertButton();
    fillProcurementWithZK20()
}

console.log(dataAboutProcurement);

function fillProcurementWithZK20() {
    dataAboutProcurement.fzNumber = FZ_44_NUMBER;
    dataAboutProcurement.applicationDeadline = document.querySelector("body > div.cardWrapper.outerWrapper > div > div.cardHeaderBlock > div:nth-child(3) > div.cardMainInfo.row > div.sectionMainInfo.borderRight.col-3.colSpaceBetween > div.date > div.cardMainInfo__section > span.cardMainInfo__content").innerText;
    dataAboutProcurement.objectOf = document.querySelector("body > div.cardWrapper.outerWrapper > div > div.cardHeaderBlock > div:nth-child(3) > div.cardMainInfo.row > div.sectionMainInfo.borderRight.col-6 > div.sectionMainInfo__body > div:nth-child(1) > span.cardMainInfo__content").innerText;
    dataAboutProcurement.publisherName = document.querySelector("body > div.cardWrapper.outerWrapper > div > div.cardHeaderBlock > div:nth-child(3) > div.cardMainInfo.row > div.sectionMainInfo.borderRight.col-6 > div.sectionMainInfo__body > div:nth-child(2) > span.cardMainInfo__content > a").innerText;
    dataAboutProcurement.procedureType = document.querySelector("body > div.cardWrapper.outerWrapper > div > div:nth-child(2) > div > div > section:nth-child(2) > span.section__info").innerText;
    dataAboutProcurement.stage = document.querySelector("body > div.cardWrapper.outerWrapper > div > div:nth-child(2) > div > div > section:nth-child(7) > span.section__info").innerText;
    dataAboutProcurement.dateOfPlacement = document.querySelector("body > div.cardWrapper.outerWrapper > div > div.cardHeaderBlock > div:nth-child(3) > div.cardMainInfo.row > div.sectionMainInfo.borderRight.col-3.colSpaceBetween > div.date > div.row > div:nth-child(1) > span.cardMainInfo__content").innerText;
    dataAboutProcurement.uin = URL.replace(ZK_20, "");
    dataAboutProcurement.contractPrice = document.body.querySelector(PRICE_SELECTOR_615).innerText.replace(RUBLES, "").replace(NBSP, "").replace(",", ".");
    dataAboutProcurement.linkOnPlacement = document.querySelector("body > div.cardWrapper.outerWrapper > div > div:nth-child(2) > div > div > section:nth-child(4) > span.section__info > a").innerText;
    dataAboutProcurement.applicationSecure = null;
    dataAboutProcurement.contractSecure = null;
    dataAboutProcurement.restrictions = document.querySelector("body > div.cardWrapper.outerWrapper > div > div:nth-child(14) > div > div > section:nth-child(4) > span.section__info > span").innerText.replace(NBSP, "");
    dataAboutProcurement.lastUpdatedFromEIS = document.querySelector("body > div.cardWrapper.outerWrapper > div > div.cardHeaderBlock > div:nth-child(3) > div.cardMainInfo.row > div.sectionMainInfo.borderRight.col-3.colSpaceBetween > div.date > div.row > div:nth-child(2) > span.cardMainInfo__content").innerText;
    dataAboutProcurement.dateOfAuction = null;
}

function fillProcurementWithOK504() {
    dataAboutProcurement.fzNumber = FZ_44_NUMBER;
    dataAboutProcurement.applicationDeadline = document.querySelector("body > div.cardWrapper.outerWrapper > div > div.cardHeaderBlock > div:nth-child(3) > div.cardMainInfo.row > div.sectionMainInfo.borderRight.col-3.colSpaceBetween > div.date > div.cardMainInfo__section > span.cardMainInfo__content").innerText;
    dataAboutProcurement.objectOf = document.querySelector("body > div.cardWrapper.outerWrapper > div > div.cardHeaderBlock > div:nth-child(3) > div.cardMainInfo.row > div.sectionMainInfo.borderRight.col-6 > div.sectionMainInfo__body > div:nth-child(1) > span.cardMainInfo__content").innerText;
    dataAboutProcurement.publisherName = document.querySelector("body > div.cardWrapper.outerWrapper > div > div.cardHeaderBlock > div:nth-child(3) > div.cardMainInfo.row > div.sectionMainInfo.borderRight.col-6 > div.sectionMainInfo__body > div:nth-child(2) > span.cardMainInfo__content > a").innerText;
    dataAboutProcurement.procedureType = document.querySelector("body > div.cardWrapper.outerWrapper > div > div.contentTabBoxBlock > div > div:nth-child(1) > div > div > section:nth-child(2) > span.section__info").innerText;
    dataAboutProcurement.stage = document.querySelector("body > div.cardWrapper.outerWrapper > div > div.contentTabBoxBlock > div > div:nth-child(1) > div > div > section:nth-child(7) > span.section__info").innerText;
    dataAboutProcurement.dateOfPlacement = document.querySelector("body > div.cardWrapper.outerWrapper > div > div.cardHeaderBlock > div:nth-child(3) > div.cardMainInfo.row > div.sectionMainInfo.borderRight.col-3.colSpaceBetween > div.date > div.row > div:nth-child(1) > span.cardMainInfo__content").innerText;
    dataAboutProcurement.uin = URL.replace(OK_504, "");
    dataAboutProcurement.contractPrice = document.body.querySelector(PRICE_SELECTOR_615).innerText.replace(RUBLES, "").replace(NBSP, "").replace(",", ".");
    dataAboutProcurement.linkOnPlacement = document.querySelector("body > div.cardWrapper.outerWrapper > div > div.contentTabBoxBlock > div > div:nth-child(1) > div > div > section:nth-child(4) > span.section__info > a").innerText;
    dataAboutProcurement.applicationSecure = document.querySelector("#custReqNoticeTable > div:nth-child(2) > div > div > section:nth-child(3) > span.section__info").innerText.replace(RUBLES, "").replace(NBSP, "").replace(",", ".");
    dataAboutProcurement.contractSecure = document.querySelector("#custReqNoticeTable > div:nth-child(6) > div > div > section:nth-child(3) > span.section__info").innerText.replace(NBSP, "");
    dataAboutProcurement.restrictions = document.querySelector("body > div.cardWrapper.outerWrapper > div > div.contentTabBoxBlock > div > div:nth-child(13) > div > div > section:nth-child(4) > span.section__info > span").innerText.replace(NBSP, "");
    dataAboutProcurement.lastUpdatedFromEIS = document.querySelector("body > div.cardWrapper.outerWrapper > div > div.cardHeaderBlock > div:nth-child(3) > div.cardMainInfo.row > div.sectionMainInfo.borderRight.col-3.colSpaceBetween > div.date > div.row > div:nth-child(2) > span.cardMainInfo__content").innerText;
    dataAboutProcurement.dateOfAuction = null;
}

function fillProcurementWithEA44() {
    dataAboutProcurement.fzNumber = FZ_44_NUMBER;
    dataAboutProcurement.applicationDeadline = document.querySelector("body > div.cardWrapper.outerWrapper > div > div.cardHeaderBlock > div:nth-child(3) > div.cardMainInfo.row > div.sectionMainInfo.borderRight.col-3.colSpaceBetween > div.date > div.cardMainInfo__section > span.cardMainInfo__content").innerText;
    dataAboutProcurement.objectOf = document.querySelector("body > div.cardWrapper.outerWrapper > div > div.cardHeaderBlock > div:nth-child(3) > div.cardMainInfo.row > div.sectionMainInfo.borderRight.col-6 > div.sectionMainInfo__body > div:nth-child(1) > span.cardMainInfo__content").innerText;
    dataAboutProcurement.publisherName = document.querySelector("body > div.cardWrapper.outerWrapper > div > div.cardHeaderBlock > div:nth-child(3) > div.cardMainInfo.row > div.sectionMainInfo.borderRight.col-6 > div.sectionMainInfo__body > div:nth-child(2) > span.cardMainInfo__content > a").innerText;
    dataAboutProcurement.procedureType = document.querySelector("body > div.cardWrapper.outerWrapper > div > div:nth-child(2) > div > div > section:nth-child(2) > span.section__info").innerText;
    dataAboutProcurement.stage = document.querySelector("body > div.cardWrapper.outerWrapper > div > div:nth-child(2) > div > div > section:nth-child(7) > span.section__info").innerText;
    dataAboutProcurement.dateOfPlacement = document.querySelector("body > div.cardWrapper.outerWrapper > div > div.cardHeaderBlock > div:nth-child(3) > div.cardMainInfo.row > div.sectionMainInfo.borderRight.col-3.colSpaceBetween > div.date > div.row > div:nth-child(1) > span.cardMainInfo__content").innerText;
    dataAboutProcurement.uin = URL.replace(EA_44, "");
    dataAboutProcurement.contractPrice = document.body.querySelector(PRICE_SELECTOR_615).innerText.replace(RUBLES, "").replace(NBSP, "").replace(",", ".");
    dataAboutProcurement.linkOnPlacement = document.querySelector("body > div.cardWrapper.outerWrapper > div > div:nth-child(2) > div > div > section:nth-child(4) > span.section__info > a").innerText;
    dataAboutProcurement.applicationSecure = document.querySelector("#custReqNoticeTable > div:nth-child(2) > div > div > section:nth-child(3) > span.section__info").innerText.replace(RUBLES, "").replace(NBSP, "").replace(",", ".");
    dataAboutProcurement.contractSecure = document.querySelector("#custReqNoticeTable > div:nth-child(6) > div > div > section:nth-child(3) > span.section__info").innerText.replace(NBSP, "");
    dataAboutProcurement.restrictions = document.querySelector("body > div.cardWrapper.outerWrapper > div > div:nth-child(15) > div > div > section:nth-child(4) > span.section__info").innerText.replace(NBSP, "");
    dataAboutProcurement.lastUpdatedFromEIS = document.querySelector("body > div.cardWrapper.outerWrapper > div > div.cardHeaderBlock > div:nth-child(3) > div.cardMainInfo.row > div.sectionMainInfo.borderRight.col-3.colSpaceBetween > div.date > div.row > div:nth-child(2) > span.cardMainInfo__content").innerText;
    dataAboutProcurement.dateOfAuction = document.querySelector("body > div.cardWrapper.outerWrapper > div > div:nth-child(6) > div > div > section:nth-child(7) > span.section__info").innerText +
        " " + document.querySelector("body > div.cardWrapper.outerWrapper > div > div:nth-child(6) > div > div > section:nth-child(8) > span.section__info").innerText;
}

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

function fillProcurementWith_EA_615() {
    fillProcurementWith615();
    dataAboutProcurement.uin = URL.replace(EA_615_FULL_WITHOUT_UIN, "");
    dataAboutProcurement.contractPrice = document.querySelector("body > div.cardWrapper.outerWrapper > div > div.cardHeaderBlock > div:nth-child(3) > div.cardMainInfo.row > div.sectionMainInfo.borderRight.col-3.colSpaceBetween > div.price > span.cardMainInfo__content.cost").innerText.replace(RUBLES, "").replace(NBSP, "").replace(",", ".");
    dataAboutProcurement.linkOnPlacement = document.querySelector(LINK_OF_PLACEMENT_SELECTOR_EA615).innerText;
    dataAboutProcurement.applicationSecure = document.querySelector(APPLICATION_SELECTOR_615).innerText.replace(NBSP, "");
    dataAboutProcurement.contractSecure = document.querySelector(CONTRACT_SELECTOR_615).innerText.replace(NBSP, "");
    dataAboutProcurement.restrictions = null;
    dataAboutProcurement.lastUpdatedFromEIS = null;
    dataAboutProcurement.dateOfAuction = document.querySelector(DATE_OF_AUCTION_SELECTOR_615).innerText + " " + document.querySelector(TIME_OF_AUCTION_SELECTOR_615).innerText;
}

function fillProcurementWith_PO_615() {
    fillProcurementWith615();
    dataAboutProcurement.uin = URL.replace(PO_615_FULL_WITHOUT_UIN, "");
    dataAboutProcurement.contractPrice = null;
    dataAboutProcurement.linkOnPlacement = document.querySelector(LINK_OF_PLACEMENT_SELECTOR_PO615).innerText;
    dataAboutProcurement.applicationSecure = null;
    dataAboutProcurement.contractSecure = null;
    dataAboutProcurement.restrictions = null;
    dataAboutProcurement.lastUpdatedFromEIS = null;
    dataAboutProcurement.dateOfAuction = null;
}

function fillProcurementWith615() {
    dataAboutProcurement.fzNumber = FZ_615_NUMBER;
    dataAboutProcurement.applicationDeadline = document.body.querySelector(DEADLINE_SELECTOR_615).innerText;
    dataAboutProcurement.objectOf = document.body.querySelector(OBJECT_SELECTOR_615).innerText;
    dataAboutProcurement.publisherName = document.body.querySelector(PUBLISHER_NAME_SELECTOR_615).innerText;
    dataAboutProcurement.procedureType = document.body.querySelector(PROCEDURE_TYPE_SELECTOR_615).innerText;
    dataAboutProcurement.stage = document.querySelector(STAGE_SELECTOR_615).innerText;
    dataAboutProcurement.dateOfPlacement = document.querySelector(DATE_OF_PLACEMENT_SELECTOR_615).innerText;
}
