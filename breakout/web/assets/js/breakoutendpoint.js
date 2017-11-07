var canvas, mouse, lives;
document.addEventListener("DOMContentLoaded", function () {
    canvas = $('canvas')[0];
    paddledata.x = canvas.width / 2 - paddledata.width / 2;
    paddledata.y = canvas.height - 50;
    mouse = {
        x: 0,
        y: canvas.height - 50
    }

    $('canvas').on('mousemove', getMouseX);

    loadLevel();


});



function loadLevel() {
    console.log("load level for game  " + getParameterByName("gameId"));
    var gameId = getParameterByName("gameId");

    fetch('level?gameId=' + gameId).then(function (response) {
        var json = response.json();
        return json;
    }).then(function (response) {
        if (!response.error) {
            if (response.allLevelsComplete) {
                console.log("Load level: got allLevelsComplete message");
                allLevelsComplete = true;
            } else {
                console.log("Load level: got data for level " + response.level);
                brickdata = response.bricks;
                balldata = response.ball;
                paddledata = response.paddle;
                lives = response.lives;
                loadLives(lives);
                console.log("lives: " + response.lives);
            }
        } else
        {
            document.location = "/breakout/";
            console.log("%c" + response.error, "background-color:red; color: white;padding:5px;");
        }
    }).then(function () {
        levelComplete = false;
        gameOver = false;
        if (!allLevelsComplete) {
            draw();
        }

    }).catch(function (err) {
        websocket.close();
        document.location = "/breakout?error=" + err;
    });
}

function draw() {
    var ctx = $('canvas')[0].getContext('2d');
    ctx.clearRect(0, 0, 300, 300);
    ctx.beginPath();

    ctx.fillStyle = balldata.color;
    // box2d draws circle from center
    ctx.arc(Math.round(balldata.x), Math.round(balldata.y), balldata.width / 2, 0, 2 * Math.PI, false);
    ctx.fill();

    brickdata.forEach(function (brick) {
        ctx.fillStyle = brick.color;
        ctx.beginPath();
        ctx.moveTo(brick.x + brick.width / 2, brick.y);
        ctx.lineTo(brick.x, brick.y + brick.height);
        ctx.lineTo(brick.x + brick.width, brick.y + brick.height);
        ctx.fill();
//        ctx.(brick.x, brick.y, brick.width, brick.height);
    })


    setPaddleX();
    sendClientLevelState();

    ctx.fillStyle = paddledata.color;
    ctx.fillRect(paddledata.x, paddledata.y, paddledata.width, paddledata.height);


    window.requestAnimationFrame(draw);
}


function sendClientLevelState() {
    if (websocket.readyState === websocket.OPEN && !levelComplete && !gameOver) {
        sendOverSocket(JSON.stringify({
            x: paddledata.x + paddledata.width / 2,
            y: paddledata.y
        }));
    }
}

function getMouseX(e) {

    var bounds = this.getBoundingClientRect();
    mouse.x = e.clientX - bounds.left;
}

function setPaddleX() {
    paddledata.x = mouse.x - paddledata.width / 2;

    if (paddledata.x < 0) {
        paddledata.x = 0;
    } else if (canvas.width - paddledata.width < paddledata.x) {
        paddledata.x = canvas.width - paddledata.width;
    }

}


function movePaddle(x, y) {
    sendOverSocket(JSON.stringify({x: x, y: y}));
}