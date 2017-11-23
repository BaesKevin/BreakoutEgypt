// ArcadeIndex.js

var mouse, brickCtx, movingPartsCtx, brickCanvas, movingPartsCanvas;

var level = new Level();

// move to ImageAssetModule
var brickImg = new Image();
var godImg = new Image();
var liveImg = new Image();
var goldImg = new Image();
var fireImg = new Image();
var brickPattern, goldPattern, firePattern;


var websocket = false; // don't open connection untill level has start

// move to ImageAssetModule
var imageAssets = [
    {image: godImg, src: "assets/media/egyptian.png"},
    {image: brickImg, src: "assets/media/brick-wall.png"},
    {image: liveImg, src: "assets/media/ankLife.png"},
    {image: goldImg, src: "assets/media/gold.jpg"},
    {image: fireImg, src: "assets/media/blue-fire.jpg"}
];



document.addEventListener("DOMContentLoaded", function () {
    brickCanvas = $('#stationaryParts')[0];
    brickCtx = brickCanvas.getContext('2d');
    movingPartsCanvas = $('#movingParts')[0];
    movingPartsCtx = movingPartsCanvas.getContext('2d');

    ScalingModule.updateScalingFactors(brickCanvas.width, brickCanvas.height);



    mouse = {
        x: 0,
        y: brickCanvas.height - 50
    };

    $('canvas').on('mousemove', getMouseX);

    new ImageLoader().then(function () {
        level.loadLevel();
    });
    
    $("#modalPlaceholder").on("click", "#nextLevelButton", level.loadLevel.bind(level));
});

$("#movingParts")[0].addEventListener("click", function () {

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

function getMouseX(e) {
    var bounds = this.getBoundingClientRect();
    mouse.x = e.clientX - bounds.left;
}
