/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.breakoutegypt.domain.powers;

import com.breakoutegypt.domain.BreakoutWorld;
import com.breakoutegypt.domain.Level;
import com.breakoutegypt.domain.LevelState;
import com.breakoutegypt.domain.ServerClientMessageRepository;
import com.breakoutegypt.domain.messages.PowerUpMessage;
import com.breakoutegypt.domain.messages.PowerUpMessageType;
import com.breakoutegypt.domain.shapes.Ball;
import com.breakoutegypt.domain.shapes.Paddle;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author BenDB
 */
public class BreakoutPowerUpHandler implements PowerUpHandler {

    private LevelState levelState;
    private BreakoutWorld breakoutWorld;
    private Level level;

    // powerups might need to be stored as a player field
    private BrokenPaddlePowerUp paddlePowerup;
    private FloorPowerUp floorPowerup;
    private List<PowerUp> powerups;

    public BreakoutPowerUpHandler(Level level, LevelState levelState, BreakoutWorld breakoutWorld) {
        this.level = level;
        this.levelState = levelState;
        this.breakoutWorld = breakoutWorld;
        powerups = new ArrayList();
    }

    public PowerUp getPowerupByName(String name) {

        for (PowerUp p : powerups) {
            if (p.getName().equals(name)) {
                return p;
            }
        }
        return null;
    }

    @Override
    public void addPowerUp(PowerUp up) {
        breakoutWorld
                .getMessageRepo()
                .addPowerupMessages(new PowerUpMessage(up.getName(), up, up.getType()));
        this.powerups.add(up); // TODO check if powerup is already in the list
    }

    @Override
    public List<PowerUp> getPowerUps() {
        return powerups;
    }

    @Override
    public void emptyPowerups() {
        this.powerups = new ArrayList();
    }

    @Override
    public PowerUpMessage handleFloorPowerUp(FloorPowerUp floorPowerup) {
        FloorPowerUp floorInLevel = levelState.getFloor();

        if (floorInLevel == null) {
            this.floorPowerup = floorPowerup;
            floorPowerup.setIsVisible(true);

            levelState.addFloor(floorPowerup);
            breakoutWorld.spawn(floorPowerup);
            return new PowerUpMessage("name", floorPowerup, PowerUpMessageType.ACTIVATEFLOOR);
        } else {
            floorInLevel.addTime(floorPowerup.getTimeVisible());
        }
        powerups.remove(floorPowerup);
        return new PowerUpMessage("brokenPaddle", floorPowerup, PowerUpMessageType.NULLMESSAGE);
    }

    @Override
    public PowerUpMessage handleAddBrokenPaddle(BrokenPaddlePowerUp bppu) {
        if (this.paddlePowerup == null) {
            this.paddlePowerup = bppu;

            levelState.removePaddle(bppu.getBasePaddle());
            breakoutWorld.deSpawn(bppu.getBasePaddle().getBody());

            for (Paddle p : bppu.getBrokenPaddle()) {
                levelState.addPaddle(p);
                breakoutWorld.spawn(p);
            }

            powerups.remove(bppu);
            return new PowerUpMessage("brokenPaddle", bppu, PowerUpMessageType.ACTIVATEBROKENPADDLE);
        } else {
            this.paddlePowerup.addTime(bppu.getTimeVisable());
        }
        return new PowerUpMessage("brokenPaddle", bppu, PowerUpMessageType.NULLMESSAGE);
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
        int timeLeft = floorPowerup.getTimeVisible();
        if (timeLeft > 0) {
            floorPowerup.setTimeVisible(timeLeft - 1);
        } else {
            breakoutWorld.deSpawn(floorPowerup.getBody());
            repo.addPowerupMessages(new PowerUpMessage(floorPowerup.getName(), floorPowerup, PowerUpMessageType.REMOVEFLOOR));
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

    @Override
    public PowerUpMessage handleAcidBall(AcidBallPowerUp abpu) {
        Ball b = levelState.getBall();
        PowerUpMessage msg = new PowerUpMessage(abpu.getName(), b.getAcidBall(), PowerUpMessageType.ADDACIDBALL);
        if (b.getAcidBall() != null) {
            b.getAcidBall().addRange(abpu.getRange());
            msg = new PowerUpMessage(abpu.getName(), abpu, PowerUpMessageType.NULLMESSAGE);
        } else {
            b.setAcidballPowerup(abpu);
        }
        powerups.remove(abpu);
        return msg;
    }

    public BrokenPaddlePowerUp getPaddlePowerup() {
        return paddlePowerup;
    }

}
