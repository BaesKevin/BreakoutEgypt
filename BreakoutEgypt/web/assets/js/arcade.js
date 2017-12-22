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
    location.replace("index");
};

var pauseGame = function () {
    var gameId = $("#gameId").val();
    fetch("pause?gameid=" + gameId)
            .then(function (response) {
                if (!response.ok)
                    console.log(err);
            })
            .catch(function () {
                console.log(err);
            })
};

var redirectToMainMenu = function (e) {
    e.preventDefault();
    location.replace('index');
};


$(document).ready(function () {

    $(".quit").on("click", ModalModule.modalQuit);
    $("#modalPlaceholder").on("click", "#quit", quitGame);
    $("#pause").on("click", pauseGame);
    $("#modalPlaceholder").on("click", "#returnToMain", redirectToMainMenu);
    $(document).on("keypress", checkKey);
});