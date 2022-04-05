const BUTTON_NAME = "To mHelper";
const BUTTON_CLASS = "btn btn-primary";
const BOOTSTRAP_LINK = "https://cdn.jsdelivr.net/npm/bootstrap@5.1.0/dist/css/bootstrap.min.css";
const BOOTSTRAP_INTEGRITY = "sha384-KyZXEAg3QhqLMpG8r+8fhAXLRk2vvoC2f3B09zVXn8CA5QIVfZOJ3BCsw2P0p/We"
const REGION_PREFIX = "Регион:";

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
    timeOfAuction: "",
    timeZone: "",
    etpName: "",
    etpUrl: "",
    summingUpDate: ""
}

if (URL.startsWith("https://zakupki.gov.ru/epz/order/notice/")) {
    addCss(BOOTSTRAP_LINK)
    insertButton("tabsNav d-flex align-items-end");
    fillProcurementWith615And44();
}

if (URL.startsWith("https://zakupki.gov.ru/223/purchase/public/purchase/info/common-info")) {
    addCss(BOOTSTRAP_LINK)
    insertButton("contentTabsWrapper");
    fillProcurementWith223()
}

console.log(dataAboutProcurement);

function fillProcurementWith223() {
    dataAboutProcurement.linkOnPlacement = URL;
    dataAboutProcurement.fzNumber = "223";
    Array.from(document.body.getElementsByClassName("padBtm20")[0].getElementsByTagName("tr")).forEach(x => {
        switch (x.getElementsByTagName("td")[0].innerText) {
            case 'Реестровый номер извещения':
                dataAboutProcurement.uin = x.getElementsByTagName("td")[1].innerText;
                break;
            case 'Способ размещения закупки':
                dataAboutProcurement.procedureType = x.getElementsByTagName("td")[1].innerText;
                break;
            case 'Наименование закупки':
                dataAboutProcurement.objectOf = x.getElementsByTagName("td")[1].innerText;
                break;
            case 'Дата размещения извещения':
                dataAboutProcurement.dateOfPlacement = x.getElementsByTagName("td")[1].innerText;
                break;
            case 'Дата размещения текущей редакции извещения':
                dataAboutProcurement.lastUpdatedFromEIS = x.getElementsByTagName("td")[1].innerText;
                break;
            case 'Наименование электронной площадки в информационно-телекоммуникационной сети «Интернет»':
                dataAboutProcurement.etpName = x.getElementsByTagName("td")[1].innerText;
                break;
            case 'Адрес электронной площадки в информационно-телекоммуникационной сети «Интернет»':
                dataAboutProcurement.etpUrl = x.getElementsByTagName("td")[1].innerText;
                break;
            case 'Наименование организации':
                dataAboutProcurement.publisherName = x.getElementsByTagName("td")[1].innerText;
                break;
            case 'Дата и время окончания подачи заявок\n' +
            '(по местному времени заказчика)':
                dataAboutProcurement.applicationDeadline = x.getElementsByTagName("td")[1].innerText;
                break;
            case 'Дата подведения итогов':
                dataAboutProcurement.summingUpDate = x.getElementsByTagName("td")[1].innerText;
                break;
            case 'Дата начала срока подачи ценовых предложений (по местному времени заказчика)':
                dataAboutProcurement.dateOfAuction = x.getElementsByTagName("td")[1].innerText;
                break;
            case 'Время начала срока подачи ценовых предложений (по местному времени заказчика)':
                dataAboutProcurement.timeOfAuction = x.getElementsByTagName("td")[1].innerText;
                break;
        }
    })
}

