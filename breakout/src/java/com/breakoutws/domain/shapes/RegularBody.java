/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.breakoutws.domain.shapes;

/**
 *
 * @author kevin
 */
public class RegularBody implements IShape {

       private Shape shape;
    private BodyType bodyType;
    
    public RegularBody(Shape s){
        this.bodyType = BodyType.REGULAR;
        this.shape = s;
    }
    
    @Override
    public String getName(){
        return shape.getName();
    }
    @Override
    public BodyType getBodyType() {
        return bodyType;
    }

    public Shape getShape() {
        return shape;
    }

    public void setShape(Shape shape) {
        this.shape = shape;
    }
    
}
