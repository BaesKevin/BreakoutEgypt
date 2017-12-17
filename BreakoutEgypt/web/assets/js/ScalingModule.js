const ScalingModule = (function(){
    let xscaling = 1;
    let yscaling = 1;
    let defaultCanvasWidth = 100;
    let defaultCanvasHeight = 100;

    function updateCanvasDimension(dimension){
        defaultCanvasHeight = dimension;
        defaultCanvasWidth = dimension;
    }
    
    function updateScalingFactors(newWidth, newHeight){
        xscaling = newWidth / defaultCanvasWidth;
        yscaling = newHeight / defaultCanvasHeight;
    }

    function xscale(value, isIncoming) {
        if (isIncoming) {
            return value * xscaling;
        } else {
            return value / xscaling;
        }
    }
    function yscale(value, isIncoming) {
        if (isIncoming) {
            return value * yscaling;
        } else {
            return value / yscaling;
        }
    }

    function scaleXForClient(x){
        return xscale(x, true);
    }

    function scaleYForClient(y){
        return yscale(y, true);
    }

    function scaleXForServer(x){
        return xscale(x, false);
    }

    function scaleYForServer(y){
        return yscale(y, false);
    }

    function scaleObject(object, xScalingFunction, yScalingFunction) {
        let scaleobj = object;
        scaleobj.x = xScalingFunction(object.x);
        scaleobj.y = yScalingFunction(object.y);
        scaleobj.width = xScalingFunction(object.width);
        scaleobj.height = yScalingFunction(object.height);

        return scaleobj;
    }

    function scaleBrick(brick, xScalingFunction, yScalingFunction) {
        return new Brick(scaleObject(brick, xScalingFunction, yScalingFunction));
    }

    function scaleBall(ball, xScalingFunction, yScalingFunction){
        let scaled = scaleObject(ball, xScalingFunction, yScalingFunction);
        return scaled;
    }

    function genericLevelScale(level, xScalingFunction, yScalingFunction){
        for(let i = 0; i < level.balls.length; i++){
            level.balls[i] = scaleBall(level.balls[i], xScalingFunction, yScalingFunction);
        }

        let scaledPaddles = [];
        level.paddles.forEach(function (paddle) {
            scaledPaddles.push(scaleObject(paddle, xScalingFunction, yScalingFunction));
        });
        level.paddles = scaledPaddles;

        for (let i = 0; i < level.bricks.length; i++) {
            level.bricks[i] = scaleBrick(level.bricks[i], xScalingFunction, yScalingFunction);
        }
        if (level.floor) {
            level.floor = scaleObject(level.floor, xScalingFunction, yScalingFunction);
        }
    }

    function scaleLevelToDefault(level){
        genericLevelScale(level, scaleXForServer, scaleYForServer);

    }

    function scaleLevel(level, width, height){
        if(width && height){
            scaleLevelToDefault(level);
            DrawingModule.resizeCanvasses(width, height);
            let canvasDimensions = DrawingModule.getBrickCanvasDimensions();

            updateScalingFactors(width, height);
        }
        genericLevelScale(level, scaleXForClient, scaleYForClient);
        DrawingModule.updateStaticContent(); // TODO change to updateStaticContent
    }

    function doDocumentLoaded(){
        let canvasDimensions = DrawingModule.getBrickCanvasDimensions();
        updateScalingFactors(canvasDimensions.width, canvasDimensions.height);
        scaleAfterResize();
    }

    function scaleAfterResize(){
        let gameMainWidth=$("#gameMain").width();
        let newWidth = newHeight = gameMainWidth * 0.8;

        scaleLevel(level, newWidth, newHeight);
    }
    
    function scaleIncoming(obj) {
        return scaleObject(obj, scaleXForClient, scaleYForClient);
    }

    return {
        updateCanvasDimension,
        updateScalingFactors: updateScalingFactors,
        scaleLevel: scaleLevel,
        scaleLevelToDefault: scaleLevelToDefault,
        scaleObject: scaleObject,
        scaleXForServer: scaleXForServer,
        scaleYForServer: scaleYForServer,
        scaleXForClient: scaleXForClient,
        scaleYForClient: scaleYForClient,
        doDocumentLoaded: doDocumentLoaded,
        scaleAfterResize: scaleAfterResize,
        scaleIncoming: scaleIncoming
    }


})();

