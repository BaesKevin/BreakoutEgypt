/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.breakoutegypt.domain.effects;

import com.breakoutegypt.domain.BreakoutWorld;
import com.breakoutegypt.domain.Level;
import com.breakoutegypt.domain.LevelState;
import com.breakoutegypt.domain.ServerClientMessageRepository;
import com.breakoutegypt.domain.messages.PowerUpMessage;
import com.breakoutegypt.domain.messages.PowerUpMessageType;
import com.breakoutegypt.domain.shapes.Paddle;

/**
 *
 * @author BenDB
 */
public class BreakoutPowerUpHandler implements PowerUpHandler {

    private LevelState levelState;
    private BreakoutWorld breakoutWorld;
    private Level level;

    private BrokenPaddlePowerUp paddlePowerup;
    private FloorPowerUp floorPowerup;

    public BreakoutPowerUpHandler(Level level, LevelState levelState, BreakoutWorld breakoutWorld) {
        this.level = level;
        this.levelState = levelState;
        this.breakoutWorld = breakoutWorld;
    }

    @Override
    public void handleFloorPowerUp(FloorPowerUp floorPowerup) {
        if (this.floorPowerup == null) {
            this.floorPowerup = floorPowerup;

            floorPowerup.setIsVisable(true);
//        level.addFloor(pu);

            levelState.addFloor(floorPowerup);
            breakoutWorld.spawn(floorPowerup);

            breakoutWorld
                    .getMessageRepo()
                    .addPowerupMessages(new PowerUpMessage("name", floorPowerup, PowerUpMessageType.ADDFLOOR));
        }

    }

    @Override
    public void handleAddBrokenPaddle(BrokenPaddlePowerUp bppu) {
        if (this.paddlePowerup == null) {
            this.paddlePowerup = bppu;

            levelState.removePaddle(bppu.getBasePaddle());
            breakoutWorld.deSpawn(bppu.getBasePaddle().getBody());

            for (Paddle p : bppu.getBrokenPaddle()) {
                levelState.addPaddle(p);
                breakoutWorld.spawn(p);
            }

            breakoutWorld
                    .getMessageRepo()
                    .addPowerupMessages(new PowerUpMessage("brokenPaddle", bppu, PowerUpMessageType.ADDBROKENPADDLE));
        }
    }


    @Override
    public void removePowerupsIfTimedOut() {
        ServerClientMessageRepository repo = breakoutWorld.getMessageRepo();

        if (paddlePowerup != null) {
            removeBrokenPaddleIfTimedOut(repo);
        }

        if (floorPowerup != null) {
            removeFloorIfTimedOut(repo);
        }

    }

    private void removeFloorIfTimedOut(ServerClientMessageRepository repo) {
        int timeLeft = floorPowerup.getTimeVisable();
        if (timeLeft > 0) {
            floorPowerup.setTimeVisable(timeLeft - 1);
        } else {
            breakoutWorld.deSpawn(floorPowerup.getBody());
            repo.addPowerupMessages(new PowerUpMessage("name", floorPowerup, PowerUpMessageType.REMOVEFLOOR));
            floorPowerup = null;
        }
    }

    private void removeBrokenPaddleIfTimedOut(ServerClientMessageRepository repo) {
        int timeLeft = paddlePowerup.getTimeVisable();
        if (timeLeft > 0) {
            paddlePowerup.setTimeVisable(timeLeft - 1);
        } else {
            handleRemoveBrokenPaddle(paddlePowerup);
            repo.addPowerupMessages(new PowerUpMessage("name", paddlePowerup, PowerUpMessageType.REMOVEBROKENPADDLE));

            paddlePowerup = null;
        }
    }
    
    
    private void handleRemoveBrokenPaddle(BrokenPaddlePowerUp bppu) {
        for (Paddle p : bppu.getBrokenPaddle()) {
            levelState.removePaddle(p);
            breakoutWorld.deSpawn(p.getBody());
        }

        level.addPaddle(bppu.getBasePaddle());
        breakoutWorld.spawn(bppu.getBasePaddle());
    }

}
