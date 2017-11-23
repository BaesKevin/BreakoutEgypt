const ScalingModule = (function(){
    let xscaling = 1;
    let yscaling = 1;
    let defaultCanvasWidth = 300;
    let defaultCanvasHeight = 300;

    function updateScalingFactors(newWidth, newHeight){
        console.log(`new width: ${newWidth} new height: ${newHeight}`);
        xscaling = newWidth / defaultCanvasWidth;
        yscaling = newHeight / defaultCanvasHeight;

        console.log("ScalingModule: xscaling: " + xscaling + " yscaling: " + yscaling);
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

    function genericLevelScale(level, xScalingFunction, yScalingFunction){
        level.balldata = scaleObject(level.balldata, xScalingFunction, yScalingFunction);
        let scaledPaddles = [];
        level.paddles.forEach(function (paddle) {
            scaledPaddles.push(scaleObject(paddle, xScalingFunction, yScalingFunction));
        });
        level.paddles = scaledPaddles;
        console.log("My paddle after default scale: " + level.mypaddle.x + " " + level.mypaddle.y);

        for (let i = 0; i < level.brickdata.length; i++) {
            level.brickdata[i] = scaleBrick(level.brickdata[i], xScalingFunction, yScalingFunction);
        }
    }

    function scaleLevelToDefault(level){
        genericLevelScale(level, scaleXForServer, scaleYForServer);
    }

    function scaleLevel(level){
        // scaleLevelToDefault(level);
        genericLevelScale(level, scaleXForClient, scaleYForClient);
    }

    return {
        updateScalingFactors: updateScalingFactors,
        scaleLevel: scaleLevel,
        scaleObject: scaleObject,
        scaleXForServer: scaleXForServer,
        scaleYForServer: scaleYForServer,
        scaleXForClient: scaleXForClient,
        scaleYForClient: scaleYForClient
    }


})();

