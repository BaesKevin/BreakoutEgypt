var Level = function () {
    this.levelComplete = false;
    this.gameOver = false;
    this.allLevelsComplete = false;

    this.balldata = {x: 0, y: 0, radius: 0, color: 'rgb(0,0,0)'};
    this.brickdata = [];
    this.paddledata = {x: 0, y: 0, width: 0, height: 0, color: 'rgb(255,0,0)'};

    this.lives = 0;
};

Level.prototype.load = function (level, balldata, brickdata, paddledata, lives) {

    console.log("Load level: got data for level " + level);
    var self = this;
    brickdata.forEach(function (brickjson) {
        self.brickdata.push(new Brick(brickjson));
    })

    this.balldata = balldata;
    this.paddledata = paddledata;
    this.lives = lives;
    loadLives(lives);
    console.log("lives: " + this.lives);

};

Level.prototype.loadLevel = function () {
    console.log("load level for game  " + getParameterByName("gameId"));
    var gameId = getParameterByName("gameId");

    var self = this;

    fetch('level?gameId=' + gameId).then(function (response) {
        var json = response.json();
        return json;
    }).then(function (response) {
        if (!response.error) {
            if (response.allLevelsComplete) {
                console.log("Load level: got allLevelsComplete message");
                self.allLevelsComplete = true;
            } else {
                console.log("Load level: got data for level " + response.level);

                self.load(response.level, response.ball, response.bricks, response.paddle, response.lives);
            }
        } else
        {
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
        websocket.close();
//        document.location = "/breakout?error=" + err;
    });
};

Level.prototype.sendClientLevelState = function () {
    if (!this.levelComplete && !this.gameOver) {
        websocket.sendOverSocket(JSON.stringify({
            x: this.paddledata.x + this.paddledata.width / 2,
            y: this.paddledata.y
        }));
    }
};

Level.prototype.updateLevelData = function (json) {
    this.balldata.x = json.ball.x;
    this.balldata.y = json.ball.y;

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
                            function(brick){ return message.name === brick.name}
                    );
            
                    if(brickToHide){
                        brickToHide.show = false;
                    }
                    break;
                case "show":
                    console.log("Show brick " + message.name);
                    
                    var brickToShow = self.brickdata.find(
                            function(brick){ return message.name === brick.name}
                    );
                    
                    if(brickToShow){
                        brickToShow.show = true;
                    }
                            
                    break;

            }

        });
    }


};