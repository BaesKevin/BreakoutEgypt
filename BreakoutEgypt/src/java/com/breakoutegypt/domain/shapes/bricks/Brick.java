/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.breakoutegypt.domain.shapes.bricks;

import com.breakoutegypt.domain.effects.BrokenPaddlePowerUp;
import com.breakoutegypt.domain.effects.PowerUp;
import com.breakoutegypt.domain.effects.PowerUpType;
import com.breakoutegypt.domain.effects.Effect;
import com.breakoutegypt.domain.effects.ExplosiveEffect;
import com.breakoutegypt.domain.effects.FloorPowerUp;
import com.breakoutegypt.domain.effects.ToggleEffect;
import com.breakoutegypt.domain.shapes.Paddle;
import com.breakoutegypt.domain.shapes.RegularBody;
import com.breakoutegypt.domain.shapes.ShapeDimension;
import java.awt.Point;
import java.util.ArrayList;
import java.util.List;
import javax.json.JsonObjectBuilder;

/**
 *
 * @author kevin
 */
public class Brick extends RegularBody {

    private boolean isTarget;
    private Point gridPosition;

    private boolean isVisibible;
    private boolean isBreakable;
    private boolean hasPowerUp;

    private String brickTypeName;

    private List<Effect> effects;
    private PowerUpType poweruptype;
    private PowerUp powerup;

//    private int points = 2000;
    
    public Brick(ShapeDimension s, Point position) {
        this(s, position, false, true);
    }

    public Brick(ShapeDimension s, Point gridPosition, boolean isTarget, boolean isVisible) {
        this(s, gridPosition, isTarget, isVisible, true);
    }

//    public Brick(ShapeDimension s, Point gridPosition, boolean isTarget, boolean isVisible, boolean isBreakable) {
//        this(s, gridPosition, isTarget, isVisible, isBreakable, 2000);
//    }
    
    public Brick(ShapeDimension s, Point gridPosition, boolean isTarget, boolean isVisible, boolean isBreakable/*, int points*/) {
        super(s);
        this.gridPosition = gridPosition;
        this.isVisibible = isVisible;
        this.isBreakable = isBreakable;
        this.brickTypeName = BrickType.REGULAR.name();
        this.isTarget = isTarget;

        effects = new ArrayList();

        if (isVisible && isBreakable) {
            effects.add(new ExplosiveEffect(this, 0));
        }
//        this.points = points;
    }
        

    public void setBreakable(boolean b) {
        isBreakable = b;
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

    public boolean isBreakable() {
        return isBreakable;
    }

    protected void setBrickTypeName(String name) {
        this.brickTypeName = name;
    }

    protected String getBrickTypeName() {
        return brickTypeName;
    }

    public JsonObjectBuilder toJson() {
        JsonObjectBuilder builder = getShape().toJson();

        builder.add("show", isVisibible);
        builder.add("type", getBrickTypeName());
        builder.add("isTarget", isTarget());
        if (hasToggleEffect()) {
            builder.add("effect", "toggle");
        } else if (getExplosiveEffect() != null) {
            builder.add("effect", "explosive");
        } else {
            builder.add("effect", "");
        }
        builder.add("isBreakable", isBreakable);
        return builder;
    }

    public ExplosiveEffect getExplosiveEffect() {
        List<Effect> effects = getEffects();
        for (Effect e : effects) {
            if (e instanceof ExplosiveEffect) {
                if (((ExplosiveEffect) e).getRadius() > 0) {
                    return (ExplosiveEffect) e;
                }
            }
        }
        return null;
    }

    public boolean hasToggleEffect() {
        List<Effect> effects = getEffects();
        boolean hasSwitch = false;

        for (Effect e : effects) {
            if (e instanceof ToggleEffect) {
                hasSwitch = true;
                break;
            }
        }
        return hasSwitch;
    }

    public void addEffect(Effect effect) {
        this.effects.add(effect);
    }

    public List<Effect> getEffects() {
        return effects; // TODO unmodifyable
    }

    public void setType(BrickType brickType) {
        this.brickTypeName = brickType.name();
    }

    public String getType() {
        return brickTypeName;
    }

    public void setPowerUp(PowerUp powerup) {
        this.powerup = powerup;

        hasPowerUp = true;
    }

    public boolean hasPowerUp() {
        return hasPowerUp;
    }

    public PowerUpType getPowerUpType() {
        return poweruptype;
    }

    public PowerUp getPowerUp() {
        return powerup;
    }
    
//    public void setPoints(int points) {
//        this.points = points;
//    }
//
//    public int getPoints() {
//        return points;
//    }
}
