var shareButton = "<button id='share' class='btn'>Share</button>";
var mainMenuButton = "<button id='mainMenuModalButton' class='btn'>Main Menu</button>";

var modalLevelCompleted = function (levelId, time) {
    var buttons = shareButton;
    buttons += mainMenuButton;
    buttons += "<button id='nextLevelButton' class='btn' data-dismiss='modal'>Next Level</button>";
    printModal("Level " + levelId + " completed", "Congratz! Your time is: " + time, buttons);
};

var modalChooseDifficulty=function(){ 
    var buttons = "";
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
    printModal("Choose difficulty","Choose a difficulty please:",buttons); 
};

var modalAllLevelsCompleted = function (levelId, time) {
    var buttons = shareButton;
    buttons += mainMenuButton;
    buttons += "<button id='highscoreModalButton' class='btn'>Highscores</button>";
    var title = "You completed the last level!";
    var message = "Choose what you want to do now";
    printModal(title, message, buttons);
};

var modalGameOver = function () {
    var buttons = "<button id='retry' class='btn'>Retry</button>";
    buttons += mainMenuButton;
    printModal("Game Over", "GAME OVER", buttons);
};
var modalLogout=function(){
    var buttons="<form action='logout' method='¨POST'><input type='submit' class='btn' value='Logout'/></form>";
    var title = "Logout";
    printModal(title,"Are you sure?",buttons);
};

var modalErrorMessage = function (reason) {
    var buttons = "<button id='mainMenuModalButton' class='btn'>Take me there</button>";
    var title = "Oops! Something went horribly wrong...";
    var message = "We are sorry for this inconvenience. Only thing to do now is to go back to the mainpage."
    if (reason !== null && reason !== undefined) {
        message += "<br/><br/>" + reason;
    }
    printModal(title, message, buttons);
};

var modalQuit=function(){
    var buttons= "<button class='btn' data-dismiss='modal'>Cancel</button>";
    buttons += "<button id='quit' class='btn' data-dismiss='modal'>Quit</button>";
    printModal("Quit","Are you sure?",buttons);
};

var printModal = function (title, content, buttons) {
    var modal = "<div class='modal fade' id='modal'>";
    modal += "<div class='modal-dialog' role='document'>";
    modal += "<div class='modal-content'><div class='modal-header'>";
    modal += "<h5 class='modal-title'>" + title + "</h5>";
    modal += "</div><div class='modal-body'>";
    modal += content;
    modal += "</div><div class='modal-footer'>";
    modal += buttons;
    modal += "</div></div></div></div>";
    $("#modalPlaceholder").html(modal);
    $("#modal").modal({backdrop: 'static', keyboard: false});
};

var logout = function (e) {
    e.preventDefault();
    modalLogout();
};

var redirectToArcade = function (e) {
    e.preventDefault();
    modalChooseDifficulty();
};

var redirectToMainMenu = function (e) {
    e.preventDefault();
    location.replace('index.html');
};

var redirectToMultiplayer = function (e) {
    e.preventDefault();
    location.replace('multiplayerMenu.html');
};

var redirectToHighscore = function (e) {
    e.preventDefault();
    location.replace('highscore.html');
};

$(document).ready(function () {
    $("#arcade").on("click", redirectToArcade);
    $("#logout").on("click", logout);
    $("#returnToMain").on("click", redirectToMainMenu);
    $("#toMultiplayer").on("click", redirectToMultiplayer);
    $("#toHighscores").on("click", redirectToHighscore);
//    $("#modalPlaceholder").on("click", "#retry", restartLevel); //TODO
    $("#modalPlaceholder").on("click", "#mainMenuModalButton", redirectToMainMenu);
    $("#modalPlaceholder").on("click", "#highscoreModalButton", redirectToHighscore);
    $("#modalPlaceholder").on("click", "#returnToMain", redirectToMainMenu);
 });