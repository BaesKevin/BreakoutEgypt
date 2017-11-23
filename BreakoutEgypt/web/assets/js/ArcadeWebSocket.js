let ArcadeWebSocket = (function(){
    let Socket = function () {
        this.gameId = getParameterByName("gameId");
        this.wsUri = "ws://" + document.location.host + "/BreakoutEgypt/gameplay?gameId=" + this.gameId;
    };

    Socket.prototype.connect = function(){
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

    Socket.prototype.isConnected = function(){
         let isSocketCreated = typeof this.websocket !== "undefined";

         if( isSocketCreated ){
             let isSocketOpen = this.websocket.readyState === this.websocket.OPEN;

             return isSocketOpen;
         }

         return false;
    };

    Socket.prototype.sendOverSocket = function (json) {
        if (this.isConnected()) {
            this.websocket.send(json);
        }
    };

    Socket.prototype.close = function () {
        this.websocket.close();
    };

    function onOpen(evt) {
        console.log('Connection open');
    }

    function onError(evt) {
        console.error(evt);
    }

    function onMessage(evt) {
        try {
            let json = JSON.parse(evt.data);

            if (json && !json.error) {

                if (json.gameOver) {
                    handleGameOver();
                } else if (json.livesLeft) {
                    handleLivesLeft(json);
                } else if (json.levelComplete) {
                    handleLevelComplete(json);
                } else {
                    level.updateLevelData(json);
                }
            } else {
                handleLevelUpdateError(json);
            }

        } catch (err) {
            ModalModule.modalErrorMessage(err);
            websocket.close();
        }

    }

    function handleGameOver(){
        console.log("Gameplay: game over");
        level.lives = 0;
        DrawingModule.drawLives(level.lives);
        level.gameOver = true;
        ModalModule.modalGameOver();
    }

    function handleLivesLeft(json){
        console.log("Gameplay: livesLeft: " + json.livesLeft);
        level.lives = json.livesLeft;
        DrawingModule.drawLives(level.lives);
    }

    function handleLevelComplete(json){
        console.log("Socket received message level complete");
        console.log("%cTime to complete this level: " + json.scoreTimer, "background-color:blue;color:white;padding:5px;");
        console.log("You completed this level in " + scoreTimerFormatter(json.scoreTimer));
        level.levelComplete = true;

        time = scoreTimerFormatter(json.scoreTimer);
        ModalModule.modalLevelCompleted(level.level, scoreTimerFormatter(json.scoreTimer));
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

    return new Socket();
})();



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



