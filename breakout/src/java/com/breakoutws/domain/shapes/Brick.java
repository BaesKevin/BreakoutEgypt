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
public class Brick  extends RegularBody{
    private BrickType brickType;
    private boolean isTarget;
    
    public Brick(Shape s, BrickType type){
        this(s, type, false);
    }
    
    public Brick(Shape s, BrickType type, boolean isTarget){
        super(s);
        this.brickType = type;
    }

    public BrickType getBricktype() {
        return brickType;
    }

    public void setType(BrickType type) {
        this.brickType = type;
    }

    public boolean isTarget() {
        return isTarget;
    }

    public void setIsTarget(boolean isTarget) {
        this.isTarget = isTarget;
    }
    
    
}
