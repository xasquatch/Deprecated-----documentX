var ASCIIART = {
    FILE: '⣿⢹⣦⡀<BR>' +
        '⣿⣿⣿⣿<BR>' +
        '⣿⣿⣿⣿'

}
var file = {
    appendList: function (parameter, searchValue, currentPage, pageLimit) {
        if (typeof parameter === 'object') {
            file.appendListAsObject(parameter, searchValue, currentPage, pageLimit);
        } else {
            file.appendListAsQueryString(parameter, searchValue, currentPage, pageLimit);
        }
    },
    appendListAsQueryString: function (queryString, searchValue, currentPage, pageLimit) {
        var element = document.querySelector(queryString);
        file.appendListAsObject(element, searchValue, currentPage, pageLimit);
    },
    appendListAsObject: function (element, searchValue, currentPage, pageLimit) {
        element.innerHTML = '';
        var searchValueQuery = '?current-page=' + currentPage + '&row-count=' + pageLimit;
        if (searchValue !== undefined && searchValue !== null && searchValue !== '')
            searchValueQuery += '&search-value=' + searchValue;

        request.submit('GET', '/members/script/files' + searchValueQuery, function (data) {
            var parsedData = JSON.parse(data);
            if (parsedData['storageList'].length <= 0) {
                nav.acceptMsg(5, '아직 등록된 파일이 없으시네요! 화면 상단 우측의 +버튼을 눌러 파일을 추가해보세요!');
                return;
            }
            var storageList = parsedData['storageList'];
            for (var file of storageList) {
                var hiddenForm = document.createElement('form');
                hiddenForm.classList.add('hidden');
                for (var key in file) {
                    var hiddenInput = document.createElement('input');
                    hiddenInput.setAttribute('name', key);
                    hiddenInput.setAttribute('value', file[key]);
                    hiddenForm.appendChild(hiddenInput);
                }

                var container = document.createElement('div');
                var preview = document.createElement('div');
                var title = document.createElement('p');

                //url에서 "/"문자 lastIndex기준으로 잘라내어 파일이름 찾기
                var fileName = file.url.substr(file.url.lastIndexOf('/') + 1, file.url.length);

                container.appendChild(hiddenForm);
                container.appendChild(preview);
                container.appendChild(title);
                container.setAttribute('draggable', 'true');
                container.setAttribute('ondragend', 'chat.uploadDragFile(this)');
                container.setAttribute('title', fileName);
                container.setAttribute('onclick',
                    'file.readyToManipulation(this)');
                element.appendChild(container);
                window.history.replaceState('', '', location.pathname + searchValueQuery);

                if (file.dataType === 'FILE') {
                    text.insert(preview, ASCIIART.FILE, 10);

                } else {
                    var img = document.createElement('img');
                    img.src = file.url;
                    preview.appendChild(img);

                }
                text.insert(title, fileName, 10);
            }

            var pageContainer = document.querySelector('#pagination');
            if (pageContainer === null) return;
            var pageList = parsedData['pagination'];
            var ulTag = document.createElement('ul');
            ulTag.className = 'pagination justify-content-center';
            for (var page of pageList) {
                var aTag = document.createElement('a');
                var liTag = document.createElement('li');
                aTag.className = 'page-link';
                liTag.classList.add('page-item');
                liTag.appendChild(aTag)
                ulTag.appendChild(liTag);


                if (page == 'current-page') {
                    liTag.classList.add('active');
                    aTag.href = '#';
                    aTag.innerHTML = currentPage;
                } else {
                    searchValueQuery = '?current-page=' + page + '&row-count=' + pageLimit;
                    if (searchValue != undefined && searchValue != null && searchValue != '')
                        searchValueQuery += '&search-value=' + searchValue;
                    aTag.href = location.pathname + searchValueQuery
                    aTag.innerHTML = page;

                }
            }
            pageContainer.appendChild(ulTag);

        });
    },
    readyToManipulation: function (container) {
        var url = container.querySelector('form input[name=url]').value;
        var fileName = url.substr(url.lastIndexOf('/') + 1, url.length);
        var childNodes = container.querySelector('form').childNodes;
        var jsonStringData = '{';
        for (var node of childNodes)
            jsonStringData += '\"' + node.getAttribute('name') + '\":' + '\"' + node.getAttribute('value') + '\",';

        jsonStringData = jsonStringData.substr(0, jsonStringData.lastIndexOf(','));
        jsonStringData += '}';
        console.log(jsonStringData);
        modal.open('파일 상세보기',
            '<div style="text-align: center;">' +
            container.innerHTML + '</div><HR>' +
            '<div class="input-group">' +
            '   <input type="text" id="renameString" class="form-control" value="' + fileName + '">' +
            '   <span class="input-group-append">' +
            '       <button type="button" class="btn btn-dark" onclick="document.querySelector(\'#renameString\').value = \'' + fileName + '\'">' +
            '           <i class="fa fa-refresh" aria-hidden="true"></i>' +
            '       </button>' +
            '       <button type="button" id="file-remove-btn" class="btn btn-danger">' +
            '           <i class="fa fa-trash" aria-hidden="true"></i>' +
            '       </button>' +
            '   </span>' +
            '</div>',
            'file.renameFile(\'' + jsonStringData + '\',document.querySelector(\'#renameString\').value)');
        document.querySelector('#file-remove-btn').setAttribute('onclick', 'file.deleteFile(\'' + jsonStringData + '\')');
    }
    ,
    renameFile: function (jsonStringData, renameString) {
        var parsedData = JSON.parse(jsonStringData);
        var fileName = parsedData.url.substr(parsedData.url.lastIndexOf('/') + 1, parsedData.url.length);
        var formData = new FormData();
        for (var key in parsedData) {
            if (key === 'date') continue;
            formData.append(key, parsedData[key]);
        }

        formData.append('renameString', renameString);

        try {
            request.submit('PUT', '/members/' + parsedData.mbr_nick_name + '/files/' + fileName, function (data) {
                if (data === 'false') {
                    window.alert('수정에 실패하였습니다.\n 새로고침 후 다시 시도해주세요.');
                    return;
                }
                modal.close();
                var second = 2;
                nav.acceptMsg(second, renameString + ': 수정이 완료되어 곧 새로고침됩니다.');
                setTimeout(function () {
                    window.history.go(0);
                }, second * 1000);
            }, 'FORMFILE', formData);

        } catch (e) {
            modal.close();
        }
    }
    ,
    deleteFile: function (jsonStringData) {
        var parsedData = JSON.parse(jsonStringData);
        var fileName = parsedData.url.substr(parsedData.url.lastIndexOf('/') + 1, parsedData.url.length);

        try {
            request.submit('DELETE', '/members/' + parsedData.mbr_nick_name + '/files/' + fileName + '?fileNo=' + parsedData.no, function (data) {
                if (data === 'false') {
                    window.alert('삭제에 실패하였습니다.\n 새로고침 후 다시 시도해주세요.');
                    return;
                }
                modal.close();
                var second = 2;
                nav.acceptMsg(second, fileName + ': 삭제가 완료되어 곧 새로고침됩니다.');
                setTimeout(function () {
                    window.history.go(0);
                }, second * 1000);
            }, 'FORM')
        } catch (e) {
            modal.close();
        }
    }
    ,
    upload: function (element) {
        var formData = new FormData();
        var files = element.files;
        for (var file of files)
            formData.append('filePackage', file);

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
    }
    ,

}

