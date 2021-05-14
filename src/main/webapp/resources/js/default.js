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

//-text객체 문자열을 character로 변환하여 한 자씩 interval로 반복해가며 삽입
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
    }

}

var headerExpansion = {

    mainHeader: document.querySelector('#main-header'),
    btn: document.querySelector('#main-header-expansion-btn'),
    isDeactivated: false,

    toggle: function () {
        headerExpansion.isDeactivated
            = headerExpansion.mainHeader.classList.toggle('reduced-main-header');
        document.querySelector('.wrap').classList.toggle('reduced-wrap');
        headerExpansion.mainHeader.querySelector('#main-header-logo').classList.toggle('reduced-header-logo');
        headerExpansion.mainHeader.querySelector('#main-header-list').classList.toggle('reduced-header-list');

        var listOfNoneVisible = headerExpansion.mainHeader.querySelectorAll(".d-xl-inline");

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
        headerExpansion.btn.classList.remove('hidden');
        headerExpansion.isDeactivated
            = headerExpansion.mainHeader.classList.add('reduced-main-header');
        document.querySelector('.wrap').classList.add('reduced-wrap');
        headerExpansion.mainHeader.querySelector('#main-header-logo').classList.add('reduced-header-logo');
        headerExpansion.mainHeader.querySelector('#main-header-list').classList.add('reduced-header-list');

        var listOfNoneVisible = headerExpansion.mainHeader.querySelectorAll(".d-xl-inline");

        for (var element of listOfNoneVisible) {
            element.classList.add('d-none');

        }
    },
    activate: function () {
        headerExpansion.btn.classList.add('hidden');
        headerExpansion.isDeactivated
            = headerExpansion.mainHeader.classList.remove('reduced-main-header');
        document.querySelector('.wrap').classList.remove('reduced-wrap');
        headerExpansion.mainHeader.querySelector('#main-header-logo').classList.remove('reduced-header-logo');
        headerExpansion.mainHeader.querySelector('#main-header-list').classList.remove('reduced-header-list');

        var listOfNoneVisible = headerExpansion.mainHeader.querySelectorAll(".d-xl-inline");

        for (var element of listOfNoneVisible) {
            element.classList.remove('d-none');

        }
    }


}

var nav = {
    container: document.querySelector('#main-nav'),
    count: document.querySelector('#main-nav-count'),
    msg: document.querySelector('#main-nav-msg'),
    decreaseCount: function () {
        nav.count.innerHTML--;
    },
    toggle: function () {
        nav.container.classList.toggle('deactivate-nav');
    },
    activate: function (msgCount, msg) {
        nav.container.classList.remove('deactivate-nav');
        nav.count.innerHTML = msgCount;
        nav.msg.innerHTML = msg;
    },
    deactivate: function () {
        nav.container.classList.add('deactivate-nav');
        nav.count.innerHTML = '';
        nav.msg.innerHTML = '';
    },
    revertToFirst: function (interval) {
        clearInterval(interval);
        nav.deactivate();
    },
    acceptMsg: function (msgCount, msg) {
        nav.activate(msgCount, msg);

        var interval = setInterval(function () {
            if (nav.count.innerHTML > 1) {
                nav.decreaseCount();
            } else {
                nav.revertToFirst(interval);
            }

        }, 1000);
    },


}


var modal = {
    title: document.querySelector('.modal-title'),
    contents: document.querySelector('.modal-body'),
    confirm: document.querySelector('.modal-footer>button:first-child'),
    cancel: document.querySelector('.modal-footer>button:last-child'),

    open: function (titleInput, contentsInput, confirmInput) {
        modal.title.innerHTML = titleInput;
        modal.contents.innerHTML = contentsInput;
        modal.confirm.setAttribute('onclick', confirmInput);

        $('#myModal').modal();
    },
    close: function () {
        modal.title.innerHTML = '';
        modal.contents.innerHTML = '';
        modal.confirm.removeAttribute('onclick');
        modal.cancel.click();
    }

}

