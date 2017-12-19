let ArcadeWebSocket = (function () {
    let Socket = function () {
        this.gameId = $("#gameId").val();
        this.wsUri = "ws://" + document.location.host + "/BreakoutEgypt/gameplay?gameId=" + this.gameId;
    };

    Socket.prototype.connect = function () {
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

    Socket.prototype.isConnected = function () {
        let isSocketCreated = typeof this.websocket !== "undefined";

        if (isSocketCreated) {
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
        if (this.isConnected()) {
            this.websocket.close();
        }
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
                if (json.lifeaction && json.playerIndex === level.playerIndex) {
                    console.log(json)
                    if (json.lifeaction === 'gameover') {
                        handleGameOver();
                    }
                    if (json.lifeaction === 'playing') {
                        handleLivesLeft(json);
                    }
                }
                if (json.ballaction) {
                    json.ballaction.forEach(function (msg) {
                        if (msg.ballaction === 'remove') {
                            level.removeBall(msg);
                        }
                        if (msg.ballaction === 'add') {
                            level.addBall(msg);
                        }
                    })
                }
                if (json.levelComplete && !level.levelComplete) {
                    handleLevelComplete(json);
                } else {
                    if (json.leveldata) {
                        if (json.leveldata.powerupactions) {
                            PowerUpModule.handlePowerUpMessage(json.leveldata.powerupactions)
                        }
                        if (json.leveldata.powerdownactions) {
                            PowerDownModule.handlePowerDown(json.leveldata.powerdownactions);
                        }
                        level.updateLevelData(json);
                    }
                }
            } else {
                handleLevelUpdateError(json);
            }

        } catch (err) {
            console.log(err);
            ModalModule.modalErrorMessage(err);
            ArcadeWebSocket.close();
        }

    }

    function handleBrickActions(json) {
        level.updateLevelData(json);
    }

    function handleBallPositionUpdate(json) {
        level.updateBalldata(json);
    }

    function handleGameOver() {
        console.log("Gameplay: game over");
        level.lives = 0;
        DrawingModule.updateStaticContent();
        level.gameOver = true;
        ModalModule.modalGameOver();
    }

    function handleLivesLeft(json) {
        console.log("Gameplay: livesLeft: " + json.livesLeft);
        level.lives = json.livesLeft;
        DrawingModule.updateStaticContent();
    }

    function handleLevelComplete(json) {
        console.log("You completed this level in " + UtilModule.scoreTimerFormatter(json.scoreTimer));
        level.levelComplete = true;

        let time = UtilModule.scoreTimerFormatter(json.scoreTimer);
        ModalModule.modalLevelCompleted(level.level, time, json.brickScore);
    }

    function handleLevelUpdateError(json) {
        if (json.error) {
            console.log("%c" + json.error, "background-color: red; color: white;padding:5px;");
            ModalModule.modalErrorMessage(json.error);
        } else {
            console.log("%cDamn something went sideways", "background-color: red; color: white;padding:5px;");
            ModalModule.modalErrorMessage();
        }
    }

    return new Socket();
})();

