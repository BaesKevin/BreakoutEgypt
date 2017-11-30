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
    };

    Brick.prototype.draw = function (brickCtx) {
        if (this.show) {
            visual = getColor(this);

            brickCtx.fillStyle = visual.color;
            brickCtx.shadowColor = "black";

            brickCtx.beginPath();
            brickCtx.moveTo((this.x + this.width / 2), this.y);
            brickCtx.lineTo(this.x, (this.y + this.height));
            brickCtx.lineTo((this.x + this.width), (this.y + this.height));
            brickCtx.fill();

            brickCtx.fillStyle = visual.pattern;
            brickCtx.fill();
        }
    };

    function getColor(brick) {

        let color = {};
        switch (brick.type) {
            case "UNBREAKABLE":
                color.color = "gray";
                color.pattern = ImageLoader.patterns["brick"];
                break;
            case "EXPLOSIVE":
                color.color = "red";
                color.pattern = ImageLoader.patterns["brick"];
                break;
            case "SWITCH":
                color.color = "blue";
                color.pattern = ImageLoader.patterns["brick"];
                break;
            case "REGULAR":
                color.color = "green";
                color.pattern = ImageLoader.patterns["brick"];
                break;
            case "TARGET":
                color.color = "gold";
                color.pattern = ImageLoader.patterns["gold"];
                break;
            default :
                color.color = "black";
                color.pattern = ImageLoader.patterns["brick"];
        }
        return color;

    }

    return Brick;
})();

