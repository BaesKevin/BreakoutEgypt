/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.breakoutws.domain.brickcollisionhandlers;

import com.breakoutws.domain.Level;
import com.breakoutws.domain.effects.ExplosiveEffect;
import com.breakoutws.domain.shapes.Brick;

/**
 *
 * @author kevin
 */
public class RegularCollision {
    private Level level;
    private Brick brick;

    public RegularCollision(Level level, Brick brick) {
        this.level = level;
        this.brick = brick;
    }
    
    public void handleCollsion(){
        System.out.println("RegularCollision: Handling regular collision");
        level.handleExplosiveEffect(new ExplosiveEffect(brick, 0));
    }
}
