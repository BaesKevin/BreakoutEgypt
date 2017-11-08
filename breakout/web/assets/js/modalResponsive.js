var modalLevelCompleted=function(level){
    var buttons="<button class='btn'>Continue</button>";
    printModal("Level "+level+" completed","*placeholder for score and username*",buttons);
};
var modalGameOver=function(){
    var buttons="<button class='btn'>Retry</button>";
    printModal("Game Over","GAME OVER",buttons);
}
var modalStartLevel=function(level){
    printModal("Are you ready?","You will start Level "+level,"");
};
var modalChooseDifficulty=function(){
    var buttons="<form action='arcade' method='post'><input type='submit' class='btn' value='Easy'/></form>";
    buttons+="<form action='arcade' method='post'><input type='submit' class='btn' value='Medium'/></form>";
    buttons+="<form action='arcade' method='post'><input type='submit' class='btn' value='Hard'/></form>";
    printModal("Choose difficulty","Choose a difficulty please:",buttons);
};
var printModal=function(title,content,buttons){
    var modal="<div class='modal fade' id='modal'>";
    modal+="<div class='modal-dialog' role='document'>";
    modal+="<div class='modal-content'><div class='modal-header'>";
    modal+="<h5 class='modal-title'>"+title+"</h5>";
    modal+="<button type='button' class='close' data-dismiss='modal' aria-label='Close'><span aria-hidden='true'>&times;</span></button>";
    modal+="</div><div class='modal-body'>";
    modal+=content;
    modal+="</div><div class='modal-footer'>";
    modal+=buttons;
    modal+="</div></div></div></div>";
    $("#modalPlaceholder").html(modal);
    $("#modal").modal();
};