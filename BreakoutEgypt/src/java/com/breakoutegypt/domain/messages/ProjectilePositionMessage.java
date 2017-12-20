/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.breakoutegypt.domain.messages;

import com.breakoutegypt.domain.shapes.Projectile;
import javax.json.Json;
import javax.json.JsonObjectBuilder;

/**
 *
 * @author BenDB
 */
public class ProjectilePositionMessage extends GenericMessage {

    private Projectile projectile;

    public ProjectilePositionMessage(Projectile projectile) {
        this(projectile, PowerDownMessageType.PROJECTILEPOSITION);
    }

    public ProjectilePositionMessage(Projectile projectile, PowerDownMessageType powerDownMessageType) {
        super(0, projectile.getName(), powerDownMessageType);
        this.projectile = projectile;
    }
    
    @Override
    public JsonObjectBuilder toJson() {
        JsonObjectBuilder job = super.toJson();
        job.add("projectile", getName());
        job.add("x", projectile.getPosition().x);
        job.add("y", projectile.getPosition().y);
        job.add("powerdownaction", getMessageType().toString());
        return job;
    }
    
}
