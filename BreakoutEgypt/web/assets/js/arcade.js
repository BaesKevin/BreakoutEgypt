// ArcadeButtonsModule

var checkKey = function (e) {
    if (level !== undefined) {
        if (e.which === 112)
            pauseGame();
        else if (e.which === 113)
            ModalModule.modalQuit();
    }
};

var quitGame = function () {
    ArcadeWebSocket.close();
    location.replace("index.html");
};

var pauseGame = function () {
    var gameId = UtilModule.getParameterByName("gameId");
    $.ajax({
        url: "pause?gameid=" + gameId
    }).fail(function (err) {
        console.log(err);
    })
};

var redirectToMainMenu = function (e) {
    e.preventDefault();
    location.replace('index.jsp');
};

$(document).ready(function () {
    $(".quit").on("click", ModalModule.modalQuit);
    $("#modalPlaceholder").on("click", "#quit", quitGame);
    $("#pause").on("click", pauseGame);
    $("#modalPlaceholder").on("click", "#returnToMain", redirectToMainMenu);
    $(document).on("keypress", checkKey);
});