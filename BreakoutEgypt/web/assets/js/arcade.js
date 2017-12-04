// ArcadeButtonsModule

var checkKey = function (e) {
    if (level !== undefined) {
        switch (e.which) {
            case 80:
            case 112:
                pauseGame();
                break;
            case 81:
            case 113:
                ModalModule.modalQuit();
                break;
        }
    }
};

var quitGame = function () {
    ArcadeWebSocket.close();
    location.replace("index.jsp");
};

var pauseGame = function () {
    console.log()
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