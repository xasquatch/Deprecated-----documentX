<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:set var="path" value="${pageContext.request.contextPath}"/>

<c:import url="${path}/WEB-INF/views/include/header.jsp"/>

<section class="wrap reduced-wrap">
    <form class="input-group">
        <input type="search" id="search-file-name" class="form-control" placeholder="search file name">
        <button type="button" class="input-group-append btn btn-dark"
                onclick="file.appendList('#file-package',document.querySelector('#search-file-name').value)">
            <i class="fa fa-search" aria-hidden="true"></i>
        </button>
        &nbsp;&nbsp;
        <button class="btn btn-dark" onclick="document.querySelector('#file-upload').click();">
            <i class="fa fa-plus" aria-hidden="true"></i>
        </button>
        <input id="file-upload" class="hidden" type="file" multiple onchange="file.upload(this)">
    </form>
    <div id="file-package" class="">
        <c:forEach var="file" items="${fileList}">
            <div title="${file.url}" onclick="file.readyToManipulation(this)">
                <form class="hidden">
                    <input name="no" value="${file.no}">
                    <input name="mbr_no" value="${file.mbr_no}">
                    <input name="mbr_nick_name" value="${file.mbr_nick_name}">
                    <input name="dataType" value="${file.dataType}">
                    <input name="url" name="date" value="${file.date}">
                </form>
                <div>
                    <c:choose>
                        <c:when test="${file.dataType eq 'IMAGE'}">
                            <img src="${file.url}">
                        </c:when>
                        <c:otherwise>
                            <img class="loading-icon" src="${path}/resources/img/loading.gif" width="50" height="50">
                        </c:otherwise>
                    </c:choose>
                </div>
                <p style="width:100%">
                        ${file.url}
                </p>
            </div>
        </c:forEach>
    </div>
    <div id="file-pagination">

        <ul class="pagination justify-content-center">
            <li class="page-item disabled">
                <a class="page-link" href="#" tabindex="-1" aria-disabled="true">&laquo;</a>
            </li>
            <li class="page-item active">
                <a class="page-link" href="#">1</a>
            </li>
            <li class="page-item">
                <a class="page-link" href="#">2</a>
            </li>
            <li class="page-item">
                <a class="page-link" href="#">&raquo;</a>
            </li>
        </ul>
    </div>
</section>
<script>
    window.onload = function (event) {
        var loadingImg = document.querySelectorAll('.loading-icon');
        for (const img of loadingImg) {
            text.insert(img.parentNode, ASCIIART.FILE, 10, img.remove());
        }
    }

</script>
<c:import url="${path}/WEB-INF/views/include/footer.jsp"/>