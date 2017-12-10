/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.breakoutegypt.test;

import com.breakoutegypt.connectionmanagement.DummyConnection;
import com.breakoutegypt.data.LevelProgressionRepository;
import com.breakoutegypt.domain.Game;
import com.breakoutegypt.domain.GameDifficulty;
import com.breakoutegypt.domain.GameManager;
import com.breakoutegypt.domain.GameType;
import com.breakoutegypt.domain.Level;
import com.breakoutegypt.domain.Player;
import com.breakoutegypt.domain.User;
import com.breakoutegypt.domain.levelprogression.LevelProgression;
import com.breakoutegypt.domain.levelprogression.LevelProgressionManager;
import com.breakoutegypt.domain.shapes.Ball;
import com.breakoutegypt.exceptions.BreakoutException;
import com.breakoutegypt.levelfactories.TestLevelFactory;
import junit.framework.Assert;
import org.junit.Before;
import org.junit.Test;

/**
 *
 * @author kevin
 */
public class LevelProgressionTest {
    private Player player;
    private LevelProgressionManager progressions;
    private TestLevelFactory factory;
    private Level level;
    
    private static final int DEFAULT_OPEN_TEST_LEVELS = TestLevelFactory.DEFAULT_OPEN_LEVELS;
    
    @Before
    public void before(){
        player = new Player(new User("player"));
        factory = new TestLevelFactory(null);
    }
    
    @Test
    public void textGetReachedLevelDefaultNotOpen() {
        progressions = player.getProgressions();
        
        progressions.addNewProgression(GameType.TEST);
        LevelProgression prog = progressions.getProgressionOrDefault(GameType.TEST);
        
        for (int i = 1; i < DEFAULT_OPEN_TEST_LEVELS + 1; i++) {
            prog.incrementHighestLevelReached();
        }
        
        factory.setCurrentLevel(DEFAULT_OPEN_TEST_LEVELS + 1, prog);
        
        // no assert because there aren't 500 levels, we would get exceptions if it didn't work
    }
    
    @Test
    public void textGetDefaultOpenLevel() {
        progressions = player.getProgressions();
        
        
        progressions.addNewProgression(GameType.TEST);
        LevelProgression prog = progressions.getProgressionOrDefault(GameType.TEST);
                
        factory.setCurrentLevel(DEFAULT_OPEN_TEST_LEVELS, prog);
        
        // no assert because there aren't 500 levels, we would get exceptions if it didn't work
    }
    
    @Test(expected = BreakoutException.class)
    public void textGetUnreachedNotDefaultLevel() {
        progressions = player.getProgressions();
        
        
        progressions.addNewProgression(GameType.TEST);
        LevelProgression prog = progressions.getProgressionOrDefault(GameType.TEST);
                
        factory.setCurrentLevel(DEFAULT_OPEN_TEST_LEVELS + 1, prog);
    }
    
    @Test
    public void gameStartsAtLevelOneByDefault(){
        int numberOfPlayerse = 1;
        int startingLevel = 1;
        int id = new GameManager().createGame(numberOfPlayerse, startingLevel, GameType.TEST, GameDifficulty.MEDIUM, LevelProgressionRepository.getDefault(GameType.ARCADE));
        final String playername = "kevin";
        Game game = new GameManager().getGame(id);
        player = new Player(new User(playername));
        
        game.addConnectingPlayer(player);
        game.addConnectionForPlayer("kevin", new DummyConnection());
        
        game.startLevel();
        
        final int expectedLevel = 1;
        Assert.assertEquals(expectedLevel, game.getCurrentLevel().getId());
    }
    
    @Test( expected = BreakoutException.class )
    public void gameCrashesWhenIllegalLevelIsSet(){
        int numberOfPlayerse = 1;
        int startingLevel = 1;
        int id = new GameManager().createGame(numberOfPlayerse, startingLevel, GameType.TEST, GameDifficulty.MEDIUM, LevelProgressionRepository.getDefault(GameType.ARCADE));
        final String playername = "kevin";
        Game game = new GameManager().getGame(id);
        player = new Player(new User(playername));
        player.getProgressions().addNewProgression(GameType.TEST);
        
        game.addConnectingPlayer(player);
        game.addConnectionForPlayer("kevin", new DummyConnection());
        
        game.setCurrentLevel(DEFAULT_OPEN_TEST_LEVELS + 1, player.getProgressions().getProgressionOrDefault(GameType.TEST));
        
    }
    
    @Test
    public void gameIncrementsLevelReachedOnLevelCompletion(){
        int numberOfPlayerse = 1;
        int startingLevel = 1;
        int id = new GameManager().createGame(numberOfPlayerse, startingLevel, GameType.TEST, GameDifficulty.MEDIUM, LevelProgressionRepository.getDefault(GameType.ARCADE));
        final String playername = "kevin";
        Game game = new GameManager().getGame(id);
        player = new Player(new User(playername));
        player.getProgressions().addNewProgression(GameType.TEST);
        
        game.addConnectingPlayer(player);
        game.addConnectionForPlayer("kevin", new DummyConnection());
        
        LevelProgression playerProgression = player.getProgressions().getProgressionOrDefault(GameType.TEST);
        
        game.setCurrentLevel(17, playerProgression);
        
        level =  game.getCurrentLevel();
        
        Ball ball = level.getLevelState().getBall();
        ball.setLinearVelocity(0, -100);
        
        stepTimes(60);
        
        int levelReached = player.getProgressions().getProgressionOrDefault(GameType.TEST).getHighestLevelReached();
        
        Assert.assertEquals(2, levelReached);
    }
    
    
    private void stepTimes(int times) {
        for (int i = 1; i <= times; i++) {
            level.step();
        }
    }
}
