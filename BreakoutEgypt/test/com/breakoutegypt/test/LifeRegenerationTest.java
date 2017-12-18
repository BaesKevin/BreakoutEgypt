/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.breakoutegypt.test;

import com.breakoutegypt.connectionmanagement.DummyConnection;
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
    private Player player;
    
    private void createGame(GameDifficulty diff, int startingLevel) {
        player = new Player("player");
        
        GameManager gm = new GameManager();
        int id = gm.createGame(GameType.TEST, diff);
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
    public void noLivesLostOnEasy() {
        createGame(GameDifficulty.EASY, 1);

        goOutOfBoundsNumberOfTimes(1);
        Assert.assertEquals(Difficulty.INFINITE_LIVES, player.getLives());
    }

    @Test
    public void LivesLostOnMediumHardBrutal() {
        createGame(GameDifficulty.MEDIUM, 1);
        goOutOfBoundsNumberOfTimes(1);
        Assert.assertEquals(2, player.getLives());

        createGame(GameDifficulty.HARD, 1);
        goOutOfBoundsNumberOfTimes(1);
        Assert.assertEquals(2, player.getLives());

        createGame(GameDifficulty.BRUTAL, 1);
        goOutOfBoundsNumberOfTimes(1);
        Assert.assertEquals(0, player.getLives());
    }

    @Test
    public void livesRegenerateAfterLevelCompleteOnMedium() {
        goToNextLevel(GameDifficulty.MEDIUM);

        Assert.assertEquals(3, player.getLives());
    }

    @Test
    public void livesDontRegenerateOnHard() {
        goToNextLevel(GameDifficulty.HARD);

        Assert.assertEquals(2, player.getLives());
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
            stepTimes(150);
        }

    }

    private void stepTimes(int times) {
        for (int i = 1; i <= times; i++) {
            level.step();
        }
    }
}
