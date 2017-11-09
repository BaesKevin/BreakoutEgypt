/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.breakoutws.domain.shapes;

import java.awt.Point;

/**
 *
 * @author kevin
 */
public class Brick  extends RegularBody{
    private BrickType brickType;
    private boolean isTarget;
    private Point gridPosition;
    
    public Brick(Shape s,  BrickType type, Point position){
        this(s, type,position, false);
    }
    
    public Brick(Shape s, BrickType type,  Point gridPosition,boolean isTarget){
        super(s);
        this.gridPosition = gridPosition;
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

    public BrickType getBrickType() {
        return brickType;
    }

    public void setBrickType(BrickType brickType) {
        this.brickType = brickType;
    }

    public Point getGridPosition() {
        return gridPosition;
    }

    public void setGridPosition(Point position) {
        this.gridPosition = position;
    }
    
    
    
}
