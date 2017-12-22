/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.breakoutegypt.test;

import com.breakoutegypt.connectionmanagement.DummyConnection;
import com.breakoutegypt.data.DummyLevelProgressionRepository;
import com.breakoutegypt.domain.BreakoutWorld;
import com.breakoutegypt.domain.Game;
import com.breakoutegypt.domain.GameManager;
import com.breakoutegypt.domain.GameType;
import com.breakoutegypt.domain.Level;
import com.breakoutegypt.domain.Player;
import com.breakoutegypt.domain.levelprogression.Difficulty;
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
    private final LevelProgress ALL_LEVELS_UNLOCKED = DummyLevelProgressionRepository.getDefault(GameType.TEST);
    
    @Before
    public void setup() {
        createGame(GameType.TEST, "medium", 1);
    }
    
    private void createGame(GameType type, String diff, int startingLevel){
        player = new Player("player");
        GameManager gm = new GameManager();
        String id = gm.createGame(GameType.TEST, Difficulty.MEDIUM);
        game = gm.getGame(id);
        game.addConnectingPlayer(player);
        game.addConnectionForPlayer(player.getUsername(), new DummyConnection());
        
        game.initStartingLevel(startingLevel, ALL_LEVELS_UNLOCKED);
        
        level = game.getCurrentLevel();

        for (int i = 0; i < 1000; i++) {
            ALL_LEVELS_UNLOCKED.incrementHighestLevelReached();
        }
    }

    @Test
    public void ballOutOfBoundsLosesLife() {
        level.startBall();

        stepTimes(60);
        Assert.assertEquals(2, player.getLives());
    }

    @Test
    public void destroyAllTargetBricksEndsLevel() {
        createGame(GameType.TEST, "medium", 2);
        
        level = game.getCurrentLevel();
        level.startBall();
        level.getLevelState().getBall().setLinearVelocity(0, -100);
        stepTimes(60);

        Assert.assertEquals(1, level.getLevelState().getTargetBricksLeft());
    }

    @Test
    public void switchBrickTogglesCollision() {
        createGame(GameType.TEST, "medium", 3);
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
        createGame(GameType.TEST, "medium", 4);
        level = game.getCurrentLevel();
        
        level.getLevelState().getBall().setLinearVelocity(0, -100);
        
        stepTimes(10);
        
        Assert.assertEquals(1, level.getLevelState().getBricks().size());
    }

    @Test
    public void ballBouncesOfTopLeftRightWalls() {
        createGame(GameType.TEST, "medium", 1);
        level = game.getCurrentLevel();
        level.startBall();

        Ball ball = level.getLevelState().getBall();
        ball.getBody().setTransform(new Vec2(50, 20), 0);
        ball.setLinearVelocity(-100, 0);
        stepTimes(40);
        Assert.assertTrue(0 <= ball.getX());

        ball.getBody().setTransform(new Vec2(20, 20), 0);
        ball.setLinearVelocity(0, -100);
        stepTimes(40);
        Assert.assertTrue(0 <= ball.getX());

        ball.getBody().setTransform(new Vec2(280, 20), 0);
        ball.setLinearVelocity(100, 0);
        stepTimes(40);
        Assert.assertTrue(ball.getY() < BreakoutWorld.DIMENSION);
    }

    @Test
    public void ballsDontCollide() {
        createGame(GameType.TEST, "medium", 5);
        level = game.getCurrentLevel();
        level.startBall();
        Ball topball = level.getLevelState().getBalls().get(0);
        Ball bottomball = level.getLevelState().getBalls().get(1);
        topball.setLinearVelocity(0, 100);
        bottomball.setLinearVelocity(0, -100);

        stepTimes(60);

        Assert.assertTrue(topball.getY() > bottomball.getY());
    }

    private void stepTimes(int times) {
        for (int i = 1; i <= times; i++) {
            level.step();
        }
    }
}
