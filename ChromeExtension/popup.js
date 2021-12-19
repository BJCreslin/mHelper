// document.head.title = "zHelper";
const tag = document.createElement("p");
tag.setAttribute("className", "check_tg_number");
tag.innerHTML = "Введите код из Телеграмм";
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
chrome.runtime.sendMessage(
    {
        destination: "isConnected",
        data: {}
    },
    function (response) {
        console.log("resp:" + response);
    });

