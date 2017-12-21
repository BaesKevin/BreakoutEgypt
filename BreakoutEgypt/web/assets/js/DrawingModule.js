let DrawingModule = (function () {
    let brickCtx, movingPartsCtx, brickCanvas, movingPartsCanvas, effectCanvas, effectCtx;
    let mouse = {
        x: 0,
        y: 0
    };
    let explosions = [];

    function doDocumentLoaded() {
        $('canvas').on('mousemove', updateMouseX);
        initCanvasAndContextFields();
        requestAnimationFrame(doExplosions);
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
        effectCanvas = $('#effectCanvas')[0];
        effectCtx = effectCanvas.getContext('2d');

        resizeCanvasses(getBrickCanvasDimensions().width, getBrickCanvasDimensions().height);
    }

    function drawProjectile() {
        movingPartsCtx.fillStyle = ImageLoader.patterns["projectile"];
        // box2d draws circle from center
        level.projectiles.forEach(function (ball) {
            movingPartsCtx.beginPath();
            movingPartsCtx.arc(Math.round(ball.x), Math.round(ball.y), (ball.width), 0, 2 * Math.PI, false);
            movingPartsCtx.fill();
        });

        movingPartsCtx.shadowBlur = 0;
    }

    function draw() {
        // initial values 300 x 300
        movingPartsCtx.clearRect(0, 0, movingPartsCanvas.width, movingPartsCanvas.height);

        movingPartsCtx.beginPath();
        movingPartsCtx.shadowColor = "black";
        movingPartsCtx.shadowOffsetX = 1;
        movingPartsCtx.shadowOffsetY = 1;

        drawBall();
        if (level.projectiles.length > 0)
            drawProjectile();
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
        // X is the most left value of the paddle
        // ctx.arc needs the center. That's why we add the half of the width
        level.paddles.forEach(function (paddle) {
            movingPartsCtx.strokeStyle = paddle.color;
            movingPartsCtx.beginPath();
            if (paddle.y < ScalingModule.scaleXForClient(level.levelDimension) / 2) {
                movingPartsCtx.arc(paddle.x + paddle.width / 2, paddle.y, paddle.width / 2, 1 * Math.PI, 2 * Math.PI, true);
            } else {
                movingPartsCtx.arc(paddle.x + paddle.width / 2, paddle.y, paddle.width / 2, 1 * Math.PI, 2 * Math.PI);
            }
            movingPartsCtx.stroke();
        });
    }

    function updateStaticContent() {
        updateBricks();
        drawLevelNumber(level.level);
        drawFloor();
        drawLives(level.lives);
        updatePowerups();
    }

    function updatePowerups() {
        let imgObj = {x: 1, y: level.levelDimension - 10, width: 7, height: 10};
        imgObj = ScalingModule.scaleObject(imgObj, ScalingModule.scaleXForClient, ScalingModule.scaleYForClient);
        let allPowerups = [];
        let padding = 0;
        let index = 1;
        let pixels = ScalingModule.scaleYForClient(3);
        level.powerups.forEach(function (powerup) {
            if (powerup.active) {
                brickCtx.fillStyle = "lightgreen";
            } else {
                brickCtx.fillStyle = "lightblue";
            }
            let powerupname = powerup.name.replace(/[0-9]/g, "");
            let width = imgObj.width * 0.7;
            let height = (imgObj.height * 0.66);
            let x = ((imgObj.width - width) / 2) + padding;
            let y = imgObj.y;
            brickCtx.fillRect(imgObj.x, imgObj.y, imgObj.width, imgObj.height);
            
            let powerupImage = ImageLoader.images[powerupname];
            if( !powerupImage ){
                powerupImage = ImageLoader.images["floor"];
            }
            brickCtx.drawImage(powerupImage, x, y, width, height);
            brickCtx.font = Math.ceil(pixels) + "px Arial";
            brickCtx.textAlign = "start";
            brickCtx.fillStyle = "black";
            brickCtx.fillText("" + index, Math.ceil(width) + padding, Math.ceil(imgObj.y + height + pixels / 2));
            padding += ScalingModule.scaleXForClient(7);
            imgObj.x += ScalingModule.scaleXForClient(7);
            index++;
        });
    }

    function updateBricks() {
        brickCtx.clearRect(0, 0, brickCanvas.width, brickCanvas.height);
        level.bricks.forEach(function (brick) {
            brick.draw(brickCtx);
        });
    }

    function drawFloor() {
        if (level.floor) {
            brickCtx.fillStyle = "gray";
            brickCtx.fillRect(level.floor.x, level.floor.y - 2, level.floor.width, level.floor.height + 2);
        }
    }

    // the paddle's X position is the left border of the shape, not the center 
    function setPaddleX() {
        let widthOfPaddlesWithGaps = calculateWidthOfAllPaddlesWithGaps();
        let xOfFirstPaddle = mouse.x - widthOfPaddlesWithGaps / 2;
        let canvasWidth = movingPartsCanvas.width;
        let maxPaddleX = canvasWidth - widthOfPaddlesWithGaps;

        if (level.invertedcontrols) {
            xOfFirstPaddle = canvasWidth - xOfFirstPaddle - widthOfPaddlesWithGaps;
        }

        if (xOfFirstPaddle <= 0) {
            xOfFirstPaddle = 0;
        } else if (xOfFirstPaddle >= maxPaddleX) {
            xOfFirstPaddle = maxPaddleX;
        }

        level.mypaddle.forEach(function (paddle) {
            paddle.x = xOfFirstPaddle;
            xOfFirstPaddle += level.gap + paddle.width;
        });

        let godImg = ImageLoader.images["god"];

        let godImgSize = {x: 0, y: 0, width: godImg.width / 3, height: godImg.height / 3};
        godImgSize = ScalingModule.scaleObject(godImgSize, ScalingModule.scaleXForClient, ScalingModule.scaleYForClient);
        let scale = 1;
        if (level.paddles.length > 1) {
            scale = 0.6;
        }

        level.paddles.forEach(function (paddle) {
            let godImgPosition = {x: paddle.x + (paddle.width / 2) - ((godImgSize.width * scale) / 2),
                y: paddle.y - (paddle.width * 0.4)};

            movingPartsCtx.drawImage(godImg, godImgPosition.x, godImgPosition.y, godImgSize.width * scale, godImgSize.height * scale);
        });
    }

    function calculateWidthOfAllPaddlesWithGaps() {

        let paddlewidth = level.mypaddle[0].width;
        let noOfPaddles = level.mypaddle.length;
        let noOfGaps = noOfPaddles - 1;
        let width = noOfPaddles * paddlewidth + noOfGaps * level.gap;
        return width;
    }


    function drawLives(lives) {
        let height = brickCanvas.height * 0.05;
        let startX = brickCanvas.width - 5 - height;
        let imgObj = {x: startX, y: brickCanvas.height - height - 5, width: height, height: height};
        for (let i = 0; i < lives; i++) {
            brickCtx.drawImage(ImageLoader.images["live"], imgObj.x, imgObj.y, imgObj.width, imgObj.height);
            imgObj.x -= (height * 0.6);
        }
    }

    function drawLevelNumber(levelnumber) {
        $("#gameMain").find("#level").html("Level: " + levelnumber);
    }

    function getBrickCanvasDimensions() {
        return {
            width: brickCanvas.width,
            height: brickCanvas.height
        };
    }

    function resizeCanvasses(width, height) {
        brickCanvas.width = width;
        brickCanvas.height = height;
        movingPartsCanvas.width = brickCanvas.width;
        movingPartsCanvas.height = brickCanvas.height;
        effectCanvas.width = brickCanvas.width;
        effectCanvas.height = brickCanvas.height;

        let pos = brickCanvas.getBoundingClientRect();
        movingPartsCanvas.style.left = pos.left + "px";
        effectCanvas.style.left = pos.left + "px";

        updateStaticContent();
    }

    function doExplosions() {
        effectCtx.clearRect(0, 0, effectCanvas.width, effectCanvas.height);
        explosions.forEach(function (myExplosion) {
            if (myExplosion.isActive) {
                myExplosion.explode();
            } else {
                removeExplosion(myExplosion);
            }
        });
        requestAnimationFrame(doExplosions);
    }

    function removeExplosion(explosionToRemove) {
        explosions = explosions.filter(function (explosion) {
            return explosionToRemove.name !== explosion.name;
        })
    }

    function createExplosion(noOfParticles, x, y) {
        let boom = new Explosion(noOfParticles, x, y, effectCtx);
        explosions.push(boom);
    }

    function createPattern(image, mode, ctx) {
        if (ctx === undefined) {
            ctx = brickCtx;
        }
        return ctx.createPattern(image, mode);
    }

    return {
        initCanvasAndContextFields: initCanvasAndContextFields,
        draw: draw,
        updateStaticContent: updateStaticContent,
        getBrickCanvasDimensions: getBrickCanvasDimensions,
        resizeCanvasses: resizeCanvasses,
        createPattern: createPattern,
        doDocumentLoaded: doDocumentLoaded,
        createExplosion: createExplosion,
    }

})();






