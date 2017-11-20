/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.breakoutegypt.domain.shapes.bricks;

import com.breakoutegypt.domain.shapes.ShapeDimension;
import java.awt.Point;
import java.util.List;
import javax.json.JsonObjectBuilder;

/**
 *
 * @author kevin
 */
public class SwitchBrick extends Brick{
    private List<Brick> switchBricks;

 
    public SwitchBrick(ShapeDimension s, Point gridPosition) {
        this( s, gridPosition, false, true);
    }
    
    public SwitchBrick(ShapeDimension s, Point gridPosition, boolean isTarget, boolean isVisible) {
        super(s, gridPosition, isTarget, isVisible);
        this.switchBricks = switchBricks;
        setBrickTypeName("SWITCH");
    }
    
    public void setSwitchBricks(List<Brick> bricks) {
        this.switchBricks = bricks;
    }

    public List<Brick> getSwitchBricks() {
        return switchBricks;
    }
    
}
