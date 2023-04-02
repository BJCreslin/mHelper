const inputTgNumber = document.getElementById("input_tg_number");
const labelNumber = document.getElementById("label_tg_number");
const buttonTgNumber = document.getElementById("button_tg_number");
const inputFormTgNumber = document.getElementById("form_tg_number");
let connected = false;

inputTgNumber.addEventListener("change", function () {
    let iNumber = Number(inputTgNumber.value);
    if (typeof iNumber === 'number' && iNumber > 1000 && iNumber < 1000000000) {
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
    }

    function createConnection() {
        let tg_numberdocument = document.getElementsByClassName("tg_number")[0];
        tg_numberdocument.style.display = "none";
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

const test_button = document.getElementById("button_test");
test_button.addEventListener("click", function () {
    chrome.runtime.sendMessage(
        {
            destination: "test_connection",
            data: this.value
        },
        (response) => {
            function setConnected() {
                test_button.removeAttribute("class");
                test_button.setAttribute("class", "btn btn-secondary")
                test_button.innerText = "Connected";
            }

            function setNotConnected() {
                test_button.removeAttribute("class");
                test_button.setAttribute("class", "btn btn-danger")
                test_button.innerText = "Not connected";
            }

            if (response >= 200 && response < 300) {
                setConnected();
            } else {
                setNotConnected()
            }
        })
});
