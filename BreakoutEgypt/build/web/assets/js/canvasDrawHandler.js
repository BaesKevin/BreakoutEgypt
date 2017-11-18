function draw() {
    // initial values 300 x 300
    ctx.clearRect(0, 0, canvas.width, canvas.height);

    ctx.beginPath();
    ctx.shadowColor = "black";
    ctx.shadowOffsetX = 1;
    ctx.shadowOffsetY = 1;

    drawBall();

    level.brickdata.forEach(function (brick) {
        brick.draw(ctx);
    });

    setPaddleX();
    level.sendClientLevelState();

    drawPaddles();

    loadLives(level.lives);

    if (!level.allLevelsComplete) {
        window.requestAnimationFrame(draw);
    } else {
        console.log("Completed all levels");
    }
}

function drawBall() {
    ctx.fillStyle = firePattern;

    // box2d draws circle from center
    ctx.shadowBlur = 25;
    ctx.shadowColor = "blue";
    ctx.arc(Math.round(level.balldata.x), Math.round(level.balldata.y), (level.balldata.width), 0, 2 * Math.PI, false);
    ctx.fill();
    ctx.shadowBlur = 0;
}

function drawPaddles() {
    level.paddles.forEach(function (paddle) {
        ctx.strokeStyle = paddle.color;
        ctx.beginPath();
        ctx.arc(paddle.x + paddle.width / 2, paddle.y, paddle.width / 2, 1 * Math.PI, 2 * Math.PI);
        ctx.stroke();
    });
}

function setPaddleX() {
    var paddle = level.mypaddle;
    paddle.x = mouse.x - paddle.width / 2;

    if (paddle.x < 0) {
        paddle.x = 0;
    } else if (canvas.width - paddle.width < paddle.x) {
        paddle.x = canvas.width - paddle.width;
    }
    godImgPosition = {x: level.mypaddle.x + (level.mypaddle.width / 2) - (godImg.width / 2),
        y: level.mypaddle.y - (level.mypaddle.width * 0.4)};
    ctx.drawImage(godImg, godImgPosition.x, godImgPosition.y);
}

var loadLives = function (lives) {
    var height = (canvas.height - level.mypaddle.y) * 0.7;
    var startX = canvas.width - 5 - height;
    for (var i = 0; i < lives; i++) {
        ctx.drawImage(liveImg, startX, level.mypaddle.y * 1.05, height, height);
        startX -= (height * 0.6);
    }
};

var loadLevelOnScreen = function (levelnumber) {
    $("#gameMain").find("#level").html("Level: " + levelnumber);
};