/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.breakoutegypt.domain.effects;

import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonObjectBuilder;

/**
 *
 * @author BenDB
 */
public class AcidBallPowerUp implements PowerUp {
    
    @Override
    public void accept(PowerUpHandler puh) {
        puh.handleAcidBall();
    }

    @Override
    public JsonObject toJson() {
        JsonObjectBuilder job = Json.createObjectBuilder();
        job.add("powerupaction", "ACIDBALL");
        return job.build();
    }
    
}
