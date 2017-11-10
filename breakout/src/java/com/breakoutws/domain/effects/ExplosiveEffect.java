/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.breakoutws.domain.effects;

import com.breakoutws.domain.Level;
import com.breakoutws.domain.shapes.Brick;
import java.util.List;

/**
 *
 * @author kevin
 */
public class ExplosiveEffect {
    private int radius;
    private Brick centreBrick;
    
    public ExplosiveEffect(Brick centreBrick, int range){
        this.radius = range;
        this.centreBrick = centreBrick;
    }

    public int getRadius() {
        return radius;
    }

    public Brick getCentreBrick() {
        return centreBrick;
    }
    
    
}
