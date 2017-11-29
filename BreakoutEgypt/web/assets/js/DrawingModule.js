let DrawingModule = (function () {
    let brickCtx, movingPartsCtx, brickCanvas, movingPartsCanvas;
    let mouse = {
        x: 0,
        y: 0
    };

    function doDocumentLoaded() {
        $('canvas').on('mousemove', updateMouseX);
        initCanvasAndContextFields();

        mouse.y = getBrickCanvasDimensions().height - 50;
    }

    function updateMouseX(e) {
        let bounds = this.getBoundingClientRect();
        mouse.x = e.clientX - bounds.left;
    }

    function initCanvasAndContextFields() {
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

    function updateStaticContent() {
        updateBricks();
        drawLevelNumber(level.level);
        drawLives(level.lives);
        drawFloor();
    }

    function updateBricks() {
        brickCtx.clearRect(0, 0, brickCanvas.width, brickCanvas.height);
        level.bricks.forEach(function (brick) {
            brick.draw(brickCtx);
        });
    }

    function drawFloor() {
        if (level.floor)
            brickCtx.fillStyle = "gray";
        brickCtx.fillRect(level.floor.x, level.floor.y, level.floor.width, level.floor.height);
    }

    function setPaddleX() {
        let paddle = level.mypaddle;
        let paddlewidth = calculateWidth();

        paddle[0].x = mouse.x - paddlewidth / 2;

        for (let i = 1; i < level.mypaddle.length; i++) {
            paddle[i].x = (paddle[i - 1].x + 2 * paddle[i - 1].width);
        }
        let lastPaddleNo = level.mypaddle.length - 1;

        if (paddle[0].x < 0) {
            paddle[0].x = 0;
        } else if (movingPartsCanvas.width - paddle[lastPaddleNo].width < paddle[lastPaddleNo].x) {
            paddle[lastPaddleNo].x = movingPartsCanvas.width - paddle[lastPaddleNo].width;
        }
        
        if (paddle.length > 1) {
            if (paddle[0].x <= 0) {
                paddle[1].x = (paddle[0].x + (2*paddle[0].width));
            } else if (paddle[0].x > (movingPartsCanvas.width - paddle[1].width - paddle[1].width*2)) {
                paddle[0].x = movingPartsCanvas.width - paddle[1].width - paddle[1].width*2;
            }
        }

        let godImg = ImageLoader.images["god"];

        level.mypaddle.forEach(function (paddle) {
            let godImgPosition = {x: paddle.x + (paddle.width / 2) - (godImg.width / 2),
                y: paddle.y - (paddle.width * 0.4)};
            movingPartsCtx.drawImage(godImg, godImgPosition.x, godImgPosition.y);
        })
    }

    function calculateWidth() {

        let paddlewidth = level.mypaddle[0].width;
        let noOfPaddles = level.mypaddle.length;
        let noOfGaps = noOfPaddles - 1;
        let width = noOfPaddles * paddlewidth + noOfGaps * paddlewidth;
        return width;
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

    function getBrickCanvasDimensions() {
        return {
            width: brickCanvas.width,
            height: brickCanvas.height
        }
    }


    function resizeCanvasses(width, height) {
        brickCanvas.width = width;
        brickCanvas.height = height;
        movingPartsCanvas.width = brickCanvas.width;
        movingPartsCanvas.height = brickCanvas.height;

        let pos = brickCanvas.getBoundingClientRect();
        movingPartsCanvas.style.left = pos.left + "px";

        updateStaticContent();
    }

    function createPattern(image, mode) {
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






