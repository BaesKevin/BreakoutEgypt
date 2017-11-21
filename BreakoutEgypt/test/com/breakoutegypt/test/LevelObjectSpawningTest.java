/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.breakoutegypt.test;

import com.breakoutegypt.domain.BreakoutWorld;
import com.breakoutegypt.domain.LevelState;
import com.breakoutegypt.domain.shapes.Ball;
import com.breakoutegypt.domain.shapes.bricks.Brick;
import com.breakoutegypt.domain.shapes.bricks.BrickType;
import com.breakoutegypt.domain.shapes.Paddle;
import com.breakoutegypt.domain.shapes.ShapeDimension;
import java.awt.Point;
import java.util.ArrayList;
import java.util.List;
import org.junit.Assert;
import org.junit.Test;

/**
 *
 * @author kevin
 */
public class LevelObjectSpawningTest {
    
    @Test
    public void testSpawnLevelWith7Objects(){
        List<Brick> bricks = new ArrayList();
        
        ShapeDimension shape = new ShapeDimension("triangle", 100,20, 20,20);
        Brick brick = new Brick(shape, new Point(0,0));
        bricks.add(brick);
        
        ShapeDimension paddleshape = new ShapeDimension("paddle", 100, 200, 100, 20);
        Paddle paddle = new Paddle(paddleshape);
        
        ShapeDimension ballshape = new ShapeDimension("paddle", 100, 100, 10, 10);
        Ball ball = new Ball(ballshape);
        
        LevelState initialState = new LevelState(ball, paddle, bricks);
        BreakoutWorld world = new BreakoutWorld();
        
        initialState.spawnAllObjects(world);
        
        // 4 walls, ball, brick and paddle
        Assert.assertEquals(7, world.countWorldObjects());
    }
    
}
