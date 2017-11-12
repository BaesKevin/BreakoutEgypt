var shareButton = "<button id='share' class='btn'>Share</button>";
var mainMenuButton = "<button id='mainMenuModalButton' class='btn'>Main Menu</button>";

var modalLevelCompleted = function (levelId, time) {
    var buttons = shareButton;
    buttons += mainMenuButton;
    buttons += "<button id='nextLevelButton' class='btn' data-dismiss='modal'>Next Level</button>";
    printModal("Level " + levelId + " completed", "Congratz! Your time is: " + time, buttons);
};

var modalChooseDifficulty=function(){ 
    var buttons="<form action='arcade' method='post'><input type='submit' class='btn' value='Easy'/></form>"; 
    buttons+="<form action='arcade' method='post'><input type='submit' class='btn' value='Medium'/></form>"; 
    buttons+="<form action='arcade' method='post'><input type='submit' class='btn' value='Hard'/></form>"; 
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
    var buttons = "<button class='btn'>Retry</button><button class='btn' id='returnToMain'>Main Menu</button>";
    printModal("Game Over", "GAME OVER", buttons);
};
var modalLogout=function(){
    var buttons="<form action='logout' method='¨POST'><input type='submit' class='btn' value='Logout'/></form>";
    printModal("Logout","Are you sure?",buttons);
};

var modalErrorMessage = function (reason) {
    var buttons = "<button id='mainMenuModalButton' class='btn'>Take me there</button>";
    var title = "Oops! Something went horribly wrong...";
    var message = "We are sorry for this inconvenience. Only thing to do now is to go back to the mainpage."
    if (reason !== null && reason !== undefined) {
        message += "<br/><br/>" + reason;
    }
    printModal(title, message, buttons);
}

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