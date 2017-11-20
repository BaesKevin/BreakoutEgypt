/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.breakoutegypt.domain.shapes.bricks;

import com.breakoutegypt.domain.shapes.RegularBody;
import com.breakoutegypt.domain.shapes.ShapeDimension;
import java.awt.Point;
import java.util.List;
import javax.json.JsonObjectBuilder;

/**
 *
 * @author kevin
 */
public class Brick  extends RegularBody{
    private boolean isTarget;
    private Point gridPosition;
    
    private boolean isVisibible;
    private boolean isBreakable;

    private String brickTypeName;
    
    public Brick(ShapeDimension s, Point position){
        this(s,position, false, true);
    }
    
    public Brick(ShapeDimension s,  Point gridPosition,boolean isTarget, boolean isVisible){
        this(s, gridPosition, isTarget, isVisible, true);
    }
    
    public Brick(ShapeDimension s,  Point gridPosition,boolean isTarget, boolean isVisible, boolean isBreakable){
        super(s);
        this.gridPosition = gridPosition;
        this.isVisibible = isVisible;
        this.isBreakable = isBreakable;
        brickTypeName = "REGULAR";
    }
    

    
    public void toggle() {
        isVisibible = !isVisibible;
    }
    
    public void setVisible(boolean b) {
        isVisibible = b;
    }
    
    public boolean isVisible() {
        return isVisibible;
    }

    public boolean isTarget() {
        return isTarget;
    }

    public void setTarget(boolean isTarget) {
        this.isTarget = isTarget;
    }

    public Point getGridPosition() {
        return gridPosition;
    }

    public void setGridPosition(Point position) {
        this.gridPosition = position;
    }
    
    public boolean isBreakable(){
        return isBreakable;
    }
    
    protected void setBrickTypeName(String name){
        this.brickTypeName = name;
    }
    
    protected String getBrickTypeName(){
        return brickTypeName;
    }
    
    
    public JsonObjectBuilder toJson(){
        JsonObjectBuilder builder = getShape().toJson();
        
        builder.add("show", isVisibible);
        builder.add("type", getBrickTypeName());
        builder.add("isTarget", isTarget());
        
        return builder;
    }
    
//    public void accept(ShapeUser u){
//        u.doForBrick(this);
//    }
    
    
}
