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

    public static final int BALL_RADIUS = 8;

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

        FixtureDefConfig fixtureConfig = new FixtureDefConfig(1f, 0f, 1f);

        BodyConfiguration config = new BodyConfiguration(bodyDef, ps, fixtureConfig);

        return config;
    }

    public BodyConfiguration createPaddleConfig(ShapeDimension s) {
        BodyDefConfig bodyDefConfig = new BodyDefConfig(BodyType.KINEMATIC, new Vec2(s.getPosX(), s.getPosY()));
        
        PolygonShape ps = new PolygonShape();
        ps.setAsBox(s.getWidth() / 2, s.getHeight() / 2);

        FixtureDefConfig fixtureConfig = new FixtureDefConfig(1f, 0f, 1f);

        return new BodyConfiguration(bodyDefConfig, ps, fixtureConfig);
    }

    public BodyConfiguration createDomePaddleConfig(ShapeDimension s) {
        long radius = s.getWidth() / 2;

        BodyDefConfig bodyDefConfig = new BodyDefConfig(BodyType.KINEMATIC, new Vec2(s.getPosX(), s.getPosY() + s.getHeight()));

        // Circles are drawn from the center of the shape
        
        CircleShape newDomePaddle = new CircleShape();
        newDomePaddle.m_radius = radius;

        FixtureDefConfig fixtureDefConfig = new FixtureDefConfig(1f, 0f, 1f);

        return new BodyConfiguration(bodyDefConfig, newDomePaddle, fixtureDefConfig);
    }

    public BodyConfiguration createBallConfig(ShapeDimension s) {
        BodyDefConfig bodyDefConfig = new BodyDefConfig(BodyType.DYNAMIC, new Vec2(s.getPosX(), s.getPosY()));
        
        CircleShape cs = new CircleShape();
        cs.m_radius = s.getWidth() / 2;

        FixtureDefConfig fixtureConfig = new FixtureDefConfig(1f, 0f, 1f, false, -1);
        
        return new BodyConfiguration(bodyDefConfig, cs, fixtureConfig);
    }

    //This method creates a walls. 
    public BodyConfiguration createWallConfig(ShapeDimension s, boolean isGround) {
        PolygonShape ps = new PolygonShape();
        ps.setAsBox(s.getWidth(), s.getHeight());

        FixtureDefConfig fixtureConfig = new FixtureDefConfig(1f, 0f, 1f, isGround);

        BodyDefConfig bodyDefConfig = new BodyDefConfig(BodyType.STATIC, new Vec2(s.getPosX(), s.getPosY()));

        return new BodyConfiguration(bodyDefConfig, ps, fixtureConfig);
    }
}
