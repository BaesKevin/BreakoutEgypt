const ModalModule = (function () {

    let shareButton = "<button id='share' class='btn'>Share</button>";
    let mainMenuButton = "<button id='mainMenuModalButton' class='btn'>Main Menu</button>";

    function modalLevelCompleted(levelId, time, brickScore, winnerName) {
        let buttons = shareButton;
        buttons += mainMenuButton;
        buttons += "<button id='retry' class='btn'>Retry</button>";
        buttons += "<button id='nextLevelButton' class='btn'>Next Level</button>";
        printModal("Level " + levelId + " completed", "Congratz " + winnerName + "! Your time is: " + time + ". Brickscore is " + brickScore, buttons);
    }

    function modalChooseDifficulty() {
        let buttons = "";
        buttons += "<form action='showLevels' method='post'>";
        buttons += "<input name='difficulty' type='hidden' value='easy' />";
        buttons += "<input type='submit' class='btn' value='Easy'/>";
        buttons += "</form>";
        buttons += "<form action='showLevels' method='post'>";
        buttons += "<input name='difficulty' type='hidden' value='medium' />";
        buttons += "<input type='submit' class='btn' value='Medium'/>";
        buttons += "</form>";
        buttons += "<form action='showLevels' method='post'>";
        buttons += "<input name='difficulty' type='hidden' value='hard' />";
        buttons += "<input type='submit' class='btn' value='Hard'/>";
        buttons += "</form>";
        printModal("Choose difficulty", "Choose a difficulty please:", buttons, true);
    }

    function modalAllLevelsCompleted(levelId) {
        let buttons = shareButton;
        buttons += mainMenuButton;
        buttons += "<button id='highscoreModalButton' class='btn'>Highscores</button>";
        let title = "You completed the last level!";
        let message = "Choose what you want to do now";
        printModal(title, message, buttons);
    }

    function modalGameOver() {
        let buttons = "<button id='retry' class='btn'>Retry</button>";
        buttons += mainMenuButton;
        printModal("Game Over", "GAME OVER", buttons);
    }

    function modalLogout() {
        let buttons = "<form action='logout' method='¨POST'><input type='submit' class='btn' value='Logout'/></form>";
        let title = "Logout";
        printModal(title, "Are you sure?", buttons, true);
    }

    function modalErrorMessage(reason) {
        let buttons = "<button id='mainMenuModalButton' class='btn'>Take me there</button>";
        let title = "Oops! Something went horribly wrong...";
        let message = "We are sorry for this inconvenience. Only thing to do now is to go back to the mainpage."
        if (reason !== null && reason !== undefined) {
            message += "<br/><br/>" + reason;
        }

        printModal(title, message, buttons);
    }

    function modalQuit() {
        let buttons = "<button class='btn' data-dismiss='modal' id='cancelQuit'>Cancel</button>";
        buttons += "<button id='quit' class='btn' data-dismiss='modal'>Quit</button>";
        printModal("Quit", "Are you sure?", buttons);
    }

    function printModal(title, content, buttons, isClosable) {
        var modal = "<div class='modal fade' id='modal'>";
        modal += "<div class='modal-dialog' role='document'>";
        modal += "<div class='modal-content'><div class='modal-header'>";
        modal += "<h5 class='modal-title'>" + title + "</h5>";
        if (isClosable) {
            modal += "<button type=\"button\" class=\"close\" data-dismiss=\"modal\">&times;</button>";
        }
        modal += "</div><div class='modal-body'>";
        modal += content;
        modal += "</div><div class='modal-footer'>";
        modal += buttons;
        if (isClosable) {
            modal += "<button type=\"button\" class=\"btn cancel\" data-dismiss=\"modal\">Cancel</button>";
        }
        modal += "</div></div></div></div>";
        $("#modalPlaceholder").html(modal);
        $("#modal").modal({backdrop: 'static', keyboard: false});
    }

    function logout(e) {
        e.preventDefault();
        modalLogout();
    }

    function redirectToArcade(e) {
        e.preventDefault();
        modalChooseDifficulty();
    }

    function redirectToMainMenu(e) {
        e.preventDefault();
        location.replace('index');
    }

    function redirectToMultiplayer(e) {
        e.preventDefault();
        location.replace('multiplayerMenu');
    }

    function redirectToHighscore(e) {
        e.preventDefault();
        location.replace('highscores');
    }

    function restartLevel() {

        if (level) {
            let difficulty = UtilModule.getParameterByName("difficulty");
            let levelid = $("#levelid").val();
            document.location = "arcade?startLevel=" + levelid + "&difficulty=" + difficulty;
        }
    }
    
    function redirectToExplanation() {
        document.location = "explanation";
    }

    function doDocumentLoaded() {
        $("#logout").on("click", logout);
        $("#returnToMain").on("click", redirectToMainMenu);
        $("#toMultiplayer").on("click", redirectToMultiplayer);
        $("#toHighscores").on("click", redirectToHighscore);
        $("#toExplanation").on("click", redirectToExplanation);
        $("#modalPlaceholder").on("click", "#mainMenuModalButton", redirectToMainMenu);
        $("#modalPlaceholder").on("click", "#highscoreModalButton", redirectToHighscore);
        $("#modalPlaceholder").on("click", "#returnToMain", redirectToMainMenu);
        $("#modalPlaceholder").on("click", "#retry", restartLevel);
    }

    return {
        redirectToArcade,
        logout,
        redirectToMainMenu,
        redirectToMultiplayer,
        redirectToHighscore,
        modalAllLevelsCompleted,
        modalQuit,
        modalErrorMessage,
        modalGameOver,
        modalLevelCompleted,
        doDocumentLoaded
    }
})();