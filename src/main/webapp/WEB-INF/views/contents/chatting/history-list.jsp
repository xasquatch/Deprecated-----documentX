<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:set var="path" value="${pageContext.request.contextPath}"/>

<c:import url="${path}/WEB-INF/views/include/header.jsp"/>

<section class="wrap reduced-wrap">
    <h2>나의 채팅 이력</h2>
    <table class="table table-hover">
        <thead>
        <tr style="text-align: center;">
            <th>방 제목</th>
            <th>방 생성일</th>
        </tr>
        </thead>
        <tbody>
        <c:forEach var="history" items="${chatHistoryList}">
            <tr>
                <td>
                    <a href="/chatting/history/${history.get("name")}?room-number=${history.get("no")}">
                            ${history.get("name")}
                    </a>
                </td>
                <td style="text-align: center;">
                    <span>
                        [${history.get("date")}]
                    </span>
                </td>
            </tr>
        </c:forEach>
        </tbody>
    </table>

</section>
<c:import url="${path}/WEB-INF/views/include/footer.jsp"/>