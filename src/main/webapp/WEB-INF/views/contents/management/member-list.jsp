<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:set var="path" value="${pageContext.request.contextPath}"/>

<c:import url="${path}/WEB-INF/views/include/header.jsp"/>

<section class="wrap reduced-wrap">
    <div class="d-flex">
        <form method="GET" id="searchMemberForm" action="${path}/management/members" class="col-7">
            <div class="input-group">
                <input type="search" name="search-value" class="form-control" placeholder="search keywords"
                       value="${searchValue}">
                <input type="hidden" name="row-count" value="${rowCount}">
                <input type="hidden" name="current-page" value="${currentPage}">
                <button type="submit" class="input-group-append btn btn-dark">
                    <i class="fa fa-search" aria-hidden="true"></i>
                </button>
            </div>
        </form>
        <div class="col-5" style="display: flex; text-align: right;">
            <select class="custom-select" aria-label="row-count"
                    onchange="var searchForm = document.querySelector('#searchMemberForm'); searchForm.querySelector('input[name=row-count]').value = this.value; searchForm.submit();">
                <option value="${rowCount}" selected>${rowCount}</option>
                <option value="50">50</option>
                <option value="100">100</option>
                <option value="200">200</option>
            </select>
        </div>
    </div>
    <HR>
    <table class="table table-hover">
        <thead>
        <tr>
            <th>Serial<BR>Number</th>
            <th>Nick Name</th>
            <th>Email</th>
            <th>Created Date</th>
            <th>Authorities</th>
        </tr>
        </thead>
        <tbody>
        <c:forEach var="member" items="${memberList}">
            <tr>
                <td>
                        ${member.no}
                </td>
                <td>
                        ${member.nick_name}
                </td>
                <td>
                    <a href="${path}/management/members/${member.no}">
                            ${member.email}
                    </a>
                </td>
                <td>
                        ${member.created_date}
                </td>
                <td>
                    [
                    <c:forEach var="auth" items="${member.authList}">
                        ${auth}&nbsp;
                    </c:forEach>
                    ]
                </td>
            </tr>
        </c:forEach>
        </tbody>
        <tfoot>
        <tr>
            <td colspan="5" id="chatting-pagination">
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
            </td>
        </tr>
        </tfoot>
    </table>
</section>

<c:import url="${path}/WEB-INF/views/include/footer.jsp"/>