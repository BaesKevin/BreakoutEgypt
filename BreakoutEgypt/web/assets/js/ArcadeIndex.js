// ArcadeIndex.js

var level = new Level();

// move all of this to ScalingModule
var resizeTimer;

$(document).ready(function(){
    // sizeCanvas();
    ScalingModule.scaleAfterResize();
});


var sizeCanvas=function(){
    var gameMainWidth=$("#gameMain").width();
    let newWidth = newHeight = gameMainWidth * 0.8;

    level.reScale(newWidth, newHeight);
};

$(window).resize(function(e){
    clearTimeout(resizeTimer);
    resizeTimer=setTimeout(function(){
        // sizeCanvas();
        ScalingModule.scaleAfterResize();
    },10);
});


document.addEventListener("DOMContentLoaded", function () {
    DrawingModule.doDocumentLoaded();
    ModalModule.doDocumentLoaded();
    ScalingModule.doDocumentLoaded();

    ImageLoader.loadImages().then(function () {
        level.loadLevel();
    });

    $("#modalPlaceholder").on("click", "#nextLevelButton", level.loadLevel.bind(level));
});



$("#movingParts")[0].addEventListener("click", function () {

    var gameId = UtilModule.getParameterByName("gameId");
    console.log("Doing post for game " + gameId + " to start the ball.");
    fetch('level', {method: "POST", body: "gameId=" + gameId,
        headers: {
            "Content-Type": "application/x-www-form-urlencoded"
        }
    }).then(function (response) {
        console.log("Server started the game");
    });

});


