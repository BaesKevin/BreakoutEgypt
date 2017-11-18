/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.breakoutegypt.test;

import com.breakoutegypt.domain.Game;
import com.breakoutegypt.domain.GameManager;
import com.breakoutegypt.domain.GameType;
import com.breakoutegypt.domain.Level;
import org.jbox2d.common.Vec2;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

/**
 *
 * @author kevin
 */
public class LevelTest {

    private Game game;
    private Level level;

    @Before
    public void setup() {
        GameManager gm = new GameManager();
        int id = gm.createGame(1, 1, GameType.TEST);
        game = gm.getGame(id);

        game.startLevel();
        level = game.getCurrentLevel();
    }

    @Test
    public void ballOutOfBoundsLosesLife() {
        System.out.println(level.getBall().getPosition());
        level.start();
        level.startBall();

        stepTimes(60);
        Assert.assertEquals(2, level.getLives());
    }

    @Test
    public void destroyAllTargetBricksEndsLevel() {
        game.setCurrentLevel(2);
        level = game.getCurrentLevel();
        level.startBall();
        level.getBall().setLinearVelocity(0, -100);
        stepTimes(60);

        Assert.assertEquals(1, level.getLevelState().getTargetBricksLeft());
    }

    @Test
    public void switchBrickTogglesCollision() {
        game.setCurrentLevel(3);
        level = game.getCurrentLevel();
        level.startBall();
        level.getBall().setLinearVelocity(0, -100);
        stepTimes(60);
        level.getBall().getBody().setTransform(new Vec2(50, 100), 0);
        stepTimes(60);

        Assert.assertEquals(3, level.getBricks().size());
    }

    @Test
    public void explosiveBickWithRadius1DestroysThreeBricks() {
        game.setCurrentLevel(4);
        level = game.getCurrentLevel();
        level.startBall();
        level.getBall().setLinearVelocity(0, -100);
        
        stepTimes(60);

        Assert.assertEquals(1, level.getBricks().size());
    }

    private void stepTimes(int times) {
        for (int i = 1; i <= times; i++) {
            level.step();
        }
    }

}
