<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:set var="path" value="${pageContext.request.contextPath}"/>

<c:import url="${path}/WEB-INF/views/include/header.jsp"/>

<section class="wrap reduced-wrap">
    <h1>${roomName}</h1>
    <div class="form-control">

    </div>
</section>
<c:import url="${path}/WEB-INF/views/include/footer.jsp"/>