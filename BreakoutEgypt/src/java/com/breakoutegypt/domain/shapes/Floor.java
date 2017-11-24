/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.breakoutegypt.domain.shapes;

/**
 *
 * @author BenDB
 */
public class Floor extends RegularBody{
    
    ShapeDimension floorShape;
    
    public Floor(ShapeDimension s) {
        // s = new ShapeDimension("floor", 0, 290, 300, 1);
        super(s);
        
        BodyConfiguration floorConfig = new BodyConfigurationFactory().createWallConfig(s, false);
        this.setBox2dConfig(floorConfig);
        
    }
    
}
