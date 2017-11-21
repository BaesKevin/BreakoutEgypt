/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.breakoutegypt.domain.shapes;

import org.jbox2d.collision.shapes.Shape;
/**
 *
 * @author kevin
 */
public class BodyConfiguration {
    private BodyDefConfig bodyDefinition;
    private Shape shape;
    private FixtureDefConfig fixtureConfig;

    public BodyConfiguration(BodyDefConfig bodyDefinition, Shape shape, FixtureDefConfig fixtureConfig) {
        this.bodyDefinition = bodyDefinition;
        this.shape = shape;
        this.fixtureConfig = fixtureConfig;
    }

    public BodyDefConfig getBodyDefinition() {
        return bodyDefinition;
    }

    public Shape getShape() {
        return shape;
    }

    public FixtureDefConfig getFixtureConfig() {
        return fixtureConfig;
    }
    
    
}
