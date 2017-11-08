$(document).ready(function(){
    $("#arcade").on("click",redirectToArcade);
    $("#returnToMain").on("click",redirectToMainMenu);
    $("#toMultiplayer").on("click",redirectToMultiplayer);
});
var redirectToArcade=function(e){
    e.preventDefault();
    modalChooseDifficulty();
}
var redirectToMainMenu=function(e){
    e.preventDefault();
    location.replace('index.html');
};
var redirectToMultiplayer=function(e){
    e.preventDefault();
    location.replace('multiplayerMenu.html');
};
var loadLives=function(lives){
    var imagePath="assets/media/ankLife.png";
    var lifeImages="";
    for(var i=0;i<lives;i++){
        lifeImages+="<img src='"+imagePath+"' alt='life' title='life'/>";
    }
    $("#healthbar").html(lifeImages);
};
var loadLevelOnScreen=function(levelnumber){
    $("#level").html("<p><span>Level: "+levelnumber+"</span></p>");
};
var gameOverMessage=function(){
    $("#healthbar").html("<h1>Game over</h1>");
};