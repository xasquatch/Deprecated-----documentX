<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:set var="path" value="${pageContext.request.contextPath}"/>

<c:import url="${path}/WEB-INF/views/include/header.jsp"/>

<section class="wrap reduced-wrap">
    <section>
        <article>
            <table class="table table-hover">
                <thead>
                <tr>
                    <th>
                        <h3>리스트</h3>
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
        <article>
            <form class="input-group">
                <textarea class="form-control" style="resize: none"></textarea>
                <button class="input-group-append btn btn-dark" style="align-items: center;">
                    보내기
                </button>
            </form>
        </article>
    </section>
    <div>
        파일 업로드
    </div>

</section>

<c:import url="${path}/WEB-INF/views/include/footer.jsp"/>