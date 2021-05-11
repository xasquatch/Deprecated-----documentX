<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:set var="path" value="${pageContext.request.contextPath}"/>

<c:import url="${path}/WEB-INF/views/include/header.jsp"/>

<section class="wrap reduced-wrap">
    <table class="table table-hover table-dark">
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