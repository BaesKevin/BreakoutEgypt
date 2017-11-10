var img = new Image();
img.source = "assets/media/brick-wall.png";
var Brick = function (brickdata) {

    this.name = brickdata.name;
    this.x = brickdata.x;
    this.y = brickdata.y;
    this.width = brickdata.width;
    this.height = brickdata.height;
    this.color = brickdata.color;

};

Brick.prototype.draw = function (ctx) {
    ctx.fillStyle = this.color;

    var my_gradient = ctx.createLinearGradient(0, 0, 0, 170);
    my_gradient.addColorStop(0, "olive");
    my_gradient.addColorStop(1, "green");
    ctx.fillStyle = my_gradient;

    ctx.beginPath();
    ctx.moveTo((this.x + this.width / 2), this.y);
    ctx.lineTo(this.x, (this.y + this.height));
    ctx.lineTo((this.x + this.width), (this.y + this.height));
    ctx.fill();
};