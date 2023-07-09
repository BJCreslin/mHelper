const inputTgNumber = document.getElementById("input_tg_number");
const labelNumber = document.getElementById("label_tg_number");
const buttonTgNumber = document.getElementById("button_tg_number");
const inputFormTgNumber = document.getElementById("form_tg_number");
let connected = false;

chrome.action.onClicked.addListener(function (tab) {
    debugger
    console.log("Иконка расширения была нажата.");
});

inputTgNumber.addEventListener("change", function () {
    let inputNumber = Number(inputTgNumber.value);
    if (typeof inputNumber === 'number' && inputNumber > 1000 && inputNumber < 1000000000) {
        inputTgNumber.classList.remove("is-invalid");
        buttonTgNumber.classList.remove("disabled");
    } else {
        inputTgNumber.classList.add("is-invalid");
        buttonTgNumber.classList.add("disabled");
    }
})

button_tg_number.addEventListener("click", function () {

    function notCreatedConnection() {
        inputTgNumber.classList.add("is-invalid");
        connected = false;
    }

    function createConnection() {
        let numberDocument = document.getElementsByClassName("tg_number")[0];
        numberDocument.style.display = "none";
        connected = true;
    }

    chrome.runtime.sendMessage(
        {
            destination: "send_code",
            data: inputTgNumber.value
        },
        (response) => {
            if (response >= 200 && response < 300) {
                createConnection();
            } else {
                notCreatedConnection();
            }
        });
});

const isConnected = () => {
    chrome.runtime.sendMessage(
        {
            destination: "check_code"
        },
        (response) => {
            connected = (response === 1);
        });
}

const testConnectionButton = document.getElementById("button_test");
testConnectionButton.addEventListener("click", function () {
    chrome.runtime.sendMessage(
        {
            destination: "test_connection",
            data: this.value
        },
        (response) => {
            function setConnected() {
                testConnectionButton.removeAttribute("class");
                testConnectionButton.setAttribute("class", "btn btn-secondary")
                testConnectionButton.innerText = "Connected";
                connected = true;
            }

            function setNotConnected() {
                testConnectionButton.removeAttribute("class");
                testConnectionButton.setAttribute("class", "btn btn-danger")
                testConnectionButton.innerText = "Not connected";
                connected = false;
            }

            if (response >= 200 && response < 300) {
                setConnected();
            } else {
                setNotConnected()
            }
        })
});
