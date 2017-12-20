/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.breakoutegypt.domain.powers;

import com.breakoutegypt.domain.messages.PowerUpMessage;
import com.breakoutegypt.domain.messages.PowerUpMessageType;
import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonObjectBuilder;

/**
 *
 * @author BenDB
 */
public class AcidBallPowerUp implements PowerUp {
    
    private String name;
    private int range = 1;
    private int playerId;
    
    
    public AcidBallPowerUp(String name) {
        this.name = name;
        this.playerId = 1;
    }

    public int getRange() {
        return range;
    }

    public void setRange(int range) {
        this.range = range;
    }
    
    @Override
    public PowerUpMessage accept(PowerUpHandler puh) {
        return puh.handleAcidBall(this);
    }

    @Override
    public JsonObjectBuilder toJson() {
        JsonObjectBuilder job = Json.createObjectBuilder();
        job.add("powerupaction", "ACIDBALL");
        job.add("powerupname", getName());
        job.add("range", getRange());
        return job;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public PowerUpMessageType getType() {
        return PowerUpMessageType.ADDACIDBALL;
    }

    void addRange(int range) {
        this.range += range;
    }

    @Override
    public void setPlayerId(int i) {
        this.playerId = i;
    }

    @Override
    public int getPlayerId() {
        return this.playerId;
    }
    
    
    
}
