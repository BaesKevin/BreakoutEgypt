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
import com.breakoutegypt.domain.levelprogression.Difficulty;
import com.breakoutegypt.domain.levelprogression.LevelProgress;
import com.breakoutegypt.domain.shapes.Ball;
import java.util.List;
import org.junit.After;
import static org.junit.Assert.assertTrue;
import org.junit.Before;
import org.junit.Test;

/**
 *
 * @author BenDB
 */
public class PowerDownTest {

    Level level;
    Game game;
    Player player;
    private final LevelProgress ALL_LEVELS_UNLOCKED = DummyLevelProgressionRepository.getDefault(GameType.TEST);

    @Before
    public void init() {
        GameManager gm = new GameManager();
        String id = gm.createGame(GameType.TEST, Difficulty.MEDIUM);
        game = gm.getGame(id);

        player = new Player("Kevin");

        game.addConnectingPlayer(player);

        game.addConnectionForPlayer("Kevin", new DummyConnection());

        Repositories.isTesting(true);
    }

    @After
    public void disableTesting() {
        Repositories.isTesting(false);
    }

    @Test
    public void testFloodPowerDown() {
        game.initStartingLevel(18, ALL_LEVELS_UNLOCKED);
        level = game.getLevel();
        List<Ball> balls;
        level.startBall();
        balls = level.getLevelState().getBalls();
        stepTimes(level, 30);
        balls = level.getLevelState().getBalls();
        assertTrue(balls.size() == 6);
    }

    @Test
    public void testProjectilePowerDown() {
        game.initStartingLevel(19, ALL_LEVELS_UNLOCKED);
        level = game.getLevel();
        level.startBall();

        stepTimes(level, 120);
        DummyConnection conn = (DummyConnection) player.getConnection();
        assertTrue(1 == conn.getLifeMessages().size());
    }

    @Test
    public void testTriggerPowerdownWithExplosive() {

        game.initStartingLevel(20, ALL_LEVELS_UNLOCKED);
        level = game.getLevel();
        level.startBall();

        stepTimes(level, 120);
        DummyConnection conn = (DummyConnection) player.getConnection();
        assertTrue(2 == conn.getBrickMessages().size());
    }

    private void stepTimes(Level level, int times) {
        for (int i = 1; i <= times; i++) {
            level.step();
        }
    }
}
