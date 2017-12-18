/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.breakoutegypt.data;

import com.breakoutegypt.domain.BreakoutWorld;
import com.breakoutegypt.domain.shapes.Ball;
import com.breakoutegypt.domain.shapes.DimensionDefaults;
import com.breakoutegypt.domain.shapes.Paddle;
import com.breakoutegypt.domain.shapes.Projectile;
import com.breakoutegypt.domain.shapes.ShapeDimension;
import com.breakoutegypt.domain.shapes.bricks.Brick;
import java.awt.Color;

/**
 *
 * @author kevin
 */
public class DefaultShapeRepository {
    private static DefaultShapeRepository INSTANCE;
    
    private DefaultShapeRepository(){
        
    }
    
    public static DefaultShapeRepository getInstance(){
        if(INSTANCE == null){
            INSTANCE = new DefaultShapeRepository();
        }
        
        return INSTANCE;
    }
    
     public Paddle getDefaultPaddle() {
        return getDefaultPaddle(50);
    }
     
    public Paddle getDefaultPaddle(float xPos) {
        return getDefaultPaddle("paddle", xPos);
    }
    
    public Paddle getDefaultPaddle(String name, float xPos) {
        return new Paddle( new ShapeDimension(name, xPos, BreakoutWorld.DIMENSION - DimensionDefaults.PADDLE_DISTANCE_FROM_BOTTOM, DimensionDefaults.PADDLE_WIDTH, DimensionDefaults.PADDLE_HEIGHT, Color.BLUE));
    }

    public Ball getDefaultBall() {
        return getDefaultBall(50, 80);
    }

    public Ball getDefaultBall(float x, float y) {
        return getDefaultBall("ball", x, y);
    }
    
    public Ball getDefaultBall(String name, float x, float y) {
        return new Ball(new ShapeDimension(name, x, y, DimensionDefaults.BALL_RADIUS, DimensionDefaults.BALL_RADIUS, Color.GREEN));
    }
    
    public ShapeDimension getDefaultFloor(){
        return getDefaultFloor("floor");
    }
   
    public ShapeDimension getDefaultFloor(String name){
        return new ShapeDimension(name, 0, BreakoutWorld.DIMENSION - 1, BreakoutWorld.DIMENSION, 1);
    }
    
    public Brick getDefaultBrick(String name, float x, float y){
        return new Brick(new ShapeDimension(name, x, y, DimensionDefaults.BRICK_WIDTH, DimensionDefaults.BRICK_HEIGHT));
    }
    public Brick getDefaultBrick(String name, float x, float y, boolean isTarget){
        return new Brick(new ShapeDimension(name, x, y, DimensionDefaults.BRICK_WIDTH, DimensionDefaults.BRICK_HEIGHT), isTarget);
    }
    public Brick getDefaultBrick(String name, float x, float y, boolean isTarget, boolean isVisible){
        return getDefaultBrick(name, x, y, isTarget, isVisible, true);
    }
    public Brick getDefaultBrick(String name, float x, float y, boolean isTarget, boolean isVisible, boolean isBreakable){
        return new Brick(new ShapeDimension(name, x, y, DimensionDefaults.BRICK_WIDTH, DimensionDefaults.BRICK_HEIGHT), isTarget, isVisible, isBreakable);
    }
    public Brick getDefaultBrick(String name, float x, float y, boolean isTarget, boolean isVisible, boolean isBreakable, boolean isInverted){
        return new Brick(new ShapeDimension(name, x, y, DimensionDefaults.BRICK_WIDTH, DimensionDefaults.BRICK_HEIGHT), isTarget, isVisible, isBreakable, isInverted);
    }
    
    public Projectile getProjectile(float x, float y){
        return getProjectile("projectile", x, y);
    }
    
    public Projectile getProjectile(String name, float x, float y){
        return new Projectile(new ShapeDimension(name, x, y, DimensionDefaults.BALL_RADIUS, DimensionDefaults.BALL_RADIUS));
    }
}
