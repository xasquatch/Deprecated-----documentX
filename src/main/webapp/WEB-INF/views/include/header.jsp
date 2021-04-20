<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<c:set var="path" value="${pageContext.request.contextPath}"/>
<!DOCTYPE html>
<html lang="ko">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <meta name="keywords" content="xasquatch">
    <meta name="author" content="xasquatch">
    <meta name="apple-mobile-web-app-capable" content="yes">
    <meta http-equiv="X-UA-Compatible" content="ie=edge">
    <title>Document X</title>
    <link rel="shortcut icon" type="image/x-icon" href="${path}/resources/img/icon/documentX.ico"/>
    <link rel="stylesheet" href="${path}/webjars/bootstrap/4.6.0/css/bootstrap.min.css"/>
    <link rel="stylesheet" href="${path}/webjars/fontawesome/4.7.0/css/font-awesome.min.css"/>
    <link rel="stylesheet" href="${path}/resources/css/style.css"/>
    <link rel="stylesheet" href="${path}/resources/css/reset.css"/>
    <script src="${path}/resources/js/default.js"></script>
    <script defer src="${path}/webjars/jquery/3.6.0/dist/jquery.min.js"></script>
    <script defer src="${path}/webjars/bootstrap/4.6.0/js/bootstrap.min.js"></script>
</head>
<body>
<header id="main-header">
    <a id="header-logo" href="${path}/">
        <span>X</span>
        <span class="d-none d-xl-inline d-xxl-none"></span><%--text스크립트로 Document글자 삽입될 자리--%>
    </a>
    <div id="header-dashboard" class="list-inline">
        <sec:authorize access="isAnonymous()">
            <a class="list-inline-item d-none d-xl-inline d-xxl-none"
                href="${path}/login">
                <i class="fa fa-sign-in" aria-hidden="true"></i>
                <span style="font-size: 1.5em;">Sign In</span>
            </a>
        </sec:authorize>
        <sec:authorize access="isAuthenticated()">
            <form id="logout" action="${path}/logout" method="POST">
                <input name="${_csrf.parameterName}" type="hidden" value="${_csrf.token}"/>
            </form>

            <a class="list-inline-item d-none d-xl-inline d-xxl-none"
               data-bs-toggle="tooltip" data-bs-placement="bottom" title="Log Out"
               href="javascript:document.querySelector('#logout').submit();">
                <i class="fa fa-sign-out " aria-hidden="true"></i>
            </a>
            <a class="list-inline-item d-none d-xl-inline d-xxl-none"
               data-bs-toggle="tooltip" data-bs-placement="bottom" title="My Information"
               href="javascript:">
                <i class="fa fa-user-circle " aria-hidden="true"></i>
            </a>
            <a class="list-inline-item d-none d-xl-inline d-xxl-none"
               data-bs-toggle="tooltip" data-bs-placement="bottom" title="Organization"
               href="javascript:">
                <i class="fa fa-users " aria-hidden="true"></i>
            </a>
            <a class="list-inline-item d-none d-xl-inline d-xxl-none"
               data-bs-toggle="tooltip" data-bs-placement="bottom" title="Chatting Room"
               href="javascript:">
                <i class="fa fa-weixin " aria-hidden="true"></i>
            </a>
        </sec:authorize>
        <a id="header-collapse" class="list-inline-item d-xl-none"
           data-bs-toggle="tooltip" data-bs-placement="left" title="Open"
           href="javascript:">
            <i class="fa fa-chevron-right " aria-hidden="true"></i>
        </a>
    </div>

    <div id="header-list" class="list">
        <a href="#" class="list-group-item list-group-item-action active">
            <i class="fa fa-angle-right" aria-hidden="true"></i>
            <span class="d-none d-xl-inline d-xxl-none">
            The current link item
            </span>
        </a>
        <a href="#" class="list-group-item list-group-item-action">
            <i class="fa fa-angle-right" aria-hidden="true"></i>
            <span class="d-none d-xl-inline d-xxl-none">
            A second link item
            </span>
        </a>
        <a href="#" class="list-group-item list-group-item-action">
            <i class="fa fa-angle-right" aria-hidden="true"></i>
            <span class="d-none d-xl-inline d-xxl-none">
                A third link item
            </span>
        </a>
        <a href="#" class="list-group-item list-group-item-action">
            <i class="fa fa-angle-right" aria-hidden="true"></i>
            <span class="d-none d-xl-inline d-xxl-none">
            A fourth link item
            </span>
        </a>
    </div>
</header>
<script>
    text.insert('#header-logo>span:last-child', 'Document', 10);
</script>
