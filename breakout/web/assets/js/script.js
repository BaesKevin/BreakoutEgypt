$(document).ready(function(){
    $("#returnToMain").on("click",redirectToMainMenu);
    $("#toMultiplayer").on("click",redirectToMultiplayer);
});
var redirectToMainMenu=function(e){
    e.preventDefault();
    location.replace('index.html');
};
var redirectToMultiplayer=function(e){
    e.preventDefault();
    location.replace('multiplayerMenu.html');
};