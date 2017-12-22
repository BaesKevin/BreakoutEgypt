/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.breakoutegypt.test;

import com.breakoutegypt.data.Repositories;
import org.junit.After;
import org.junit.Before;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;

/**
 *
 * @author kevin
 */
@RunWith(Suite.class)

@Suite.SuiteClasses({
    GameManagerTest.class,
    SessionManagerTest.class,
    LevelTest.class,
    LevelPackProgressTest.class,
    LevelObjectSpawningTest.class,
    MessageTest.class,
    PowerUpTest.class,
    LevelStateTest.class,
    ScoreTester.class,
    PowerDownTest.class,
    LifeRegenerationTest.class,
    MultiplayerTest.class
})

public class BreakoutTestSuite {

    @After
    public void disableTesting() {
        Repositories.isTesting(false);
    }

}
