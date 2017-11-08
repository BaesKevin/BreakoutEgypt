/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.breakoutws.domain.brickcollisionhandlers;

import com.breakoutws.domain.BreakoutWorld;
import com.breakoutws.domain.shapes.Brick;
import org.jbox2d.dynamics.Body;

/**
 *
 * @author kevin
 */
public class RegularCollision {
    private BreakoutWorld world;
    private Body brickBody;
    private String shapeName;

    public RegularCollision(BreakoutWorld world, Body brickBody, String shapeNamee) {
        this.world = world;
        this.brickBody = brickBody;
        this.shapeName = shapeNamee;
    }
    
    public void handleCollsion(){
        System.out.println("RegularCollision: Handling regular collision");
        world.destroyBrick(brickBody);
    }
}
