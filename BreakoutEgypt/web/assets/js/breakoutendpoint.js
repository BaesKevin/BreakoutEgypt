var canvas, mouse;
var ctx = $("canvas")[0].getContext('2d');

var level = new Level();
var brickImg = new Image();
var godImg = new Image();
var liveImg = new Image();
var goldImg = new Image();
var fireImg = new Image();
var brickPattern, goldPattern, firePattern;
var websocket = false; // don't open connection untill level has start

//var images = [brickImg, personImg];
//var imageSrc = ["assets/media/person.png", "assets/media/brick-wall.png"]

var imageAssets = [
    {image: godImg, src: "assets/media/egyptian.png"},
    {image: brickImg, src: "assets/media/brick-wall.png"},
    {image: liveImg, src: "assets/media/ankLife.png"},
    {image: goldImg, src: "assets/media/gold.jpg"},
    {image: fireImg, src: "assets/media/blue-fire.jpg"}
];

document.addEventListener("DOMContentLoaded", function () {
    canvas = $('canvas')[0];

    level.xscaling = canvas.width / 300;
    level.yscaling = canvas.height / 300;
    console.log("xscaling: " + level.xscaling + " yscaling: " + level.yscaling);

    mouse = {
        x: 0,
        y: canvas.height - 50
    };

    $('canvas').on('mousemove', getMouseX);

    loadImages().then(function () {
        brickPattern = ctx.createPattern(brickImg, "repeat");
        goldPattern = ctx.createPattern(goldImg, "repeat");
        firePattern = ctx.createPattern(fireImg, "repeat");
        level.loadLevel();
    }).catch(function (err) {
        console.log(err);
    });

    $("#modalPlaceholder").on("click", "#nextLevelButton", level.loadLevel.bind(level));
    $("#modalPlaceholder").on("click", "#mainMenuModalButton", redirectToMainMenu);
    $("#modalPlaceholder").on("click", "#highscoreModalButton", redirectToHighscore);
});

function loadImages() {
    var sequence = Promise.resolve();

    return imageAssets.reduce(function (sequence, image) {
        return sequence.then(function () {
            return getImage(image.image, image.src);
        })
    }, Promise.resolve());
}

function getImage(img, src) {
    return new Promise(function (resolve, reject) {
        img.onload = function () {
            resolve(src);
        };
        img.src = src;
        img.onerror = function (err) {
            reject(err);
        };
    });
}

$("canvas")[0].addEventListener("click", function () {

    var gameId = getParameterByName("gameId");
    console.log("Doing post for game " + gameId + " to start the ball.");
    fetch('level', {method: "POST", body: "gameId=" + gameId,
        headers: {
            "Content-Type": "application/x-www-form-urlencoded"
        }
    }).then(function (response) {
        console.log("Server started the game");
    });

});

var responsivePaddle = function (paddle) {
    paddle.width = (paddle.width / 300) * canvas.width;
    paddle.height = (paddle.height / 300) * canvas.height;
    return paddle;
};

function draw() {
    // initial values 300 x 300
    ctx.clearRect(0, 0, canvas.width, canvas.height);
    ctx.beginPath();
    ctx.shadowColor = "black";
    ctx.shadowOffsetX = 1;
    ctx.shadowOffsetY = 1;

    ctx.fillStyle = firePattern;

    // box2d draws circle from center
    ctx.shadowBlur = 25;
    ctx.shadowColor = "blue";
    ctx.arc(Math.round(level.balldata.x), Math.round(level.balldata.y), (level.balldata.width), 0, 2 * Math.PI, false);
    ctx.fill();
    ctx.shadowBlur = 0;

    level.brickdata.forEach(function (brick) {
        brick.draw(ctx);
    });

    setPaddleX();
    level.sendClientLevelState();

    level.paddles.forEach(function (paddle) {
        ctx.strokeStyle = paddle.color;
        ctx.beginPath();
        ctx.arc(paddle.x + paddle.width / 2, paddle.y, paddle.width / 2, 1 * Math.PI, 2 * Math.PI);
        ctx.stroke();
    })

    loadLives(level.lives);

    if (!level.allLevelsComplete) {
        window.requestAnimationFrame(draw);
    } else {
        console.log("Completed all levels");
    }
}

function getMouseX(e) {

    var bounds = this.getBoundingClientRect();
    mouse.x = e.clientX - bounds.left;
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