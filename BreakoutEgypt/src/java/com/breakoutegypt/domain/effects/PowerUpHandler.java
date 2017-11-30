/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.breakoutegypt.domain.effects;

/**
 *
 * @author BenDB
 */
public interface PowerUpHandler {
    void handleFloorPowerUp(FloorPowerUp fpu);
    void handleAddBrokenPaddle(BrokenPaddlePowerUp bppu);
    void handleAcidBall();
    void removePowerupsIfTimedOut();
}
