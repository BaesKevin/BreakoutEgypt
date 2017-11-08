var gameId = getParameterByName("gameId");

var wsUri = "ws://" + document.location.host + "/breakout/gameplay?gameId=" + gameId;
var websocket = new WebSocket(wsUri);
var balldata = {x: 0, y: 0, radius: 0, color: 'rgb(0,0,0)'};
var brickdata = [];
var paddledata = {x: 0, y: 0, width: 0, height: 0, color: 'rgb(255,0,0)'};
var levelComplete = false;
var gameOver = false;
var allLevelsComplete = false;

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
            console.log("Gameplay: game over");
            gameOver = true;            
        } else if (json.livesLeft) {
            console.log("Gameplay: livesLeft: " + json.livesLeft);
        }
        else if (json.levelComplete) {   
            console.log("Socket received message level complete");
            levelComplete = true;
         
            loadLevel();
                
           
        } else {
           
            updateLevelData(json);
        }
    } else {
        handleLevelUpdateError(json);
    }

    // todo removed bricks
}

function updateLevelData(json) {
    balldata.x = json.ball.x;
    balldata.y = json.ball.y;

    if (json.destroy) {
        console.log("Received bricks to destroy");
        console.log(json.destroy);
        json.destroy.forEach(function (key) {
            if (key.includes("brick")) {
                brickdata = brickdata.filter(function (brick) {
                    return brick.name !== key;
                });
            }
        });
    }
}

function handleLevelUpdateError(json) {
    if (json.error) {
        document.location = "/breakout?error=" + json.error;
    } else
    {
        document.location = "/breakout?error=Something went wrong";
    }
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
