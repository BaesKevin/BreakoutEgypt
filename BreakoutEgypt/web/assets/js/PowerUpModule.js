let PowerUpModule = (function () {
    let powerups = [];

    function checkKey(e) {
        if (level !== undefined) {
            if (e.which >= 49 && e.which <= 57) {
                let powerupindex = e.which - 49;
                if (powerups[powerupindex] !== undefined) {
                    requestActivatePowerUp(powerups[powerupindex].name);
                }
            }
        }
    }

    function requestActivatePowerUp(name) {
        var gameId = UtilModule.getParameterByName("gameId");
        fetch("powerup?gameId=" + gameId + "&powerup=" + name, {method: "post", credentials: "same-origin",
            headers: {
                "Content-Type": "application/x-www-form-urlencoded"
            }
        }).then(function (response) {
            return response.json();
        }).then(function (json) {
            activatePowerUp(json);
            let powerup = powerups.find(function (powerup) {
                return powerup.name === name;
            });
            powerup.active = true;
            level.powerups = powerups;
            DrawingModule.updateStaticContent();
        })
    }

    function activatePowerUp(json) {
        switch (json.powerupaction) {
            case "ACTIVATEFLOOR":
                let jsonpowerup = json.powerup;
                if (!level.floor) {
                    level.floor = ScalingModule.scaleObject({x: jsonpowerup.x, y: jsonpowerup.y, width: jsonpowerup.width, height: jsonpowerup.height},
                            ScalingModule.scaleXForClient, ScalingModule.scaleYForClient);
                }
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
            let sameType = powerup.name.replace(/[0-9]/g, "") === name.replace(/[0-9]/g, "");
            let differentType = !sameType;
            return differentType || ( !powerup.active && sameType );
        });

    }

    function handlePowerUpMessage(json) {
        json.forEach(function (data) {
            doPowerupaction(data);
            level.powerups = powerups;
            DrawingModule.updateStaticContent();
        });
    }

    function doPowerupaction(json) {
        switch (json.powerupaction) {
            case "REMOVEFLOOR":
                level.floor = false;
                removeActivePowerup(json.powerup.powerupname);
                break;
            case "REMOVEBROKENPADDLE":
                removeBrokenPaddle(json);
                removeActivePowerup(json.powerup.powerupname);
                break;
            case "REMOVEACIDBALL":
                removeActivePowerup(json.powerup.powerupname);
                break;
            case "ADDFLOOR":
            case "ADDBROKENPADDLE":
            case "ADDACIDBALL":
                powerups.push({name: json.powerup.powerupname, active: false});
        }
    }

    function removeBrokenPaddle(json) {
        level.mypaddle.forEach(function (mypaddle) {
            level.paddles = level.paddles.filter(function (paddle) {
                return mypaddle.name === paddle.name;
            })
        })
        level.mypaddle = [];
        let paddleToAdd = ScalingModule.scaleObject(json.powerup.base, ScalingModule.scaleXForClient, ScalingModule.scaleYForClient);
        level.gap = 0;
        level.mypaddle.push(paddleToAdd);
        level.paddles.push(paddleToAdd);
    }

    function addBrokenPaddle(json) {
        level.paddles = level.paddles.filter(function (paddle) {
            return level.mypaddle.name === paddle.name;
        })
        level.mypaddle = [];
        for (let i = 0; i < json.powerup.brokenpaddle.length; i++) {
            let paddleToAdd = ScalingModule.scaleObject(json.powerup.brokenpaddle[i], ScalingModule.scaleXForClient, ScalingModule.scaleYForClient);
            let gap = ScalingModule.scaleXForClient(json.powerup.gap);
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