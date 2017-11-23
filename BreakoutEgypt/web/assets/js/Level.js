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
    ],
        this.mypaddle = {x: 0, y: 0, width: 0, height: 0, color: 'rgb(0,0,0)'};
    this.lives = 0;
};

Level.prototype.reScale = function (width, height) {
    ScalingModule.updateScalingFactors(width, height);
    ScalingModule.scaleLevelToDefault(this);
    ScalingModule.scaleLevel(this);
};

Level.prototype.load = function (level, jsonballdata, brickdata, paddledata, mypaddle, lives) {

    console.log("Load level: got data for level " + level);
    let self = this;
    brickdata.forEach(function (brickjson) {
        self.brickdata.push(new Brick(brickjson));
    });

    for (let i = 0; i < jsonballdata.length; i++) {
        this.balldata[i] = jsonballdata[i]; //TODO multiple balls
    }

    this.paddles = [];

    paddledata.forEach(function (paddle) {
        self.paddles.push(paddle);
    });

    this.mypaddle = this.paddles.find(function (paddle) {
        return paddle.name === mypaddle;
    });

    this.lives = lives;
    this.level = level;

    DrawingModule.drawLives(lives);
    ScalingModule.scaleLevel(this);
};

Level.prototype.loadLevel = function () {

<<<<<<< HEAD
    let gameId = getParameterByName("gameId");
=======
    var gameId = UtilModule.getParameterByName("gameId");
>>>>>>> typescript
    console.log("load level " + level + " for game  " + gameId);
    let self = this;

    fetch('level?gameId=' + gameId).then(function (response) {
        return parseJsonFromResponse(response);

    }).then(function (response) {
        console.log(response);
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
                self.load(response.level, response.balls, response.bricks, response.paddles, response.mypaddle, response.lives);
                DrawingModule.drawLives(level.lives);
            }
        } else {
//            document.location = "/breakout/";
            console.log("%c" + response.error, "background-color:red; color: white;padding:5px;");
        }
    }).then(function () {
        self.levelComplete = false;
        self.gameOver = false;

        if (!ArcadeWebSocket.isConnected()) {
            ArcadeWebSocket.connect();
        }

        if (!self.allLevelsComplete) {
            DrawingModule.draw();
        }

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
                    showBrick(message);
                    break;
            }
        });
    }
    DrawingModule.updateBricks();
};

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
    console.log("Show brick " + message.name);

    let brickToShow = self.brickdata.find(
        function (brick) {
            return message.name === brick.name;
        }
    );

    if (brickToShow) {
        brickToShow.show = true;
    }
}