/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.breakoutegypt.domain.shapes.bricks;

import com.breakoutegypt.domain.powers.PowerUp;
import com.breakoutegypt.domain.powers.PowerUpType;
import com.breakoutegypt.domain.effects.Effect;
import com.breakoutegypt.domain.effects.ExplosiveEffect;
import com.breakoutegypt.domain.effects.ToggleEffect;
import com.breakoutegypt.domain.powers.PowerDown;
import com.breakoutegypt.domain.shapes.BodyConfiguration;
import com.breakoutegypt.domain.shapes.BodyConfigurationFactory;
import com.breakoutegypt.domain.shapes.RegularBody;
import com.breakoutegypt.domain.shapes.ShapeDimension;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import javax.json.JsonObjectBuilder;

/**
 *
 * @author kevin
 */
public class Brick extends RegularBody {
    
    private static AtomicInteger brickIdentifier = new AtomicInteger(1);
    private int brickId=0;
    private boolean isTarget;

    private boolean isVisibible;
    private boolean isBreakable;
    private boolean hasPowerUp;

    private String brickTypeName;

    private List<Effect> effects;
    private PowerUpType poweruptype;
    private PowerUp powerup;
    private PowerDown powerdown;
    private boolean isInverted;
    private boolean isSquare;
//    private int points = 2000;
    public Brick(ShapeDimension s) {
        this(s, false);
    }
    
    public Brick(ShapeDimension s, boolean isTarget){
        this(s, isTarget, true);
    }

    public Brick(ShapeDimension s, boolean isTarget, boolean isVisible) {
        this(s, isTarget, isVisible, true);
    }
    
    public Brick(ShapeDimension s, boolean isTarget, boolean isVisible, boolean isBreakable) {
        this(s, isTarget, isVisible, isBreakable, false);
    }

    public Brick(ShapeDimension s, boolean isTarget, boolean isVisible, boolean isBreakable, boolean isInverted) {
        super(s);
        s.setName("Brick"+brickIdentifier.getAndIncrement());
        this.isVisibible = isVisible;
        this.isBreakable = isBreakable;
        this.brickTypeName = new BrickType("REGULAR").getName();
        this.isTarget = isTarget;
        this.isInverted = isInverted;

        effects = new ArrayList();

        if (isBreakable) {
            effects.add(new ExplosiveEffect(this, 0));
        }

    }

    public void setBreakable(boolean b) {
        isBreakable = b;
    }

    public void toggle() {
        isVisibible = !isVisibible;
        if (!isVisibible) {
            this.getBody().getFixtureList().m_filter.maskBits = 0;
        } else {
            this.getBody().getFixtureList().m_filter.maskBits = 0x0010;
        }
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

    public boolean isBreakable() {
        return isBreakable;
    }

    public boolean isIsSquare() {
        return isSquare;
    }

    public void setIsSquare(boolean isSquare) {
        this.isSquare = isSquare;
    }

    public JsonObjectBuilder toJson() {
        JsonObjectBuilder builder = super.toJson();

        builder.add("show", isVisibible);
        builder.add("isTarget", isTarget());
        if (hasToggleEffect()) {
            builder.add("effect", "toggle");
        } else if (getExplosiveEffect() != null) {
            builder.add("effect", "explosive");
        } else {
            builder.add("effect", "");
        }
        builder.add("isBreakable", isBreakable);
        builder.add("isInverted", isInverted);
        builder.add("isSquare", isSquare);
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
        this.brickTypeName = brickType.getName();
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

    public void setPowerdown(PowerDown powerdown) {
        this.powerdown = powerdown;
    }

    public boolean hasPowerDown() {
        return powerdown != null;
    }

    public PowerDown getPowerDown() {
        return powerdown;
    }

    public boolean isRegular() {
        if (effects.size() == 1 && !isTarget) {
            Effect e = effects.get(0);
            if (e instanceof ExplosiveEffect) {
                ExplosiveEffect explosive = (ExplosiveEffect) e;
                if (explosive.getRadius() == 0) {
                    return true;
                }
            }
        }
        return false;
    }

    @Override
    public BodyConfiguration getConfig() {
        BodyConfigurationFactory factory = BodyConfigurationFactory.getInstance();
        BodyConfiguration brickBody;
        if(!isSquare){
            brickBody = factory.createTriangleConfig(this.dimension, isInverted);
        } else {
            brickBody = factory.createSquareConfig(this.dimension);
        }
        
        if (!this.isVisibible) {
            brickBody.getFixtureConfig().setMaskBits(0);
        }

        this.config = brickBody;
        
        return config;
    }

    public int getBrickId() {
        return this.brickId;
    }
    
    public void setBrickId(int brickId){
        this.brickId=brickId;
    }
    
    public boolean isInverted(){
        return isInverted;
    }
}
