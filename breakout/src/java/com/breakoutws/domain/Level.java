/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.breakoutws.domain;

import java.util.ArrayList;
import java.util.List;
import org.jbox2d.dynamics.Body;
import org.jbox2d.dynamics.World;

/**
 * keeps track of all the objects present in the level, only one level for now
 * @author kevin
 */
public class Level {
    private int id;
    private List<Body> bricks;
    private Body ball;
    private Body paddle;
    
    private BodyFactory factory;
    
    public Level(int id, World world){
    
        this.id = id;
        bricks = new ArrayList();
        
        BodyFactory factory = new BodyFactory(world);
        this.factory = factory;
        
        createBounds();
    }
    
    public Level(int id, World world, Shape ball, Shape paddle, List<Shape> bricks){
        this(id, world);
        
        addBall(ball);
        addPaddle(paddle);
        
        for(Shape brick : bricks){
            addBrick(brick);
        }
    }
    
    public void addPaddle(Shape s){
        paddle = factory.createPaddle(s);
    }
    
    public void addBrick(Shape s){
        bricks.add(factory.createBrick(s));
    }
    
    public void addBall(Shape s){
        ball = factory.createCircle(s);
    }
    
    private void createBounds(){
        factory.addGround(300, 10);
        factory.addWall(0, 300, 1, 300); //Left wall
        factory.addWall(290, 300, 1, 300); //Right wall, keep in mind 
        factory.addWall(0, 300, 300, 1); //Left wall
    }
    
    public int getId() {
        return id;
    }

    public List<Body> getBricks() {
        return bricks;
    }

    public Body getBall() {
        return ball;
    }

    public Body getPaddle() {
        return paddle;
    }

    public void removeBrick(Body brick){
        bricks.remove(brick);
    }
}
