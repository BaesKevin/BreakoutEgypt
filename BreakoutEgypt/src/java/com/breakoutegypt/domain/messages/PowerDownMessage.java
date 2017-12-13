/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.breakoutegypt.domain.messages;

import com.breakoutegypt.domain.powers.PowerDown;
import javax.json.Json;
import javax.json.JsonObjectBuilder;

/**
 *
 * @author BenDB
 */

public class PowerDownMessage implements Message {

    private String name;
    private PowerDown powerdown;
    private PowerDownMessageType messageType;
    
    public PowerDownMessage(String name, PowerDown powerdown, PowerDownMessageType pdmt) {
        this.name = name;
        this.powerdown = powerdown;
        this.messageType = pdmt;
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
        if (powerdown != null) 
            job.add("powerdown", powerdown.toJson());
        job.add("powerdownaction", getMessageType().toString());
        return job;
    }
    
}
