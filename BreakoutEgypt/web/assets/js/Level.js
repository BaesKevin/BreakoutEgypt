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
        this.powerups = [];
        this.gap = 0;
        this.projectiles = [];
        this.invertedcontrols = false;
        this.levelDimension = 100;
    };

    Level.prototype.initLevelState = function (balls, bricks, paddles, myPaddleName) {
        this.powerups = [];
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
        let gameId = $("#gameId").val();
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

            let positionToSend = {
                x: ScalingModule.scaleXForServer(this.mypaddle[0].x) + ScalingModule.scaleXForServer(this.mypaddle[0].width) / 2,
                y: ScalingModule.scaleYForServer(this.mypaddle[0].y)
            }

            if (level.invertedcontrols) {
                positionToSend.x = this.levelDimension - positionToSend.x;
            }
            ArcadeWebSocket.sendOverSocket(JSON.stringify(positionToSend));

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
    
    Level.prototype.resizeBody = function(json, useOriginalSize){
        useOriginalSize = useOriginalSize || false;
        UpdateLevelDataHelper.resizeBody(json, useOriginalSize,this);
    }

    Level.prototype.updateLevelData = function (json) {

        let self = this;
        
        if (json.leveldata.ballpositions) {
            UpdateLevelDataHelper.updateBalldata(json.leveldata.ballpositions, self);
        }
        
        if(json.leveldata.paddlepositions){
            UpdateLevelDataHelper.updatePaddledata(json.leveldata.paddlepositions, self);
        }

        if (json.leveldata.brickactions) {

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
            let brickToRemove = self.bricks.find(function (brick) {
                return brick.name === message.name;
            })
            self.bricks = self.bricks.filter(function (brick) {
                return brick.name !== message.name;
            });
            DrawingModule.createExplosion(100, brickToRemove.x + brickToRemove.width / 2, brickToRemove.y + brickToRemove.height / 2)
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
        }

        function updateBalldata(json, self) {
            for (let i = 0; i < self.balls.length; i++) {
                self.balls[i].x = ScalingModule.scaleXForClient(json[i].x);
                self.balls[i].y = ScalingModule.scaleYForClient(json[i].y);
            }
        }
        
        function updatePaddledata(json, self){
            for (let i = 0; i < json.length; i++) {
                self.paddles.forEach(function(paddle){
                    if(paddle.name === json[i].name){
                        paddle.x = ScalingModule.scaleXForClient(json[i].x)  - paddle.width / 2;
                        paddle.y = ScalingModule.scaleYForClient(json[i].y);
                    }
                });
            }
        }

        function removeBall(json, self) {
            self.balls = self.balls.filter(function (ball) {
                return ball.name !== json.name;
            });
        }

        function addBall(json, self) {
            self.balls.push(ScalingModule.scaleObject({name: json.name, x: json.x, y: json.y, width: json.width / 2, height: json.height / 2},
                    ScalingModule.scaleXForClient, ScalingModule.scaleYForClient));
        }
        
        function resizeBody(json, useOriginalSize, self){
            let width = json.powerup.width;
            let height = json.powerup.height;
            
            if(useOriginalSize){
                width = json.powerup.originalWidth;
                height = json.powerup.originalHeight;
            }
            
            let name = json.powerup.bodyname;
            let type = json.powerup.type;
            
            switch(type){
                case "ball":
                    resizeBall(name, width, height, self);
                    break;
            }
        }
        
        function resizeBall(name, width, height, self){
            let ballToResize = self.balls.find(function(ball){
                return ball.name === name;
            });
            
            
            ballToResize.width = ScalingModule.scaleXForClient(width) / 2;
            ballToResize.height = ScalingModule.scaleYForClient(height) / 2;
        }


        return {
            destroyBrick,
            hideBrick,
            showBrick,
            updateBalldata,
            removeBall,
            addBall,
            updatePaddledata,
            resizeBody
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
                    self.playerIndex = response.playerIndex;
                    console.log("%cMy paddle : " + response.mypaddle, "font-size: 2em;");
                    console.log(response.lives);
                    self.levelDimension = response.levelDimension;
                    
                    ScalingModule.updateCanvasDimension(self.levelDimension);
                    DrawingModule.updateStaticContent();
                    ScalingModule.scaleLevel(self);

                }
            } else {
                console.log("%c" + response.error, "background-color:red; color: white;padding:5px;");
                ModalModule.modalErrorMessage(response.error);
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

