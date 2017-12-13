/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.breakoutegypt.domain.powers;

import com.breakoutegypt.domain.messages.PowerDownMessage;
import com.breakoutegypt.domain.messages.PowerDownMessageType;
import javax.json.JsonObject;

/**
 *
 * @author BenDB
 */
public interface PowerDown {
    
    public PowerDownMessage accept(PowerDownHandler puh);

    public JsonObject toJson();

    public String getName();
    
    public PowerDownMessageType getType();
    
}
