var Level = function () {
    this.levelComplete = false;
    this.gameOver = false;
    this.allLevelsComplete = false;
    this.level = 0;
    this.balldata = {x: 0, y: 0, radius: 0, color: 'rgb(0,0,0)'};
    this.brickdata = [];
    this.paddledata = {x: 0, y: 0, width: 0, height: 0, color: 'rgb(255,0,0)'};
    this.lives = 0;
    this.xscaling = 1;
    this.yscaling = 1;
};

Level.prototype.defaultScale = function () {
    this.balldata = scaleObject(this.balldata, false);
    this.paddledata = scaleObject(this.paddledata, false);
    for (var i = 0; i < this.brickdata.length; i++) {
        this.brickdata[i] = scaleBrick(this.brickdata[i], false);
    }
};

Level.prototype.reScale = function (width, height) {
    console.log("RESCALE");
    this.defaultScale();
    this.xscaling = width / 300;
    this.yscaling = height / 300;

    this.balldata = scaleObject(this.balldata, true);
    this.paddledata = scaleObject(this.paddledata, true);
    for (var i = 0; i < this.brickdata.length; i++) {
        this.brickdata[i] = scaleBrick(this.brickdata[i], true);
    }
};

Level.prototype.load = function (level, balldata, brickdata, paddledata, lives) {

    console.log("Load level: got data for level " + level);
    var self = this;
    brickdata.forEach(function (brickjson) {
        self.brickdata.push(scaleBrick(brickjson, true));
    });

    this.balldata = scaleObject(balldata, true);
    this.paddledata = scaleObject(paddledata, true);
    this.lives = lives;
    this.level = level;
    loadLives(lives);
    console.log("lives: " + this.lives);

};

Level.prototype.loadLevel = function () {
    console.log("load level for game  " + getParameterByName("gameId"));
    var gameId = getParameterByName("gameId");

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
                console.log("Load level: got data for level " + response.level);
                loadLevelOnScreen(response.level);
                self.load(response.level, response.ball, response.bricks, response.paddle, response.lives);
            }
        } else {
//            document.location = "/breakout/";
            console.log("%c" + response.error, "background-color:red; color: white;padding:5px;");
        }
    }).then(function () {
        self.levelComplete = false;
        self.gameOver = false;
        if (!self.allLevelsComplete) {
            draw();
        }

    }).catch(function (err) {
        console.log("%c" + err, "background-color:red; color: white;padding:5px;");
        modalErrorMessage(err);
        websocket.close();
    });
};

var scaleObject = function (object, state) {
    var scaleobj = object;
    scaleobj.x = xscale(object.x, state);
    scaleobj.y = yscale(object.y, state);
    scaleobj.width = xscale(object.width, state);
    scaleobj.height = yscale(object.height, state);

    return scaleobj;
};

var scaleBrick = function (brick, state) {
    var scaledBrick = scaleObject(brick, state);
    return new Brick(scaledBrick);
};

Level.prototype.sendClientLevelState = function () {
    if (!this.levelComplete && !this.gameOver) {
        websocket.sendOverSocket(JSON.stringify({
            x: xscale(this.paddledata.x, false) + xscale(this.paddledata.width, false) / 2,
            y: yscale(this.paddledata.y, false)
        }));
    }
};

Level.prototype.updateLevelData = function (json) {
    this.balldata.x = xscale(json.ball.x, true);
    this.balldata.y = yscale(json.ball.y, true);

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
                                return message.name === brick.name
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
                                return message.name === brick.name
                            }
                    );

                    if (brickToShow) {
                        brickToShow.show = true;
                    }
                    break;
            }
        });
    }
};

function xscale(value, isIncoming) {
    if (isIncoming) {
        return value * level.xscaling;
    } else {
        return value / level.xscaling;
    }
}
;

function yscale(value, isIncoming) {
    if (isIncoming) {
        return value * level.yscaling;
    } else {
        return value / level.yscaling;
    }
}
;