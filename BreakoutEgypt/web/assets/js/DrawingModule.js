let DrawingModule = (function(){
    let brickCtx, movingPartsCtx, brickCanvas, movingPartsCanvas;
    let mouse = {
        x: 0,
        y: 0
    };

    function doDocumentLoaded(){
        $('canvas').on('mousemove', updateMouseX);
        initCanvasAndContextFields();

        mouse.y = getBrickCanvasDimensions().height - 50;
    }

    function updateMouseX(e) {
        let bounds = this.getBoundingClientRect();
        mouse.x = e.clientX - bounds.left;
    }

    function initCanvasAndContextFields(){
        brickCanvas = $('#stationaryParts')[0];
        brickCtx = brickCanvas.getContext('2d');
        movingPartsCanvas = $('#movingParts')[0];
        movingPartsCtx = movingPartsCanvas.getContext('2d');

        resizeCanvasses(getBrickCanvasDimensions().width, getBrickCanvasDimensions().height);
    }

    function draw() {
        // initial values 300 x 300
        movingPartsCtx.clearRect(0, 0, movingPartsCanvas.width, movingPartsCanvas.height);

        movingPartsCtx.beginPath();
        movingPartsCtx.shadowColor = "black";
        movingPartsCtx.shadowOffsetX = 1;
        movingPartsCtx.shadowOffsetY = 1;

        drawBall();

        setPaddleX();
        level.sendClientLevelState();

        drawPaddles();

        if (!level.allLevelsComplete) {
            window.requestAnimationFrame(draw);
        } else {
            console.log("Completed all levels");
        }
    }

    function drawBall() {
        movingPartsCtx.fillStyle = ImageLoader.patterns["fire"];
        // box2d draws circle from center
        movingPartsCtx.shadowColor = "blue";
        level.balls.forEach(function (ball) {
            movingPartsCtx.beginPath();
            movingPartsCtx.arc(Math.round(ball.x), Math.round(ball.y), (ball.width), 0, 2 * Math.PI, false);
            movingPartsCtx.fill();
        });

        movingPartsCtx.shadowBlur = 0;
    }

    function drawPaddles() {
        level.paddles.forEach(function (paddle) {
            movingPartsCtx.strokeStyle = paddle.color;
            movingPartsCtx.beginPath();
            movingPartsCtx.arc(paddle.x + paddle.width / 2, paddle.y, paddle.width / 2, 1 * Math.PI, 2 * Math.PI);
            movingPartsCtx.stroke();
        });
    }

    function updateStaticContent(){
        updateBricks();
        drawLevelNumber(level.level);
        drawLives(level.lives);
    }
    function updateBricks() {
        brickCtx.clearRect(0, 0, brickCanvas.width, brickCanvas.height);
        level.bricks.forEach(function (brick) {
            brick.draw(brickCtx);
        });
    }

    function setPaddleX() {
        let paddle = level.mypaddle;
        paddle.x = mouse.x - paddle.width / 2;

        if (paddle.x < 0) {
            paddle.x = 0;
        } else if (movingPartsCanvas.width - paddle.width < paddle.x) {
            paddle.x = movingPartsCanvas.width - paddle.width;
        }

        let godImg = ImageLoader.images["god"];

        let godImgPosition = {x: level.mypaddle.x + (level.mypaddle.width / 2) - (godImg.width / 2),
            y: level.mypaddle.y - (level.mypaddle.width * 0.4)};
        movingPartsCtx.drawImage(godImg, godImgPosition.x, godImgPosition.y);
    }

    function drawLives(lives) {
        let height = (brickCanvas.height - level.mypaddle.y) * 0.7;
        let startX = brickCanvas.width - 5 - height;
        for (let i = 0; i < lives; i++) {
            brickCtx.drawImage(ImageLoader.images["live"], startX, level.mypaddle.y * 1.05, height, height);
            startX -= (height * 0.6);
        }
    }

    function drawLevelNumber(levelnumber) {
        $("#gameMain").find("#level").html("Level: " + levelnumber);
    }

    function getBrickCanvasDimensions(){
        return {
            width: brickCanvas.width,
            height: brickCanvas.height
        }
    }


    function resizeCanvasses(width, height){
        brickCanvas.width=width;
        brickCanvas.height=height;
        movingPartsCanvas.width = brickCanvas.width;
        movingPartsCanvas.height = brickCanvas.height;

        let pos = brickCanvas.getBoundingClientRect();
        movingPartsCanvas.style.left = pos.left + "px";

        updateStaticContent();
    }

    function createPattern(image, mode){
        return brickCtx.createPattern(image, mode);
    }

    return {
        initCanvasAndContextFields: initCanvasAndContextFields,
        draw: draw,
        updateStaticContent: updateStaticContent,
        getBrickCanvasDimensions: getBrickCanvasDimensions,
        resizeCanvasses: resizeCanvasses,
        createPattern: createPattern,
        doDocumentLoaded: doDocumentLoaded
    }

})();






