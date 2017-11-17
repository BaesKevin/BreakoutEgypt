var paused = false;

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
    if (paused) {
        var action = "resume";
    } else {
        var action = "pause";
    }
    $.ajax({
        url: "pause?gameid=" + gameId + "&action=" + action
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