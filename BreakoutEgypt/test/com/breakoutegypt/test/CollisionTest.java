/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.breakoutegypt.test;

import com.breakoutegypt.domain.Game;
import com.breakoutegypt.domain.GameType;
import com.breakoutegypt.domain.Level;
import org.jbox2d.common.Vec2;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

/**
 *
 * @author kevin
 */
public class CollisionTest {
    
    private Game game;
    private Level level;
    
    @Before
    public void setup(){
        game = new Game(1,1, GameType.TEST);
        level = game.getCurrentLevel();
    }
    
    @Test
    public void testMovingBall(){
        System.out.println(level.getBall().getPosition());
        level.startBall();
        level.step();
        level.step();
       System.out.println(level.getBall().getPosition());
        
        
        Assert.assertEquals(1, level.getId());
    }
    
}