function fillProcurementWith615And44() {
    dataAboutProcurement.linkOnPlacement = URL;
    dataAboutProcurement.stage = document.body.getElementsByClassName("cardMainInfo__state")[0].innerText;
    dataAboutProcurement.uin = document.body.getElementsByClassName("cardMainInfo__purchaseLink")[0].innerText.replace("№ ", "");
    dataAboutProcurement.fzNumber = document.body.getElementsByClassName("cardMainInfo__title d-flex text-truncate")[0].innerText.split("\n")[0];
    dataAboutProcurement.timeZone = document.body.getElementsByClassName("time-zone__value")[0].innerText.trim();
    Array.from(document.body.getElementsByClassName("cardMainInfo__title")).forEach(x => {
        switch (x.innerText) {
            case 'Объект закупки':
                dataAboutProcurement.objectOf = x.nextElementSibling.textContent;
                break;
            case 'Заказчик':
                dataAboutProcurement.publisherName = x.nextElementSibling.innerText.trim();
                break;
            case 'Организация, осуществляющая размещение':
                dataAboutProcurement.publisherName = x.nextElementSibling.innerText.trim();
                break;
            case 'Начальная цена':
                dataAboutProcurement.contractPrice = x.nextElementSibling.innerText.replace(" ₽", "").replace(",", ".").replaceAll(/\W/g, "").trim();
                break;
            case 'Размещено':
                dataAboutProcurement.dateOfPlacement = x.nextElementSibling.innerText.trim();
                break;
            case 'Обновлено':
                dataAboutProcurement.lastUpdatedFromEIS = x.nextElementSibling.innerText.trim();
                break;
            case 'Окончание подачи заявок':
                dataAboutProcurement.applicationDeadline = x.nextElementSibling.innerText.trim();
                break;
            case 'Регион':
                if (dataAboutProcurement.timeZone === "") {
                    dataAboutProcurement.timeZone = REGION_PREFIX + x.nextElementSibling.innerText.trim();
                }
                break;
        }
    });
    Array.from(document.body.getElementsByClassName("section__title")).forEach(x => {
        switch (x.innerText) {
            case 'Наименование электронной площадки в сети «Интернет»':
                dataAboutProcurement.etpName = x.nextElementSibling.textContent.replaceAll("\n", "").trim();
                break;
            case 'Наименование электронной площадки':
                dataAboutProcurement.etpName = x.nextElementSibling.textContent.replaceAll("\n", "").trim();
                break;
            case 'Наименование электронной площадки в информационно-телекоммуникационной сети "Интернет"':
                dataAboutProcurement.etpName = x.nextElementSibling.innerText.trim();
                break;
            case 'Сайт оператора электронной площадки в сети «Интернет»':
                dataAboutProcurement.etpUrl = x.nextElementSibling.textContent.trim();
                break;
            case 'Адрес электронной площадки в сети «Интернет»':
                dataAboutProcurement.etpUrl = x.nextElementSibling.textContent.trim();
                break;
            case 'Адрес электронной площадки в информационно-телекоммуникационной сети "Интернет"':
                dataAboutProcurement.etpUrl = x.nextElementSibling.textContent.trim();
                break;
            case 'Дата проведения электронного аукциона':
                dataAboutProcurement.dateOfAuction = x.nextElementSibling.textContent.trim();
                break;
            case 'Дата проведения аукциона в электронной форме':
                dataAboutProcurement.dateOfAuction = x.nextElementSibling.textContent.trim();
                break;
            case 'Время проведения электронного аукциона':
                dataAboutProcurement.timeOfAuction = x.nextElementSibling.textContent.trim();
                break;
            case 'Время проведения аукциона':
                dataAboutProcurement.timeOfAuction = x.nextElementSibling.textContent.trim();
                break;
            case 'Размер обеспечения заявки на участие в электронном аукционе':
                dataAboutProcurement.applicationSecure = x.nextElementSibling.innerText.replaceAll("Российский рубль", "").replaceAll("()", "").replace(",", ".").replaceAll(/\s/g, "").trim();
                break;
            case 'Размер обеспечения заявки':
                dataAboutProcurement.applicationSecure = x.nextElementSibling.innerText.replaceAll("Российский рубль", "").replaceAll("()", "").replace(",", ".").replaceAll(/\s/g, "").trim();
                break;
            case 'Размер обеспечения исполнения обязательств по договору':
                dataAboutProcurement.contractSecure = x.nextElementSibling.innerText.replaceAll("Российский рубль", "").replaceAll("()", "").replace(",", ".").replaceAll(/\s/g, "").trim();
                break;
            case 'Размер обеспечения исполнения контракта':
                dataAboutProcurement.contractSecure = x.nextElementSibling.innerText.replaceAll("Российский рубль", "").replaceAll("()", "").replace(",", ".").replaceAll(/\s/g, "").trim();
                break;
            case 'Способ определения поставщика (подрядчика, исполнителя, подрядной организации)':
                dataAboutProcurement.procedureType = x.nextElementSibling.innerText.trim();
                break;
            case 'Способ определения поставщика (подрядчика, исполнителя)':
                dataAboutProcurement.procedureType = x.nextElementSibling.innerText.trim();
                break;
            case 'Дата окончания срока рассмотрения заявок на участие в предварительном отборе':
                dataAboutProcurement.summingUpDate = x.nextElementSibling.innerText.trim();
                break;
            case 'Ограничения и запреты':
                dataAboutProcurement.restrictions = x.nextElementSibling.innerText.trim();
                break;

        }
    });
}

function insertButton(className) {
    let buttonPlace = document.getElementsByClassName(className)[0];
    let buttonToMHelper = document.createElement("input");
    buttonToMHelper.type = "button";
    buttonToMHelper.setAttribute("class", BUTTON_CLASS);
    buttonToMHelper.setAttribute("style", "color: white; background-color:grey; border:2px solid black; padding: 12px 16px; font-size:20px");
    buttonToMHelper.value = BUTTON_NAME;
    buttonToMHelper.onclick = function () {
        chrome.runtime.sendMessage(
            {
                destination: "sender",
                data: dataAboutProcurement
            },
            function (response) {
                console.log(response);
            })
    };
    buttonPlace.appendChild(buttonToMHelper);
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
