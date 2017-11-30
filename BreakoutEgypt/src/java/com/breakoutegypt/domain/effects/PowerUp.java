/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.breakoutegypt.domain.effects;

import javax.json.JsonObject;

/**
 *
 * @author BenDB
 */
public interface PowerUp {
    
    public void accept(PowerUpHandler puh);

    public JsonObject toJson();
}
