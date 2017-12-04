let PowerUpModule = (function () {
    let powerups = [];
    
    function checkKey(e) {
        console.log("PowerUpModule: " + e.which);
        if (level !== undefined) {
            switch (e.which) {
                case 49:
                    if (powerups[0] !== undefined)
                        requestActivatePowerUp(powerups[0].name);
                    break;
                case 50:
                    if (powerups[1] !== undefined)
                        requestActivatePowerUp(powerups[1].name);
                    break;
                case 51:
                    if (powerups[2] !== undefined)
                        requestActivatePowerUp(powerups[2].name);
                    break;
            }
        }
    }

    function requestActivatePowerUp(name) {
        var gameId = UtilModule.getParameterByName("gameId");
        console.log("Ajax")
        $.ajax({
            url: "powerup?gameId=" + gameId + "&powerup=" + name,
            method: "POST",
            credentials: "same-origin",
            headers: {
                "Content-Type": "application/x-www-form-urlencoded"
            }
        }).done(function (response) {
            let json = JSON.parse(response);
            activatePowerUp([json]);
            let powerup = powerups.find(function (powerup) {
                return powerup.name === name;
            });
            powerup.active = true;
            level.powerups = powerups;
            DrawingModule.updateStaticContent();
        }).fail(function (err) {
            console.log(err);
        });
    }

    function activatePowerUp(json) {
        switch (json[0].powerupaction) {
            case "ACTIVATEFLOOR":
                let jsonpowerup = json[0].powerup;
                level.floor = ScalingModule.scaleObject({x: jsonpowerup.x, y: jsonpowerup.y, width: jsonpowerup.width, height: jsonpowerup.height}, ScalingModule.scaleXForClient, ScalingModule.scaleYForClient);
                break;
            case "ACTIVATEBROKENPADDLE":
                addBrokenPaddle(json);
                break;
        }
        ScalingModule.scaleAfterResize();
        DrawingModule.updateStaticContent();
    }

    function removeActivePowerup(name) {
        powerups = powerups.filter(function (powerup) {
            return name !== powerup.name;
        });
    }

    function handlePowerUpMessage(json) {
        switch (json[0].powerupaction) {
            case "REMOVEFLOOR":
                level.floor = false;
                removeActivePowerup(json[0].powerup.powerupname);
                break;
            case "REMOVEBROKENPADDLE":
                removeBrokenPaddle(json);
                removeActivePowerup(json[0].powerup.powerupname);
                break;
            case "REMOVEACIDBALL":
                removeActivePowerup(json[0].powerup.powerupname);
                break;
            case "ADDFLOOR":
            case "ADDBROKENPADDLE":
            case "ADDACIDBALL":
                powerups.push({name: json[0].powerup.powerupname, active: false});
        }
        level.powerups = powerups;
        DrawingModule.updateStaticContent();
    }

    function removeBrokenPaddle(json) {
        level.mypaddle.forEach(function (mypaddle) {
            level.paddles = level.paddles.filter(function (paddle) {
                return mypaddle.name === paddle.name;
            })
        })
        level.mypaddle = [];
        let paddleToAdd = ScalingModule.scaleObject(json[0].powerup.base, ScalingModule.scaleXForClient, ScalingModule.scaleYForClient);
        level.gap = 0;
        level.mypaddle.push(paddleToAdd);
        level.paddles.push(paddleToAdd);
    }
    function addBrokenPaddle(json) {
        level.paddles = level.paddles.filter(function (paddle) {
            return level.mypaddle.name === paddle.name;
        })
        level.mypaddle = [];
        for (let i = 0; i < json[0].powerup.brokenpaddle.length; i++) {
            let paddleToAdd = ScalingModule.scaleObject(json[0].powerup.brokenpaddle[i], ScalingModule.scaleXForClient, ScalingModule.scaleYForClient);
            let gap = ScalingModule.scaleXForClient(json[0].powerup.gap);
            level.gap = gap;
            level.mypaddle.push(paddleToAdd);
            level.paddles.push(paddleToAdd);
        }
    }

    $(document).ready(function () {
        $(document).on("keypress", checkKey);
    });

    return {
        handlePowerUpMessage: handlePowerUpMessage
    };

})();