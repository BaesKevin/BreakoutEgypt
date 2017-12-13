/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.breakoutegypt.domain.powers;

import com.breakoutegypt.domain.messages.PowerDownMessage;

/**
 *
 * @author BenDB
 */
public interface PowerDownHandler {
    void addPowerDown(PowerDown down);
    PowerDownMessage handle(FloodPowerDown flood);
    PowerDownMessage handle(ProjectilePowerDown projectile);
}
