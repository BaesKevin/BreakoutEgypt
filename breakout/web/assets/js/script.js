var timer = null;
var totSeconds = 0;
var hours = 0;
var minutes = 0;
var seconds = 0;

$(document).ready(function(){
    $("#arcade").on("click",redirectToArcade);
    $("#logout").on("click",logout);
    $("#returnToMain").on("click",redirectToMainMenu);
    $("#toMultiplayer").on("click",redirectToMultiplayer);
    $("#toHighscores").on("click", redirectToHighscore);
    $("#modalPlaceholder").on("click","#returnToMain",redirectToMainMenu);
});
var logout=function(e){
    e.preventDefault();
    modalLogout();
}

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

var loadLives = function (lives) {
    var imagePath = "assets/media/ankLife.png";
    var lifeImages = "";
    for (var i = 0; i < lives; i++) {
        lifeImages += "<img src='" + imagePath + "' alt='life' title='life'/>";
    }
    $("#healthbar").html(lifeImages);
};

var loadLevelOnScreen = function (levelnumber) {
    $("#level").html("<p><span>Level: " + levelnumber + "</span></p>");
};

var incrSeconds = function () {
    totSeconds++;
    hours = parseInt(totSeconds / 3600);
    minutes = parseInt((totSeconds - (hours * 3600)) / 60);
    seconds = totSeconds - (hours * 3600) - (minutes * 60);
};

var startTimer = function () {
    timer = window.setInterval(incrSeconds, 1000);
};

var stopTimer = function () {
    clearInterval(timer);
};