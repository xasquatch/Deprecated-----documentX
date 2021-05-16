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
            if (data === 'true'){
                window.alert('삭제가 완료되었습니다.');
                history.back();
            }else{
                window.alert('삭제가 실패하였습니다.\n새로고침 후 다시 시도해주세요');

            }

        }, 'FORM');
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
                       name="email" value="${member.email}" maxlength="50" readonly="readonly">
            </div>
        </div>
        <div class="mb-3">
            <label for="info-pwd" class="form-label">비밀번호</label>
            <input type="password" class="form-control" id="info-pwd" aria-describedby="pwdHelp"
                   placeholder="영소문자또는 숫자를 8~20자 이내로 입력해주세요"
                   name="pwd">
            <div id="pwdHelp" class="form-text"></div>
        </div>
        <div class="mb-3">
            <label for="info-pwd" class="form-label">비밀번호 확인</label>
            <input type="password" class="form-control" id="info-pwd-confirm" aria-describedby="pwdConfirmHelp"
                   placeholder="비밀번호 확인">
            <div id="pwdConfirmHelp" class="form-text"></div>
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
        <button type="button" class="btn btn-dark" onclick="">
            수정
        </button>
        &nbsp;&nbsp;&nbsp;
        <button type="button" class="btn btn-danger" onclick="deleteMember('#user-information-form');">
            회원 삭제
        </button>
    </div>
</section>

<c:import url="${path}/WEB-INF/views/include/footer.jsp"/>