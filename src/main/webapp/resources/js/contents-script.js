var ASCIIART = {
    FILE: '⢸⠉⢹⣦⡀<BR>' +
        '⢸⠀⠀⠀⣿<BR>' +
        '⢸⣀⣀⣀⡇',

}

var chat = {
    webSocket: undefined,
    roomNumber: undefined,
    nickName: undefined,

    createRoom: function () {
        var title = window.prompt("[채팅방 생성: 방 제목]\n채팅방을 개설합니다.\n방 제목을 입력해주세요");
        if (title === null || title === undefined || title === '') return nav.activate(3, '방 개설을 취소합니다');
        var pwd = window.prompt("[채팅방 생성: 비밀번호]\n설정하실 비밀번호를 입력해주시고\n입력을 원하지 않는 경우\n취소버튼을 누르시기바랍니다");
        request.submit('POST', '/chatting/new/' + title, function (redirectUrl) {
            if (redirectUrl !== 'false')
                window.location.href = redirectUrl + '?status=new';

        }, 'FORM', 'pwd=' + pwd)
    },
    connect: function (roomNo, nickName) {
        webSocket = new WebSocket("ws://localhost/chat");
        chat.roomNumber = roomNo;
        chat.nickName = nickName;
        webSocket.onopen = chat.onOpen;
        webSocket.onclose = chat.onClose;
        webSocket.onmessage = chat.onMessage;
    },
    disconnect: function () {
        webSocket.send(JSON.stringify({
            chatting_room_no: chat.roomNumber,
            messageType: 'LEAVE',
            mbr_nick_name: chat.nickName
        }));
        webSocket.close();
    },
    send: function (element) {
        msg = element.value;
        webSocket.send(JSON.stringify({
            chatting_room_no: chat.roomNumber,
            messageType: 'CHAT',
            mbr_nick_name: chat.nickName,
            contents: msg
        }));
        element.value = "";
    },
    onOpen: function () {
        webSocket.send(JSON.stringify({
            chatting_room_no: chat.roomNumber,
            messageType: 'ENTER',
            mbr_nick_name: chat.nickName
        }));
    },
    //메시지 갱신
    onMessage: function (event) {
        var chatroom = document.querySelector("#chatting-contents>div:first-child");
        var parsedData = JSON.parse(event.data);
        var addElement = chat.createMsgNode(parsedData.mbr_nick_name, parsedData.contents);
        //TODO: 추가작업 필요
        chatroom.appendChild(addElement);
    },
    onClose: function () {
        chat.disconnect();
    },
    createMsgNode: function (nickName, contents) {
        if (nickName === null || nickName === '') {
            var navTag = document.createElement('nav');
            navTag.innerText = contents;
            return navTag;
        }
        var container = document.createElement('div');

        var msgContainer = document.createElement('div');
        var nickNameTag = document.createElement('b');
        var contentsTag = document.createElement('pre');

        msgContainer.classList.add('chatting-msg');
        if (chat.nickName === nickName) {
            container.classList.add('chatting-my-msg');
        }else{
            container.classList.add('chatting-other-msg')
        }
        nickNameTag.innerText = nickName;
        contentsTag.innerHTML = contents;

        msgContainer.appendChild(nickNameTag);
        msgContainer.appendChild(contentsTag);
        container.appendChild(msgContainer)
        return container;
    }
}