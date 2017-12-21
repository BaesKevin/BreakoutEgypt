/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.breakoutegypt.domain.powers.generic;

import com.breakoutegypt.domain.messages.PowerUpMessage;
import com.breakoutegypt.domain.powers.PowerUpHandler;
import com.breakoutegypt.domain.shapes.RegularBody;
import javax.json.JsonObjectBuilder;

/**
 *
 * @author kevin
 */
public class PaddlePowerup extends GenericPowerup {

    public PaddlePowerup(RegularBody baseBody, int width, int height) {
        super(baseBody, width, height);
    }

    @Override
    public PowerUpMessage accept(PowerUpHandler puh) {
        return puh.handlePaddlePowerUp(this);
    }

    @Override
    public JsonObjectBuilder toJson() {
        JsonObjectBuilder json = super.toJson();
        json.add("type", PADDLETYPE);
        return json;
    }

}
