var url = "ws://localhost:8080/currency/";
var dataLength = 60;

var chart = null;
var currencyToWebSocketMap = new Map([ ['UAH', null], ['USD', null] ]);
var currencyToDataPointsMap = new Map([ ['UAH', []], ['USD', []] ]);
var timeRangeMinutes = 0;

window.onload = function () {
    chart = new CanvasJS.Chart("chartContainer", {
        exportEnabled: true,
        title :{
            text: "Market dynamic"
        },
        data: [{
            type: "splineArea",
            fillOpacity: .03,
            xValueType: "dateTime",
            markerSize: 0,
            dataPoints: currencyToDataPointsMap.get("UAH")
        },
        {
            type: "splineArea",
            fillOpacity: .03,
            xValueType: "dateTime",
            markerSize: 0,
            dataPoints: currencyToDataPointsMap.get("USD")
        }]
    });
	chart.render();
}

function setTimeRange(rangeMinutes) {
    if (rangeMinutes === timeRangeMinutes) {
        return;
    }
    toggleActive(timeRangeMinutes + "-minute-range");
    timeRangeMinutes = rangeMinutes;
    if (isActive("toggle-uah")) {
            showCurrency('UAH')
    }
    if (isActive("toggle-usd")) {
            showCurrency('USD')
    }
    toggleActive(rangeMinutes + "-minute-range");
}

function toggle(currency) {
    var id = "toggle-" + currency.toLowerCase();

    if (isActive(id)) {
        hideCurrency(currency);
    } else {
        showCurrency(currency);
    }
    toggleActive(id);
}

function isActive(id) {
    return !document.getElementById(id).classList.contains("inactive")
}

function toggleActive(id) {
    if (isActive(id)) {
        document.getElementById(id).classList.add("inactive");
    } else {
        document.getElementById(id).classList.remove("inactive");
    }
}

function showCurrency(currency) {
    hideCurrency(currency); // Cleanup.
    if (timeRangeMinutes !== 0) {
        showRetro(currency, timeRangeMinutes * 60);
        return;
    }

    // Else: show live data.
    var parsedRetroData = getPricesForLastSeconds(currency, 60);
    var dataPoints = currencyToDataPointsMap.get(currency);
    copyToDataPoints(parsedRetroData, dataPoints);

    var ws = new WebSocket(url + currency.toLowerCase());
        ws.onopen = function() {
            log('Info: Connection to ' + currency + ' currency Established.');
        };

        ws.onmessage = function(event) {
            var data = JSON.parse(event.data);
            updateChart(data, currency);
        };

        ws.onerror = function(event) {
            log('Error: ' + event);
            console.error("WebSocket error observed: ", event);
        };

    currencyToWebSocketMap.set(currency, ws);
}

function showRetro(currency, duration) {
    var retroData = getPricesForLastSeconds(currency, duration);
    var dataPoints = currencyToDataPointsMap.get(currency);
    copyToDataPoints(retroData, dataPoints);
    chart.render();
}

function getPricesForLastSeconds(currency, duration) {
    var end = new Date ();
    var start = new Date(Date.now() - (duration * 1000));
    return httpGetJson('http://localhost:8080/api/prices/get-in-range?'
        + 'start=' + start.toISOString()
        + '&end=' + end.toISOString()
        + '&currency=' + currency.toUpperCase()
    );
}

function hideCurrency(currency) {
    currencyToDataPointsMap.get(currency).length = 0;
    var ws = currencyToWebSocketMap.get(currency);
    if (ws != null) {
            ws.close();
            currencyToWebSocketMap.set(currency, null);
    }
    chart.render()
}

function httpGetJson(theUrl)
{
    var xmlHttp = new XMLHttpRequest();
    xmlHttp.open( "GET", theUrl, false ); // false for synchronous request
    xmlHttp.send( null );
    return JSON.parse(xmlHttp.responseText);
}

function copyToDataPoints(rawData, destination) {
    for (var i = 0; i < rawData.length; i++) {
        destination.push({
            x: rawData[i].timestamp.seconds * 1000,
            y: rawData[i].value
        });
    }
}


var updateChart = function (data, currency) {
    var dataPoints = currencyToDataPointsMap.get(currency);
        dataPoints.push({
			x: data.timestamp.seconds * 1000, //timestamp
			y: data.value  //value
		});

	if (dataPoints.length > dataLength) {
		dataPoints.shift();
	}
	chart.render();
};

function log(message)
{
    var console = document.getElementById('logging');
    var p = document.createElement('p');
    p.appendChild(document.createTextNode(message));
    console.appendChild(p);
}
