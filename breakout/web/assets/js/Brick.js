var Brick = function (brickdata) {

    this.name = brickdata.name;
    this.x = brickdata.x;
    this.y = brickdata.y;
    this.width = brickdata.width;
    this.height = brickdata.height;
    this.color = brickdata.color;

}

Brick.prototype.draw = function (ctx) {

    ctx.fillStyle = this.color;
    
    ctx.beginPath();
    ctx.moveTo((this.x + this.width / 2), this.y);
    ctx.lineTo(this.x, (this.y + this.height));
    ctx.lineTo((this.x + this.width), (this.y + this.height));
    ctx.fill();

}