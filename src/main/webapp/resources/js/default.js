var request = {
    json: 'application; charset=utf-8',
    form: 'application/x-www-form-urlencoded; charset=utf-8',
    formFile: 'multipart/form-data; charset=utf-8',

    setContentsType: function (inputContentsType) {
        var contentsType = 'text/plain; charset=utf-8';

        if (inputContentsType.toUpperCase() === 'FORM') {
            contentsType = request.form;

        } else if (inputContentsType.toUpperCase() === 'FORMFILE') {
            contentsType = request.formFile;

        } else if (inputContentsType.toUpperCase() === 'JSON') {
            contentsType = request.json;

        }
        return contentsType;
    },

    submit: function (method, url, callback, inputContentsType, sendData) {
        var xhr = new XMLHttpRequest();

        var result = null;
        var contentsType = null;

        xhr.onreadystatechange = function () {
            if (xhr.readyState === xhr.DONE) {
                if (xhr.status === 200 || xhr.status === 201) {
                    result = xhr.response;
                    callback(result);
                }
            }
        };
        if (method.toUpperCase() === 'GET') {
            xhr.open(method, url);
            xhr.send();
        } else if (method.toUpperCase() === 'POST' || method.toUpperCase() === 'PUT' || method.toUpperCase() === 'DELETE' || method.toUpperCase() === 'PATCH') {
            method = method.toUpperCase();
            contentsType = request.setContentsType(inputContentsType);
            xhr.open(method, url, true);
            if (contentsType !== request.formFile)
                xhr.setRequestHeader('Content-Type', contentsType);
            xhr.send(sendData);
        }

    },
    submitWithCSRF: function (method, url, callback, inputContentsType, sendData) {
        var xhr = new XMLHttpRequest();
        xhr.open('GET', '/', false);
        xhr.withCredentials = true;
        xhr.setRequestHeader('x-xsrf-token', 'fetch');
        xhr.setRequestHeader("Accept", "application/json");
        xhr.setRequestHeader("Content-Type", "application/json; charset=utf-8")
        xhr.send();

        var result = null;
        var contentsType = null;
        if (xhr.readyState === 4) {
            xhr.onreadystatechange = function () {
                if (xhr.readyState === xhr.DONE && (xhr.status === 200 || xhr.status === 201)) {
                    result = xhr.response;
                    callback(result);
                }
            }
            switch (method.toUpperCase()) {
                case 'GET':
                    xhr.open(method, url);
                    xhr.send();
                    break;

                default:
                    method = method.toUpperCase();
                    contentsType = request.setContentsType(inputContentsType);
                    xhr.open(method, url, true);
                    if (contentsType !== request.formFile)
                        xhr.setRequestHeader('Content-Type', contentsType);
                    xhr.send(sendData);
                    break;

            }


        }
    }
}

