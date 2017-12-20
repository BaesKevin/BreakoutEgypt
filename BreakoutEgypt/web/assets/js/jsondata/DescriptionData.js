const DescriptionData = (function () {
    let brickDescriptions = [];
    let powerupDescriptions = [];
    let canvas;
    let ctx;
    let exampleBricks = [];

    function initExampleBricks(json) {
        let keys = Object.keys(json);
        for (let x in keys) {
            exampleBricks.push(new Brick(json[keys[x]]));
        }
    }

    function drawSelectedBrick() {
        canvas = document.getElementById('brickExample');
        ctx = canvas.getContext('2d');
        let select = document.getElementById('bricks');
        let selectedValue = select.options[select.selectedIndex].value;
        let brickToDraw = exampleBricks.filter(x => x.name === selectedValue);
        brickToDraw[0].draw(ctx);
        let descriptionToShow = brickDescriptions.filter(x => x.name === selectedValue);
        document.getElementById('brickDescription').innerHTML = descriptionToShow[0].description;
    }


    function drawSelectedPowerup() {
        canvas = document.getElementById('powerupExample');
        ctx = canvas.getContext('2d');
        let select = document.getElementById('powerups');
        let selectedValue = select.options[select.selectedIndex].value;
        ctx.fillStyle = 'lightblue';
        ctx.fillRect(0, 0, canvas.width, canvas.height);
        ctx.drawImage(ImageLoader.images[selectedValue], 20, 10, 60, 60);
        ctx.font = 20 + "px Arial";
        ctx.textAlign = "start";
        ctx.fillStyle = "black";
        ctx.fillText("1", 80, 90);
        let descriptionToShow = powerupDescriptions.filter(x => x.name === selectedValue);
        document.getElementById('powerupDescription').innerHTML = descriptionToShow[0].description;
    }

    function initBrickDescriptions(json) {
        let keys = Object.keys(json);
        for (let x in keys) {
            brickDescriptions.push({name: keys[x], description: json[keys[x]]});
        }
    }

    function initPowerupDescriptions(json) {
        let keys = Object.keys(json);
        for (let x in keys) {
            powerupDescriptions.push({name: keys[x], description: json[keys[x]]});
        }
    }

    function createHtmlNode(tag, textToShow) {
        let node;
        let text;
        node = document.createElement(tag);
        text = document.createTextNode(textToShow);
        node.appendChild(text);
        return node;
    }

    function showPowerdownData(json) {
        let keys = Object.keys(json);
        let target = document.querySelector('.container:last-of-type');
        for (let x in keys) {
            target.appendChild(createHtmlNode('h4', keys[x]));
            target.appendChild(createHtmlNode('p', json[keys[x]]));
        }
    }

    function getJsonData() {
        return fetch("assets/js/jsondata/JsonDescriptionData.json").then(function (response) {
            return response.json();
        }).then(function (json) {
            initExampleBricks(json.examplebricks);
            initBrickDescriptions(json.brick);
            initPowerupDescriptions(json.powerup);
            showPowerdownData(json.powerdown);
        })
    }

    window.addEventListener("DOMContentLoaded", function () {
        canvas = document.getElementById('brickExample');
        ctx = canvas.getContext('2d');
        ImageLoader.loadImages(ctx).then(function () {
            getJsonData().then(function () {
                drawSelectedBrick();
                drawSelectedPowerup();
            });
        });
        document.getElementById('bricks').addEventListener('change', drawSelectedBrick);
        document.getElementById('powerups').addEventListener('change', drawSelectedPowerup);
    });

})();