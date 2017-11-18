var checkKey = function (e) {
    if (level !== undefined) {
        if (e.which === 112)
            pauseGame();
        else if (e.which === 113)
            modalQuit();
    }
};

var quitGame = function () {
    websocket.close();
    location.replace("index.html");
    console.log("quit");
};

var pauseGame = function () {
    var gameId = getParameterByName("gameId");
    $.ajax({
        url: "pause?gameid=" + gameId
    }).done(function () {
        console.log("stopped");
    });
    paused = !paused;
};



$(document).ready(function () {
    $(".quit").on("click", modalQuit);
    $("#modalPlaceholder").on("click", "#quit", quitGame);
    $("#pause").on("click", pauseGame);
    
    $(document).on("keypress", checkKey);
});