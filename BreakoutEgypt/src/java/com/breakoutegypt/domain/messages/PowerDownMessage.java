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

public class PowerDownMessage extends GenericMessage {

    private PowerDown powerdown;
    
    public PowerDownMessage(String name, PowerDown powerdown, PowerDownMessageType pdmt) {
        super(0, name, pdmt);
        this.powerdown = powerdown;
    }

    @Override
    public JsonObjectBuilder toJson() {
        JsonObjectBuilder job = super.toJson();
        if (powerdown != null) 
            job.add("powerdown", powerdown.toJson());
        job.add("powerdownaction", getMessageType().toString());
        return job;
    }
    
}
