<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:set var="path" value="${pageContext.request.contextPath}"/>

<c:import url="include/header.jsp"/>

<section class="wrap reduced-wrap">

    <ul class="list-inline">
        <li class="list-inline-item">
            <button type="button" class="btn btn-primary" data-toggle="modal" data-target="#myModal">
                ${home}
            </button>
        </li>
        <li class="list-inline-item"><a class="btn" href="${path}/management">management</a></li>
        <li class="list-inline-item"><a class="btn" href="${path}/member">member</a></li>
        <li class="list-inline-item"><a class="btn" href="${path}/guest">guest</a></li>
    </ul>

    <div class="container">
        <canvas id="myChart"></canvas>
    </div>
    <div class="container">
        <canvas id="myChart2"></canvas>
    </div>

    <div class="container">
        <div class="row">
            <canvas id="myChart4" class="col-6"></canvas>
            <canvas id="myChart5" class="col-6"></canvas>
        </div>
    </div>
    <div class="container">
        <div class="row my-3">
            <div class="col"><h4>Bootstrap 4 Chart.js - pie to donut</h4></div>
        </div>
        <div class="row py-2">
            <div class="col-md-4 py-1">
                <div class="card">
                    <div class="card-body">
                        <canvas id="chDonut1"></canvas>
                    </div>
                </div>
            </div>
            <div class="col-md-4 py-1">
                <div class="card">
                    <div class="card-body">
                        <canvas id="chDonut2"></canvas>
                    </div>
                </div>
            </div>
            <div class="col-md-4 py-1">
                <div class="card">
                    <div class="card-body">
                        <canvas id="chDonut3"></canvas>
                    </div>
                </div>
            </div>
        </div>
    </div>

</section>
<script>

    function chart1() {
        var ctx = document.getElementById('myChart');
        var myChart = new Chart(ctx, {
            type: 'bar',
            data: {
                labels: ['Red', 'Blue', 'Yellow', 'Green', 'Purple', 'Orange'],
                datasets: [{
                    label: ' of Votes',
                    data: [12, 19, 3, 5, 2, 3],
                    backgroundColor: ['rgba(255, 99, 132, 1)', 'rgba(54, 162, 235, 1)', 'rgba(255, 206, 86, 1)', 'rgba(75, 192, 192, 1)', 'rgba(153, 102, 255, 1)', 'rgba(255, 159, 64, 1)'],
                    borderColor: ['rgba(255, 99, 132, 1)', 'rgba(54, 162, 235, 1)', 'rgba(255, 206, 86, 1)', 'rgba(75, 192, 192, 1)', 'rgba(153, 102, 255, 1)', 'rgba(255, 159, 64, 1)'],
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

    function chart3() {
        data = {
            datasets: [{backgroundColor: ['red', 'yellow', 'blue'], data: [10, 20, 30]}],
            // 라벨의 이름이 툴팁처럼 마우스가 근처에 오면 나타남
            labels: ['red', 'yellow', 'blue']
        };
        // 가운데 구멍이 없는 파이형 차트
        var ctx1 = document.getElementById("myChart4");
        var myPieChart = new Chart(ctx1, {type: 'pie', data: data, options: {}});
        // 도넛형 차트
        var ctx2 = document.getElementById("myChart5");
        var myDoughnutChart = new Chart(ctx2, {type: 'doughnut', data: data, options: {}});

    }

    function chart4() {
        var colors = ['darkred', 'gray', 'aqua', '#c3e6cb', '#dc3545', '#6c757d'];
        var donutOptions = {
            cutoutPercentage: 10, //도넛두께 : 값이 클수록 얇아짐
            legend: {
                position: 'bottom', padding: 5,
                labels: {
                    pointStyle: 'circle', usePointStyle: true
                }
            }
        };
        var chDonutData1 = {
            labels: ['Bootstrap', 'Popper', 'Other'],
            datasets: [
                {
                    backgroundColor: colors.slice(0, 3), borderWidth: 0, data: [74, 11, 40]
                }
            ]
        };

        var chDonut1 = document.getElementById("chDonut1");
        if (chDonut1) {
            new Chart(chDonut1,
                {
                    type: 'pie',
                    data: chDonutData1,
                    options: donutOptions
                }
            );
        }

        var chDonutData2 = {
            labels: ['Wips', 'Pops', 'Dags'],
            datasets: [
                {
                    backgroundColor: colors.slice(0, 3),
                    borderWidth: 0,
                    data: [40, 45, 30]
                }
            ]
        };
        var chDonut2 = document.getElementById("chDonut2");
        if (chDonut2) {
            new Chart(chDonut2,
                {
                    type: 'pie',
                    data: chDonutData2,
                    options: donutOptions
                }
            );
        }

        var chDonutData3 = {
            labels: ['Angular', 'React', 'Other', 'Otdher'],
            datasets: [
                {
                    backgroundColor: colors.slice(0, 4),
                    borderWidth: 0,
                    data: [21, 45, 55, 33]
                }
            ]
        };
        var chDonut3 = document.getElementById("chDonut3");
        if (chDonut3) {
            new Chart(chDonut3,
                {
                    type: 'doughnut',
                    data: chDonutData3,
                    options: donutOptions
                });
        }


    }

    chart1();
    chart2();
    chart3();
    chart4();

</script>

<c:import url="include/footer.jsp"/>