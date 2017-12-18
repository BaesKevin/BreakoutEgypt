/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.breakoutegypt.domain.messages;

import com.breakoutegypt.domain.powers.PowerUp;
import javax.json.Json;
import javax.json.JsonObjectBuilder;

/**
 *
 * @author BenDB
 */
public class PowerUpMessage extends GenericMessage {

    private PowerUp powerup;

    public PowerUpMessage(String name, PowerUp powerup, PowerUpMessageType pumt) {
        super(0, name, pumt);
        this.powerup = powerup;
    }

    @Override
    public JsonObjectBuilder toJson() {
        JsonObjectBuilder job = super.toJson();
        if (powerup != null) {
            job.add("powerup", powerup.toJson());
        }
        job.add("powerupaction", getMessageType().toString());
        
        return job;
    }

}