//-text?????? ???????????? character??? ???????????? ??? ?????? interval??? ??????????????? ??????
var text = {

    soloTagList: ['IMG', 'BR', 'HR', 'META', 'LINK', 'INPUT'],

    insert: function (element, insertTextValue, interval, callback) {
        try {
            text.insertForObject(element, insertTextValue, interval, callback)
        } catch (e) {
            text.insertForTagName(element, insertTextValue, interval, callback);
        }
    },
    operation: function (insertTextValue, frontSpan, backSpan, callback, interval) {
        var timeCount = true;
        var loopCount = 0;
        var intervalAddr = setInterval(function () {
            try {

                var character = insertTextValue.charAt(loopCount);
                if (insertTextValue.length === loopCount) {
                    throw 'go catch';

                } else if (character === '<') {
                    var textSubString = insertTextValue.substr(loopCount, insertTextValue.length);
                    var startTagEndIndex = textSubString.indexOf('>');
                    var tagAttributes = textSubString.substr(0, startTagEndIndex + 1);
                    var tagName;
                    var isSoloTag = false;
                    var soloTagList = text.soloTagList;
                    if (tagAttributes.indexOf(' ') !== -1) {
                        tagName = textSubString.substring(1, tagAttributes.indexOf(' '));

                    } else {
                        tagName = textSubString.substring(1, startTagEndIndex);

                    }
                    for (var soloTag of soloTagList) {
                        if (soloTag === tagName.toUpperCase()) {
                            isSoloTag = true;
                            break;
                        }
                    }
                    var tagContents;
                    if (isSoloTag) {
                        tagContents = textSubString.substring(0, startTagEndIndex + 1);

                    } else {
                        var closeTagName = '</' + tagName + '>';
                        var closeTagLastIndex = textSubString.lastIndexOf(closeTagName);
                        tagContents = textSubString.substring(0, closeTagLastIndex + closeTagName.length);

                    }
                    frontSpan.innerHTML += tagContents;
                    backSpan.innerHTML = '';
                    timeCount = true;
                    loopCount += tagContents.length;

                } else if (timeCount === true) {
                    backSpan.innerHTML = insertTextValue.charAt(loopCount);
                    timeCount = false;

                } else if (character === '\\') {
                    loopCount++;

                } else {
                    frontSpan.innerHTML += backSpan.innerHTML;
                    backSpan.innerHTML = '';
                    timeCount = true;
                    loopCount++;

                }

            } catch (error) {
                clearInterval(intervalAddr);
                intervalAddr = null;
                try {
                    callback();
                } catch (error) {
                    console.log('Ended writing Text');
                }
            }
        }, interval);
    },
    insertForObject: function (element, insertTextValue, interval, callback) {
        var frontSpan = document.createElement('span');
        var backSpan = document.createElement('span');
        backSpan.className = 'input-character';
        element.appendChild(frontSpan);
        element.appendChild(backSpan);
        text.operation(insertTextValue, frontSpan, backSpan, callback, interval);

    },
    insertForTagName: function (tagName, insertTextValue, interval, callback) {

        var frontSpan = document.createElement('span');
        var backSpan = document.createElement('span');
        backSpan.className = 'input-character';
        document.querySelector(tagName).appendChild(frontSpan);
        document.querySelector(tagName).appendChild(backSpan);
        text.operation(insertTextValue, frontSpan, backSpan, callback, interval);

    }
};

