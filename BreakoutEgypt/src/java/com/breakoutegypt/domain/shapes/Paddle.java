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
    private int paddleId=0;
            
    public Paddle(ShapeDimension s){
        super(s);  
    }

    public void setPaddleId(int paddleId) {
        this.paddleId=paddleId;
    }    

    @Override
    public BodyConfiguration getConfig() {
        
        BodyConfiguration domePaddleConfig = BodyConfigurationFactory.getInstance().createDomePaddleConfig(this.dimension);
        setBox2dConfig(domePaddleConfig);
        
        return config;
    }
    
    public int getPaddleId(){
        return this.paddleId;
    }
    
   
}

