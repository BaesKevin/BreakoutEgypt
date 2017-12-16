/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.breakoutegypt.test;

import com.breakoutegypt.data.LevelProgressionRepository;
import com.breakoutegypt.domain.BreakoutWorld;
import com.breakoutegypt.domain.Game;
import com.breakoutegypt.domain.levelprogression.GameDifficulty;
import com.breakoutegypt.domain.GameManager;
import com.breakoutegypt.domain.GameType;
import com.breakoutegypt.domain.Level;
import com.breakoutegypt.domain.Player;
import com.breakoutegypt.domain.levelprogression.LevelProgress;
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
    private Player player;
    private final LevelProgress ALL_LEVELS_UNLOCKED = LevelProgressionRepository.getDefault(GameType.TEST);
    
    @Before
    public void setup() {
        createGame(GameType.TEST, GameDifficulty.MEDIUM, 1);
    }
    
    private void createGame(GameType type, GameDifficulty diff, int startingLevel){
        GameManager gm = new GameManager();
        int id = gm.createGame(GameType.TEST, GameDifficulty.MEDIUM);
        game = gm.getGame(id);
        game.initStartingLevel(startingLevel, ALL_LEVELS_UNLOCKED);

        level = game.getCurrentLevel();

        for (int i = 0; i < 1000; i++) {
            ALL_LEVELS_UNLOCKED.incrementHighestLevelReached();
        }
    }

    @Test
    public void ballOutOfBoundsLosesLife() {
        level.startBall();

        stepTimes(10);
        Assert.assertEquals(2, level.getLives());
    }

    @Test
    public void destroyAllTargetBricksEndsLevel() {
        createGame(GameType.TEST, GameDifficulty.MEDIUM, 2);
        
        level = game.getCurrentLevel();
        level.startBall();
        level.getLevelState().getBall().setLinearVelocity(0, -100);
        stepTimes(60);

        Assert.assertEquals(1, level.getLevelState().getTargetBricksLeft());
    }

    @Test
    public void switchBrickTogglesCollision() {
        createGame(GameType.TEST, GameDifficulty.MEDIUM, 3);
        level = game.getCurrentLevel();
        level.startBall();
        level.getLevelState().getBall().setLinearVelocity(0, -100);
        stepTimes(10);
        level.getLevelState().getBall().getBody().setTransform(new Vec2(50, 100), 0);
        stepTimes(10);

        Assert.assertEquals(3, level.getLevelState().getBricks().size());
    }

    @Test
    public void explosiveBickWithRadius1DestroysThreeBricks() {
        createGame(GameType.TEST, GameDifficulty.MEDIUM, 4);
        level = game.getCurrentLevel();
        
        level.getLevelState().getBall().setLinearVelocity(0, -100);
        
        stepTimes(10);
        
        Assert.assertEquals(1, level.getLevelState().getBricks().size());
    }

    @Test
    public void ballBouncesOfTopLeftRightWalls() {
        createGame(GameType.TEST, GameDifficulty.MEDIUM, 1);
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
        Assert.assertTrue(ball.getPosition().y < BreakoutWorld.DIMENSION);
    }

    @Test
    public void ballsDontCollide() {
        createGame(GameType.TEST, GameDifficulty.MEDIUM, 5);
        level = game.getCurrentLevel();
        level.startBall();
        Ball topball = level.getLevelState().getBalls().get(0);
        Ball bottomball = level.getLevelState().getBalls().get(1);
        topball.setLinearVelocity(0, 100);
        bottomball.setLinearVelocity(0, -100);

        stepTimes(60);

        Assert.assertTrue(topball.getPosition().y > bottomball.getPosition().y);
    }

    private void stepTimes(int times) {
        for (int i = 1; i <= times; i++) {
            level.step();
        }
    }
}