var drag = {

    addElement: function (element) {
        drag.mouseElement(element);
        drag.touchElement(element);
    },
    addCloneElement: function (element) {
        drag.mouseCloneElement(element);
        drag.touchCloneElement(element);
    },

    addExpansionElement: function (element) {
        drag.mouseElementX(element);
        drag.touchElementX(element);
    },

    mouseElementX: function (element) {
        var currentPos = 0;
        var originContainerWidth = element.style.width;

        try {
            element.onmousedown = dragMouseDown;
        } catch (e) {
            console.log(e);
        }

        function dragMouseDown(event) {
            event = event || window.event;
            event.preventDefault();
            currentPos = event.clientX;
            document.onmouseup = function () {
                if (currentPos > 200) headerExpansion.toggle();
                element.style.width = originContainerWidth;
                closeDragElement();
            }
            document.onmousemove = elementDrag;
        }

        function elementDrag(event) {
            if (event.clientX < 200) return;

            event = event || window.event;
            event.preventDefault();
            currentPos = event.clientX;
            element.setAttribute('style',
                'width : ' + currentPos + 'px !important');
        }

        function closeDragElement() {
            document.onmouseup = null;
            document.onmousemove = null;
        }

    },

    touchElementX: function (element) {
        var currentPos = 0;
        var originContainerWidth = element.style.width;

        try {
            element.ontouchstart = dragTouchStart;
        } catch (e) {
            console.log(e);
        }

        function dragTouchStart(event) {
            event = event || window.event;
            currentPos = event.clientX;
            document.ontouchend = function () {
                if (currentPos > 200) headerExpansion.toggle();
                element.style.width = originContainerWidth;
                closeDragElement();
            }
            document.ontouchmove = elementDrag;
        }

        function elementDrag(event) {
            if (event.changedTouches[0].clientX < 200) return;

            event = event || window.event;
            event.preventDefault();
            currentPos = event.changedTouches[0].clientX;
            element.setAttribute('style',
                'width : ' + currentPos + 'px !important');
        }

        function closeDragElement() {
            document.ontouchend = null;
            document.ontouchmove = null;
        }
    },

    mouseElement: function (element) {
        var pos1 = 0, pos2 = 0, pos3 = 0, pos4 = 0;
        try {
            element.onmousedown = dragMouseDown;
        } catch (e) {
            console.log(e);
        }

        function dragMouseDown(event) {
            event = event || window.event;
            event.preventDefault();
            pos3 = event.clientX;
            pos4 = event.clientY;
            document.onmouseup = closeDragElement;
            document.onmousemove = elementDrag;
        }

        function elementDrag(event) {
            event = event || window.event;
            event.preventDefault();
            pos1 = pos3 - event.clientX;
            pos2 = pos4 - event.clientY;
            pos3 = event.clientX;
            pos4 = event.clientY;
            element.parentNode.style.top = (element.parentNode.offsetTop - pos2) + "px";
            element.parentNode.style.left = (element.parentNode.offsetLeft - pos1) + "px";
        }

        function closeDragElement() {
            document.onmouseup = null;
            document.onmousemove = null;
        }

    },

    touchElement: function (element) {
        var pos1 = 0, pos2 = 0, pos3 = 0, pos4 = 0;
        try {
            element.ontouchstart = dragTouchStart;
        } catch (e) {
            console.log(e);
        }

        function dragTouchStart(event) {
            event = event || window.event;
            pos3 = event.clientX;
            pos4 = event.clientY;
            document.ontouchend = closeDragElement;
            document.ontouchmove = elementDrag;
        }

        function elementDrag(event) {
            event = event || window.event;
            pos1 = pos3 - event.changedTouches[0].clientX;
            pos2 = pos4 - event.changedTouches[0].clientY;
            pos3 = event.changedTouches[0].clientX;
            pos4 = event.changedTouches[0].clientY;
            element.parentNode.style.top = (element.parentNode.offsetTop - pos2) + "px";
            element.parentNode.style.left = (element.parentNode.offsetLeft - pos1) + "px";
        }

        function closeDragElement() {
            document.ontouchend = null;
            document.ontouchmove = null;
        }
    },

    mouseCloneElement: function (element) {
        var elementClone;
        try {
            element.onmousedown = dragMouseDown;
        } catch (e) {
            console.log(e);
        }

        function dragMouseDown(event) {
            event = event || window.event;
            event.preventDefault();

            elementClone = element.cloneNode(true);
            elementClone.setAttribute('onclick', '');
            elementClone.classList.add('dragElement');
            elementClone.style.top = event.clientY + "px";
            elementClone.style.left = event.clientX + "px";
            document.querySelector('html').appendChild(elementClone);

            document.onmouseup = closeDragElement;
            document.onmousemove = elementDrag;
        }

        function elementDrag(event) {
            event = event || window.event;
            event.preventDefault();
            elementClone.style.top = (event.clientY + 5) + "px";
            elementClone.style.left = (event.clientX + 5) + "px";
        }

        function closeDragElement(event) {
            elementClone.remove();
            document.onmouseup = null;
            document.onmousemove = null;
            if (isContainTargetParentNode(event.target))
                chat.uploadDragFile(elementClone);

        }

        function isContainTargetParentNode(element) {
            try {
                if (element === document.querySelector('#chatting-contents>div:first-child')) {
                    return true;
                } else {
                    return isContainTargetParentNode(element.parentNode);
                }
            } catch (e) {
                return false;
            }
        }

    },

    touchCloneElement: function (element) {
        var elementClone;
        try {
            element.ontouchstart = dragTouchStart;
        } catch (e) {
            console.log(e);
        }

        function dragTouchStart(event) {
            event = event || window.event;

            elementClone = element.cloneNode(true);
            elementClone.setAttribute('onclick', '');
            elementClone.classList.add('dragElement');
            elementClone.style.top = event.clientY + "px";
            elementClone.style.left = event.clientX + "px";
            document.querySelector('html').appendChild(elementClone);

            document.ontouchend = closeDragElement;
            document.ontouchmove = elementDrag;
        }

        function elementDrag(event) {
            event = event || window.event;
            elementClone.style.top = (event.changedTouches[0].clientY + 30) + "px";
            elementClone.style.left = (event.changedTouches[0].clientX + 30) + "px";


        }

        function closeDragElement(event) {
            elementClone.remove();
            document.ontouchend = null;
            document.ontouchmove = null;
            if (isContainTargetParentNode(document.elementFromPoint(event.changedTouches[0].clientX, event.changedTouches[0].clientY)))
                chat.uploadDragFile(elementClone);
        }

        function isContainTargetParentNode(element) {
            try {
                if (element === document.querySelector('#chatting-contents>div:first-child')) {
                    return true;
                } else {
                    return isContainTargetParentNode(element.parentNode);
                }
            } catch (e) {
                return false;
            }
        }

    }

}

