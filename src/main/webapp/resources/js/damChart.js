
// var dataList = JSON.parse('${list}')

var dates = dataList.map(function(item) {
    var ymdhmDate = new Date(item.ymdhm.time);
    var formattedDate = ymdhmDate.toLocaleString('en-US', { year: 'numeric', month: '2-digit', day: '2-digit', hour: '2-digit', minute: '2-digit' });
    return formattedDate;
});

var swl = dataList.map(function(item) {
    return item.swl;
});

var ecpc = dataList.map(function(item) {
    return item.ecpc;
});

var inf = dataList.map(function(item) {
    return item.inf;
});

var sfw = dataList.map(function(item) {
    return item.sfw;
});

var tototf = dataList.map(function(item) {
    return item.tototf;
});

var fldlmtwl = dataList.map(function(item) {
    return item.fldlmtwl;
});

var pfh = dataList.map(function(item) {
    return item.pfh;
});

var ctx = document.getElementById('myChart').getContext('2d');
var chartData = {
    labels: dates,
    datasets: [
        {
            label: '수위',
            data: swl,
            borderColor: '#00BFFF',
            borderWidth: 0,
            pointRadius: 0,
            fill: true,
            backgroundColor: 'rgba(0, 0, 65, 0.7)',
            lineTension: 0.6
        },
        {
            label: '유량',
            data: ecpc,
            borderColor: '#3DFF92',
            borderWidth: 2,
            pointRadius: 0,
            fill: false,
            lineTension: 0.6
        },
        {
            label: '유입량',
            data: inf,
            borderColor: '#FFE650',
            borderWidth: 1,
            pointRadius: 0,
            fill: true,
            backgroundColor: 'rgba(255, 255, 0, 0.3)'
        },
        {
            label: '저수량',
            data: sfw,
            borderColor: '#64CD3C',
            borderWidth: 2,
            pointRadius: 0,
            fill: false
        },
        {
            label: '총 방류량',
            data: tototf,
            borderColor: '#0000CD',
            borderWidth: 2,
            pointRadius: 0,
            fill: false
        },
        {
            label: '홍수 우려 수위',
            data: fldlmtwl,
            borderColor: '#CD0000',
            borderWidth: 1,
            pointRadius: 0,
            fill: false
        },
        {
            label: '수위 제한선',
            data: pfh,
            borderColor: '#000000',
            borderWidth: 1,
            pointRadius: 0,
            fill: false
        }
    ]
};

var myChart = new Chart(ctx, {
    type: 'line',
    data: chartData,
    options: {
        scales: {}
    }
});

function validateForm() {
    var startDate = new Date(document.getElementById('startDate').value);
    var endDate = new Date(document.getElementById('endDate').value);
    var currentDate = new Date();
    var st = document.getElementById('sd').value;

    var timeDifference = endDate - startDate;
    var daysDifference = timeDifference / (1000 * 60 * 60 * 24);

    if (st == 1) {
        if (daysDifference > 14) {
            alert("시작 날짜와 끝 날짜 간격은 14일 이하여야 합니다.");
            return false;
        }
    } else if (st == 2 || st == 3) {
        if (daysDifference > 30) {
            alert("시작 날짜와 끝 날짜 간격은 30일 이하여야 합니다.");
            return false;
        }
    }

    if (startDate > currentDate || endDate > currentDate) {
        alert("시작 날짜와 끝 날짜는 현재 시간 이전이여야 합니다.");
        return false;
    }

    if (startDate > endDate) {
        alert("시작 날짜는 끝 날짜 이후일 수 없습니다.");
        return false;
    }

    return true;
}
