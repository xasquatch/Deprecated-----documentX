<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:set var="path" value="${pageContext.request.contextPath}"/>

<c:import url="${path}/WEB-INF/views/include/header.jsp"/>

<section class="wrap reduced-wrap">
    <section class="d-md-flex flex-md-row-reverse">
        <article id="chatting-clients" class="chatting-container flex-grow-0">
            <table class="table table-hover">
                <thead>
                <tr>
                    <th>
                        리스트
                    </th>
                </tr>
                </thead>
                <tbody>
                <tr>
                    <td>
                        <div class="input-group">
                            <select class="btn btn-dark form-control" onchange="">
                                <option selected value="">
                                    <span class="form-control">Nick Name (test@test.com)</span>
                                </option>
                                <option value="">정보보기</option>
                                <%--권한필요--%>
                                <option value="">강퇴</option>
                                <option value="">위임하기</option>
                            </select>
                        </div>
                    </td>
                </tr>
                </tbody>
                <%-- 50명을 최대로 리밋--%>
            </table>
        </article>
        <article id="chatting-contents" class="chatting-container flex-grow-1">
            <div class="form-control">

            </div>
            <form class="input-group">
                <textarea class="form-control" style="resize: none"></textarea>
                <button class="input-group-append btn btn-dark" style="align-items: center;">
                    보내기
                </button>
            </form>
        </article>
    </section>
    <section id="chatting-file-list" class="chatting-container">
        <div style="font-weight:bold; padding: 12px;">파일 업로드</div>
        <HR>
        <div id="file-package">

        </div>
    </section>
    <div>
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

<c:import url="${path}/WEB-INF/views/include/footer.jsp"/>