var headerExpansion = {

    isDeactivated: false,

    toggle: function () {
        var mainHeader = document.querySelector('#main-header');
        headerExpansion.isDeactivated
            = mainHeader.classList.toggle('reduced-main-header');
        document.querySelector('.wrap').classList.toggle('reduced-wrap');
        mainHeader.querySelector('#main-header-logo').classList.toggle('reduced-header-logo');
        mainHeader.querySelector('#main-header-list').classList.toggle('reduced-header-list');

        var listOfNoneVisible = mainHeader.querySelectorAll(".d-xl-inline");

        for (var element of listOfNoneVisible) {
            element.classList.toggle('d-none');

        }
        /*
                if (headerExpansion.isDeactivated) {
                    headerExpansion.btn.classList.remove('hidden');

                } else {
                    headerExpansion.btn.classList.add('hidden');

                }
        */

    },
    deactivate: function () {
        var mainHeader = document.querySelector('#main-header');
        var btn = document.querySelector('#main-header-expansion-btn');
        btn.classList.remove('hidden');
        headerExpansion.isDeactivated = mainHeader.classList.add('reduced-main-header');
        document.querySelector('.wrap').classList.add('reduced-wrap');
        mainHeader.querySelector('#main-header-logo').classList.add('reduced-header-logo');
        mainHeader.querySelector('#main-header-list').classList.add('reduced-header-list');

        var listOfNoneVisible = mainHeader.querySelectorAll(".d-xl-inline");

        for (var element of listOfNoneVisible) {
            element.classList.add('d-none');

        }
    },
    activate: function () {
        var btn = document.querySelector('#main-header-expansion-btn');
        var mainHeader = document.querySelector('#main-header');
        btn.classList.add('hidden');
        headerExpansion.isDeactivated = mainHeader.classList.remove('reduced-main-header');
        document.querySelector('.wrap').classList.remove('reduced-wrap');
        mainHeader.querySelector('#main-header-logo').classList.remove('reduced-header-logo');
        mainHeader.querySelector('#main-header-list').classList.remove('reduced-header-list');

        var listOfNoneVisible = mainHeader.querySelectorAll(".d-xl-inline");

        for (var element of listOfNoneVisible) {
            element.classList.remove('d-none');

        }
    }


}

var nav = {
    decreaseCount: function () {
        document.querySelector('#main-nav-count').innerHTML--;
    },
    toggle: function () {
        document.querySelector('#main-nav').classList.toggle('deactivate-nav');
    },
    activate: function (msgCount, msg) {
        document.querySelector('#main-nav').classList.remove('deactivate-nav');
        document.querySelector('#main-nav-count').innerHTML = msgCount;
        document.querySelector('#main-nav-msg').innerHTML = msg;
    },
    deactivate: function () {
        document.querySelector('#main-nav').classList.add('deactivate-nav');
        document.querySelector('#main-nav-count').innerHTML = '';
        document.querySelector('#main-nav-msg').innerHTML = '';
    },
    revertToFirst: function (interval) {
        clearInterval(interval);
        nav.deactivate();
    },
    acceptMsg: function (msgCount, msg) {
        nav.activate(msgCount, msg);

        var interval = setInterval(function () {
            if (document.querySelector('#main-nav-count').innerHTML > 1) {
                nav.decreaseCount();
            } else {
                nav.revertToFirst(interval);
            }

        }, 1000);
    },


}


