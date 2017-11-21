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
import com.breakoutegypt.domain.shapes.Ball;
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
        System.out.println(level.getLevelState().getBall().getPosition());
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
        level.getLevelState().getBall().setLinearVelocity(0, -100);
        stepTimes(60);

        Assert.assertEquals(1, level.getLevelState().getTargetBricksLeft());
    }

    @Test
    public void switchBrickTogglesCollision() {
        game.setCurrentLevel(3);
        level = game.getCurrentLevel();
        level.startBall();
        level.getLevelState().getBall().setLinearVelocity(0, -100);
        stepTimes(60);
        level.getLevelState().getBall().getBody().setTransform(new Vec2(50, 100), 0);
        stepTimes(60);

        Assert.assertEquals(3, level.getLevelState().getBricks().size());
    }

    @Test
    public void explosiveBickWithRadius1DestroysThreeBricks() {
        game.setCurrentLevel(4);
        level = game.getCurrentLevel();
        level.startBall();
        level.getLevelState().getBall().setLinearVelocity(0, -100);

        stepTimes(60);

        Assert.assertEquals(1, level.getLevelState().getBricks().size());
    }

    @Test
    public void ballBouncesOfTopLeftRightWalls() {
        game.setCurrentLevel(1);
        level = game.getCurrentLevel();
        level.startBall();

        Ball ball = level.getLevelState().getBall();
        ball.getBody().setTransform(new Vec2(50, 20), 0);
        ball.setLinearVelocity(-100, 0);
        stepTimes(40);
        Assert.assertTrue(0 <= ball.getPosition().x);

        ball.getBody().setTransform(new Vec2(20, 20), 0);
        ball.setLinearVelocity(0, -100);
        stepTimes(40);
        Assert.assertTrue(0 <= ball.getPosition().y);

        ball.getBody().setTransform(new Vec2(280, 20), 0);
        ball.setLinearVelocity(100, 0);
        stepTimes(40);
        Assert.assertTrue( ball.getPosition().y < 300);
    }

    private void stepTimes(int times) {
        for (int i = 1; i <= times; i++) {
            level.step();
        }
    }
}