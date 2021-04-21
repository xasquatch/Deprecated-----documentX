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


