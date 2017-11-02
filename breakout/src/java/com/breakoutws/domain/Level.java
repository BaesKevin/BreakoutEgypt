/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.breakoutws.domain;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;
import org.jbox2d.collision.shapes.PolygonShape;
import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.Body;
import org.jbox2d.dynamics.BodyDef;
import org.jbox2d.dynamics.FixtureDef;
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
    
    private World world;
    
    public Level(int id, World world){
    
        this.id = id;
        this.world = world;
        bricks = new ArrayList();
        addGround(300, 10);
        addWall(0, 300, 1, 300); //Left wall
        addWall(290, 300, 1, 300); //Right wall, keep in mind 
        addWall(0, 300, 300, 1); //Left wall
        
        BodyFactory factory = new BodyFactory(world);

        Shape ballShape = new Shape("ball", 60, 90, BodyFactory.BALL_RADIUS, BodyFactory.BALL_RADIUS, Color.GREEN);
        ball = factory.createCircle(ballShape);
        
        int row = 1;
        int col = 1;
        int rows = 3;
        int cols = 5;
        int width = 30;
        int height = 10;
        
        for(int x = 45; x < 45 + (( width + 1) * cols); x+=width + 1){
            for(int y = 45; y < 45 + (( height + 1) * rows ); y += height + 1){
                Shape brickShape = new Shape("brick" + col++ + "" + row, x, y, width, height, Color.PINK);
                bricks.add(factory.createBrick(brickShape));
            }
            row++;
        }
        
        
        Shape paddleShape = new Shape("paddle", 45, 250, 100, 4, Color.BLUE);
        paddle = factory.createPaddle(paddleShape);
    }
    
    void updatePositions() {
        Shape ballShape =  (Shape) ball.getUserData();
        ballShape.setPosX(ball.getPosition().x);
        ballShape.setPosY(ball.getPosition().y);
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
    
    
    public List<Body> getObjects() {
        List<Body> bodies = new ArrayList();
        bodies.addAll(bricks);
        bodies.add(ball);
        bodies.add(paddle);
        return bodies;
    }
    
    
    
    public void addGround(float width, float height) {
        PolygonShape ps = new PolygonShape();
        ps.setAsBox(width, height);

        FixtureDef fd = new FixtureDef();
        fd.shape = ps;

        BodyDef bd = new BodyDef();
        bd.position = new Vec2(0.0f, -10f);

        world.createBody(bd).createFixture(fd);
    }

    //This method creates a walls. 
    public void addWall(float posX, float posY, float width, float height) {
        PolygonShape ps = new PolygonShape();
        ps.setAsBox(width, height);

        FixtureDef fd = new FixtureDef();
        fd.shape = ps;
        fd.density = 1.0f;
        fd.friction = 0.3f;

        BodyDef bd = new BodyDef();
        bd.position.set(posX, posY);

        world.createBody(bd).createFixture(fd);
    }

    
}
