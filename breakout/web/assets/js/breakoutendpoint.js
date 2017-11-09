var canvas, mouse;
var ctx = $("canvas")[0].getContext('2d');

var level = new Level();
var websocket = new ArcadeWebSocket();

document.addEventListener("DOMContentLoaded", function () {
    canvas = $('canvas')[0];
    level.paddledata.x = canvas.width / 2 - level.paddledata.width / 2;
    level.paddledata.y = canvas.height - 50;
    mouse = {
        x: 0,
        y: canvas.height - 50
    }

    $('canvas').on('mousemove', getMouseX);

    level.loadLevel();
});

$("canvas")[0].addEventListener("click", function() {
    
    var gameId = getParameterByName("gameId");
    console.log("Doing post for game " + gameId + " to start the ball.");
    fetch('level', { method: "POST", body: "gameId=" + gameId,
    headers: {
    "Content-Type": "application/x-www-form-urlencoded"
    }
    }).then(function (response) {
        console.log("levelPOST response::" + response);
        console.debug(response);
    });
    
});

var responsivePaddle=function(paddle){
    paddle.width=(paddle.width/300)*canvas.width;
    paddle.height=(paddle.height/300)*canvas.height;
    return paddle;

}

function draw() {
    // initial values 300 x 300
    ctx.clearRect(0, 0, canvas.width, canvas.height);
    ctx.beginPath();

    ctx.fillStyle = level.balldata.color;
    
    // box2d draws circle from center
    ctx.arc(Math.round(level.balldata.x), Math.round(level.balldata.y), (level.balldata.width / 2), 0, 2 * Math.PI, false);
    ctx.fill();
    
    level.brickdata.forEach(function (brick) {
        brick.draw(ctx);
    })

    setPaddleX();
    level.sendClientLevelState();

    ctx.fillStyle = level.paddledata.color;
    ctx.fillRect(level.paddledata.x, level.paddledata.y, level.paddledata.width, level.paddledata.height);

    if (!level.allLevelsComplete) {
        window.requestAnimationFrame(draw);
    }
    else {
        console.log("Completed all levels");
        // some code
    }
}

function getMouseX(e) {

    var bounds = this.getBoundingClientRect();
    mouse.x = e.clientX - bounds.left;
}

function setPaddleX() {
    level.paddledata.x = mouse.x - level.paddledata.width / 2;

    if (level.paddledata.x < 0) {
        level.paddledata.x = 0;
    } else if (canvas.width - level.paddledata.width < level.paddledata.x) {
        level.paddledata.x = canvas.width - level.paddledata.width;
    }

}