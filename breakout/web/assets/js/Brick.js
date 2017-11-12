var img = new Image();
img.source = "assets/media/brick-wall.png";
var Brick = function (brickdata) {
    this.name = brickdata.name;
    this.x = brickdata.x;
    this.y = brickdata.y;
    this.width = brickdata.width;
    this.height = brickdata.height;
    this.color = brickdata.color;
    this.show = brickdata.show;
    this.type = brickdata.type;
    this.isTarget = brickdata.isTarget;
}

Brick.prototype.draw = function (ctx) {
    if (this.show) {
        ctx.globalAlpha = 0.7;
        color = getColor(this);

        ctx.fillStyle = color.color1;

        ctx.beginPath();
        ctx.moveTo((this.x + this.width / 2), this.y);
        ctx.lineTo(this.x, (this.y + this.height));
        ctx.lineTo((this.x + this.width), (this.y + this.height));
        ctx.fill();
        
        ctx.fillStyle = brickPattern;
        ctx.fill();
        
        ctx.globalAlpha = 1;
    }
}

function getColor(brick) {

    //REGULAR, UNBREAKABLE, EXPLOSIVE, SWITCH
    var color = {};
    switch (brick.type) {
        case "UNBREAKABLE":
            color.color1 = "gray";
            color.color2 = "silver";
            break;
        case "EXPLOSIVE":
            color.color1 = "red";
            color.color2 = "maroon";
            break;
        case "SWITCH":
            color.color1 = "blue";
            color.color2 = "navy";
            break;
        case "REGULAR":
            if (brick.isTarget) {
                color.color1 = "gold";
                color.color2 = "orange";
            } else {
                color.color1 = "green";
                color.color2 = "olive";
            }
            break;
        default :
            color.color1 = "black";
            color.color2 = "white";
    }
    return color;

}