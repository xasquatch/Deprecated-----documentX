<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:set var="path" value="${pageContext.request.contextPath}"/>

<c:import url="${path}/WEB-INF/views/include/header.jsp"/>

<section class="wrap reduced-wrap">
    <div class="input-group">
        <input type="search" id="search-file-name" class="form-control" placeholder="search file name">
        <button type="button" class="input-group-append btn btn-dark">
            <i class="fa fa-search" aria-hidden="true"></i>
        </button>
        &nbsp;&nbsp;
        <button class="btn btn-dark" onclick="document.querySelector('#file-upload').click();">
            <i class="fa fa-plus" aria-hidden="true"></i>
        </button>
        <input id="file-upload" class="hidden" type="file" multiple onchange="file.upload(this)">
    </div>
    <div id="file-package" class="d-flex flex-wrap">

    </div>
</section>
<script>
    window.onload = function (event) {
        file.appendList('#file-package');
    }
</script>
<c:import url="${path}/WEB-INF/views/include/footer.jsp"/>