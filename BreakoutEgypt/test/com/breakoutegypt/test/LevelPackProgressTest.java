/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.breakoutegypt.test;

import com.breakoutegypt.connectionmanagement.DummyConnection;
import com.breakoutegypt.data.LevelProgressionRepository;
import com.breakoutegypt.domain.Game;
import com.breakoutegypt.domain.levelprogression.GameDifficulty;
import com.breakoutegypt.domain.GameManager;
import com.breakoutegypt.domain.GameType;
import com.breakoutegypt.domain.Level;
import com.breakoutegypt.domain.Player;
import com.breakoutegypt.domain.User;
import com.breakoutegypt.domain.levelprogression.LevelProgress;
import com.breakoutegypt.domain.levelprogression.LevelProgressManager;
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
public class LevelPackProgressTest {

    private Player player;
    private LevelProgressManager progressions;
    private TestLevelFactory factory;
    private Level level;
    private GameManager gm;
    private Game game;

    private static final int DEFAULT_OPEN_TEST_LEVELS = TestLevelFactory.DEFAULT_OPEN_LEVELS;
    private final LevelProgress ALL_LEVELS_UNLOCKED = LevelProgressionRepository.getDefault(GameType.TEST);

    @Before
    public void before() {
        player = new Player(new User("player"));
        factory = new TestLevelFactory(null);
        gm = new GameManager();
    }

    private void createGame(int startingLevel) {
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
    public void textGetReachedLevelDefaultNotOpen() {
        progressions = player.getProgressions();

        progressions.addNewProgression(GameType.TEST, GameDifficulty.EASY);
        LevelProgress prog = progressions.getProgress(GameType.TEST, GameDifficulty.EASY).getLevelProgress();

        for (int i = 1; i < DEFAULT_OPEN_TEST_LEVELS + 1; i++) {
            prog.incrementHighestLevelReached();
        }

        factory.setCurrentLevel(DEFAULT_OPEN_TEST_LEVELS + 1, prog);

        // no assert because there aren't 500 levels, we would get exceptions if it didn't work
    }

    @Test
    public void textGetDefaultOpenLevel() {
        progressions = player.getProgressions();

        progressions.addNewProgression(GameType.TEST, GameDifficulty.EASY);
        LevelProgress prog = progressions.getProgress(GameType.TEST, GameDifficulty.EASY).getLevelProgress();

        factory.setCurrentLevel(DEFAULT_OPEN_TEST_LEVELS, prog);

        // no assert because there aren't 500 levels, we would get exceptions if it didn't work
    }

    @Test(expected = BreakoutException.class)
    public void textGetUnreachedNotDefaultLevel() {
        progressions = player.getProgressions();

        progressions.addNewProgression(GameType.TEST, GameDifficulty.EASY);
        LevelProgress prog = progressions.getProgress(GameType.TEST, GameDifficulty.EASY).getLevelProgress();

        factory.setCurrentLevel(DEFAULT_OPEN_TEST_LEVELS + 1, prog);
    }

    @Test
    public void gameStartsAtLevelOneByDefault() {
        createGame(1);
        
        final String playername = "kevin";

        player = new Player(new User(playername));

        game.addConnectingPlayer(player);
        game.addConnectionForPlayer("kevin", new DummyConnection());

        final int expectedLevel = 1;
        Assert.assertEquals(expectedLevel, game.getCurrentLevel().getId());
    }

    @Test(expected = BreakoutException.class)
    public void gameCrashesWhenIllegalLevelIsSet() {
        int id = gm.createGame(GameType.TEST, GameDifficulty.EASY);

        final String playername = "kevin";
        Game game = new GameManager().getGame(id);

        player = new Player(new User(playername));
        player.getProgressions().addNewProgression(GameType.TEST, GameDifficulty.MEDIUM);

        game.addConnectingPlayer(player);
        game.addConnectionForPlayer("kevin", new DummyConnection());

        
        game.initStartingLevel(DEFAULT_OPEN_TEST_LEVELS + 1, player.getProgressions().getProgress(GameType.TEST, GameDifficulty.EASY).getLevelProgress());

    }

    @Test
    public void gameIncrementsLevelReachedOnLevelCompletion() {
        int id = gm.createGame(GameType.TEST, GameDifficulty.BRUTAL);

        final String playername = "kevin";
        game = gm.getGame(id);

        player = new Player(new User(playername));
        player.getProgressions().addNewProgression(GameType.TEST, GameDifficulty.BRUTAL);

        game.addConnectingPlayer(player);
        game.addConnectionForPlayer("kevin", new DummyConnection());

        LevelProgress playerProgress = player.getProgressions().getProgress(GameType.TEST, GameDifficulty.BRUTAL).getLevelProgress();
        
        game.initStartingLevel(17, playerProgress);

        level = game.getCurrentLevel();

        Ball ball = level.getLevelState().getBall();
        ball.setLinearVelocity(0, -100);

        stepTimes(120);

        int levelReached = player.getProgressions().getHighestLevelReached(GameType.TEST, GameDifficulty.BRUTAL);

        Assert.assertEquals(2, levelReached);
    }

    private void stepTimes(int times) {
        for (int i = 1; i <= times; i++) {
            level.step();
        }
    }
}
