var ArcadeWebSocket = function () {

    this.gameId = getParameterByName("gameId");
    this.wsUri = "ws://" + document.location.host + "/breakout/gameplay?gameId=" + this.gameId;
    this.websocket = new WebSocket(this.wsUri);

    this.websocket.onerror = function (evt) {
        onError(evt);
    };
    this.websocket.onopen = function (evt) {
        onOpen(evt);
    };

    this.websocket.onmessage = function (evt) {
        onMessage(evt);
    };

};

ArcadeWebSocket.prototype.sendOverSocket = function (json) {
    if (this.websocket.readyState === this.websocket.OPEN) {
        this.websocket.send(json);
    }
}

ArcadeWebSocket.prototype.close = function () {
    this.websocket.close();
}

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
            level.gameOver = true;
        } else if (json.livesLeft) {
            console.log("Gameplay: livesLeft: " + json.livesLeft);
            loadLives(json.livesLeft);
        } else if (json.levelComplete) {
            console.log("Socket received message level complete");
            level.levelComplete = true;

            level.loadLevel();

        } else {
            level.updateLevelData(json);
        }
    } else {
        handleLevelUpdateError(json);
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




