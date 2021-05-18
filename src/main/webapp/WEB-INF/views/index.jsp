<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>

<c:set var="path" value="${pageContext.request.contextPath}"/>

<c:import url="include/header.jsp"/>

<section class="wrap reduced-wrap">
    <h3>채팅 메시지 히스토리</h3>
    <div class="d-flex flex-wrap">
        <div class="container">
            <canvas id="myChart"></canvas>
        </div>
        <%--        <div class="container">--%>
        <%--            <canvas id="myChart2"></canvas>--%>
        <%--        </div>--%>
    </div>
    <div class="d-flex flex-wrap justify-content-around">
        <div class="card">
            <ul class="list-group">
               <li class="list-group-item">
                   <a href="">

                   </a>
               </li>
            </ul>
        </div>
        <div class="card">
            <div class="card-body">
                <canvas id="chDonut"></canvas>
            </div>
        </div>
    </div>

</section>
<script>

    function chart() {

        var ctx = document.getElementById('myChart');
        var myChart = new Chart(ctx, {
            type: 'bar',
            data: {
                labels: [
                    <c:forEach var="messageCount" items="${messageCountList}">
                    '${messageCount.get("nick_name")}',
                    </c:forEach>
                ],
                datasets: [{
                    label: ' of Votes',
                    data:
                        [
                            <c:forEach var="messageCount" items="${messageCountList}">
                            '${messageCount.get("count")}',
                            </c:forEach>
                        ],
                    backgroundColor: ['darkred', 'gray', 'aqua', '#c3e6cb', '#dc3545', '#6c757d','rgba(255, 99, 132, 1)', 'rgba(54, 162, 235, 1)', 'rgba(255, 206, 86, 1)', 'rgba(75, 192, 192, 1)', 'rgba(153, 102, 255, 1)', 'rgba(255, 159, 64, 1)'],
                    borderColor: ['darkred', 'gray', 'aqua', '#c3e6cb', '#dc3545', '#6c757d','rgba(255, 99, 132, 1)', 'rgba(54, 162, 235, 1)', 'rgba(255, 206, 86, 1)', 'rgba(75, 192, 192, 1)', 'rgba(153, 102, 255, 1)', 'rgba(255, 159, 64, 1)'],
                    borderWidth: 1
                }]
            },
            options: {scales: {yAxes: [{ticks: {beginAtZero: true}}]}}
        });

    }

    function chart2() {

        var ctx = document.getElementById('myChart2').getContext('2d');
        var chart = new Chart(ctx, { // 챠트 종류를 선택
            type: 'line', // 챠트를 그릴 데이타
            data: {
                labels: ['January', 'February', 'March', 'April', 'May', 'June', 'July'],
                datasets: [
                    {
                        label: 'My First dataset',
                        backgroundColor: 'transparent',
                        borderColor: 'red',
                        data: [0, 10, 5, 2, 20, 30, 45]
                    }
                ]
            },
            options:
                {
                    legend: {display: false},
                    title: {display: true, text: '라인차트 제목'}
                }

        });
    }

    function circleChart() {
        var colors = ['darkred', 'gray', 'aqua', '#c3e6cb', '#dc3545', '#6c757d','rgba(255, 99, 132, 1)', 'rgba(54, 162, 235, 1)', 'rgba(255, 206, 86, 1)', 'rgba(75, 192, 192, 1)', 'rgba(153, 102, 255, 1)', 'rgba(255, 159, 64, 1)'];
        var donutOptions = {
            cutoutPercentage: 10,
            legend: {
                position: 'bottom', padding: 5,
                labels: {
                    pointStyle: 'circle', usePointStyle: true
                }
            }
        };

        var chDonutData = {

            labels:
                [
                    <c:forEach var="messageCount" items="${messageCountList}">
                    '${messageCount.get("nick_name")}',
                    </c:forEach>
                ],

            datasets: [
                {
                    backgroundColor: colors.slice(0, ${fn:length(messageCountList)}),
                    borderWidth: 0,
                    data:
                        [
                            <c:forEach var="messageCount" items="${messageCountList}">
                            '${messageCount.get("count")}',
                            </c:forEach>
                        ]
                }
            ]
        };

        var chDonut = document.getElementById("chDonut");
        if (chDonut) {
            new Chart(chDonut,
                {
                    type: 'pie',
                    data: chDonutData,
                    options: donutOptions
                }
            );
        }
    }

    chart();
    // chart2();
    circleChart();

</script>

<c:import url="include/footer.jsp"/>