var chat = {
    webSocket: undefined,
    roomNumber: undefined,
    nickName: undefined,
    clientNo: undefined,

    createRoom: function () {
        var title = window.prompt("[채팅방 생성: 방 제목]\n채팅방을 개설합니다.\n방 제목을 입력해주세요");
        if (title === null || title === undefined || title === '') return nav.acceptMsg(3, '방 개설을 취소합니다');
        // var pwd = window.prompt("[채팅방 생성: 비밀번호]\n설정하실 비밀번호를 입력해주시고\n입력을 원하지 않는 경우\n취소버튼을 누르시기바랍니다");
        request.submit('POST', '/chatting/new/' + title, function (redirectUrl) {
            if (redirectUrl !== 'false')
                window.location.href = redirectUrl + '?status=new';

        }, 'FORM', 'pwd=' + '')
    },
    connect: function (roomNo, nickName, clientNo) {
        chat.webSocket = new WebSocket("wss://document.xasquatch.net/chat");
        chat.roomNumber = roomNo;
        chat.nickName = nickName;
        chat.clientNo = clientNo;
        window.onbeforeunload = function () {
            chat.disconnect();
            chat.webSocket = null;
        }
        chat.webSocket.onmessage = chat.onMessage;
        chat.webSocket.onclose = function () {
            chat.disconnect();
            chat.webSocket = null;
            // setTimeout(function (){
            //     chat.connect(roomNo, nickName);
            // }, 3000);
        }
        chat.webSocket.onopen = chat.onOpen;
    },
    disconnect: function () {
        chat.webSocket.send(JSON.stringify({
            chatting_room_no: chat.roomNumber,
            messageType: 'LEAVE',
            mbr_nick_name: chat.nickName,
            mbr_no: chat.clientNo
        }));
        chat.webSocket.close();
    },
    send: function (element) {
        var msg = element.value;
        if (msg.length == 0) return;
        chat.webSocket.send(JSON.stringify({
            chatting_room_no: chat.roomNumber,
            messageType: 'CHAT',
            mbr_nick_name: chat.nickName,
            contents: msg,
            mbr_no: chat.clientNo
        }));
        element.value = "";

    },
    uploadDragFile: function (element) {
        var chatInput = document.querySelector('#chatting-msg-input');
        var urlValue = element.querySelector('form input[name=url]').value;
        var uploadTarget = element.querySelector('div').cloneNode(true);
        var container = document.createElement('div');
        uploadTarget.setAttribute('onclick', 'window.open("' + urlValue + '",null,null,false);')
        uploadTarget.style.cursor = 'pointer';
        container.appendChild(uploadTarget);
        chatInput.value = container.innerHTML;
        document.querySelector('#chatting-send-btn').click();

    },
    onOpen: function () {
        chat.webSocket.send(JSON.stringify({
            chatting_room_no: chat.roomNumber,
            messageType: 'ENTER',
            mbr_nick_name: chat.nickName,
            mbr_no: chat.clientNo
        }));
    },
    //메시지 갱신
    onMessage: function (event) {
        var chatroom = document.querySelector('#chatting-contents>div:first-child');
        var parsedData = JSON.parse(event.data);

        if (parsedData.contents === undefined) {
            var clientList = document.querySelector('#chatting-client-list');
            clientList.innerHTML = '';
            for (var client of parsedData)
                chat.getUserList(clientList, client['principal'].nick_name, client['principal'].email);

        } else {
            var addElement = chat.createMsgNode(parsedData.mbr_nick_name, parsedData.contents);

            chatroom.appendChild(addElement);
            chatroom.scrollTo(0, chatroom.scrollHeight);
        }

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
    },
    getUserList: function (targetElement, nickName, email) {
        var component =
            '<tr>' +
            '   <td>' +
            '      <div class="input-group" title="' + nickName + ' (' + email + ')">' +
            '          <select class="btn btn-dark form-control" onchange="chat.manipulateClient(this)">' + //TODO: 함수 넣어야됨
            '              <option selected value="default" readonly="readonly">' +
            '                  <span class="form-control">' + nickName + ' (' + email + ')</span>' +
            '              </option>' +
            '              <option value="info">정보보기</option>' +
            // '              <option value="ban">강퇴</option>' +
            // '              <option value="">위임하기</option>' +
            '          </select>' +
            '      </div>' +
            '   </td>' +
            '</tr>';

        targetElement.innerHTML += component;
    },
    manipulateClient: function (element) {
        element.value = 'default';
        var userInfoString = element.querySelector('option[value=default]').innerText;
        var targetEmail = userInfoString.substring(userInfoString.indexOf('(') + 1, userInfoString.lastIndexOf(')'));
        request.submit('GET', '/members/email/' + targetEmail + '.', function (data) {
            var parsedData = JSON.parse(data);
            modal.open('User Information',
                '<div class="mb-3">' +
                '               <label class="form-label">Nick Name</label>' +
                '               <input type="text" class="form-control" readonly value="' + parsedData['nickName'] + '">' +
                '           </div>' +
                '           <div class="mb-3">' +
                '               <label class="form-label">Email</label>' +
                '               <input type="text" class="form-control" readonly value="' + parsedData['email'] + '">' +
                '           </div>' +
                '           <div class="mb-3">' +
                '               <label class="form-label">가입일</label>' +
                '               <input type="text" class="form-control" readonly value="' + parsedData['date'] + '">' +
                '           </div>', 'modal.close()');

        });
    }
}