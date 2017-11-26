/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.breakoutegypt.domain.messages;

import com.breakoutegypt.domain.effects.PowerUp;
import java.math.BigDecimal;
import javax.json.Json;
import javax.json.JsonObjectBuilder;

/**
 *
 * @author BenDB
 */
public class PowerUpMessage implements Message {

    private String name;
    private PowerUp powerup;
    private PowerUpMessageType messageType;

    public PowerUpMessage(String name, PowerUp powerup, PowerUpMessageType pumt) {
        this.name = name;
        this.powerup = powerup;
        this.messageType = pumt;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public MessageType getMessageType() {
        return messageType;
    }

    @Override
    public JsonObjectBuilder toJson() {
        JsonObjectBuilder job = Json.createObjectBuilder();
        
        job.add("powerup", powerup.toJson());
        job.add("powerupaction", getMessageType().toString());
        
        return job;
    }

}
