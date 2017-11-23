// move all of this to ScalingModule
var resizeTimer;

$(document).ready(function(){
    sizeCanvas();
});

$(window).resize(function(e){
//    clearTimeout(resizeTimer);
//    resizeTimer=setTimeout(function(){
//        sizeCanvas();        
//    },10);
    sizeCanvas();
});

var sizeCanvas=function(){
    var gameMainWidth=$("#gameMain").width();
    var brickCanvas=$("canvas")[0];
    var movingCanvas = $("#movingParts")[0];
    brickCanvas.width=gameMainWidth*0.8;
    brickCanvas.height=gameMainWidth*0.8;
    movingCanvas.width = brickCanvas.width;
    movingCanvas.height = brickCanvas.height;
    // level.reScale(brickCanvas.width,brickCanvas.height);
    var pos = brickCanvas.getBoundingClientRect();
    console.log("Brickcanvas left margin: " + pos);
    movingCanvas.style.left = pos.left + "px";
    level.reScale(brickCanvas.width, brickCanvas.height);
};
