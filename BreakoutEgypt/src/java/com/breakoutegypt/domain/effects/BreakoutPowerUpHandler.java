/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.breakoutegypt.domain.effects;

import com.breakoutegypt.domain.BreakoutWorld;
import com.breakoutegypt.domain.Level;
import com.breakoutegypt.domain.LevelState;
import com.breakoutegypt.domain.messages.PowerUpMessage;
import com.breakoutegypt.domain.messages.PowerUpMessageType;
import com.breakoutegypt.domain.shapes.Paddle;
import java.util.List;

/**
 *
 * @author BenDB
 */
public class BreakoutPowerUpHandler implements PowerUpHandler {

    private LevelState levelState;
    private BreakoutWorld breakoutWorld;
    private Level level;

    public BreakoutPowerUpHandler(Level level, LevelState levelState, BreakoutWorld breakoutWorld) {
        this.level = level;
        this.levelState = levelState;
        this.breakoutWorld = breakoutWorld;
    }

    @Override
    public void handle(FloorPowerUp pu) {
        pu.setIsVisable(true);
        level.addFloor(pu);
    }

    @Override
    public void handle(BrokenPaddlePowerUp bppu) {
        List<Paddle> brokenPaddle = bppu.getBrokenPaddle();
        level.deSpawn(bppu.getBasePaddle());
        level.setBrokenPaddle(bppu);
    }
}
