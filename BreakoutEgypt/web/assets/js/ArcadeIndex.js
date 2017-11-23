// ArcadeIndex.js

var mouse;

var level = new Level();


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

    ImageLoader.loadImages().then(function () {
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
