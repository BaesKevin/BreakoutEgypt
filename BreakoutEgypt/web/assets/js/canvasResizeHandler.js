// move all of this to ScalingModule
var resizeTimer;

$(document).ready(function(){
    sizeCanvas();
});

$(window).resize(function(e){
   clearTimeout(resizeTimer);
   resizeTimer=setTimeout(function(){
       sizeCanvas();
   },10);
    sizeCanvas();
});

var sizeCanvas=function(){
    var gameMainWidth=$("#gameMain").width();
    let newWidth = newHeight = gameMainWidth * 0.8;
    DrawingModule.resizeCanvasses(newWidth, newHeight);

    let canvasDimensions = DrawingModule.getBrickCanvasDimensions();
    level.reScale(canvasDimensions.width, canvasDimensions.height);
};
