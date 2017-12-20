/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.breakoutegypt.domain.powers;

import com.breakoutegypt.domain.messages.PowerDownMessage;
import com.breakoutegypt.domain.BreakoutWorld;
import com.breakoutegypt.domain.Level;
import com.breakoutegypt.domain.LevelState;
import com.breakoutegypt.domain.messages.Message;
import com.breakoutegypt.domain.messages.PowerDownMessageType;
import com.breakoutegypt.domain.shapes.Ball;
import com.breakoutegypt.domain.shapes.Paddle;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 *
 * @author BenDB
 */
public class BreakoutPowerDownHandler implements PowerDownHandler {

    private InvertedControlsPowerDown invertedControls;
    private LevelState levelState;
    private BreakoutWorld breakoutWorld;
    private Level level;

    public BreakoutPowerDownHandler(Level level, LevelState levelState, BreakoutWorld breakoutWorld) {
        this.level = level;
        this.levelState = levelState;
        this.breakoutWorld = breakoutWorld;
        this.invertedControls = null;
    }

    @Override
    public void addPowerDown(PowerDown down) {
//        powerdowns.add(down);
    }

    @Override
    public PowerDownMessage handle(FloodPowerDown flood) {
        flood.initBalls();
        Random r = new Random();
        PowerDownMessage m = new PowerDownMessage(flood.getName(), flood, PowerDownMessageType.FLOOD);
        m.setRecipientIndex(flood.getPlayerId());
        for (Ball b : flood.getBalls()) {
            levelState.addBall(b);
            breakoutWorld.spawn(b);
        }
        flood.doPulse();
        return m;
    }

    @Override
    public PowerDownMessage handle(ProjectilePowerDown projectile) {
        levelState.addProjectile(projectile.getProjectile());
        breakoutWorld.spawn(projectile.getProjectile());
        Paddle target = levelState.getPaddleWithPlayerIndex(projectile.getPlayerId());
        projectile.startProjectile(target.getPosition().x, target.getPosition().y, target.getWidth());
        PowerDownMessage m = new PowerDownMessage(projectile.getName(), projectile, PowerDownMessageType.PROJECTILE);
        m.setRecipientIndex(projectile.getPlayerId());
        return m;
    }

    @Override
    public PowerDownMessage handle(InvertedControlsPowerDown invertedControl) {
        PowerDownMessage m = new PowerDownMessage(invertedControl.getName(), invertedControl, PowerDownMessageType.NULLMESSAGE);
        if (this.invertedControls == null) {
            this.invertedControls = invertedControl;
            level.invertControls();
            m = new PowerDownMessage(invertedControl.getName(), invertedControl, PowerDownMessageType.INVERTEDCONTROLS);
        } else {
            this.invertedControls.increaseTimeActive(invertedControl.getTimeActive());
        }
        m.setRecipientIndex(invertedControl.getPlayerId());
        return m;
    }

    @Override
    public void handle(PowerDown pd) {
        Message m = pd.accept(this);
        breakoutWorld.getMessageRepo().addPowerdownMessages(m);
    }

    @Override
    public void removeInvertedControlIfTimedOut() {
        if (invertedControls != null) {
            if (invertedControls.isActive()) {
                invertedControls.decreaseTimeActive();
            } else {
                level.invertControls();
                breakoutWorld.getMessageRepo()
                        .addPowerdownMessages(new PowerDownMessage(invertedControls.getName(), invertedControls, PowerDownMessageType.REMOVEINVERTEDCONTROLS));
                invertedControls = null;
            }
        }
    }

}
