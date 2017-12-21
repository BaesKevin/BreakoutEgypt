/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.breakoutegypt.domain.messages;

import com.breakoutegypt.domain.powers.generic.GenericPowerup;
import com.breakoutegypt.domain.shapes.RegularBody;
import javax.json.JsonObjectBuilder;

/**
 *
 * @author kevin
 */
public class GenericPowerupMessage extends PowerUpMessage{
    private RegularBody newBody;

    public GenericPowerupMessage(String name, GenericPowerup powerup, PowerUpMessageType pumt) {
        super(name, powerup, pumt);
        this.newBody = powerup.getBaseBody();
    }

    @Override
    public JsonObjectBuilder toJson() {
        return super.toJson();
    }
    
    
}
