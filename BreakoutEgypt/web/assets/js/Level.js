const Level = (function () {
    let Level = function () {
        this.init(0, 0, false, false, false);
        this.initLevelState([], [], []);
    };

    Level.prototype.init = function (level, lives, levelComplete, gameOver, allLevelsComplete) {
        this.levelComplete = levelComplete;
        this.gameOver = gameOver;
        this.allLevelsComplete = allLevelsComplete;
        this.level = level;
        this.lives = lives;
        this.floor = false;
        this.gap = 0;
    };

    Level.prototype.initLevelState = function (balls, bricks, paddles, myPaddleName) {
        this.bricks = [];
        bricks.forEach((brickjson) => {
            this.bricks.push(new Brick(brickjson));
        });

        this.balls = [];
        for (let i = 0; i < balls.length; i++) {
            this.balls[i] = balls[i];
            this.balls[i].width = this.balls[i].width / 2;
        }

        this.paddles = [];

        paddles.forEach((paddle) => {
            this.paddles.push(paddle);
        });

        if (myPaddleName) {
            this.mypaddle = [];
            this.mypaddle.push(this.paddles.find(function (paddle) {
                return paddle.name === myPaddleName;
            }));
        } else {
            this.mypaddle = [{x: 0, y: 0, width: 0, height: 0, color: 'rgb(0,0,0)'}];
        }
    };

    Level.prototype.loadLevel = function () {

        let gameId = UtilModule.getParameterByName("gameId");
        let self = this;

        fetch('level?gameId=' + gameId, {method: "GET", credentials: "same-origin",
            headers: {
                "Content-Type": "application/x-www-form-urlencoded"
            }
        }).then(function (response) {
            return LoadLevelHelper.parseJsonFromResponse(response);
        }).then(function (response) {
            LoadLevelHelper.initializeNextLevel(response, self);
        }).then(function () {
            LoadLevelHelper.connectAndStartDrawing(self);
        }).catch(function (err) {
            console.log(err);
            ArcadeWebSocket.close();
        });
    };


    Level.prototype.sendClientLevelState = function () {
        if (!this.levelComplete && !this.gameOver) {

            ArcadeWebSocket.sendOverSocket(JSON.stringify({
                x: ScalingModule.scaleXForServer(this.mypaddle[0].x) + ScalingModule.scaleXForServer(this.mypaddle[0].width) / 2,
                y: ScalingModule.scaleYForServer(this.mypaddle[0].y)
            }));

        }
    };

    Level.prototype.updateBalldata = function (json) {
        UpdateLevelDataHelper.updateBalldata(json, this);
    };

    Level.prototype.removeBall = function (json) {
        UpdateLevelDataHelper.removeBall(json, this);
    };

    Level.prototype.addBall = function (json) {
        UpdateLevelDataHelper.addBall(json, this);
    };

    Level.prototype.handleUpdate = function (json) {
        console.log(json);
        switch (json[0].powerupaction) {
            case "ADDFLOOR":
                let powerup = json[0].powerup;
                level.floor = ScalingModule.scaleObject({x: powerup.x, y: powerup.y, width: powerup.width, height: powerup.height}, ScalingModule.scaleXForClient, ScalingModule.scaleYForClient);
                break;
            case "REMOVEFLOOR":
                level.floor = false;
                break;
            case "ADDBROKENPADDLE":
                level.paddles = level.paddles.filter(function (paddle) {
                    return level.mypaddle.name === paddle.name;
                })
                level.mypaddle = [];
                for (let i = 0; i < json[0].powerup.brokenpaddle.length; i++) {
                    let paddleToAdd = ScalingModule.scaleObject(json[0].powerup.brokenpaddle[i], ScalingModule.scaleXForClient, ScalingModule.scaleYForClient);
                    let gap = ScalingModule.scaleXForClient(json[0].powerup.gap);
                    level.gap = gap;
                    level.mypaddle.push(paddleToAdd);
                    level.paddles.push(paddleToAdd);
                }
                break;
            case "REMOVEBROKENPADDLE":
                level.mypaddle.forEach(function (mypaddle) {
                    level.paddles = level.paddles.filter(function (paddle) {
                        return mypaddle.name === paddle.name;
                    })
                })
                level.mypaddle = [];
                let paddleToAdd = ScalingModule.scaleObject(json[0].powerup.base, ScalingModule.scaleXForClient, ScalingModule.scaleYForClient);
                level.gap = 0;
                level.mypaddle.push(paddleToAdd);
                level.paddles.push(paddleToAdd);
                break;
        }
        ScalingModule.scaleAfterResize();
        DrawingModule.updateStaticContent();
    };

    Level.prototype.updateLevelData = function (json) {

        let self = this;

        if (json.leveldata.ballpositions) {
            UpdateLevelDataHelper.updateBalldata(json.leveldata.ballpositions, self);
        }


        if (json.leveldata.brickactions) {
            console.log("Received actions to perform on bricks");

            json.leveldata.brickactions.forEach(function (message) {
                switch (message.brickaction) {
                    case "destroy":
                        UpdateLevelDataHelper.destroyBrick(message, self);
                        break;
                    case "hide":
                        UpdateLevelDataHelper.hideBrick(message, self);
                        break;
                    case "show":
                        UpdateLevelDataHelper.showBrick(message, self);
                        break;
                }
            });
            DrawingModule.updateStaticContent();
        }
//        DrawingModule.updateStaticContent();

    };



    const UpdateLevelDataHelper = (function (self) {

        function destroyBrick(message, self) {
            self.bricks = self.bricks.filter(function (brick) {
                return brick.name !== message.name;
            });
        }

        function hideBrick(message, self) {
            let brickToHide = self.bricks.find(
                    function (brick) {
                        return message.name === brick.name;
                    }
            );

            if (brickToHide) {
                brickToHide.show = false;
            }

        }

        function showBrick(message, self) {
            let brickToShow = self.bricks.find(
                    function (brick) {
                        return message.name === brick.name;
                    }
            );

            if (brickToShow) {
                brickToShow.show = true;
            }
            console.log("show brick");
        }

        function updateBalldata(json, self) {
            for (let i = 0; i < self.balls.length; i++) {
                self.balls[i].x = ScalingModule.scaleXForClient(json[i].x);
                self.balls[i].y = ScalingModule.scaleYForClient(json[i].y);
            }
        }

        function removeBall(json, self) {
            self.balls = self.balls.filter(function (ball) {
                return ball.name !== json.ball;
            });
            console.log("remove ball");
        }

        function addBall(json, self) {
            self.balls.push(ScalingModule.scaleObject({name: json.ball, x: json.x, y: json.y, width: json.width / 2, height: json.height / 2},
                            ScalingModule.scaleXForClient, ScalingModule.scaleYForClient));
            console.log("add ball");
        }


        return {
            destroyBrick,
            hideBrick,
            showBrick,
            updateBalldata,
            removeBall,
            addBall
        };
    })(  );

    const LoadLevelHelper = (function () {
        function parseJsonFromResponse(response) {
            let json = response.json();
            let contentType = response.headers.get("content-type");
            if (contentType && contentType.indexOf("application/json") !== -1) {
                return json;
            } else {
                throw new Error(response.statusText);
            }
        }

        function initializeNextLevel(response, self) {
            if (!response.error) {
                if (response.allLevelsComplete) {
                    console.log("Load level: got allLevelsComplete message");
                    console.log(response)
                    ModalModule.modalAllLevelsCompleted(self.level);
                    self.allLevelsComplete = true;
                } else {
                    console.log("Load level: got data for level " + response.level);
                    self.init(response.level, response.lives, false, false, false);
                    self.initLevelState(response.balls, response.bricks, response.paddles, response.mypaddle);

                    DrawingModule.updateStaticContent();
                    ScalingModule.scaleLevel(self);

                }
            } else {
                //            document.location = "/breakout/";
                console.log("%c" + response.error, "background-color:red; color: white;padding:5px;");
            }
        }

        function connectAndStartDrawing(self) {
            if (!ArcadeWebSocket.isConnected()) {
                ArcadeWebSocket.connect();
            }

            if (!self.allLevelsComplete) {
                DrawingModule.draw();
            }
        }

        return {parseJsonFromResponse, initializeNextLevel, connectAndStartDrawing}
    })();

    return Level;
})();

