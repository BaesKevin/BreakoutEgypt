/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.breakoutegypt.test;

import com.breakoutegypt.connectionmanagement.DummyConnection;
import com.breakoutegypt.data.DummyDifficultyRepository;
import com.breakoutegypt.data.DummyLevelProgressionRepository;
import com.breakoutegypt.data.Repositories;
import com.breakoutegypt.domain.Game;
import com.breakoutegypt.domain.GameManager;
import com.breakoutegypt.domain.GameType;
import com.breakoutegypt.domain.Level;
import com.breakoutegypt.domain.Player;
import com.breakoutegypt.domain.User;
import com.breakoutegypt.domain.levelprogression.Difficulty;
import com.breakoutegypt.domain.levelprogression.LevelProgress;
import com.breakoutegypt.domain.shapes.Ball;
import org.jbox2d.common.Vec2;
import org.junit.After;
import org.junit.Assert;
import static org.junit.Assert.assertEquals;
import org.junit.Before;
import org.junit.Test;

/**
 *
 * @author BenDB
 */
public class ScoreTester {

    Level level;
    Game game;
    Player player;
    private final LevelProgress ALL_LEVELS_UNLOCKED = DummyLevelProgressionRepository.getDefault(GameType.TEST);

    public ScoreTester() {
    }
    
    @Before
    public void setTesting() {
        Repositories.isTesting(true);
    }
    
//    @After
//    public void disableTesting() {
//        Repositories.isTesting(false);
//    }
    
    
    private void createGame(int startingLevel, String diff) {
        GameManager gm = new GameManager();
        String id = gm.createGame(GameType.TEST, diff);
        game = gm.getGame(id);
        game.initStartingLevel(startingLevel, ALL_LEVELS_UNLOCKED);

        level = game.getCurrentLevel();

        for (int i = 0; i < 1000; i++) {
            ALL_LEVELS_UNLOCKED.incrementHighestLevelReached();
        }

        player = new Player("Kevin");

        game.addConnectingPlayer(player);

        game.addConnectionForPlayer("Kevin", new DummyConnection());
    }

    // Level with 3 bricks, each 2-, 4-, 6 or 8000 points, multiplier goes up after 2 hits without hitting the paddle or losing a ball/life.
    @Test
    public void testScoreDestroy3BricksWithoutTouchingPaddle() {
        testScoreForDifficulty("easy", 8000);
        testScoreForDifficulty("medium", 6000);
        testScoreForDifficulty("hard", 4000);
        testScoreForDifficulty("brutal", 2000);
    }
    
    @Test
    public void testScoreDestroy3BricksTouchPaddleInBetweenResetsMultiplier(){
        createGame(13, "medium");
        level = game.getLevel();

        level.getLevelState().getBalls().get(0).setLinearVelocity(0, 100);

        // step untill ball hits a brick
        int totalBricks = level.getLevelState().getBricks().size();
        
        while(level.getLevelState().getBricks().size() >  totalBricks - 1){
            stepTimes(level, 1);
        }
        
        // save the ball's position and speed after hitting the brick
        Ball ball = level.getLevelState().getBall();
        Vec2 afterHittingBrick = new Vec2(ball.getPosition());
        
        Vec2 speedAfterHittingBrick = new Vec2(ball.getLinearVelocity());
        
        // place the ball above the paddle and make it hit the paddle
        Vec2 paddlePosition = level.getLevelState().getPaddles().get(0).getPosition();
        Vec2 ballPositionAbovePaddle = new Vec2(paddlePosition.x, paddlePosition.y - 10);
        
        ball.getBody().setTransform(ballPositionAbovePaddle, 0);
        
        ball.setLinearVelocity(0, 100);
        
        stepTimes(level, 10);
        
        // reset the ball position and speed
        ball.getBody().setTransform(afterHittingBrick, 0);
        ball.setLinearVelocity(speedAfterHittingBrick.x, speedAfterHittingBrick.y);
        
        // move untill all bricks are gone
        while(level.getLevelState().getBricks().size() > 1){
            stepTimes(level, 10);
        }
        
        int expectedScore = 3 * Repositories.getDifficultyRepository().findByName("medium").getPointsPerBlock();
        Assert.assertEquals(expectedScore, level.getBrickScore());
    }
    
    private void testScoreForDifficulty(String diff, int blockScore){
        createGame(13, diff);
        level = game.getLevel();

        level.getLevelState().getBalls().get(0).setLinearVelocity(0, 100);

        stepTimes(level, 120);
        
        int expectedScore = 2 * blockScore + (2 * blockScore);
        assertEquals(expectedScore, level.getBrickScore());
    }

    private void stepTimes(Level level, int times) {
        for (int i = 1; i <= times; i++) {
            level.step();
        }
    }

}
