var ASCIIART = {
    FILE: '⢸⠉⢹⣦⡀<BR>' +
        '⢸⠀⠀⠀⣿<BR>' +
        '⢸⣀⣀⣀⡇',

}

var chat = {
    webSocket: undefined,
    roomNumber: undefined,
    nickName: undefined,

    clickAddEventConnectChat: function (masterNickName) {
        chat.nickName = masterNickName;
        chat.connect();
    },
    connect: function () {
        webSocket = new WebSocket("ws://localhost/chat");
        webSocket.onopen = chat.onOpen;
        webSocket.onclose = chat.onClose;
        webSocket.onmessage = chat.onMessage;
    },
    disconnect: function () {
        webSocket.send(JSON.stringify({
            no: chat.roomNumber,
            messageType: 'LEAVE',
            mbr_nick_name: chat.nickName
        }));
        webSocket.close();
    },
    send: function (element) {
        msg = element.value;
        webSocket.send(JSON.stringify({
            no: chat.roomNumber,
            messageType: 'CHAT',
            mbr_nick_name: chat.nickName,
            contents: msg
        }));
        element.value = "";
    },
    onOpen: function () {
        webSocket.send(JSON.stringify({
            no: chat.roomNumber,
            messageType: 'ENTER',
            mbr_nick_name: chat.nickName
        }));
    },
    //메시지 갱신
    onMessage: function (data) {
        console.log(data);
        var chatroom = document.querySelector("#chatting-contents>div:first-child");
        var parsedData = JSON.parse(data);

        var addElement = chat.createMsgNode(parsedData.mbr_nick_name, parsedData.contents);
        //TODO: 추가작업 필요
        chatroom.appendChild(addElement);
    },
    onClose: function () {
        chat.disconnect();
    },
    createMsgNode: function (nickName, contents) {
        var msgContainer = document.createElement('div');
        var nickNameTag = document.createElement('p');
        var contentsTag = document.createElement('pre');

        msgContainer.classList.add('chatting-msg');
        if (chat.nickName === nickName) msgContainer.classList.add('chatting-my-msg');
        nickNameTag.innerText = nickName;
        contentsTag.innerHTML = contents;

        msgContainer.appendChild(nickNameTag);
        msgContainer.appendChild(contentsTag);

        return msgContainer;
    }
}