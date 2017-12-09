let Explosion = (function () {
    let i = 0;
    const opacityStep = 0.07;
    const greenColorStep = 10;
    
    let Explosion = function (noOfParticles, x, y, context) {
        this.context = context;
        this.noOfParticles = noOfParticles;
        this.x = x;
        this.y = y;
        this.name = "boom" + i++;
        this.particles = createParticles(noOfParticles, x, y);
        this.isActive = true;
        this.green = 162;
    };

    Explosion.prototype.explode = function () {
        let noOfParticlesLeft = 0;
        for (let i = 0; i < this.noOfParticles; i++) {
            if (this.particles[i].isVisable) {
                this.particles[i].update();
                this.context.beginPath();
                this.context.fillStyle = "rgba(240, " + this.green + ",14, " + this.particles[i].opacity + ")";
                this.context.arc(this.particles[i].position.getX(), this.particles[i].position.getY(), 3, 0, 2 * Math.PI, false);
                this.context.fill();
                this.particles[i].setOpacity(this.particles[i].opacity - opacityStep);
                if (this.particles[i].opacity <= 0) {
                    this.particles[i].isVisable = false;
                }
                noOfParticlesLeft++;
            }
        }
        this.green -= greenColorStep;
        if (noOfParticlesLeft <= 0) {
            this.isActive = false;
        }
    };

    function createParticles(noOfParticles, x, y) {
        let particles = [];
        for (let i = 0; i < noOfParticles; i++) {
            let speed = (Math.random() * 3) + 1;
            particles.push(new Particle(x, y, speed, Math.random() * Math.PI * 2));
        }
        return particles;
    }

    return Explosion;
})();