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
    console.error(evt);
}

function onMessage(evt) {
    var json;
    try {
        json = JSON.parse("evt.data");

        if (json && !json.error) {

            if (json.gameOver) {
                console.log("Gameplay: game over");
                level.gameOver = true;
            } else if (json.livesLeft) {
                console.log("Gameplay: livesLeft: " + json.livesLeft);
                loadLives(json.livesLeft);
            } else if (json.levelComplete) {
                console.log("Socket received message level complete");
                console.log("%cTime to complete this level: " + json.scoreTimer, "background-color:blue;color:white;padding:5px;");
                console.log("You completed this level in " + scoreTimerFormatter(json.scoreTimer));
                level.levelComplete = true;

                time = scoreTimerFormatter(json.scoreTimer);
                modalLevelCompleted(level.level, scoreTimerFormatter(json.scoreTimer));

//            level.loadLevel();

            } else {
                level.updateLevelData(json);
                //console.debug(json);
            }
        } else {
            handleLevelUpdateError(json);
        }

    } catch (err) {
        modalErrorMessage(err);
        websocket.close();
    }

}

function handleLevelUpdateError(json) {
    if (json.error) {
        console.log("%c" + json.error, "background-color: red; color: white;padding:5px;");
        modalErrorMessage();
    } else
    {
//        document.location = "/breakout?error=Something went wrong";
        console.log("%cDamn something went sideways", "background-color: red; color: white;padding:5px;");
        modalErrorMessage();
    }
}

function scoreTimerFormatter(millisecs) {

    var secs = Math.round(millisecs / 1000);

    var mins = parseInt(secs / 60);
    secs = secs % 60;

    return prenull(mins) + ":" + prenull(secs);
}

function prenull(number) {

    return number < 10 ? "0" + number : "" + number;

}
;



