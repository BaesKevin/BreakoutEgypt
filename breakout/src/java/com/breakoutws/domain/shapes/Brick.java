/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.breakoutws.domain.shapes;

import java.awt.Point;
import java.util.List;

/**
 *
 * @author kevin
 */
public class Brick  extends RegularBody{
    private BrickType brickType;
    private boolean isTarget;
    private Point gridPosition;
    private List<Brick> switchBricks;
    private boolean isSwitched;
    
    public Brick(Shape s,  BrickType type, Point position){
        this(s, type,position, false, true);
    }
    
    public Brick(Shape s, BrickType type,  Point gridPosition,boolean isTarget, boolean isSwitched){
        super(s);
        this.gridPosition = gridPosition;
        this.brickType = type;
        this.isSwitched = isSwitched;
    }
    
    public void setSwitchBricks(List<Brick> bricks) {
        this.switchBricks = bricks;
    }

    public List<Brick> getSwitchBricks() {
        return switchBricks;
    }
    
    public void toggle() {
        isSwitched = !isSwitched;
    }
    
    public void setSwitched(boolean b) {
        isSwitched = b;
    }
    
    public boolean isSwitched() {
        return isSwitched;
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
    
//    public void accept(ShapeUser u){
//        u.doForBrick(this);
//    }
    
    
}
