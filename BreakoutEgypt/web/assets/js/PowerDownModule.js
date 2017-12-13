const PowerDownModule = (function () {
    
    const powerdownnames = {FLOOD: "FLOOD", PROJECTILE: "PROJECTILE", 
                            PROJECTILEPOSITION: "PROJECTILEPOSITION",
                            REMOVEPROJECTILE: "REMOVEPROJECTILE"};
    
    function handlePowerDown(json) {
        json.forEach(function (jsonmessage) {
            handlePowerdownMessage(jsonmessage);
        });
    }
    
    function addBalls(balls) {
        balls.forEach(function (b) {
            level.addBall(b);
        })
    }
    
    function handlePowerdownMessage(json) {
        console.log(json)
        switch (json.powerdownaction) {
            case powerdownnames.FLOOD:
                addBalls(json.powerdown.decoyballs);
                break;
            case powerdownnames.PROJECTILE:
                level.projectiles.push(ScalingModule.scaleObject(json.powerdown.powerdown, ScalingModule.scaleXForClient, ScalingModule.scaleYForClient));
                break;
            case powerdownnames.PROJECTILEPOSITION:
                let projectile = level.projectiles.find(function (p) {
                    return json.projectile === p.name;
                })
                projectile.x = ScalingModule.scaleXForClient(json.x);
                projectile.y = ScalingModule.scaleYForClient(json.y);
                break;
            case powerdownnames.REMOVEPROJECTILE:
                level.projectiles = level.projectiles.filter(function  (p) {
                    return json.projectile !== p.name;
                })
        }
        
    }
    
    return {
        handlePowerDown
    };
    
})();