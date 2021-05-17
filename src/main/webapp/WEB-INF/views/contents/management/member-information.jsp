<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:set var="path" value="${pageContext.request.contextPath}"/>

<c:import url="${path}/WEB-INF/views/include/header.jsp"/>
<script>
    function deleteMember(query) {
        if (!window.confirm('회원삭제를 진행합니다')) return;
        var form = document.querySelector(query);
        var email = form.querySelector('input[name=email]').value;
        var memberNo = form.querySelector('input[name=no]').value;

        request.submit('DELETE', '/management/members/' + memberNo + '?email=' + email, function (data) {
            if (data === 'true') {
                window.alert('삭제가 완료되었습니다.');
                history.back();
            } else {
                window.alert('삭제가 실패하였습니다.\n새로고침 후 다시 시도해주세요');

            }

        }, 'FORM');
    }

    function modifyMember(query) {
        if (!window.confirm('회원정보 수정을 진행합니다')) return;
        var form = document.querySelector(query);
        var memberNo = form.querySelector('input[name=no]').value;
        var formData = new FormData(form);
        request.submit('PUT', '/management/members/' + memberNo, function (data) {
            if (data === 'true') {
                window.alert('수정이 완료되었습니다.');
                history.go(0);

            } else {
                window.alert('수정에 실패하였습니다.\n새로고침 후 다시 시도해주세요');

            }

        }, 'FORMFILE', formData);

    }

    function manipulatePermission(element) {
        var checkEvent = element.checked
        var permissionName = element.value;
        if (checkEvent) {
            request.submit('POST', '/management/members/${member.no}/permissions', function (data) {
                if (data === 'true') {
                    nav.acceptMsg(2, permissionName + ': 권한 부여가 완료되었습니다.');
                } else {
                    nav.acceptMsg(2, ' 권한 부여에러: 새로고침 후 다시 시도해주세요');
                }

            }, 'FORM', 'permission=' + permissionName);

        } else {
            request.submit('DELETE', '/management/members/${member.no}/permissions?permission=' + permissionName, function (data) {
                if (data === 'true') {
                    nav.acceptMsg(2, permissionName + ': 권한 제거가 완료되었습니다.');
                } else {
                    nav.acceptMsg(2, ' 권한 제거에러: 새로고침 후 다시 시도해주세요');
                }

            }, 'FORM');
        }
    }

</script>

<section class="wrap reduced-wrap">
    <h1>${member.nick_name}(${member.email})</h1>
    <BR>
    <form id="user-information-form">
        <div class="mb-3">
            <label for="info-no" class="form-label">Serial Number</label>
            <div class="input-group mb-3">
                <input type="text" class="form-control" id="info-no"
                       name="no" value="${member.no}" readonly="readonly">
            </div>
        </div>
        <div class="mb-3">
            <label for="info-email" class="form-label">이메일</label>
            <div class="input-group mb-3">
                <input type="email" class="form-control" id="info-email" aria-describedby="emailHelp"
                       name="email" value="${member.email}" maxlength="50" onchange="sign.confirmAvailableEmail(this)">
                <div id="emailHelp" class="form-text"></div>
            </div>
        </div>
        <div class="mb-3">
            <label for="info-nickName" class="form-label">닉네임</label>
            <input type="text" class="form-control" id="info-nickName" aria-describedby="nickNameHelp"
                   placeholder="한글, 영문자또는 숫자를 2~20자 이내로 입력해주세요" name="nick_name"
                   onchange="sign.confirmAvailableNickName(this);"
                   value="${member.nick_name}">
            <div id="nickNameHelp" class="form-text"></div>
        </div>
        <input id="csrf-input" name="${_csrf.parameterName}" type="hidden" value="${_csrf.token}"/>
    </form>
    <div style="text-align: center;">
        <button type="button" class="btn btn-dark" onclick="modifyMember('#user-information-form');">
            수정
        </button>
        &nbsp;&nbsp;&nbsp;
        <button type="button" class="btn btn-danger" onclick="deleteMember('#user-information-form');">
            회원 삭제
        </button>
    </div>
    <hr>
    <div class="mb-3">
        <div>
            <label for="info-permission" class="form-label">권한</label>
        </div>
        <div class="input-group">
            <c:forEach var="permission" items="${permissionList}">
                <div class="input-group-text">
                    <c:choose>
                        <c:when test="${member.authList.contains(permission)}">
                            <input type="checkbox" id="info-permission" value="${permission}" checked
                                   onchange="manipulatePermission(this)">
                        </c:when>
                        <c:otherwise>
                            <input type="checkbox" id="info-permission" value="${permission}"
                                   onchange="manipulatePermission(this)">
                        </c:otherwise>
                    </c:choose>
                </div>
                <div class="form-control">${permission}</div>
            </c:forEach>
        </div>
    </div>

</section>

<c:import url="${path}/WEB-INF/views/include/footer.jsp"/>