var sign = {
    upPage: function () {
        request.submit('GET', '/resources/html/sign-up.html', function (data) {
            modal.open('회원가입', data, 'sign.up()');

        })
    },
    up: function () {
        var emailInput = document.querySelector('#sign-up-email');
        var pwdInput = document.querySelector('#sign-up-pwd');
        var pwdConfirmInput = document.querySelector('#sign-up-pwd-confirm');
        var nickNameInput = document.querySelector('#sign-up-nickName');
        var msgBox = '[회원가입 실패: 다음을 확인해주세요]\n';
        if (!sign.isAvailableEmail(emailInput.value)) msgBox += '- 이메일 형식이 맞지 않습니다.\n';
        if (!sign.isAvailablePwd(pwdInput.value)) msgBox += '- 비밀번호 형식이 맞지 않습니다.\n';
        if (pwdInput.value !== pwdConfirmInput.value) msgBox += '- 비밀번호가 일치하지 않습니다.\n';
        if (!sign.isAvailableNickName(nickNameInput.value)) msgBox += '- 이름 형식이 맞지 않습니다.\n';

        if (msgBox !== '[회원가입 실패: 다음을 확인해주세요]\n') {
            alert(msgBox);
            return;
        }

        var signForm = document.querySelector('#sign-up-form');
        signForm.appendChild(document.querySelector('#csrf-input'));
        var formData = new FormData(signForm);
        request.submit('POST', '/members/new/' + nickNameInput.value, function (data) {

            // data값 boolean여부따라 결정
            if (data === 'false') {
                nav.acceptMsg(3, '계정 생성에 실패하였습니다. 잠시 후 다시 이용바랍니다.');
                return;
            }
            modal.close();
            nav.acceptMsg(3, nickNameInput.value + '님 환영합니다. 입력하신 정보로 로그인 후 이용바랍니다.')
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
        var regExp = /^[A-Za-z0-9가-힣 ]{2,20}/
        return regExp.test(data);
    },
    confirmAvailableEmail: function (element) {
        var msgBox = document.querySelector('#emailHelp');
        if (sign.isAvailableEmail(element.value)) {
            request.submit('GET', '/members/available-email/' + element.value, function (available) {
                if (available === 'true') {
                    msgBox.innerHTML = '사용가능한 이메일입니다.'
                    msgBox.style.color = 'green';
                } else {
                    msgBox.innerHTML = '이미 사용중인 이메일입니다.'
                    msgBox.style.color = 'red';
                }
            });

        } else {
            msgBox.innerHTML = '잘 못된 형식의 이메일입니다. 다시 확인해주세요'
            msgBox.style.color = 'red';

        }
    },
    tokenCountDown: function (element) {
        var emailInput = document.querySelector('#sign-up-email');
        element.setAttribute('onclick', "window.alert('이미 인증요청을 시도하였습니다.')");
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
                    window.alert('[인증요청 완료]\n' + emailInput.value + '\n해당 이메일을 확인해주시기바랍니다.')

                } else {
                    window.alert("[인증요청 실패]\n페이지 새로고침 후 다시 시도해주시기바랍니다.")

                }
            });
    },
    confirmEmailToken: function () {
        var emailInput = document.querySelector('#sign-up-email');
        var tokenValue = document.querySelector('#sign-up-email-token');
        //서버 통신 후 성공여부
        request.submit('GET', '/members/confirm-token/' + emailInput.value + '/status', function (isConfirmed) {
            // 성공시
            if (isConfirmed === 'true') {
                sign.successEmailAuthorization();
                //실패
            } else {
                window.alert('인증확인이 되지않았습니다.\n이메일을 다시 확인해주세요.');
            }
        });

    },
    successEmailAuthorization: function () {
        var msgBox = document.querySelector('#emailHelp');
        msgBox.style.color = 'green';
        msgBox.innerHTML = '이메일 인증에 성공하였습니다.'
        document.querySelector('#sign-up-email').setAttribute('readonly', 'readonly');
        document.querySelector('#sign-up-email-token-submit').setAttribute('onclick', '');

    },
    confirmAvailablePwd: function (element) {
        var msgBox = document.querySelector('#pwdHelp');
        if (sign.isAvailablePwd(element.value)) {
            msgBox.innerHTML = '올바른 형식의 비밀번호입니다.'
            msgBox.style.color = 'green';

        } else {
            msgBox.innerHTML = '잘 못된 형식의 비밀번호입니다. 다시 확인해주세요'
            msgBox.style.color = 'red';

        }
    },
    confirmSamePwd: function (element) {
        var msgBox = document.querySelector('#pwdConfirmHelp');
        if (document.querySelector('#sign-up-pwd').value === element.value) {
            msgBox.innerHTML = '비밀번호가 일치합니다.'
            msgBox.style.color = 'green';
        } else {
            msgBox.innerHTML = '비밀번호가 일치하지않습니다. 다시 확인해주세요'
            msgBox.style.color = 'red';

        }
    },
    confirmAvailableNickName: function (element) {
        var msgBox = document.querySelector('#nickNameHelp');
        if (sign.isAvailableNickName(element.value)) {
            request.submit('GET', '/members/available-nick-name/' + element.value, function (available) {
                if (available === 'true') {
                    msgBox.innerHTML = '사용가능한 닉네임입니다.'
                    msgBox.style.color = 'green';
                } else {
                    msgBox.innerHTML = '이미 사용중인 닉네임입니다.'
                    msgBox.style.color = 'red';
                }
            });
        } else {
            msgBox.innerHTML = '잘 못된 형식의 닉네임입니다. 다시 확인해주세요'
            msgBox.style.color = 'red';

        }
    },
}
var login = {
    submit: function () {
        var emailInput = document.querySelector('#login-email');
        var pwdInput = document.querySelector('#login-pwd');
        var msgBox = '[로그인 실패: 다음을 확인해주세요]\n';
        if (!sign.isAvailableEmail(emailInput.value)) msgBox += '- 이메일 형식이 맞지 않습니다.\n';
        if (!sign.isAvailablePwd(pwdInput.value)) msgBox += '- 비밀번호 형식이 맞지 않습니다.\n';

        if (msgBox !== '[로그인 실패: 다음을 확인해주세요]\n') {
            alert(msgBox);
            return;
        }
        document.querySelector('#login-form').submit();
    },
    confirmAvailableEmail: function (element) {
        var msgBox = document.querySelector('#loginEmailHelp');
        if (sign.isAvailableEmail(element.value)) {
            msgBox.innerHTML = '올바른 이메일형식입니다.'
            msgBox.style.color = 'green';

        } else {
            msgBox.innerHTML = '잘 못된 형식의 이메일입니다. 다시 확인해주세요'
            msgBox.style.color = 'red';

        }

    },
    confirmAvailablePwd: function (element) {
        var msgBox = document.querySelector('#loginPwdHelp');
        if (sign.isAvailablePwd(element.value)) {
            msgBox.innerHTML = '올바른 형식의 비밀번호입니다.'
            msgBox.style.color = 'green';

        } else {
            msgBox.innerHTML = '잘 못된 형식의 비밀번호입니다. 다시 확인해주세요'
            msgBox.style.color = 'red';

        }

    }
}
var memberInfo = {

    modify: function (nickName) {
        var pwdInput = document.querySelector('#info-pwd');
        var pwdConfirmInput = document.querySelector('#info-pwd-confirm');
        var nickNameInput = document.querySelector('#info-nickName');
        var msgBox = '[회원가입 실패: 다음을 확인해주세요]\n';

        if (!sign.isAvailablePwd(pwdInput.value)) msgBox += '- 비밀번호 형식이 맞지 않습니다.\n';
        if (pwdInput.value !== pwdConfirmInput.value) msgBox += '- 비밀번호가 일치하지 않습니다.\n';
        if (!sign.isAvailableNickName(nickNameInput.value)) msgBox += '- 이름 형식이 맞지 않습니다.\n';
        if (msgBox !== '[회원가입 실패: 다음을 확인해주세요]\n') {
            alert(msgBox);
            return;
        }

        var userInfoForm = document.querySelector('#user-information-form');
        var formData = new FormData(userInfoForm);
        request.submit('PUT', '/members/' + nickName, function (isSuccesses) {
            if (isSuccesses === 'true') {
                window.alert('[수정 완료]\n수정이 완료되었습니다.\n수정된 정보로 다시 로그인바랍니다.');
                document.querySelector('#logout').submit();
            } else {
                window.alert('[수정 실패]\n수정에 실패하였습니다.\n잠시 후 다시 시도해주시기바랍니다.\n문의처:test@test.com');

            }

        }, 'FORMFILE', formData);
    },
    delete: function (nickName) {
        if (window.prompt("[회원 탈퇴]\n탈퇴를 진행합니다. 동의하시면 \"동의\"라고 적어주시기바랍니다.") !== '동의') return;
        var userInfoForm = document.querySelector('#user-information-form');
        var formData = new FormData(userInfoForm);
        var urlString = '';
        formData.forEach((value, key) => {
            urlString += key + '=' + value + '&';
        });
        request.submit('DELETE', '/members/' + nickName + '?' + urlString, function (isSuccesses) {
            if (isSuccesses === 'true') {
                window.alert('[회원 탈퇴 완료]\n그 동안 이용해 주셔서 감사합니다.');
                document.querySelector('#logout').submit();
            } else {
                window.alert('[회원 탈퇴 실패]\n탈퇴 과정 중 에러가 발생하였습니다.\n잠시 후 다시 시도해주시기바랍니다.\n문의처:test@test.com');

            }
        }, '');
    }
}