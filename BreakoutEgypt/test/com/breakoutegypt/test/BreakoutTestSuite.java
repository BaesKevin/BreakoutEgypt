/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.breakoutegypt.test;

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
   LevelObjectSpawningTest.class,
   MessageTest.class,
   PowerUpTest.class,
   LevelStateTest.class,
   ScoreTester.class,
   LevelPackProgressTest.class,
   PowerDownTest.class,
   LifeRegenerationTest.class,
   MysqlTest.class
})

public class BreakoutTestSuite {
    
}
