<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<c:set var="path" value="${pageContext.request.contextPath}"/>

<c:import url="${path}/WEB-INF/views/include/header.jsp"/>

<section class="wrap reduced-wrap">
    <h1>${roomName}</h1>
    <article id="chatting-contents" class="chatting-container flex-grow-1">
        <div class="form-control">
            <c:forEach var="message" items="${messageList}">
                <c:choose>
                    <c:when test="${fn:contains(message.get('contents'),'님이 입장하였습니다')}">
                        <nav>${message.get("contents")}</nav>
                    </c:when>
                    <c:when test="${fn:contains(message.get('contents'),'님이 퇴장하셨습니다')}">
                        <nav>${message.get("contents")}</nav>
                    </c:when>
                    <c:when test="${sessionMember.nick_name eq message.get('mbr_nick_name')}">
                        <div class="chatting-my-msg">
                            <div class="chatting-msg"><b>${message.get('mbr_nick_name')}</b>
                                <pre>${message.get("contents")}</pre>
                            </div>
                        </div>
                    </c:when>
                    <c:otherwise>
                        <div class="chatting-other-msg">
                            <div class="chatting-msg"><b>${message.get('mbr_nick_name')}</b>
                                <pre>${message.get("contents")}</pre>
                            </div>
                        </div>
                    </c:otherwise>
                </c:choose>
            </c:forEach>
        </div>
        <div class="input-group">
            <textarea id="chatting-msg-input" class="form-control" style="resize: none" disabled></textarea>
            <button type="button" id="chatting-send-btn" class="input-group-append btn btn-dark" disabled
                    onclick="chat.send(document.querySelector('#chatting-msg-input'));">
                보내기
            </button>
        </div>
    </article>
    <hr>
    <div style="text-align: center;">
        <button class="btn btn-dark" type="button" onclick="window.history.back();">
            뒤로가기
        </button>
    </div>
</section>
<c:import url="${path}/WEB-INF/views/include/footer.jsp"/>