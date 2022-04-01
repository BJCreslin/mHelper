const tag = document.createElement("a");
tag.setAttribute("className", "check_tg_number");
tag.setAttribute("href", "https://telegram.me/mHelperTestBot");
tag.innerText = "Перейдите в телеграмм t.me/mHelperTestBot. Введите полученный код";
const start_div = document.getElementById("start-js");
document.body.insertBefore(tag, start_div);
const x = document.createElement("INPUT");
x.setAttribute("type", "text");
x.setAttribute("className", "input_tg_number");
x.addEventListener("change", function () {
    console.log(this.value);
    chrome.runtime.sendMessage(
        {
            destination: "send_code",
            data: this.value
        },
        function (response) {
            console.log(response);
        })
});
tag.appendChild(x);
const test_div = document.getElementById("test-js");
const test_button = document.createElement("INPUT");
test_button.setAttribute("type", "button");
test_button.value = "Test connection";
test_button.addEventListener("click", function () {
    console.log(this.value);
    chrome.runtime.sendMessage(
        {
            destination: "test_connection",
            data: this.value
        },
        function (response) {
            console.log(response);
        })
});
test_div.appendChild(test_button);
