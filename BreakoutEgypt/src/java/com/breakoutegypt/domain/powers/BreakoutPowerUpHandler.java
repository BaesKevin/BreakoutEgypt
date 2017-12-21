/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.breakoutegypt.domain.powers;

import com.breakoutegypt.domain.powers.generic.GenericPowerup;
import com.breakoutegypt.domain.BreakoutWorld;
import com.breakoutegypt.domain.Level;
import com.breakoutegypt.domain.LevelState;
import com.breakoutegypt.domain.ServerClientMessageRepository;
import com.breakoutegypt.domain.messages.GenericPowerupMessage;
import com.breakoutegypt.domain.messages.Message;
import com.breakoutegypt.domain.messages.PowerUpMessage;
import com.breakoutegypt.domain.messages.PowerUpMessageType;
import com.breakoutegypt.domain.powers.generic.BallPowerup;
import com.breakoutegypt.domain.powers.generic.BrickPowerup;
import com.breakoutegypt.domain.powers.generic.PaddlePowerup;
import com.breakoutegypt.domain.shapes.Ball;
import com.breakoutegypt.domain.shapes.Paddle;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *
 * @author BenDB
 */
public class BreakoutPowerUpHandler implements PowerUpHandler {

    private LevelState levelState;
    private BreakoutWorld breakoutWorld;
    private Level level;

    // powerups might need to be stored as a player field
    private Map<Integer, List<PowerUp>> playerToPowerUpMap;
    private List<BrokenPaddlePowerUp> activeBrokenPaddlePowerUps;
    private List<FloorPowerUp> activeFloorPowerUps;
    private List<GenericPowerup> genericPowerups;
    
    public BreakoutPowerUpHandler(Level level, LevelState levelState, BreakoutWorld breakoutWorld) {
        this.level = level;
        this.levelState = levelState;
        this.breakoutWorld = breakoutWorld;
        this.activeBrokenPaddlePowerUps = new ArrayList();
        this.activeFloorPowerUps = new ArrayList();
        this.playerToPowerUpMap = new HashMap<>();
    }

    public PowerUp getPowerupByName(String name, int playerIndex) {

        List<PowerUp> powerupsFromPlayer = playerToPowerUpMap.get(playerIndex);

        if (powerupsFromPlayer == null || powerupsFromPlayer.isEmpty()) {
            return null;
        }

        for (PowerUp p : powerupsFromPlayer) {
            if (p.getName().equals(name)) {
                return p;
            }
        }
        return null;
    }

    @Override
    public void addPowerUp(PowerUp up) {
        List<PowerUp> powerUpsForPlayer = playerToPowerUpMap.get(up.getPlayerId());
        if (powerUpsForPlayer == null) {
            powerUpsForPlayer = new ArrayList<>();
        }
        if (powerUpsForPlayer.size() < 3) {
            Message message = new PowerUpMessage(up.getName(), up, up.getType());
            message.setRecipientIndex(up.getPlayerId());

            breakoutWorld
                    .getMessageRepo()
                    .addPowerupMessages(message);

            powerUpsForPlayer.add(up);

            playerToPowerUpMap.put(up.getPlayerId(), powerUpsForPlayer);
        }
    }

    @Override
    public List<PowerUp> getPowerUps() {
        List<PowerUp> all = new ArrayList<>();
        for (List<PowerUp> l : playerToPowerUpMap.values()) {
            all.addAll(l);
        }
        return all;
    }

    @Override
    public PowerUpMessage handleBallPowerUp(BallPowerup gp) {
        level.resizeBall((Ball)gp.getBaseBody(), gp.getWidth(), gp.getHeight());
        
        return new GenericPowerupMessage(gp.getName(), gp, PowerUpMessageType.GENERICPOWERUP);
    }

    @Override
    public PowerUpMessage handlePaddlePowerUp(PaddlePowerup gp) {
        
        return new GenericPowerupMessage(gp.getName(), gp, PowerUpMessageType.GENERICPOWERUP);
    }

    @Override
    public PowerUpMessage handleBrickPowerUp(BrickPowerup gp) {
        
        return new GenericPowerupMessage(gp.getName(), gp, PowerUpMessageType.GENERICPOWERUP);
    }

    @Override
    public PowerUpMessage handleFloorPowerUp(FloorPowerUp floorPowerup) {
        FloorPowerUp floorInLevel = getActiveFloorForPlayer(floorPowerup.getPlayerId());

        PowerUpMessage m = new PowerUpMessage("floor", floorPowerup, PowerUpMessageType.NULLMESSAGE);
        if (floorInLevel == null) {
            this.activeFloorPowerUps.add(floorPowerup);
            floorPowerup.setIsVisible(true);

            levelState.addFloor(floorPowerup);
            breakoutWorld.spawn(floorPowerup);
            m = new PowerUpMessage("floor", floorPowerup, PowerUpMessageType.ACTIVATEFLOOR);
        } else {
            floorInLevel.addTime(floorPowerup.getTimeVisible());
            floorPowerup.setIsVisible(true);
        }
        m.setRecipientIndex(floorPowerup.getPlayerId());
        removePowerUpFromMap(floorPowerup);
        return m;
    }

