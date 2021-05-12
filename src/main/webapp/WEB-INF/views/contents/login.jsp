<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:set var="path" value="${pageContext.request.contextPath}"/>
<c:import url="${path}/WEB-INF/views/include/header.jsp"/>

<section class="wrap reduced-wrap">
    <h2>LogIn</h2>
    <BR>
    <form id="login-form" class="form-horizontal" method="POST" action="${path}/loginProcess">
        <div class="form-group">
            <label for="login-email" class="control-label">이메일</label>
            <input type="text" name="email" class="form-control" id="login-email" placeholder="xxxxx@gmail.com"
                   onchange="login.confirmAvailableEmail(this)"
                   value="${email}">
            <div id="loginEmailHelp" class="form-text"></div>
        </div>
        <div class="form-group">
            <label for="login-pwd" class="control-label">비밀번호</label>
            <input type="password" name="pwd" class="form-control" id="login-pwd" placeholder="Password"
                   onchange="login.confirmAvailablePwd(this);">
            <div id="loginPwdHelp" class="form-text"></div>
        </div>
        <div class="form-group">
            <div class="checkbox">
                <label>
                    <input type="checkbox"> 자동 로그인
                </label>
            </div>
        </div>
        <div class="form-group" style="text-align: center;">
            <input name="${_csrf.parameterName}" type="hidden" value="${_csrf.token}"/>
            <button type="button" class="btn btn-dark" onclick="login.submit()">로그인</button>
            &nbsp;
            &nbsp;
            &nbsp;
            <button type="button" class="btn btn-light" onclick="sign.upPage()">회원가입</button>
        </div>
    </form>
</section>

<c:import url="${path}/WEB-INF/views/include/footer.jsp"/>