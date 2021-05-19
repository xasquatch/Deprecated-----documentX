<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:set var="path" value="${pageContext.request.contextPath}"/>

<c:import url="${path}/WEB-INF/views/include/header.jsp"/>
<script>
    function deleteFile(form) {
        var fileNo = form.querySelector('input[name=fileNo]').value;
        var nickName = form.querySelector('input[name=mbr_nick_name]').value;
        request.submit('DELETE', '/members/' + nickName + '/files/' + fileNo + '?fileNo=' + fileNo, function (data) {
            if (data === 'false') {
                window.alert('삭제에 실패하였습니다.\n 새로고침 후 다시 시도해주세요.');
                return;
            }
            var second = 1;
            nav.acceptMsg(second, '삭제완료');
            setTimeout(function () {
                window.history.go(0);
            }, second * 1000);
        }, 'FORM');
    }
</script>
<section class="wrap reduced-wrap">
    <form class="input-group">
        <input type="search" id="search-file-name" class="form-control" placeholder="search file name">
        <button type="button" class="input-group-append btn btn-dark"
                onclick="file.appendList('#file-package',document.querySelector('#search-file-name').value)">
            <i class="fa fa-search" aria-hidden="true"></i>
        </button>
        &nbsp;&nbsp;
        <button class="btn btn-dark" type="button" onclick="document.querySelector('#file-upload').click();">
            <i class="fa fa-plus" aria-hidden="true"></i>
        </button>
        <input id="file-upload" class="hidden" type="file" multiple onchange="file.upload(this)">
    </form>
    <div id="file-package">
        <c:forEach var="file" items="${fileList}">
            <div class="d-flex flex-row" title="${file.url}" style="cursor: auto;">
                <form id="management-file-form-${file.no}" class="hidden">
                    <input name="fileNo" value="${file.no}">
                    <input name="mbr_no" value="${file.mbr_no}">
                    <input name="mbr_nick_name" value="${file.mbr_nick_name}">
                    <input name="dataType" value="${file.dataType}">
                    <input name="url" name="date" value="${file.date}">
                </form>
                <section style="text-align:center; width: 70px;"
                         onclick="window.open('${path}${file.url}','${file.url}','${file.dataType}',false)">
                    <c:choose>
                        <c:when test="${file.dataType eq 'IMAGE'}">
                            <img style="cursor: pointer; width: 100%;" src="${file.url}">
                        </c:when>
                        <c:otherwise>
                            <img class="loading-icon" src="${path}/resources/img/loading.gif" width="50" height="50">
                        </c:otherwise>
                    </c:choose>
                </section>
                <section class="d-flex flex-row flex-grow-1">
                    <section class="d-flex flex-column flex-grow-1" style="margin-left: 20px;">
                        <article>
                            <b>Serial Number</b> : ${file.no}
                        </article>
                        <article>
                            <b>url</b> : ${file.url}
                        </article>
                        <article>
                            <b>uploader</b> :
                            <a href="${path}/management/members/${file.mbr_no}">
                                    ${file.mbr_nick_name}(<b>Serial Number</b>:${file.mbr_no})
                            </a>
                        </article>
                        <article>
                            <b>Created Date</b> : ${file.date}
                        </article>
                    </section>
                    <section class="d-flex flex-column flex-grow-0">
                        <button type="button" class="btn btn-danger"
                                onclick="deleteFile(document.querySelector('#management-file-form-${file.no}'));">
                            <i class="fa fa-trash" aria-hidden="true"></i>
                        </button>
                    </section>
                </section>
            </div>
        </c:forEach>
    </div>
    <div id="pagination">
        <ul class="pagination justify-content-center">
            <c:forEach var="page" items="${pagination}">
                <c:choose>
                    <c:when test="${page eq 'current-page'}">
                        <li class="page-item active">
                            <a class="page-link" href="#">${currentPage}</a>
                        </li>
                    </c:when>
                    <c:otherwise>
                        <li class="page-item">
                            <a class="page-link"
                               href="${path}/management/files?current-page=${page}&row-count=${rowCount}&search-value=${searchValue}">
                                    ${page}
                            </a>
                        </li>
                    </c:otherwise>
                </c:choose>
            </c:forEach>
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