<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:set var="path" value="${pageContext.request.contextPath}"/>

<c:import url="${path}/WEB-INF/views/include/header.jsp"/>

<section class="wrap reduced-wrap">
    <h1>${chattingRoom.name}</h1>
    <section class="d-md-flex flex-md-row-reverse">
        <article id="chatting-clients" class="chatting-container flex-grow-0">
            <table class="table table-hover">
                <thead>
                <tr>
                    <th>
                        리스트
                    </th>
                </tr>
                </thead>
                <tbody>
                <tr>
                    <td>
                        <div class="input-group" title="Nick Name (test@test.com)">
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
        <article id="chatting-contents" class="chatting-container flex-grow-1">
            <div class="form-control">

            </div>
            <div class="input-group">
                <textarea id="chatting-msg-input" class="form-control" style="resize: none"></textarea>
                <button type="button" id="chatting-send-btn" class="input-group-append btn btn-dark"
                        onclick="chat.send(document.querySelector('#chatting-msg-input'));">
                    보내기
                </button>
            </div>
        </article>
    </section>
    <section id="chatting-file-list" class="chatting-container">
        <div class="d-flex flex-wrap">
            <div style="font-weight:bold; padding: 12px;">파일 업로드 (최근 이용파일 10개)</div>
            <div class="input-group">
                <input type="search" id="search-file-name" class="form-control" placeholder="search file name">
                <button type="button" class="input-group-append btn btn-dark">
                    <i class="fa fa-search" aria-hidden="true"></i>
                </button>
                &nbsp;&nbsp;
                <button class="btn btn-dark" onclick="document.querySelector('#file-upload').click();">
                    <i class="fa fa-plus" aria-hidden="true"></i>
                </button>
                <input id="file-upload" class="hidden" type="file" multiple onchange="file.upload(this)">
            </div>
        </div>
        <HR>
        <div id="file-package" class="d-flex flex-nowrap">
        </div>
    </section>

</section>
<script>
    window.onload = function (event) {
        chat.connect('${chattingRoom.no}', '${sessionMember.nick_name}');
        file.appendList('#file-package');

        var $drop = $('#chatting-contents > div:first-child');
        $drop.on('dragover', function (e) {
            e.stopPropagation();
            e.preventDefault();
        }).on('drop', function (e) {
            e.preventDefault();
        });
    }

</script>
<c:import url="${path}/WEB-INF/views/include/footer.jsp"/>