var gameId = getParameterByName("gameId");

var wsUri = "ws://" + document.location.host + "/breakout/gameplay?gameId=" + gameId;
var websocket = new WebSocket(wsUri);
var balldata = {x: 0, y: 0, radius: 0, color: 'rgb(0,0,0)'};
var brickdata = [];
var paddledata = {x: 0, y: 0, width: 0, height: 0, color: 'rgb(255,0,0)'};

function onOpen(evt) {
    console.log('Connection open');
}

function onError(evt) {
    console.error(evt.message);
}

function onMessage(evt) {
    var json = JSON.parse(evt.data);

    if (json && !json.error) {
        if (json.gameOver) {
            console.log("game over");
        } else {
            balldata.x = json.ball.x;
            balldata.y = json.ball.y;

            if (json.destroy) {

                console.log(json.destroy);
                json.destroy.forEach(function (key) {
                    if (key.includes("brick")) {
                        brickdata = brickdata.filter(function (brick) {
                            return brick.name !== key;
                        });
                    }
                })
            }
        }

    } else {
        console.log(json);
//        document.location = "/breakout";
    }

    // todo removed bricks
}

function sendOverSocket(json) {
    websocket.send(json);
}

websocket.onerror = function (evt) {
    onError(evt)
};
websocket.onopen = function (evt) {
    onOpen(evt)
};

websocket.onmessage = function (evt) {
    onMessage(evt);
};