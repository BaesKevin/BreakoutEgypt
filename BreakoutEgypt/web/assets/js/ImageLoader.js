const ImageLoader = (function(){
    let images = {
        "brick" : new Image(),
        "god" : new Image(),
        "live" : new Image(),
        "gold" : new Image(),
        "fire" : new Image(),
        "acidball" : new Image(),
        "brokenpaddle" : new Image(),
        "floor" : new Image(),
        "projectile": new Image()
    };

    let imageAssets = [
        {image: images["god"], src: "assets/media/egyptian.png"},
        {image: images["brick"], src: "assets/media/brick-wall.png"},
        {image: images["live"], src: "assets/media/ankLife.png"},
        {image: images["gold"], src: "assets/media/gold.jpg"},
        {image: images["fire"], src: "assets/media/blue-fire.jpg"},
        {image: images["brokenpaddle"], src: "assets/media/brokenpaddle.png"},
        {image: images["floor"], src: "assets/media/floor.png"},
        {image: images["acidball"], src: "assets/media/acidball.png"},
        {image: images["projectile"], src: "assets/media/flames.jpg"}
    ];




    let ImageLoader = function () {
        this.patterns = {};
        this.images = images;
    };

    ImageLoader.prototype.loadImages = function(){
        let self = this;
        return getImageRequestPromiseChain().then(function () {

            self.patterns["brick"] = DrawingModule.createPattern(images["brick"], "repeat");
            self.patterns["gold"] = DrawingModule.createPattern(images["gold"], "repeat");
            self.patterns["fire"] = DrawingModule.createPattern(images["fire"], "repeat");
            self.patterns["projectile"] = DrawingModule.createPattern(images["projectile"], "repeat");
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

