const ImageLoader = (function(){
    let images = {
        "brick" : new Image(),
        "god" : new Image(),
        "live" : new Image(),
        "gold" : new Image(),
        "fire" : new Image(),
    };

    let imageAssets = [
        {image: images["god"], src: "assets/media/egyptian.png"},
        {image: images["brick"], src: "assets/media/brick-wall.png"},
        {image: images["live"], src: "assets/media/ankLife.png"},
        {image: images["gold"], src: "assets/media/gold.jpg"},
        {image: images["fire"], src: "assets/media/blue-fire.jpg"}
    ];




    let ImageLoader = function () {
        this.patterns = {};
        this.images = images;
    };

    ImageLoader.prototype.loadImages = function(){
        let self = this;
        return getImageRequestPromiseChain().then(function () {
            self.patterns["brick"] = brickCtx.createPattern(images["brick"], "repeat");
            self.patterns["gold"] = brickCtx.createPattern(images["gold"], "repeat");
            self.patterns["fire"] = brickCtx.createPattern(images["fire"], "repeat");
        }).catch(function (err) {
            console.log(err);
        });
    };

    function getImageRequestPromiseChain() {
        let sequence = Promise.resolve();

        return imageAssets.reduce(function (sequence, image) {
            return sequence.then(function () {
                return getImage(image.image, image.src);
            })
        }, Promise.resolve());
    }

    function getImage(img, src) {
        return new Promise(function (resolve, reject) {
            img.onload = function () {
                resolve(src);
            };
            img.src = src;
            img.onerror = function (err) {
                reject(err);
            };
        });
    }

    return new ImageLoader();
})();

