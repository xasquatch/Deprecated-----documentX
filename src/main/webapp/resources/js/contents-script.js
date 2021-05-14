var ASCIIART = {
    FILE: '⣿⢹⣦⡀<BR>' +
        '⣿⣿⣿⣿<BR>' +
        '⣿⣿⣿⣿',

}
var file = {
    appendList: function (parameter) {
        if (typeof parameter === 'object') {
            file.appendListAsObject(parameter);
        } else {
            file.appendListAsQueryString(parameter);
        }
    },
    appendListAsQueryString: function (queryString) {
        var element = document.querySelector(queryString);
        file.appendListAsObject(element);
    },
    appendListAsObject: function (element) {
        request.submit('GET', '/members/script/files', function (data) {

            var parsedData = JSON.parse(data);
            for (var file of parsedData) {
                var container = document.createElement('div');
                var preview = document.createElement('div');
                var title = document.createElement('p');

                //url에서 "/"문자 lastIndex기준으로 잘라내어 파일이름 찾기
                var fileName = file.url.substr(file.url.lastIndexOf('/') + 1, file.url.length);

                container.appendChild(preview);
                container.setAttribute('title', fileName);
                container.appendChild(title);
                element.appendChild(container);

                if (file.dataType === 'FILE') {
                    text.insert(preview, ASCIIART.FILE, 10);

                } else {
                    var img = document.createElement('img');
                    img.src = file.url;
                    preview.appendChild(img);

                }
                text.insert(title, fileName, 10);
            }
        });
    },
    upload: function (element) {
        var formData = new FormData();
        var files = element.files;
        for (var file of files) {
            formData.append('filePackage', file);
        }
        request.submit('POST', '/members/script/files/new', function (data) {
            if (data === 'false') {
                window.alert('파일 업로드에 실패하였습니다.');

            } else {
                var second = 1;
                nav.acceptMsg(second, data);
                setTimeout(function () {
                    window.history.go(0);

                }, second * 1000);
            }

        }, 'FORMFILE', formData)
    },

}


var chat = {
    webSocket: undefined,
    roomNumber: undefined,
    nickName: undefined,

    createRoom: function () {
        var title = window.prompt("[채팅방 생성: 방 제목]\n채팅방을 개설합니다.\n방 제목을 입력해주세요");
        if (title === null || title === undefined || title === '') return nav.acceptMsg(3, '방 개설을 취소합니다');
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

        chatroom.appendChild(addElement);
    },
    //TODO: 보충 필요
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
        } else {
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