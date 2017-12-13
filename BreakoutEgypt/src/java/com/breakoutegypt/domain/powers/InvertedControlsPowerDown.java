/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.breakoutegypt.domain.powers;

import com.breakoutegypt.domain.messages.PowerDownMessage;
import com.breakoutegypt.domain.messages.PowerDownMessageType;
import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonObjectBuilder;

/**
 *
 * @author BenDB
 */
public class InvertedControlsPowerDown implements PowerDown {

    private int timeActive;
    
    public InvertedControlsPowerDown(int timeActive) {
        this.timeActive = timeActive;
    }
    
    public int getTimeActive() {
        return timeActive;
    }

    public boolean isActive() {
        return timeActive > 0;
    }

    public void decreaseTimeActive() {
        this.timeActive--;
    }
    
    public void increaseTimeActive(int otherPowerDownTime) {
        this.timeActive += otherPowerDownTime;
    }
    
    @Override
    public PowerDownMessage accept(PowerDownHandler puh) {
        return puh.handle(this);
    }

    @Override
    public JsonObject toJson() {
        JsonObjectBuilder job = Json.createObjectBuilder();
        job.add("powerdownname", getName());
        job.add("poweruptype", getType().name());
        return job.build();
    }

    @Override
    public String getName() {
        return "invertedControls";
    }

    @Override
    public PowerDownMessageType getType() {
        return PowerDownMessageType.INVERTEDCONTROLS;
    }    
}
