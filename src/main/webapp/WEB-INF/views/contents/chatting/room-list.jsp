<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:set var="path" value="${pageContext.request.contextPath}"/>

<c:import url="${path}/WEB-INF/views/include/header.jsp"/>

<section class="wrap reduced-wrap">
    <div class="chatting-header">
        <form method="GET" action="" class="col-7">
            <div class="input-group">
                <input type="search" name="chattingSearch" class="form-control" placeholder="search keywords">
                <input type="hidden" name="chattingRowCount" value="<%--여기 로우카운트값--%>">
                <button type="button" class="input-group-append btn btn-dark">
                    <i class="fa fa-search" aria-hidden="true"></i>
                </button>
            </div>
        </form>
        <div class="col-5" style="display: flex; text-align: right;">
            <select id="chatting-row-count" class="custom-select" aria-label="rowCount"
                    onchange="/*form태그submit이벤트*/">
                <option value="10" selected>10</option>
                <option value="25">25</option>
                <option value="50">50</option>
            </select>
            <form action="/members/${sessionMember.nick_name}/chatting-rooms/${sessionMember.nick_name}님의_채팅방입니다">
                <button class="btn btn-dark">
                    <i class="fa fa-plus" aria-hidden="true"></i>
                </button>

            </form>
        </div>
    </div>
    <HR>
    <table class="table table-hover">
        <thead>
        <tr>
            <th>No</th>
            <th>방 제목</th>
            <th>참여자 수</th>
            <th>개설자</th>
        </tr>
        </thead>
        <tbody>
        <tr>
            <td>
                1
            </td>
            <td>
                <a href="/chatting/faketitle">
                    예시
                </a>
            </td>
            <td>
                1
            </td>
            <td>
                xas
            </td>
        </tr>
        </tbody>
        <tfoot>
        <tr>
            <td colspan="4" style="text-align: center;">
                페이지네이션
            </td>
        </tr>
        </tfoot>
    </table>

</section>

<c:import url="${path}/WEB-INF/views/include/footer.jsp"/>