var modal = {
    open: function (titleInput, contentsInput, confirmInput) {
        document.querySelector('.modal-title').innerHTML = titleInput;
        document.querySelector('.modal-body').innerHTML = contentsInput;
        document.querySelector('.modal-footer>button:first-child').setAttribute('onclick', confirmInput);

        $('#myModal').modal();
    },
    close: function () {
        document.querySelector('.modal-title').innerHTML = '';
        document.querySelector('.modal-body').innerHTML = '';
        document.querySelector('.modal-footer>button:first-child').removeAttribute('onclick');
        document.querySelector('.modal-footer>button:last-child').click();
    }

}

var sign = {
    upPage: function () {
        request.submit('GET', '/resources/html/sign-up.html', function (data) {
            modal.open('????????????', data, 'sign.up()');

        })
    },
    up: function () {
        var emailInput = document.querySelector('#sign-up-email');
        var pwdInput = document.querySelector('#sign-up-pwd');
        var pwdConfirmInput = document.querySelector('#sign-up-pwd-confirm');
        var nickNameInput = document.querySelector('#sign-up-nickName');
        var msgBox = '[???????????? ??????: ????????? ??????????????????]\n';
        if (!sign.isAvailableEmail(emailInput.value)) msgBox += '- ????????? ????????? ?????? ????????????.\n';
        if (!sign.isAvailablePwd(pwdInput.value)) msgBox += '- ???????????? ????????? ?????? ????????????.\n';
        if (pwdInput.value !== pwdConfirmInput.value) msgBox += '- ??????????????? ???????????? ????????????.\n';
        if (!sign.isAvailableNickName(nickNameInput.value)) msgBox += '- ?????? ????????? ?????? ????????????.\n';

        if (msgBox !== '[???????????? ??????: ????????? ??????????????????]\n') {
            alert(msgBox);
            return;
        }

        var signForm = document.querySelector('#sign-up-form');
        signForm.appendChild(document.querySelector('#csrf-input'));
        var formData = new FormData(signForm);
        request.submit('POST', '/members/new/' + nickNameInput.value, function (data) {

            // data??? boolean???????????? ??????
            if (data === 'false') {
                nav.acceptMsg(3, '?????? ????????? ?????????????????????. ?????? ??? ?????? ??????????????????.');
                return;
            }
            modal.close();
            nav.acceptMsg(3, nickNameInput.value + '??? ???????????????. ???????????? ????????? ????????? ??? ??????????????????.')
            document.querySelector('#login-email').value = emailInput.value;
        }, 'FORMFILE', formData);
    },
    isAvailableEmail: function (data) {
        var regExp = /^[0-9a-zA-Z]([-_.]?[0-9a-zA-Z])*@[0-9a-zA-Z]([-_.]?[0-9a-zA-Z])*.[a-zA-Z]{2,3}/;
        return regExp.test(data);
    },
    isAvailablePwd: function (data) {
        var regExp = /^[a-z0-9]{8,20}/
        return regExp.test(data);
    },
    isAvailableNickName: function (data) {
        var regExp = /^[A-Za-z0-9???-??? ]{2,20}/
        return regExp.test(data);
    },
    confirmAvailableEmail: function (element) {
        //????????? ???????????? dot(.)??? ???????????? ???????????? ????????? ?????? ????????? ??????

        var msgBox = document.querySelector('#emailHelp');
        if (sign.isAvailableEmail(element.value)) {
            request.submit('GET', '/members/available-email/' + element.value + '.', function (available) {
                if (available === 'true') {
                    msgBox.innerHTML = '??????????????? ??????????????????.'
                    msgBox.style.color = 'green';
                } else {
                    msgBox.innerHTML = '?????? ???????????? ??????????????????.'
                    msgBox.style.color = 'red';
                }
            });

        } else {
            msgBox.innerHTML = '??? ?????? ????????? ??????????????????. ?????? ??????????????????'
            msgBox.style.color = 'red';

        }
    },
    tokenCountDown: function (element) {
        var emailInput = document.querySelector('#sign-up-email');
        element.setAttribute('onclick', "window.alert('?????? ??????????????? ?????????????????????.')");
        var originValue = element.innerHTML;
        var count = 60 * 5;
        var intervalNumber = setInterval(function () {
            try {
                if (count < 0) throw new Error('Count zero');
                if (emailInput.getAttribute('readonly') === 'readonly')
                    new Error('success Email Authorization');
                count--;
                element.innerHTML = originValue + '(' + count + ')';
            } catch (e) {
                clearInterval(intervalNumber);
            }

        }, 1000);
        request.submit('GET', '/members/confirm-token/count-down?email=' + emailInput.value,
            function (data) {
                if (data === "true") {
                    window.alert('[???????????? ??????]\n' + emailInput.value + '\n?????? ???????????? ??????????????????????????????.')

                } else {
                    window.alert("[???????????? ??????]\n????????? ???????????? ??? ?????? ??????????????????????????????.")

                }
            });
    },
    confirmEmailToken: function () {
        var emailInput = document.querySelector('#sign-up-email');
        var tokenValue = document.querySelector('#sign-up-email-token');
        //?????? ?????? ??? ????????????
        request.submit('GET', '/members/confirm-token/' + emailInput.value + '/status', function (isConfirmed) {
            // ?????????
            if (isConfirmed === 'true') {
                sign.successEmailAuthorization();
                //??????
            } else {
                window.alert('??????????????? ?????????????????????.\n???????????? ?????? ??????????????????.');
            }
        });

    },
    successEmailAuthorization: function () {
        var msgBox = document.querySelector('#emailHelp');
        msgBox.style.color = 'green';
        msgBox.innerHTML = '????????? ????????? ?????????????????????.'
        document.querySelector('#sign-up-email').setAttribute('readonly', 'readonly');
        document.querySelector('#sign-up-email-token-submit').setAttribute('onclick', '');

    },
    confirmAvailablePwd: function (element) {
        var msgBox = document.querySelector('#pwdHelp');
        if (sign.isAvailablePwd(element.value)) {
            msgBox.innerHTML = '????????? ????????? ?????????????????????.'
            msgBox.style.color = 'green';

        } else {
            msgBox.innerHTML = '??? ?????? ????????? ?????????????????????. ?????? ??????????????????'
            msgBox.style.color = 'red';

        }
    },
    confirmSamePwd: function (element) {
        var msgBox = document.querySelector('#pwdConfirmHelp');
        if (document.querySelector('#sign-up-pwd').value === element.value) {
            msgBox.innerHTML = '??????????????? ???????????????.'
            msgBox.style.color = 'green';
        } else {
            msgBox.innerHTML = '??????????????? ????????????????????????. ?????? ??????????????????'
            msgBox.style.color = 'red';

        }
    },
    confirmAvailableNickName: function (element) {
        var msgBox = document.querySelector('#nickNameHelp');
        if (sign.isAvailableNickName(element.value)) {
            request.submit('GET', '/members/available-nick-name/' + element.value, function (available) {
                if (available === 'true') {
                    msgBox.innerHTML = '??????????????? ??????????????????.'
                    msgBox.style.color = 'green';
                } else {
                    msgBox.innerHTML = '?????? ???????????? ??????????????????.'
                    msgBox.style.color = 'red';
                }
            });
        } else {
            msgBox.innerHTML = '??? ?????? ????????? ??????????????????. ?????? ??????????????????'
            msgBox.style.color = 'red';

        }
    },
}
var login = {
    submit: function () {
        var emailInput = document.querySelector('#login-email');
        var pwdInput = document.querySelector('#login-pwd');
        var msgBox = '[????????? ??????: ????????? ??????????????????]\n';
        if (!sign.isAvailableEmail(emailInput.value)) msgBox += '- ????????? ????????? ?????? ????????????.\n';
        if (!sign.isAvailablePwd(pwdInput.value)) msgBox += '- ???????????? ????????? ?????? ????????????.\n';

        if (msgBox !== '[????????? ??????: ????????? ??????????????????]\n') {
            alert(msgBox);
            return;
        }
        document.querySelector('#login-form').submit();
    },
    confirmAvailableEmail: function (element) {
        var msgBox = document.querySelector('#loginEmailHelp');
        if (sign.isAvailableEmail(element.value)) {
            msgBox.innerHTML = '????????? ????????????????????????.'
            msgBox.style.color = 'green';

        } else {
            msgBox.innerHTML = '??? ?????? ????????? ??????????????????. ?????? ??????????????????'
            msgBox.style.color = 'red';

        }

    },
    confirmAvailablePwd: function (element) {
        var msgBox = document.querySelector('#loginPwdHelp');
        if (sign.isAvailablePwd(element.value)) {
            msgBox.innerHTML = '????????? ????????? ?????????????????????.'
            msgBox.style.color = 'green';

        } else {
            msgBox.innerHTML = '??? ?????? ????????? ?????????????????????. ?????? ??????????????????'
            msgBox.style.color = 'red';

        }

    }
}
var memberInfo = {

    modify: function (nickName) {
        var pwdInput = document.querySelector('#info-pwd');
        var pwdConfirmInput = document.querySelector('#info-pwd-confirm');
        var nickNameInput = document.querySelector('#info-nickName');
        var msgBox = '[???????????? ??????: ????????? ??????????????????]\n';

        if (!sign.isAvailablePwd(pwdInput.value)) msgBox += '- ???????????? ????????? ?????? ????????????.\n';
        if (pwdInput.value !== pwdConfirmInput.value) msgBox += '- ??????????????? ???????????? ????????????.\n';
        if (!sign.isAvailableNickName(nickNameInput.value)) msgBox += '- ?????? ????????? ?????? ????????????.\n';
        if (msgBox !== '[???????????? ??????: ????????? ??????????????????]\n') {
            alert(msgBox);
            return;
        }

        var userInfoForm = document.querySelector('#user-information-form');
        var formData = new FormData(userInfoForm);
        request.submit('PUT', '/members/' + nickName, function (isSuccesses) {
            if (isSuccesses === 'true') {
                window.alert('[?????? ??????]\n????????? ?????????????????????.\n????????? ????????? ?????? ?????????????????????.');
                document.querySelector('#logout').submit();
            } else {
                window.alert('[?????? ??????]\n????????? ?????????????????????.\n?????? ??? ?????? ??????????????????????????????.\n?????????:test@test.com');

            }

        }, 'FORMFILE', formData);
    },
    delete: function (nickName) {
        if (window.prompt("[?????? ??????]\n????????? ???????????????. ??????????????? \"??????\"?????? ???????????????????????????.") !== '??????') return;
        var userInfoForm = document.querySelector('#user-information-form');
        var formData = new FormData(userInfoForm);
        var urlString = '';
        formData.forEach((value, key) => {
            urlString += key + '=' + value + '&';
        });
        request.submit('DELETE', '/members/' + nickName + '?' + urlString, function (isSuccesses) {
            if (isSuccesses === 'true') {
                window.alert('[?????? ?????? ??????]\n??? ?????? ????????? ????????? ???????????????.');
                document.querySelector('#logout').submit();
            } else {
                window.alert('[?????? ?????? ??????]\n?????? ?????? ??? ????????? ?????????????????????.\n?????? ??? ?????? ??????????????????????????????.\n?????????:test@test.com');

            }
        }, '');
    }
}