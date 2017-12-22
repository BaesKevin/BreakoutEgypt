/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.breakoutegypt.test;

import com.breakoutegypt.connectionmanagement.DummyConnection;
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
import com.breakoutegypt.domain.levelprogression.LevelProgressManager;
import com.breakoutegypt.domain.shapes.Ball;
import com.breakoutegypt.exceptions.BreakoutException;
import com.breakoutegypt.levelfactories.LevelFactory;
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
    private LevelFactory factory;
    private Level level;
    private GameManager gm;
    private Game game;

    private static final int DEFAULT_OPEN_TEST_LEVELS = TestLevelFactory.DEFAULT_OPEN_LEVELS;
    private final LevelProgress ALL_LEVELS_UNLOCKED = DummyLevelProgressionRepository.getDefault(GameType.TEST);

    @Before
    public void before() {
        player = new Player("player");
        factory = new TestLevelFactory(null);
        gm = new GameManager();
        Repositories.isTesting(true);
    }

    private void createGame(int startingLevel) {
        GameManager gm = new GameManager();

        int id = gm.createGame(GameType.TEST, "medium");
        game = gm.getGame(id);
        factory = game.getLevelFactory();
        game.initStartingLevel(startingLevel, ALL_LEVELS_UNLOCKED);

        level = game.getCurrentLevel();

        for (int i = 0; i < 1000; i++) {
            ALL_LEVELS_UNLOCKED.incrementHighestLevelReached();
        }
    }

    @Test
    public void textGetReachedLevelDefaultNotOpen() {
        createGame(DEFAULT_OPEN_TEST_LEVELS);
        progressions = player.getProgressions();

        progressions.addNewProgression(GameType.TEST, "easy");
        LevelProgress prog = progressions.getProgress(GameType.TEST, "easy").getLevelProgress();

        for (int i = 1; i < DEFAULT_OPEN_TEST_LEVELS + 1; i++) {
            prog.incrementHighestLevelReached();
        }

        factory.setCurrentLevel(DEFAULT_OPEN_TEST_LEVELS + 1, prog);

        // no assert because there aren't 500 levels, we would get exceptions if it didn't work
    }

    @Test
    public void textGetDefaultOpenLevel() {
        createGame(1);
        progressions = player.getProgressions();

        progressions.addNewProgression(GameType.TEST, "easy");
        LevelProgress prog = progressions.getProgress(GameType.TEST, "easy").getLevelProgress();

        factory.setCurrentLevel(DEFAULT_OPEN_TEST_LEVELS, prog);

        // no assert because there aren't 500 levels, we would get exceptions if it didn't work
    }

    @Test(expected = BreakoutException.class)
    public void textGetUnreachedNotDefaultLevel() {
        progressions = player.getProgressions();

        progressions.addNewProgression(GameType.TEST, Difficulty.EASY);
        LevelProgress prog = progressions.getProgress(GameType.TEST, "easy").getLevelProgress();

        factory.setCurrentLevel(DEFAULT_OPEN_TEST_LEVELS + 1, prog);
    }

    @Test(expected = BreakoutException.class)
    public void gameCrashesWhenIllegalLevelIsSet() {
        int id = gm.createGame(GameType.TEST, Difficulty.EASY);

        final String playername = "kevin";
        Game game = new GameManager().getGame(id);

        player = new Player(playername);
        player.getProgressions().addNewProgression(GameType.TEST, Difficulty.EASY);

        game.addConnectingPlayer(player);
        game.addConnectionForPlayer("kevin", new DummyConnection());

        game.initStartingLevel(DEFAULT_OPEN_TEST_LEVELS + 1, player.getProgressions().getProgress(GameType.TEST, Difficulty.EASY).getLevelProgress());

    }

    @Test
    public void gameIncrementsLevelReachedOnLevelCompletion() {
        int id = gm.createGame(GameType.TEST, Difficulty.BRUTAL);

        final String playername = "kevin";
        game = gm.getGame(id);

        player = new Player(playername);
        player.getProgressions().addNewProgression(GameType.TEST, Difficulty.BRUTAL);

        game.addConnectingPlayer(player);
        game.addConnectionForPlayer("kevin", new DummyConnection());

        LevelProgress playerProgress = player.getProgressions().getProgress(GameType.TEST, Difficulty.BRUTAL).getLevelProgress();

        game.initStartingLevel(17, playerProgress);

        level = game.getCurrentLevel();

        Ball ball = level.getLevelState().getBall();
        ball.setLinearVelocity(0, -100);

        stepTimes(60);

        int levelReached = player.getProgressions().getHighestLevelReached(GameType.TEST, Difficulty.BRUTAL);

        Assert.assertEquals(2, levelReached);
    }

    private void stepTimes(int times) {
        for (int i = 1; i <= times; i++) {
            level.step();
        }
    }
}
