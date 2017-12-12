/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.breakoutegypt.test;

import com.breakoutegypt.data.LevelProgressionRepository;
import com.breakoutegypt.domain.Game;
import com.breakoutegypt.domain.GameManager;
import com.breakoutegypt.domain.GameType;
import com.breakoutegypt.domain.Level;
import com.breakoutegypt.domain.Player;
import com.breakoutegypt.domain.levelprogression.Difficulty;
import com.breakoutegypt.domain.levelprogression.GameDifficulty;
import com.breakoutegypt.domain.levelprogression.LevelProgress;
import org.junit.Assert;
import org.junit.Test;

/**
 *
 * @author kevin
 */
public class LifeRegenerationTest {

    private Game game;
    private Level level;
    private final LevelProgress ALL_LEVELS_UNLOCKED = LevelProgressionRepository.getDefault(GameType.TEST);

    private void createGame(GameDifficulty diff, int startingLevel) {
        GameManager gm = new GameManager();
        int id = gm.createGame(GameType.TEST, diff);
        game = gm.getGame(id);
        game.initStartingLevel(startingLevel, ALL_LEVELS_UNLOCKED);

        level = game.getCurrentLevel();

        for (int i = 0; i < 1000; i++) {
            ALL_LEVELS_UNLOCKED.incrementHighestLevelReached();
        }
    }

    @Test
    public void noLivesLostOnEasy() {
        createGame(GameDifficulty.EASY, 1);

        goOutOfBoundsNumberOfTimes(100);
        Assert.assertEquals(Difficulty.INFINITE_LIVES, level.getLives());
    }

    @Test
    public void LivesLostOnMediumHardBrutal() {
        createGame(GameDifficulty.MEDIUM, 1);
        goOutOfBoundsNumberOfTimes(1);
        Assert.assertEquals(2, level.getLives());

        createGame(GameDifficulty.HARD, 1);
        goOutOfBoundsNumberOfTimes(1);
        Assert.assertEquals(2, level.getLives());

        createGame(GameDifficulty.BRUTAL, 1);
        goOutOfBoundsNumberOfTimes(1);
        Assert.assertEquals(0, level.getLives());
    }

    @Test
    public void livesRegenerateAfterLevelCompleteOnMediumButNotOnHard() {
        goToNextLevel(GameDifficulty.MEDIUM);

        Assert.assertEquals(3, level.getLives());
    }

    @Test
    public void livesDontRegenerateOnHard() {
        goToNextLevel(GameDifficulty.HARD);

        Assert.assertEquals(2, level.getLives());
    }

    private void goToNextLevel(GameDifficulty diff) {
        createGame(diff, 17);

        goOutOfBoundsNumberOfTimes(1);

        level.getLevelState().getBall().setLinearVelocity(0, -100);
        stepTimes(60);

        level = game.getCurrentLevel();
    }

    private void goOutOfBoundsNumberOfTimes(int times) {
        for (int i = 1; i <= times; i++) {
            level.startBall();
            stepTimes(300);
        }

    }

    private void stepTimes(int times) {
        for (int i = 1; i <= times; i++) {
            level.step();
        }
    }
}
