/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.breakoutegypt.domain.powers;

import com.breakoutegypt.domain.messages.PowerUpMessage;
import java.util.List;

/**
 *
 * @author BenDB
 */
public interface PowerUpHandler {
    void addPowerUp(PowerUp up);
    List<PowerUp> getPowerUps();
    PowerUpMessage handleFloorPowerUp(FloorPowerUp fpu);
    PowerUpMessage handleBrokenPaddle(BrokenPaddlePowerUp bppu);
    PowerUpMessage handleAcidBall(AcidBallPowerUp abpu);
    void removePowerupsIfTimedOut();
}
