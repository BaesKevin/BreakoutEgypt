/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.breakoutegypt.domain.shapes.bricks;

import com.breakoutegypt.domain.shapes.ShapeDimension;
import java.awt.Point;
import javax.json.JsonObjectBuilder;

/**
 *
 * @author kevin
 */
public class ExplosiveBrick extends Brick {

    private int explosionRadius;

    public ExplosiveBrick(ShapeDimension s, Point gridPosition) {
        this(s, gridPosition, false, true);
    }

    public ExplosiveBrick(ShapeDimension s, Point gridPosition, boolean isTarget, boolean isVisible) {
        this(s, gridPosition, isTarget, isVisible, 0);
    }

    public ExplosiveBrick(ShapeDimension s, Point gridPosition, boolean isTarget, boolean isVisible, int explosionRadius) {
        super(s, gridPosition, isTarget, isVisible);
        this.explosionRadius = explosionRadius;
        setBrickTypeName("EXPLOSIVE");
    }

    public int getExplosionRadius() {
        return explosionRadius;
    }

    public void setExplosionRadius(int radius) {
        this.explosionRadius = radius;
    }
}
