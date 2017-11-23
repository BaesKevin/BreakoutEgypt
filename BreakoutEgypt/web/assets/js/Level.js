var Level = function () {
    this.levelComplete = false;
    this.gameOver = false;
    this.allLevelsComplete = false;
    this.level = 0;
    this.balldata = {x: 0, y: 0, width: 0, color: 'rgb(0,0,0)'};
    this.brickdata = [];
    this.paddles = [
        {x: 0, y: 0, width: 0, height: 0, color: 'rgb(0,0,0)'},
        {x: 0, y: 0, width: 0, height: 0, color: 'rgb(0,0,0)'}
    ], 
            this.mypaddle = {x: 0, y: 0, width: 0, height: 0, color: 'rgb(0,0,0)'};
    this.lives = 0;
};

Level.prototype.reScale = function (width, height) {
    ScalingModule.updateScalingFactors(width, height);
    ScalingModule.scaleLevel(this);
};

Level.prototype.load = function (level, balldata, brickdata, paddledata, mypaddle, lives) {

    console.log("Load level: got data for level " + level);
    var self = this;
    brickdata.forEach(function (brickjson) {
        self.brickdata.push(new Brick(brickjson));
    });

    this.balldata = balldata;
    this.paddles = [];

    paddledata.forEach(function (paddle) {
        self.paddles.push(paddle);
    });

    this.mypaddle = this.paddles.find(function (paddle) {
        return paddle.name === mypaddle;
    });

    this.lives = lives;
    this.level = level;
    loadLives(lives);

    ScalingModule.scaleLevel(this);
};

Level.prototype.loadLevel = function () {

    var gameId = getParameterByName("gameId");
    console.log("load level " + level + " for game  " + gameId);
    var self = this;

    fetch('level?gameId=' + gameId).then(function (response) {
        var json = response.json();
        var contentType = response.headers.get("content-type");
        if (contentType && contentType.indexOf("application/json") !== -1) {
            return json;
        } else {
            throw new Error(response.statusText);
        }
    }).then(function (response) {
        if (!response.error) {
            if (response.allLevelsComplete) {
                console.log("Load level: got allLevelsComplete message");
                modalAllLevelsCompleted(self.level, time);
                self.allLevelsComplete = true;
            } else {
                level.brickdata = [];
                console.log("Load level: got data for level " + response.level);
                loadLevelOnScreen(response.level);
                self.load(response.level, response.ball, response.bricks, response.paddles, response.mypaddle, response.lives);
                loadLives(level.lives);
            }
        } else {
//            document.location = "/breakout/";
            console.log("%c" + response.error, "background-color:red; color: white;padding:5px;");
        }
    }).then(function () {
        self.levelComplete = false;
        self.gameOver = false;

        // create connection if not already established
        if (!websocket) {
            websocket = new ArcadeWebSocket();
        }
        if (!self.allLevelsComplete) {
            draw();
        }

    }).catch(function (err) {
        console.log(err);
        console.log("%c" + err, "background-color:red; color: white;padding:5px;");
        websocket.close();
    });
};

Level.prototype.sendClientLevelState = function () {
    if (!this.levelComplete && !this.gameOver) {

        if (websocket) {
            websocket.sendOverSocket(JSON.stringify({
                x: ScalingModule.scaleXForServer(this.mypaddle.x) + ScalingModule.scaleXForServer(this.mypaddle.width) / 2,
                y: ScalingModule.scaleYForServer(this.mypaddle.y)
            }));

        }

    }
};

Level.prototype.updateLevelData = function (json) {
    this.balldata.x = ScalingModule.scaleXForClient(json.ball.x);
    this.balldata.y = ScalingModule.scaleYForClient(json.ball.y);

    var self = this;

    if (json.actions) {
        console.log("Received actions to perform on bricks");

        json.actions.forEach(function (message) {
            switch (message.action) {
                case "destroy":
                    self.brickdata = self.brickdata.filter(function (brick) {
                        return brick.name !== message.name;
                    });
                    break;
                case "hide":
                    console.log("Hide brick " + message.name);
                    var brickToHide = self.brickdata.find(
                            function (brick) {
                                return message.name === brick.name;
                            }
                    );

                    if (brickToHide) {
                        brickToHide.show = false;
                    }
                    break;
                case "show":
                    console.log("Show brick " + message.name);

                    var brickToShow = self.brickdata.find(
                            function (brick) {
                                return message.name === brick.name;
                            }
                    );

                    if (brickToShow) {
                        brickToShow.show = true;
                    }

                    break;
            }
        });
    }
    updateBricks();
};