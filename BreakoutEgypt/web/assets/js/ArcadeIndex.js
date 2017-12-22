
var level = new Level();

var resizeTimer;


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

    var gameId = $("#gameId").val();
    fetch('level', {method: "POST", body: "gameId=" + gameId,credentials: "same-origin",
        headers: {
            "Content-Type": "application/x-www-form-urlencoded"
        }
    });

});


