/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.breakoutegypt.test;

import com.breakoutegypt.domain.shapes.Ball;
import com.breakoutegypt.domain.shapes.Paddle;
import com.breakoutegypt.domain.shapes.ShapeDimension;
import com.breakoutegypt.domain.shapes.bricks.Brick;
import java.util.ArrayList;
import java.util.List;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author Bjarne Deketelaere
 */
public class AutomaticIdentificationShapes {
    
    public AutomaticIdentificationShapes() {
    }
    @Test
    public void uniquePaddles(){
        ShapeDimension paddleshape=new ShapeDimension(45, 275, 100, 4);
        ShapeDimension paddleshape2=new ShapeDimension(45, 275, 100, 4);
        Paddle paddle1=new Paddle(paddleshape);
        Paddle paddle2=new Paddle(paddleshape2);
        
        assertEquals(paddle1.getName(), "Paddle1");
        assertEquals(paddle2.getName(), "Paddle2");
        
    }
    @Test
    public void uniqueBalls(){
        ShapeDimension ballshape=new ShapeDimension(45, 275, 100, 4);
        ShapeDimension ballshape2=new ShapeDimension(45, 275, 100, 4);
        Ball ball=new Ball(ballshape);
        Ball ball2=new Ball(ballshape2);
        
        assertEquals(ball.getName(), "Ball1");
        assertEquals(ball2.getName(), "Ball2");
    }
    @Test
    public void uniqueBricks(){
        ShapeDimension brickshape=new ShapeDimension(45, 275, 100, 4);
        ShapeDimension brickshape2=new ShapeDimension(45, 275, 100, 4);
        
        Brick brick=new Brick(brickshape);
        Brick brick2=new Brick(brickshape2);

        
        assertEquals(brick.getName(), "Brick1");
        assertEquals(brick2.getName(), "Brick2");
    }
}
