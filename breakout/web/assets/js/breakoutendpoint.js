var canvas, mouse;
document.addEventListener("DOMContentLoaded", function () {
    canvas = $('canvas')[0];
    paddledata.x = canvas.width / 2 - paddledata.width / 2;
    paddledata.y = canvas.height - 50;
    mouse = {
        x: 0,
        y: canvas.height - 50
    }

    $('canvas').on('mousemove', getMouseX);

    draw();
});

function draw() {
    var ctx = $('canvas')[0].getContext('2d');
    ctx.clearRect(0, 0, 300, 300);
    ctx.beginPath();

    ctx.fillStyle = balldata.color;
    ctx.arc(balldata.x + balldata.radius / 2, balldata.y + balldata.radius / 2, balldata.radius / 2, 0, 2 * Math.PI, false);
    ctx.fill();

    if (brickdata) {
        ctx.fillStyle = brickdata.color;
        ctx.fillRect(brickdata.x, brickdata.y, brickdata.width, brickdata.height);
    }

    setPaddleX();

    if (websocket.readyState === websocket.OPEN) {
        sendOverSocket(JSON.stringify({
            x: paddledata.x + paddledata.width/2, 
            y: paddledata.y
        }));
    }

    ctx.fillStyle = paddledata.color;
    ctx.fillRect(paddledata.x, paddledata.y, paddledata.width, paddledata.height);


    window.requestAnimationFrame(draw);
}

function getMouseX(e) {

    var bounds = this.getBoundingClientRect();
    mouse.x = e.clientX - bounds.left;
}

function setPaddleX() {
    paddledata.x = mouse.x - paddledata.width / 2;

    if (paddledata.x < 0) {
        paddledata.x = 0;
    } else if (canvas.width - paddledata.width < paddledata.x) {
        paddledata.x = canvas.width - paddledata.width;
    }

}


function movePaddle(x, y) {
    sendOverSocket(JSON.stringify({x: x, y: y}));
}