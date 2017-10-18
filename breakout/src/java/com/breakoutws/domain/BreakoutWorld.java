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
import org.jbox2d.dynamics.BodyDef;
import org.jbox2d.dynamics.FixtureDef;
import org.jbox2d.dynamics.World;

/**
 *
 * @author kevin
 */
public class BreakoutWorld {

    private World world;
    public static final int BALL_RADIUS = 8;
    public static final int WIDTH = 600;
    public static final int HEIGHT = 600;
    private final float timestep = 1.0f / 60.0f;
    private final int velocityIterations = 2;
    private final int positionIterations = 3;

    private List<Box2dObject> objects = new ArrayList();
    private List<Brick> bricksToDestroy = new ArrayList();
    private Paddle p;
    
    public BreakoutWorld() {
        world = new World(new Vec2(0.0f, 0.0f));
        world.setContactListener(new BreakoutContactListener(this));

        addGround(300, 10);
        addWall(0, 300, 1, 300); //Left wall
        addWall(290, 300, 1, 300); //Right wall, keep in mind 
        addWall(0, 300, 300, 1); //Left wall

        objects.add(new Ball(45, 90, BreakoutWorld.BALL_RADIUS, Color.GREEN, this.getWorld()));
        objects.add(new Brick(45, 200, 8, 8, Color.PINK, this.world));
        p = new Paddle(45, 250, 100, 4, Color.BLUE, this.world);
        objects.add(p);
    }

    public List<Box2dObject> getObjects() {
        return objects;
    }
    
    public void movePaddle(float x, float y){
        p.move(x, y);
    }

    public void destroyBrick(Brick brick) {
        if (!bricksToDestroy.contains(brick)) {
            objects.remove(brick);
            bricksToDestroy.add(brick);
        }

        
    }

    public void step() {
        world.step(timestep, velocityIterations, positionIterations);
        for(Brick brick : bricksToDestroy){
            world.destroyBody(brick.getBody());
        }
        bricksToDestroy.clear();
    }

    public World getWorld() {
        return world;
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
