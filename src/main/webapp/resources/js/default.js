var request = {
    json: 'application/json',
    form: 'application/x-www-form-urlencoded',
    formFile: 'multipart/form-data',

    setContentsType: function (inputContentsType) {
        var contentsType = 'text/plain';

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
                    debugger;
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
        var formData = new FormData(signForm);
        request.submit('POST', '/sign-up', function (data) {

            // data값 boolean여부따라 결정


        }, 'FORMFILE', formData);
    },
    isAvailableEmail: function (data) {
        var regExp = /^[0-9a-zA-Z]([-_\.]?[0-9a-zA-Z])*@[0-9a-zA-Z]([-_\.]?[0-9a-zA-Z])*\.[a-zA-Z]{2,3}$/i;
        return regExp.test(data);
    },
    isAvailablePwd: function (data) {
        var regExp = /^[0-9a-z]{8,20}$/
        return regExp.test(data);
    },
    isAvailableNickName: function (data) {
        var regExp = /^[0-9a-z]{8,20}$/
        return regExp.test(data);
    }
}
