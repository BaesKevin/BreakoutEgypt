/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.breakoutegypt.domain.effects;

import com.breakoutegypt.domain.shapes.bricks.Brick;
import java.util.List;

/**
 *
 * @author kevin
 */
public class ToggleEffect implements Effect{
    private List<Brick> bricksToToggle;
    
    public ToggleEffect(List<Brick> bricksToToggle){
        this.bricksToToggle = bricksToToggle;
    }

    public List<Brick> getBricksToToggle() {
        return bricksToToggle;
    }

    @Override
    public void accept(EffectHandler eh) {
        eh.handle(this);
    }
    
    
}
