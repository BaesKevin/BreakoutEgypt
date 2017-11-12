var canvas, mouse;
var ctx = $("canvas")[0].getContext('2d');

var level = new Level();
var websocket = new ArcadeWebSocket();
var brickImg = new Image();
var personImg = new Image();
var brickPattern;

//var images = [brickImg, personImg];
//var imageSrc = ["assets/media/person.png", "assets/media/brick-wall.png"]

var imageAssets = [
    {image: personImg, src: "assets/media/person.png"},
    {image: brickImg, src: "assets/media/brick-wall.png"}
]

document.addEventListener("DOMContentLoaded", function () {
    canvas = $('canvas')[0];
    level.paddledata.x = canvas.width / 2 - level.paddledata.width / 2;
    level.paddledata.y = canvas.height - 50;

    level.xscaling = canvas.width / 300;
    level.yscaling = canvas.height / 300;
    console.log("xscaling: " + level.xscaling + " yscaling: " + level.yscaling);

    mouse = {
        x: 0,
        y: canvas.height - 50
    };

    $('canvas').on('mousemove', getMouseX);

    loadImages().then(function () {
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
    brickPattern = ctx.createPattern(brickImg, "repeat");
    ctx.shadowColor = "black";
    ctx.shadowOffsetX = 1;
    ctx.shadowOffsetY = 1;

    ctx.fillStyle = level.balldata.color;

    // box2d draws circle from center
    ctx.arc(Math.round(level.balldata.x), Math.round(level.balldata.y), (level.balldata.width / 2), 0, 2 * Math.PI, false);
    ctx.fill();

    level.brickdata.forEach(function (brick) {
        brick.draw(ctx);
    });

    setPaddleX();
    level.sendClientLevelState();

    ctx.strokeStyle = level.paddledata.color;
    ctx.beginPath();
    ctx.arc(level.paddledata.x + level.paddledata.width / 2, level.paddledata.y, level.paddledata.width / 2, 1 * Math.PI, 2 * Math.PI);
    ctx.stroke();

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
    level.paddledata.x = mouse.x - level.paddledata.width / 2;

    if (level.paddledata.x < 0) {
        level.paddledata.x = 0;
    } else if (canvas.width - level.paddledata.width < level.paddledata.x) {
        level.paddledata.x = canvas.width - level.paddledata.width;
    }

    ctx.drawImage(personImg, mouse.x - personImg.width / 2, level.paddledata.y);

}