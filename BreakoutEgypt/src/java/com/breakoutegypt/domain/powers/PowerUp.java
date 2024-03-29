/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.breakoutegypt.domain.powers;

import com.breakoutegypt.domain.messages.PowerUpMessage;
import com.breakoutegypt.domain.messages.PowerUpMessageType;
import javax.json.JsonObjectBuilder;

/**
 *
 * @author BenDB
 */
public interface PowerUp {
    
    public PowerUpMessage accept(PowerUpHandler puh);

    public JsonObjectBuilder toJson();

    public String getName();
    
    public PowerUpMessageType getType();
    
    public void setPlayerId(int i);
    
    public int getPlayerId();
    
    
}
