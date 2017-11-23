// ArcadeIndex.js

var level = new Level();


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


