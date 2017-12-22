/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.breakoutegypt.domain.powers.generic;

import com.breakoutegypt.domain.messages.PowerUpMessage;
import com.breakoutegypt.domain.powers.PowerUpHandler;
import com.breakoutegypt.domain.shapes.Ball;
import javax.json.JsonObjectBuilder;

/**
 *
 * @author kevin
 */
public class BallPowerup extends GenericPowerup {
    public BallPowerup(Ball baseBody, int width, int height, int timeVisible) {
        super("ballpowerup", baseBody, width, height, timeVisible);
    }
   
    @Override
    public PowerUpMessage accept(PowerUpHandler puh) {
        return puh.handleBallPowerUp(this);
    }

    @Override
    public JsonObjectBuilder toJson() {
        JsonObjectBuilder json = super.toJson();
        json.add("type", BALLTYPE);
        return json;
    }

}
