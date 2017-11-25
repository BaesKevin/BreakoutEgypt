const Level = (function () {
    let Level = function () {
        this.init(0, 0, false, false, false);
        this.initLevelState([],[],[])
    };

    Level.prototype.init = function(level, lives, levelComplete, gameOver, allLevelsComplete){
        this.levelComplete = levelComplete;
        this.gameOver = gameOver;
        this.allLevelsComplete = allLevelsComplete;
        this.level = level;
        this.lives = lives;
    };

    Level.prototype.initLevelState = function(balls, bricks, paddles, myPaddleName){
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

        if(myPaddleName){
            this.mypaddle = this.paddles.find(function (paddle) {
                return paddle.name === myPaddleName;
            });
        } else {
            this.mypaddle = {x: 0, y: 0, width: 0, height: 0, color: 'rgb(0,0,0)'};
        }
    };

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
            console.log(err);
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
        UpdateLevelDataHelper.updateBalldata(json, this);
    };

    Level.prototype.removeBall = function (json) {
        UpdateLevelDataHelper.removeBall(json, this);
    };

    Level.prototype.addBall = function (json) {
        UpdateLevelDataHelper.addBall(json, this);
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


        }

<<<<<<< HEAD
    }).catch(function (err) {
        console.log(err);
        console.log("%c" + err, "background-color:red; color: white;padding:5px;");
        ArcadeWebSocket.close();
    });
};

function parseJsonFromResponse(response) {
    let json = response.json();
    let contentType = response.headers.get("content-type");
    if (contentType && contentType.indexOf("application/json") !== -1) {
        return json;
    } else {
        throw new Error(response.statusText);
    }
}

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
        console.log(ball)
        return ball.name !== json.ball;
    });
};

Level.prototype.addBall = function (json) {
    level.balldata.push({name: json.ball, x: json.x, y: json.y, width: json.width, height: json.height});
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
                    destroyBrick(message);
                    break;
                case "hide":
                    hideBrick(message);
                    break;
                case "show":
                    console.log(json.leveldata.brickactions)
                    showBrick(message);
                    break;
=======
        DrawingModule.updateStaticContent();

    };



    const UpdateLevelDataHelper = (function( self ){

        function destroyBrick(message, self) {
            self.bricks = self.bricks.filter(function (brick) {
                return brick.name !== message.name;
            });
        }

        function hideBrick(message, self) {
            console.log("Hide brick " + message.name);
            let brickToHide = self.bricks.find(
                function (brick) {
                    return message.name === brick.name;
                }
            );

            if (brickToHide) {
                brickToHide.show = false;
>>>>>>> dev_staging
            }

            console.log("hide ball");
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
            self.balls.push({name: json.ball, x: json.x, y: json.y, width: json.width / 2, height: json.height / 2});
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

<<<<<<< HEAD
function showBrick(message) {
    console.log("Show brick " + message.name);
    console.log(message)
    let brickToShow = level.brickdata.find(
        function (brick) {
            return message.name === brick.name;
=======
        function connectAndStartDrawing(self) {
            if (!ArcadeWebSocket.isConnected()) {
                ArcadeWebSocket.connect();
            }

            if (!self.allLevelsComplete) {
                DrawingModule.draw();
            }
>>>>>>> dev_staging
        }

        return {parseJsonFromResponse, initializeNextLevel, connectAndStartDrawing}
    })();

    return Level;
})();

