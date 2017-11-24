const Level = (function () {
    let Level = function () {
        this.levelComplete = false;
        this.gameOver = false;
        this.allLevelsComplete = false;
        this.level = 0;
        this.balldata = [];
        this.brickdata = [];
        this.paddles = [
            {x: 0, y: 0, width: 0, height: 0, color: 'rgb(0,0,0)'},
            {x: 0, y: 0, width: 0, height: 0, color: 'rgb(0,0,0)'}
        ];
        this.mypaddle = {x: 0, y: 0, width: 0, height: 0, color: 'rgb(0,0,0)'};
        this.lives = 0;
    };

    Level.prototype.reScale = function (width, height) {
        ScalingModule.scaleLevel(this, width, height);
    };



    // Level.prototype.load = function (level, jsonballdata, brickdata, paddledata, mypaddle, lives) {
    //     let self = this;
    //     brickdata.forEach(function (brickjson) {
    //         self.brickdata.push(new Brick(brickjson));
    //     });
    //
    //     for (let i = 0; i < jsonballdata.length; i++) {
    //         this.balldata[i] = jsonballdata[i]; //TODO multiple balls
    //         this.balldata[i].width = this.balldata[i].width / 2;
    //     }
    //
    //     this.paddles = [];
    //
    //     paddledata.forEach(function (paddle) {
    //         self.paddles.push(paddle);
    //     });
    //
    //     this.mypaddle = this.paddles.find(function (paddle) {
    //         return paddle.name === mypaddle;
    //     });
    //
    //     this.lives = lives;
    //     this.level = level;
    //
    //     DrawingModule.drawLives(lives);
    //     ScalingModule.scaleLevel(this);
    // };


    Level.prototype.loadLevel = function () {

        let gameId = UtilModule.getParameterByName("gameId");
        let self = this;

        fetch('level?gameId=' + gameId).then(function (response) {
            return LoadLevelHelper.parseJsonFromResponse(response);
        }).then(function (response) {
            LoadLevelHelper.initializeNextLevel(response, self);
        }).then(function () {
            LoadLevelHelper.connectAndStartDrawing(self);
        }).catch(function (err) {
            ArcadeWebSocket.close();
        });
    };


    Level.prototype.sendClientLevelState = function () {
        if (!this.levelComplete && !this.gameOver) {

            ArcadeWebSocket.sendOverSocket(JSON.stringify({
                x: ScalingModule.scaleXForServer(this.mypaddle.x) + ScalingModule.scaleXForServer(this.mypaddle.width) / 2,
                y: ScalingModule.scaleYForServer(this.mypaddle.y)
            }));

        }
    };

    Level.prototype.updateBalldata = function (json) {
        for (let i = 0; i < this.balldata.length; i++) {
            this.balldata[i].x = ScalingModule.scaleXForClient(json[i].x);
            this.balldata[i].y = ScalingModule.scaleYForClient(json[i].y);
        }
    };

    Level.prototype.removeBall = function (json) {
        level.balldata = level.balldata.filter(function (ball) {
            return ball.name !== json.ball;
        });
    };

    Level.prototype.addBall = function (json) {
        level.balldata.push({name: json.ball, x: json.x, y: json.y, width: json.width / 2, height: json.height / 2});
    };

    Level.prototype.updateLevelData = function (json) {

        let self = this;

        if (json.leveldata.ballpositions) {
            self.updateBalldata(json.leveldata.ballpositions);
        }


        if (json.leveldata.brickactions) {
            console.log("Received actions to perform on bricks");

            json.leveldata.brickactions.forEach(function (message) {
                switch (message.brickaction) {
                    case "destroy":
                        UpdateLevelDataHelper.destroyBrick(message);
                        break;
                    case "hide":
                        UpdateLevelDataHelper.hideBrick(message);
                        break;
                    case "show":
                        UpdateLevelDataHelper.showBrick(message);
                        break;
                }
            });

            DrawingModule.updateBricks();
        }

        DrawingModule.drawLives(level.lives);

    };



    const UpdateLevelDataHelper = (function(){
        function destroyBrick(message) {
            level.brickdata = level.brickdata.filter(function (brick) {
                return brick.name !== message.name;
            });
        }

        function hideBrick(message) {
            console.log("Hide brick " + message.name);
            let brickToHide = level.brickdata.find(
                function (brick) {
                    return message.name === brick.name;
                }
            );

            if (brickToHide) {
                brickToHide.show = false;
            }
        }

        function showBrick(message) {
            let brickToShow = self.brickdata.find(
                function (brick) {
                    return message.name === brick.name;
                }
            );

            if (brickToShow) {
                brickToShow.show = true;
            }
        }

        return { destroyBrick, hideBrick, showBrick };
    })();

    const LoadLevelHelper = ( function (){
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
                    ModalModule.modalAllLevelsCompleted(self.level, time);
                    self.allLevelsComplete = true;
                } else {
                    level.brickdata = [];
                    level.balldata = [];
                    console.log("Load level: got data for level " + response.level);
                    DrawingModule.drawLevelNumber(response.level);
                    load(response.level, response.balls, response.bricks, response.paddles, response.mypaddle, response.lives, self);
                    DrawingModule.drawLives(level.lives);
                }
            } else {
                //            document.location = "/breakout/";
                console.log("%c" + response.error, "background-color:red; color: white;padding:5px;");
            }
        }

        function connectAndStartDrawing(self) {
            self.levelComplete = false;
            self.gameOver = false;

            if (!ArcadeWebSocket.isConnected()) {
                ArcadeWebSocket.connect();
            }

            if (!self.allLevelsComplete) {
                DrawingModule.draw();
            }
        }

        function load(level, jsonballdata, brickdata, paddledata, mypaddle, lives, levelObject) {
            // let self = this;
            brickdata.forEach(function (brickjson) {
                levelObject.brickdata.push(new Brick(brickjson));
            });

            for (let i = 0; i < jsonballdata.length; i++) {
                levelObject.balldata[i] = jsonballdata[i]; //TODO multiple balls
                levelObject.balldata[i].width = levelObject.balldata[i].width / 2;
            }

            levelObject.paddles = [];

            paddledata.forEach(function (paddle) {
                levelObject.paddles.push(paddle);
            });

            levelObject.mypaddle = levelObject.paddles.find(function (paddle) {
                return paddle.name === mypaddle;
            });

            levelObject.lives = lives;
            levelObject.level = level;

            DrawingModule.drawLives(lives);
            ScalingModule.scaleLevel(levelObject);
        }

        return {parseJsonFromResponse, initializeNextLevel, connectAndStartDrawing}
    })();

    return Level;
})();

