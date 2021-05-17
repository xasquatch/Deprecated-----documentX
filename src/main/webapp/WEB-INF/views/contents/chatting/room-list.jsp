<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<c:set var="path" value="${pageContext.request.contextPath}"/>

<c:import url="${path}/WEB-INF/views/include/header.jsp"/>

<section class="wrap reduced-wrap">
    <div class="d-flex">
        <form method="GET" action="" class="col-10">
            <div class="input-group">
                <input type="search" name="chattingSearch" class="form-control" placeholder="search keywords">
                <input type="hidden" name="chattingRowCount" value="<%--여기 로우카운트값--%>">
                <button type="button" class="input-group-append btn btn-dark">
                    <i class="fa fa-search" aria-hidden="true"></i>
                </button>
            </div>
        </form>
        <div class="col-2" style="display: flex; justify-content: center;">
            <%--
            <select id="chatting-row-count" class="custom-select" aria-label="rowCount"
                    onchange="/*form태그submit이벤트*/">
                <option value="10" selected>10</option>
                <option value="25">25</option>
                <option value="50">50</option>
            </select>
            --%>
            <button class="btn btn-dark" onclick="chat.createRoom()">
                <i class="fa fa-plus" aria-hidden="true"></i>&nbsp;Create
            </button>
        </div>
    </div>
    <HR>
    <table class="table table-hover">
        <thead>
        <tr>
            <th>방 제목</th>
            <th>참여자 수</th>
        </tr>
        </thead>
        <tbody>
        <c:forEach var="room" items="${chattingRoomList}">
            <tr>
                <td>
                    <a href="/chatting/${room.no}">
                            ${room.name}
                    </a>
                </td>
                <td>
                    ${fn:length(room.sessions)} 명
                </td>
            </tr>
        </c:forEach>
        </tbody>
        <tfoot>
        </tfoot>
    </table>
</section>

<c:import url="${path}/WEB-INF/views/include/footer.jsp"/>