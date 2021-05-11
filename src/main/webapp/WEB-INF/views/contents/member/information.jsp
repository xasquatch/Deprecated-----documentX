<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:set var="path" value="${pageContext.request.contextPath}"/>

<c:import url="${path}/WEB-INF/views/include/header.jsp"/>

<section class="wrap reduced-wrap">
    <h1>내 정보</h1>
    <BR>
    <form id="user-information-form">
        <div class="mb-3">
            <label for="sign-up-email" class="form-label">이메일</label>
            <div class="input-group mb-3">
                <input type="email" class="form-control" id="sign-up-email" aria-describedby="emailHelp"
                       name="email" value="${sessionMember.email}" maxlength="50" readonly="readonly">
            </div>
        </div>
        <div class="mb-3">
            <label for="sign-up-pwd" class="form-label">비밀번호</label>
            <input type="password" class="form-control" id="sign-up-pwd" aria-describedby="pwdHelp"
                   placeholder="영소문자와 숫자를 조합하여 8~20자 이내로 입력해주세요"
                   name="pwd" onchange="sign.confirmAvailablePwd(this);">
            <div id="pwdHelp" class="form-text"></div>
        </div>
        <div class="mb-3">
            <label for="sign-up-pwd" class="form-label">비밀번호 확인</label>
            <input type="password" class="form-control" id="sign-up-pwd-confirm" aria-describedby="pwdConfirmHelp"
                   placeholder="비밀번호 확인"
                   onchange="sign.confirmSamePwd(this);">
            <div id="pwdConfirmHelp" class="form-text"></div>
        </div>
        <div class="mb-3">
            <label for="sign-up-nickName" class="form-label">닉네임</label>
            <input type="text" class="form-control" id="sign-up-nickName" aria-describedby="nickNameHelp"
                   placeholder="8~20자 이내로 입력해주세요" name="nick_name"
                   onchange="sign.confirmAvailableNickName(this);"
                   value="${sessionMember.nick_name}">
            <div id="nickNameHelp" class="form-text"></div>
        </div>
        <input id="csrf-input" name="${_csrf.parameterName}" type="hidden" value="${_csrf.token}"/>
    </form>
    <div style="text-align: center;">
        <button type="button" class="btn btn-dark" onclick="memberInfo.modify('${sessionMember.nick_name}')">
            수정
        </button>
        &nbsp;&nbsp;&nbsp;
        <button type="button" class="btn btn-danger" onclick="memberInfo.delete('${sessionMember.nick_name}')">
            회원 탈퇴
        </button>
    </div>
</section>

<c:import url="${path}/WEB-INF/views/include/footer.jsp"/>
