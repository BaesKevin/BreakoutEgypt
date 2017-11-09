/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.breakoutws.domain.effects;

import com.breakoutws.domain.shapes.Brick;

/**
 *
 * @author kevin
 */
public class BrickRemoveEffect {
    private Brick brick;
    
    public BrickRemoveEffect(Brick brick){
        this.brick = brick;
    }

    public Brick getBrick() {
        return brick;
    }
}
