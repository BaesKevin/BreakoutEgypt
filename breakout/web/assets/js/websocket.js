var wsUri = "ws://" + document.location.host + document.location.pathname + "breakoutendpoint";
var websocket = new WebSocket(wsUri);
var balldata = {x: 0, y:0, radius: 0, color: 'rgb(0,0,0)'};
var brickdata = {x:0, y:0, width: 0, height: 0, color: 'rgb(0,0,0)'};
var paddledata = {x:0, y:0, width: 0,height: 0, color: 'rgb(255,0,0)'};

function onOpen(evt) {
    console.log('Connection open');
}

function onError(evt) {
    console.error(evt.message);
}

function onMessage(evt){
    var json = JSON.parse(evt.data);
    balldata = json.ball;
    brickdata = json.brick;
    paddledata.width = json.paddle.width;
    paddledata.height = json.paddle.height;
    paddledata.color = json.paddle.color;
//    paddledata = json.paddle; debug to see if simulation is correct
}

function sendOverSocket(json){
    websocket.send(json);
}

websocket.onerror = function (evt) {
    onError(evt)
};
websocket.onopen = function (evt) {
    onOpen(evt)
};

websocket.onmessage = function(evt){
    onMessage(evt);
};