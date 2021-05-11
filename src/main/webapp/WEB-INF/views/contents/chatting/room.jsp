<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:set var="path" value="${pageContext.request.contextPath}"/>

<c:import url="${path}/WEB-INF/views/include/header.jsp"/>

<section class="wrap reduced-wrap">
    <section>
        <article>
            참여자
        </article>
        <article>
            채팅
        </article>

    </section>
    <div>
        파일 업로드
    </div>

</section>

<c:import url="${path}/WEB-INF/views/include/footer.jsp"/>