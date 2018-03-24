//Establish the WebSocket connection and set up event handlers

var webSocket = new WebSocket("ws://" + location.hostname + ":" + location.port + "/chat");

webSocket.onmessage = function (msg) { updateChat(msg); };
webSocket.onclose = function ()
{
    alert("WebSocket connection closed")
};


//Sending message after clicking "Send"
id("send").addEventListener("click", function(){
    sendMessage(id("message").value);
});

//Sending message if "Enter" is pressed
id("message").addEventListener("keypress", function(e){
    if(e.keyCode === 13) { sendMessage(e.target.value);}
});

//Updating message text area and sending message function
function sendMessage(message) {
    if(message !== ""){
        webSocket.send(message);
        id("message").value = "";
    }
}

function updateChat(msg) {
    var data = JSON.parse(msg.data);
    insert("chat", data.userMessage);
    id("userlist").innerHTML = "";
    data.userlist.forEach(function (user) {
        insert("userlist", "<li>" + user + "</li>" + "<br>");
    });
}

function insert(targetId, message) {
    id(targetId).insertAdjacentHTML("afterbegin", "<div class = messageBox>" + message + "</div>");
}

function id(id){
    return document.getElementById(id);
}