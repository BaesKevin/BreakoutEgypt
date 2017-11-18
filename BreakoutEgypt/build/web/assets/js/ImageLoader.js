var ImageLoader = function () {
     return loadImages().then(function () {
        brickPattern = ctx.createPattern(brickImg, "repeat");
        goldPattern = ctx.createPattern(goldImg, "repeat");
        firePattern = ctx.createPattern(fireImg, "repeat");
    }).catch(function (err) {
        console.log(err);
    });
}

function loadImages() {
    var sequence = Promise.resolve();

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