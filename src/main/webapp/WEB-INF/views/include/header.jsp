<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<c:set var="path" value="${pageContext.request.contextPath}"/>
<!DOCTYPE html>
<html lang="ko">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=0.8">
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
    <link rel="preconnect" href="https://fonts.gstatic.com">
    <link href="https://fonts.googleapis.com/css2?family=Goudy+Bookletter+1911&display=swap" rel="stylesheet">
    <script defer src="${path}/resources/js/default.js"></script>
    <script defer src="${path}/webjars/jquery/3.6.0/dist/jquery.min.js"></script>
    <script defer src="${path}/webjars/bootstrap/4.6.0/js/bootstrap.min.js"></script>
    <script src="${path}/webjars/chart.js/3.1.1/dist/chart.min.js"></script>
</head>
<body>
<header id="main-header" class="reduced-main-header">
    <a id="main-header-logo" class="reduced-header-logo" href="${path}/">
        <span>X</span>
        <span class="d-none d-xl-inline d-xxl-none"></span><%--text스크립트로 Document글자 삽입될 자리--%>
    </a>

    <div id="main-header-dashboard" class="list-group list-group-flush">
        <a id="main-header-expansion-btn" class="list-group-item d-xl-none"
           data-bs-toggle="tooltip" data-bs-placement="bottom" title="Open"
           href="javascript:headerExpansion.toggle()">
            <i class="fa fa-hand-pointer-o" aria-hidden="true"></i>
        </a>
        <sec:authorize access="isAnonymous()">
            <a class="list-group-item"
               href="${path}/login">
                <i class="fa fa-sign-in" aria-hidden="true"></i>
                <span style="font-size: 1.5em;"
                      class="d-none d-xl-inline d-xxl-none">
                    로그인
                </span>
            </a>
        </sec:authorize>
        <sec:authorize access="isAuthenticated()">
            <form id="logout" action="${path}/logout" method="POST">
                <input name="${_csrf.parameterName}" type="hidden" value="${_csrf.token}"/>
            </form>

            <a class="list-group-item"
               data-bs-toggle="tooltip" data-bs-placement="bottom" title="Log Out"
               href="javascript:document.querySelector('#logout').submit();">
                <i class="fa fa-sign-out " aria-hidden="true"></i>
                <span class="d-none d-xl-inline d-xxl-none">
                로그 아웃
                </span>
            </a>
            <a class="list-group-item"
               data-bs-toggle="tooltip" data-bs-placement="bottom" title="My Information"
               href="javascript:">
                <i class="fa fa-user-circle " aria-hidden="true"></i>
                <span class="d-none d-xl-inline d-xxl-none">
                내 정보관리
                </span>
            </a>
        </sec:authorize>
        <sec:authorize access="hasAnyRole('USER')">

            <%--
            <a class="list-group-item"
               data-bs-toggle="tooltip" data-bs-placement="bottom" title="Organization"
               href="javascript:">
                <i class="fa fa-users " aria-hidden="true"></i>
                <span class="d-none d-xl-inline d-xxl-none">
                내 단체관리
                </span>
            </a>
            --%>
            <a class="list-group-item"
               data-bs-toggle="tooltip" data-bs-placement="bottom" title="Chatting Room"
               href="javascript:">
                <i class="fa fa-weixin " aria-hidden="true"></i>
                <span class="d-none d-xl-inline d-xxl-none">
                채팅 관리
                </span>
            </a>
            <a class="list-group-item"
               data-bs-toggle="tooltip" data-bs-placement="bottom" title="Chatting Room"
               href="javascript:">
                <i class="fa fa-file-archive-o " aria-hidden="true"></i>
                <span class="d-none d-xl-inline d-xxl-none">
                파일 관리
                </span>
            </a>
        </sec:authorize>
    </div>
    <div id="main-header-list" class="reduced-header-list">
        <sec:authorize access="hasAnyRole('MANAGEMENT')">
            <div class="d-none d-xl-inline d-xxl-none" style="text-align: center; font-weight: bold;">
                Management
            </div>
            <a href="#" class="list-group-item list-group-item-action">
                <i class="fa fa-users" aria-hidden="true"></i>
                <span class="d-none d-xl-inline d-xxl-none">
                회원 관리
            </span>
            </a>
            <a id="searchDropdown" href="#" class="list-group-item list-group-item-action">
                <i class="fa fa-weixin " aria-hidden="true"></i>
                <span class="d-none d-xl-inline d-xxl-none">
                채팅 관리
            </span>
            </a>
            <a href="#" class="list-group-item list-group-item-action">
                <i class="fa fa-files-o" aria-hidden="true"></i>
                <span class="d-none d-xl-inline d-xxl-none">
                파일 관리
            </span>
            </a>
        </sec:authorize>

        <%--
                <a id="searchDropdown" href="#" class="list-group-item list-group-item-action">
                    <i class="fa fa-search" aria-hidden="true"></i>
                    <span class="d-none d-xl-inline d-xxl-none">
                        검색
                    </span>
                </a>
                <a href="#" class="list-group-item list-group-item-action">
                    <i class="fa fa-files-o" aria-hidden="true"></i>
                    <span class="d-none d-xl-inline d-xxl-none">
                        파일 관리
                    </span>
                </a>
                <a href="#" class="list-group-item list-group-item-action">
                    <i class="fa fa-pie-chart" aria-hidden="true"></i>
                    <span class="d-none d-xl-inline d-xxl-none">
                        핫 키워드
                    </span>
                </a>
                <a href="#" class="list-group-item list-group-item-action">
                    <i class="fa fa-line-chart" aria-hidden="true"></i>
                    <span class="d-none d-xl-inline d-xxl-none">
                        단체 통계
                    </span>
                </a>
            --%>
    </div>
</header>
<nav id="main-nav" class="reduced-main-nav deactivate-nav">
    <span id="main-nav-count"></span>
    <span id="main-nav-msg">

    </span>
</nav>
<script>
    window.onload = function () {
        text.insert('#main-header-logo>span:last-child', 'Document', 10);
        drag.addExpansionElement(document.querySelector('#main-header'));
    }


</script>