var resizeTimer;

$(document).ready(function(){
    sizeCanvas();
});

$(window).resize(function(e){
    clearTimeout(resizeTimer);
    resizeTimer=setTimeout(function(){
        sizeCanvas();        
    },400);
    
});

var sizeCanvas=function(){
    var gameMainWidth=$("#gameMain").width();
    var canvas=$("canvas")[0];
    canvas.width=gameMainWidth*0.8;
    canvas.height=gameMainWidth*0.8;
    level.reScale(canvas.width,canvas.height);
};
