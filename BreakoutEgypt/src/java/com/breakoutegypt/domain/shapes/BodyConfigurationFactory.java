/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.breakoutegypt.domain.shapes;

import org.jbox2d.collision.shapes.CircleShape;
import org.jbox2d.collision.shapes.PolygonShape;
import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.BodyType;

/**
 * Creates Box2d Body objects and adds them to the world.
 *
 * @author kevin
 */
public class BodyConfigurationFactory {
    private static BodyConfigurationFactory INSTANCE;

    //TESTING BITS FOR COLLISION
    private final int BOUNDS_CATEGORY = 0x0001;
    private final int PROJECTILE_CATEGORY = 0x0002;
    private final int BRICK_CATEGORY = 0x0004;
    private final int PADDLE_CATEGORY = 0x0008;
    private final int BALL_CATEGORY = 0X0010;
    private final int PROJECTILE_MASK = BOUNDS_CATEGORY | PADDLE_CATEGORY;
    private final int BRICK_MASK = BALL_CATEGORY;
    private final int PADDLE_MASK = BALL_CATEGORY | PROJECTILE_CATEGORY;
    private final int BALL_MASK = BOUNDS_CATEGORY | BRICK_CATEGORY | PADDLE_CATEGORY;
    private final int BOUNDS_MASK = -1;

    private BodyConfigurationFactory(){
        
    }
    
    public static BodyConfigurationFactory getInstance(){
        if(INSTANCE ==  null){
            INSTANCE = new BodyConfigurationFactory();
        }
        
        return INSTANCE;
    }
    
    public BodyConfiguration createTriangleConfig(ShapeDimension shape) {

        BodyDefConfig bodyDef = new BodyDefConfig(BodyType.STATIC, new Vec2(shape.getPosX(), shape.getPosY()));

        PolygonShape ps = new PolygonShape();

        // the coordinates of the points of the lines are relative to the shape's coordinates
        // ex. a triangle at [50,50] with width 10 will create a triangle with points [ 55, 50], [50, 60], [60,60]
        // vertices must be given counterclockwise
        Vec2[] vertices = new Vec2[3];
        vertices[0] = new Vec2(shape.getWidth() / 2, 0);
        vertices[1] = new Vec2(0, shape.getHeight());
        vertices[2] = new Vec2(shape.getWidth(), shape.getHeight());
        ps.set(vertices, 3);

        FixtureDefConfig fixtureConfig = new FixtureDefConfig(1f, 0f, 1f, false, BRICK_CATEGORY, BRICK_MASK);

        BodyConfiguration config = new BodyConfiguration(bodyDef, ps, fixtureConfig);

        return config;
    }
    
    public BodyConfiguration createPaddleConfig(ShapeDimension s) {
        BodyDefConfig bodyDefConfig = new BodyDefConfig(BodyType.KINEMATIC, new Vec2(s.getPosX(), s.getPosY()));

        PolygonShape ps = new PolygonShape();
        ps.setAsBox(s.getWidth() / 2, s.getHeight() / 2);

        FixtureDefConfig fixtureConfig = new FixtureDefConfig(1f, 0f, 1f, false, PADDLE_CATEGORY, PADDLE_MASK);

        return new BodyConfiguration(bodyDefConfig, ps, fixtureConfig);
    }

    // x and y position of the shapedimension are the middle of the circle
    public BodyConfiguration createDomePaddleConfig(ShapeDimension s) {
        long radius = s.getWidth() / 2;

        BodyDefConfig bodyDefConfig = new BodyDefConfig(BodyType.KINEMATIC, new Vec2(s.getPosX(), s.getPosY()));

        // Circles are drawn from the center of the shape
        CircleShape newDomePaddle = new CircleShape();
        newDomePaddle.m_radius = radius;

        FixtureDefConfig fixtureDefConfig = new FixtureDefConfig(1f, 0f, 1f, false, PADDLE_CATEGORY, PADDLE_MASK);

        return new BodyConfiguration(bodyDefConfig, newDomePaddle, fixtureDefConfig);
    }

    public BodyConfiguration createProjectileConfig(ShapeDimension s) {
        BodyDefConfig bodyDefConfig = new BodyDefConfig(BodyType.DYNAMIC, new Vec2(s.getPosX(), s.getPosY()));

        CircleShape cs = new CircleShape();
        cs.m_radius = s.getWidth() / 2;

        FixtureDefConfig fixtureConfig = new FixtureDefConfig(1f, 0f, 1f, false, PROJECTILE_CATEGORY, PROJECTILE_MASK);
        
        return new BodyConfiguration(bodyDefConfig, cs, fixtureConfig);
    }

    public BodyConfiguration createBallConfig(ShapeDimension s) {
        BodyDefConfig bodyDefConfig = new BodyDefConfig(BodyType.DYNAMIC, new Vec2(s.getPosX(), s.getPosY()));

        CircleShape cs = new CircleShape();
        cs.m_radius = s.getWidth() / 2;

        FixtureDefConfig fixtureConfig = new FixtureDefConfig(1f, 0f, 1f, false, BALL_CATEGORY, BALL_MASK);

        return new BodyConfiguration(bodyDefConfig, cs, fixtureConfig);
    }

    //This method creates a walls. 
    public BodyConfiguration createWallConfig(ShapeDimension s, boolean isGround) {
        PolygonShape ps = new PolygonShape();
        ps.setAsBox(s.getWidth(), s.getHeight());

        FixtureDefConfig fixtureConfig = new FixtureDefConfig(1f, 0f, 1f, isGround, BOUNDS_CATEGORY, BOUNDS_MASK);

        BodyDefConfig bodyDefConfig = new BodyDefConfig(BodyType.STATIC, new Vec2(s.getPosX(), s.getPosY()));

        return new BodyConfiguration(bodyDefConfig, ps, fixtureConfig);
    }
}
