let Particle = function (x, y, speed, angle) {
    let velocity = null;
    let position = null;

    this.velocity = vector.create(0, 0);

    this.velocity.setLength(speed);
    this.velocity.setAngle(angle);
    this.position = vector.create(x, y);
    this.opacity = 1;
    this.isVisable = true;
};

Particle.prototype.update = function () {
    this.position.addTo(this.velocity);
};

Particle.prototype.setOpacity = function (opa) {
    this.opacity = opa;
};