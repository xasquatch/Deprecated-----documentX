<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:set var="path" value="${pageContext.request.contextPath}"/>

<c:import url="${path}/WEB-INF/views/include/header.jsp"/>

<section class="wrap reduced-wrap">
    <table id="member-information-table" class="table table-hover">
        <thead>
        <tr>
            <td colspan="2">col2</td>
        </tr>
        </thead>
        <tbody>
        <tr>
            <td>1</td>
            <td>2</td>
        </tr>
        <tr>
            <td>2</td>
            <td>3</td>
        </tr>
        <tr>
            <td>4</td>
            <td>5</td>
        </tr>
        <tr>
            <td>6</td>
            <td>7</td>
        </tr>
        </tbody>
    </table>

</section>

<c:import url="${path}/WEB-INF/views/include/footer.jsp"/>