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
                <%--<div class="chatting-msg">
                    <b>nick</b>
                    <pre id="test">sdfasdfsd
                sdfasd</pre>
                </div>
                <nav>뉴ㅜ가들어옴</nav>
                <div class="chatting-msg chatting-my-msg">
                    <b>me</b>
                    <pre>나임내가 길어지는거에 대해 어떻게 생각해 너는????
                    대답해봐 이거 가운데 정렬아니지? 헷갈리네대답해봐 이거 가운데 정렬아니지? 헷갈리네대답해봐 이거 가운데 정렬아니지? 헷갈리네</pre>
                </div>--%>
            </div>
            <div class="input-group">
                <textarea id="chatting-msg-input" class="form-control" style="resize: none"></textarea>
                <button type="button" class="input-group-append btn btn-dark"
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
            </div>
        </div>
        <HR>
        <div id="file-package" class="d-flex flex-nowrap">
            <div title="titldddddddddddddddddddddddde"
                 onclick="">
                <div>
                    <span>
                    ⢸⠉⢹⣦⡀<BR>
                    ⢸⠀⠀⠀⣿<BR>
                    ⢸⣀⣀⣀⡇
                    </span>
                    <span></span>
                </div>
                <p>titldddddddddddddddddddddddde</p>
            </div>
            <div title="title"
                 onclick="">
                <div>
                    <img src="/resources/img/icon/documentX.png">
                </div>
                <p>title</p>
            </div>
            <div title="title"
                 onclick="">
                <div>
                    ⢸⠉⢹⣦⡀<BR>
                    ⢸⠀⠀⠀⣿<BR>
                    ⢸⣀⣀⣀⡇
                </div>
                <p>title</p>
            </div>
            <div title="title"
                 onclick="">
                <div>
                    ⢸⠉⢹⣦⡀<BR>
                    ⢸⠀⠀⠀⣿<BR>
                    ⢸⣀⣀⣀⡇
                </div>
                <p>title</p>
            </div>
            <div title="title"
                 onclick="">
                <div>
                    ⢸⠉⢹⣦⡀<BR>
                    ⢸⠀⠀⠀⣿<BR>
                    ⢸⣀⣀⣀⡇
                </div>
                <p>title</p>
            </div>
            <div title="title"
                 onclick="">
                <div>
                    ⢸⠉⢹⣦⡀<BR>
                    ⢸⠀⠀⠀⣿<BR>
                    ⢸⣀⣀⣀⡇
                </div>
                <p>title</p>
            </div>
            <div title="title"
                 onclick="">
                <div>
                    ⢸⠉⢹⣦⡀<BR>
                    ⢸⠀⠀⠀⣿<BR>
                    ⢸⣀⣀⣀⡇
                </div>
                <p>title</p>
            </div>
            <div title="title"
                 onclick="">
                <div>
                    ⢸⠉⢹⣦⡀<BR>
                    ⢸⠀⠀⠀⣿<BR>
                    ⢸⣀⣀⣀⡇
                </div>
                <p>title</p>
            </div>
            <div title="title"
                 onclick="">
                <div>
                    ⢸⠉⢹⣦⡀<BR>
                    ⢸⠀⠀⠀⣿<BR>
                    ⢸⣀⣀⣀⡇
                </div>
                <p>title</p>
            </div>
            <div title="title"
                 onclick="">
                <div>
                    ⢸⠉⢹⣦⡀<BR>
                    ⢸⠀⠀⠀⣿<BR>
                    ⢸⣀⣀⣀⡇
                </div>
                <p>title</p>
            </div>
        </div>
    </section>

</section>

<c:import url="${path}/WEB-INF/views/include/footer.jsp"/>