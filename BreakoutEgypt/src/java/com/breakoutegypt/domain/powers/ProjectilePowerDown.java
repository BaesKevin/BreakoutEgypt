/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.breakoutegypt.domain.powers;

import com.breakoutegypt.domain.messages.PowerDownMessage;
import com.breakoutegypt.domain.messages.PowerDownMessageType;
import com.breakoutegypt.domain.shapes.BodyConfiguration;
import com.breakoutegypt.domain.shapes.BodyConfigurationFactory;
import com.breakoutegypt.domain.shapes.Projectile;
import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonObjectBuilder;
import org.jbox2d.common.Vec2;

/**
 *
 * @author BenDB
 */
public class ProjectilePowerDown implements PowerDown {

    private Projectile projectile;
    private String name;
    private int playerId;

    public ProjectilePowerDown(Projectile projectile) {
        this.name = projectile.getName();
        this.projectile = projectile;
//        BodyConfiguration projectileBodyConfig = new BodyConfigurationFactory().createProjectileConfig(this.projectile.getShape());
//        this.projectile.setBox2dConfig(projectileBodyConfig);
        
        this.playerId = 1;
    }
    
    public void startProjectile(float endX, float endY, int width) {
        float startX = this.projectile.getPosition().x;
        float startY = this.projectile.getPosition().y;
        float velocityX = endX - startX;
        float velocityY = endY - startY;
        this.projectile.getBody().setLinearVelocity(new Vec2(velocityX*0.5f, velocityY*0.5f));
    }

    @Override
    public PowerDownMessage accept(PowerDownHandler puh) {
        return puh.handle(this);
    }

    @Override
    public JsonObject toJson() {
        JsonObjectBuilder job = Json.createObjectBuilder();
        job.add("powerdownname", getName());
        job.add("powerdown", this.projectile.toJson().build());
        job.add("powerdowntype", getType().name());

        return job.build();
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public PowerDownMessageType getType() {
        return PowerDownMessageType.PROJECTILE;
    }

    public Projectile getProjectile() {
        return projectile;
    }
    
     @Override
    public int getPlayerId() {
        return playerId;
    }

    @Override
    public void setPlayerId(int i) {
        this.playerId = i;
    }

}
