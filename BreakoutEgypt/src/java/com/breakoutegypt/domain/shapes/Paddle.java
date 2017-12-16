/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.breakoutegypt.domain.shapes;

/**
 *
 * @author kevin
 */
public class Paddle extends RegularBody{
   
    public Paddle(ShapeDimension s){
        super(s);
        
        BodyConfiguration domePaddleConfig = new BodyConfigurationFactory().createDomePaddleConfig(s);
        setBox2dConfig(domePaddleConfig);
    }
    
}

