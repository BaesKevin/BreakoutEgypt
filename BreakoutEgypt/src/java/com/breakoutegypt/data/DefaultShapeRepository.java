/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.breakoutegypt.data;

import com.breakoutegypt.domain.BreakoutWorld;
import com.breakoutegypt.domain.shapes.BodyConfigurationFactory;
import com.breakoutegypt.domain.shapes.DimensionDefaults;
import com.breakoutegypt.domain.shapes.ShapeDimension;
import com.breakoutegypt.domain.shapes.bricks.Brick;
import java.awt.Color;
import java.awt.Point;

/**
 *
 * @author kevin
 */
public class DefaultShapeRepository {
     public ShapeDimension getDefaultPaddle() {
        return getDefaultPaddle(50);
    }
     
    public ShapeDimension getDefaultPaddle(float xPos) {
        return new ShapeDimension("paddle", xPos, BreakoutWorld.DIMENSION - DimensionDefaults.PADDLE_DISTANCE_FROM_BOTTOM, DimensionDefaults.PADDLE_WIDTH, DimensionDefaults.PADDLE_HEIGHT, Color.BLUE);
    }

    public ShapeDimension getDefaultBall() {
        return new ShapeDimension("ball", 50, 80, DimensionDefaults.BALL_RADIUS, DimensionDefaults.BALL_RADIUS, Color.GREEN);
    }

    public ShapeDimension getDefaultBall(float x, float y) {
        return new ShapeDimension("ball", x, y, DimensionDefaults.BALL_RADIUS, DimensionDefaults.BALL_RADIUS, Color.GREEN);
    }
    
    public ShapeDimension getDefaultFloor(){
        return new ShapeDimension("floor", 0, BreakoutWorld.DIMENSION - 10, BreakoutWorld.DIMENSION, 3);
    }
    
    public Brick getDefaultBrick(String name, float x, float y, Point gridPoint){
        return new Brick(new ShapeDimension(name, x, y, DimensionDefaults.BRICK_WIDTH, DimensionDefaults.BRICK_HEIGHT), gridPoint);
    }
}
