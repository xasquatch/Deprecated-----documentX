<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<c:set var="path" value="${pageContext.request.contextPath}"/>
<!DOCTYPE html>
<html lang="ko">
<head>
    <meta charset="UTF-8">
    <title>Title</title>
    <link rel="stylesheet" href="${path}/webjars/bootstrap/4.6.0/css/bootstrap.min.css"/>
    <link rel="stylesheet" href="${path}/resources/css/style.css"/>
    <link rel="stylesheet" href="${path}/resources/css/reset.css"/>
    <script src="${path}/resources/js/default.js"></script>
    <script defer src="${path}/webjars/jquery/3.6.0/dist/jquery.min.js"></script>
    <script defer src="${path}/webjars/bootstrap/4.6.0/js/bootstrap.min.js"></script>
</head>
<body>
<header>
    <div id="header-logo">
        <span>X</span>
        <span></span><%--text스크립트로 Document글자 삽입될 자리--%>
    </div>
    <sec:authorize access="isAuthenticated()">
        <div>
            <a class="btn btn-danger" href="javascript:document.getElementById('logout').submit();">
                Log Out
            </a>
        </div>
        <form id="logout" action="${path}/logout" method="POST">
            <input name="${_csrf.parameterName}" type="hidden" value="${_csrf.token}"/>
        </form>
    </sec:authorize>
</header>
<script>
    text.insert('#header-logo>span:last-child', 'Document', 10);
</script>