    @Override
    public PowerUpMessage handleBrokenPaddle(BrokenPaddlePowerUp bppu) {

        BrokenPaddlePowerUp brokenPaddleInLevel = getActiveBrokenPaddleForPlayer(bppu.getPlayerId());

        PowerUpMessage m = new PowerUpMessage("brokenPaddle", bppu, PowerUpMessageType.NULLMESSAGE);

        if (brokenPaddleInLevel == null) {
            this.activeBrokenPaddlePowerUps.add(bppu);

            levelState.removePaddle(bppu.getBasePaddle());
            breakoutWorld.deSpawn(bppu.getBasePaddle().getBody());

            for (Paddle p : bppu.getBrokenPaddle()) {
                levelState.addPaddle(p);
                breakoutWorld.spawn(p);
            }
            m = new PowerUpMessage("brokenPaddle", bppu, PowerUpMessageType.ACTIVATEBROKENPADDLE);
        } else {
            brokenPaddleInLevel.addTime(bppu.getTimeVisible());
        }
        m.setRecipientIndex(bppu.getPlayerId());

        removePowerUpFromMap(bppu);
        return m;
    }

    @Override
    public void removePowerupsIfTimedOut() {
        ServerClientMessageRepository repo = breakoutWorld.getMessageRepo();
        List<FloorPowerUp> floorsToRemove = new ArrayList();
        List<BrokenPaddlePowerUp> paddlesToRemove = new ArrayList();
        for (FloorPowerUp fpu : activeFloorPowerUps) {
            if (removeFloorIfTimedOut(fpu, repo)) {
                floorsToRemove.add(fpu);
            }
        }
        for (BrokenPaddlePowerUp bppu : activeBrokenPaddlePowerUps) {
            if (removeBrokenPaddleIfTimedOut(bppu, repo)) {
                paddlesToRemove.add(bppu);
            }
        }
        activeBrokenPaddlePowerUps.removeAll(paddlesToRemove);
        activeFloorPowerUps.removeAll(floorsToRemove);
    }

    private boolean removeFloorIfTimedOut(FloorPowerUp fpu, ServerClientMessageRepository repo) {

        int timeLeft = fpu.getTimeVisible();
        if (timeLeft > 0) {
            fpu.setTimeVisible(timeLeft - 1);
        } else {
            breakoutWorld.deSpawn(fpu.getBody());
            repo.addPowerupMessages(new PowerUpMessage(fpu.getName(), fpu, PowerUpMessageType.REMOVEFLOOR));
            this.levelState.removeFloor();
            return true;
        }
        return false;
    }

    private boolean removeBrokenPaddleIfTimedOut(BrokenPaddlePowerUp bppu, ServerClientMessageRepository repo) {
        int timeLeft = bppu.getTimeVisible();
        if (timeLeft > 0) {
            bppu.setTimeVisible(timeLeft - 1);
        } else {
            handleRemoveBrokenPaddle(bppu);
            repo.addPowerupMessages(new PowerUpMessage("name", bppu, PowerUpMessageType.REMOVEBROKENPADDLE));
            return true;
        }
        return false;
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
        playerToPowerUpMap.get(abpu.getPlayerId()).remove(abpu);
        msg.setRecipientIndex(abpu.getPlayerId());
        return msg;
    }

    public List<BrokenPaddlePowerUp> getPaddlePowerup() {
        return activeBrokenPaddlePowerUps;
    }

    public FloorPowerUp getActiveFloorForPlayer(int playerId) {
        for (FloorPowerUp p : activeFloorPowerUps) {
            if (p.getPlayerId() == playerId) {
                return p;
            }
        }
        return null;
    }

    public BrokenPaddlePowerUp getActiveBrokenPaddleForPlayer(int playerId) {
        for (BrokenPaddlePowerUp p : activeBrokenPaddlePowerUps) {
            if (p.getPlayerId() == playerId) {
                return p;
            }
        }
        return null;
    }

    public void removePowerUpFromMap(PowerUp powerupToRemove) {

        List<PowerUp> powerupsForPlayer = playerToPowerUpMap.get(powerupToRemove.getPlayerId());
        PowerUp foundPowerUp = null;

        for (PowerUp p : powerupsForPlayer) {
            if (p.equals(powerupToRemove)) {
                foundPowerUp = p;
            }
        }
        powerupsForPlayer.remove(foundPowerUp);

    }

}
