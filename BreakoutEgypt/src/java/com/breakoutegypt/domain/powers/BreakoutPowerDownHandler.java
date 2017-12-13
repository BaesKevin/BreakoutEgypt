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
        for (Ball b : flood.getBalls()) {
            levelState.addBall(b);
            breakoutWorld.spawn(b);
        }
        flood.doPulse();
        return new PowerDownMessage("todo", flood, PowerDownMessageType.FLOOD);
    }

    @Override
    public PowerDownMessage handle(ProjectilePowerDown projectile) {
        levelState.addProjectile(projectile.getProjectile());
        breakoutWorld.spawn(projectile.getProjectile());
        Paddle target = levelState.getPaddles().get(0);
        projectile.startProjectile(target.getPosition().x, target.getPosition().y, target.getShape().getWidth());
        return new PowerDownMessage("TODO", projectile, PowerDownMessageType.PROJECTILE);
    }

    @Override
    public PowerDownMessage handle(InvertedControlsPowerDown invertedControl) {
        if (this.invertedControls == null) {
            this.invertedControls = invertedControl;
            level.invertControls();
            return new PowerDownMessage(invertedControl.getName(), invertedControl, PowerDownMessageType.INVERTEDCONTROLS);
        } else {
            this.invertedControls.increaseTimeActive(invertedControl.getTimeActive());
        }
        return new PowerDownMessage(invertedControl.getName(), invertedControl, PowerDownMessageType.NULLMESSAGE);
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
