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
public class ProjectilePositionMessage implements Message {

    private String name;
    private Projectile projectile;
    private MessageType messageType;

    public ProjectilePositionMessage(Projectile projectile) {
        this.name = projectile.getName();
        this.projectile = projectile;
        this.messageType = PowerDownMessageType.PROJECTILEPOSITION;
    }

    public ProjectilePositionMessage(Projectile projectile, PowerDownMessageType powerDownMessageType) {
        this.name = projectile.getName();
        this.projectile = projectile;
        this.messageType = powerDownMessageType;
    }
    
    @Override
    public String getName() {
        return name;
    }

    @Override
    public MessageType getMessageType() {
        return messageType;
    }

    @Override
    public JsonObjectBuilder toJson() {
        JsonObjectBuilder job = Json.createObjectBuilder();
        job.add("projectile", getName());
        job.add("x", projectile.getPosition().x);
        job.add("y", projectile.getPosition().y);
        job.add("powerdownaction", getMessageType().toString());
        return job;
    }
    
}
