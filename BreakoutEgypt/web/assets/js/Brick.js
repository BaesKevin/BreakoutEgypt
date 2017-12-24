// BrickModule.js

const Brick = (function () {
    let Brick = function (brickdata) {
        this.name = brickdata.name;
        this.x = brickdata.x;
        this.y = brickdata.y;
        this.width = brickdata.width;
        this.height = brickdata.height;
        this.color = brickdata.color;
        this.show = brickdata.show;
        this.type = brickdata.type;
        this.isTarget = brickdata.isTarget;
        this.effect = brickdata.effect;
        this.isBreakable = brickdata.isBreakable;
        this.isInverted = brickdata.isInverted;
        this.isSquare = brickdata.isSquare;
    };

    Brick.prototype.draw = function (brickCtx) {
        if (this.show) {
            visual = getColor(this);

            brickCtx.fillStyle = visual.color;
            brickCtx.shadowColor = "black";

            if (!this.isSquare) {
                brickCtx.beginPath();
                if (this.isInverted) {
                    brickCtx.moveTo((this.x + this.width), this.y);
                    brickCtx.lineTo((this.x + this.width / 2), this.y + this.height);
                    brickCtx.lineTo(this.x, this.y);
                } else {
                    brickCtx.moveTo((this.x + this.width / 2), this.y);
                    brickCtx.lineTo(this.x, (this.y + this.height));
                    brickCtx.lineTo((this.x + this.width), (this.y + this.height));
                }
                brickCtx.fill();
            } else {
                brickCtx.beginPath();
                
                brickCtx.moveTo(this.x, this.y);
                brickCtx.lineTo(this.x, (this.y + this.height));
                brickCtx.lineTo((this.x + this.width), (this.y + this.height));
                brickCtx.lineTo((this.x + this.width), this.y);
                
                brickCtx.fill();
            }


            brickCtx.fillStyle = visual.pattern;
            brickCtx.fill();
        }
    };

    function getColor(brick) {

        let color = {};
        color.color = "green";
        color.pattern = ImageLoader.patterns["brick"];

        if (brick.effect === "toggle") {
            color.color = "blue";
        } else if (brick.effect === "explosive") {
            color.color = "red";
        } else if (!brick.isBreakable) {
            color.color = "gray";
        } else if (brick.isTarget) {
            color.color = "gold";
            color.pattern = ImageLoader.patterns["gold"];
        }

        return color;

    }

    return Brick;
})();

