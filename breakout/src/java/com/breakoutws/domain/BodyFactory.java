/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.breakoutws.domain;

import org.jbox2d.collision.shapes.CircleShape;
import org.jbox2d.collision.shapes.PolygonShape;
import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.Body;
import org.jbox2d.dynamics.BodyDef;
import org.jbox2d.dynamics.BodyType;
import org.jbox2d.dynamics.FixtureDef;
import org.jbox2d.dynamics.World;

/**
 * Creates Box2d Body objects and adds them to the world.  
 * @author kevin
 */
public class BodyFactory {
    private World world;
    public static final int BALL_RADIUS = 8;
    
    public BodyFactory(World world){
        this.world = world;
    }
    
    public Body createTriangle(Shape s){
        BodyDef bd = new BodyDef();
        bd.type = BodyType.STATIC;
        bd.position.set(s.getPosX(), s.getPosY());
        PolygonShape ps = new PolygonShape();

        Vec2[] vertices = new Vec2[3];
        vertices[0] = new Vec2(s.getWidth()/2, 0);
        vertices[1] = new Vec2(0, s.getHeight());
        vertices[2] = new Vec2(s.getWidth(), s.getHeight());
        ps.set(vertices, 3);
        
        // Create a fixture for ball
        FixtureDef fd = new FixtureDef();
        fd.shape = ps;
        fd.density = 0.6f;
        fd.friction = 0f;        
        fd.restitution = 1f;
        
        /**
        * Virtual invisible JBox2D body of ball. Bodies have velocity and position. 
        * Forces, torques, and impulses can be applied to these bodies.
        */
        Body body = world.createBody(bd);
        body.createFixture(fd);
        body.setUserData(s);
        return body;
    }
    
    public Body createPaddle(Shape s){
        BodyDef bd = new BodyDef();
        bd.type = BodyType.KINEMATIC;
        bd.position.set(s.getPosX(), s.getPosY());
        PolygonShape ps = new PolygonShape();
        ps.setAsBox(s.getWidth() / 2, s.getHeight() /2);
        
        // Create a fixture for ball
        FixtureDef fd = new FixtureDef();
        fd.shape = ps;
        fd.density = 0.6f;
        fd.friction = 0f;        
        fd.restitution = 1f;
        
        /**
        * Virtual invisible JBox2D body of ball. Bodies have velocity and position. 
        * Forces, torques, and impulses can be applied to these bodies.
        */
        Body body = world.createBody(bd);
        body.createFixture(fd);
        body.setUserData(s);
        return body;
    }
    
    public Body createCircle(Shape s){
        BodyDef bd = new BodyDef();
        bd.type = BodyType.DYNAMIC;
        bd.position.set(s.getPosX(), s.getPosY());
//        bd.linearVelocity.x = -100;
        bd.linearVelocity.y = -50;
        CircleShape cs = new CircleShape();
        cs.m_radius = s.getWidth() / 2;
        
        
        // Create a fixture for ball
        FixtureDef fd = new FixtureDef();
        fd.shape = cs;
        fd.density = 0.6f;
        fd.friction = 0f;        
        fd.restitution = 1f;
        
        /**
        * Virtual invisible JBox2D body of ball. Bodies have velocity and position. 
        * Forces, torques, and impulses can be applied to these bodies.
        */
        Body body = world.createBody(bd);
        body.createFixture(fd);
        body.setUserData(s);
        return body;
    }
    
    public void addGround(float y, int width ) {
        PolygonShape ps = new PolygonShape();
        ps.setAsBox(width, 1);

        FixtureDef fd = new FixtureDef();
        fd.shape = ps;
        fd.isSensor = true;

        BodyDef bd = new BodyDef();
        bd.position = new Vec2(0.0f, y);

        Shape groundShape = new Shape("ground", 0, y, width, 1);
        Body body = world.createBody(bd);
        body.createFixture(fd);
        body.setUserData(groundShape);
    }

    //This method creates a walls. 
    public void addWall(float posX, float posY, float width, float height) {
        PolygonShape ps = new PolygonShape();
        ps.setAsBox(width, height);

        FixtureDef fd = new FixtureDef();
        fd.shape = ps;
        fd.density = 1.0f;
        fd.friction = 0.0f;

        BodyDef bd = new BodyDef();
        bd.position.set(posX, posY);

        world.createBody(bd).createFixture(fd);
    }
}
