/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.breakoutegypt.domain.powers;

import com.breakoutegypt.domain.messages.PowerUpMessage;
import com.breakoutegypt.domain.messages.PowerUpMessageType;
import com.breakoutegypt.domain.shapes.BodyConfiguration;
import com.breakoutegypt.domain.shapes.BodyConfigurationFactory;
import com.breakoutegypt.domain.shapes.RegularBody;
import com.breakoutegypt.domain.shapes.ShapeDimension;
import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonObjectBuilder;

/**
 *
 * @author BenDB
 */
public class FloorPowerUp extends RegularBody implements PowerUp {

    private boolean isVisible;
    private int timeVisible;
    private int startTime;
    private String name;
    private int playerId;

    public FloorPowerUp(ShapeDimension s, int timeVisible) {
        super(s);
        this.timeVisible = timeVisible;
        isVisible = false;
        startTime = timeVisible;
        this.name = s.getName();
        this.playerId = 1;
    }

    public boolean isVisible() {
        return isVisible;
    }

    public void setIsVisible(boolean isVisible) {
        this.isVisible = isVisible;
    }

    public void setTimeVisible(int time) {
        this.timeVisible = time;
    }

    public int getTimeVisible() {
        return timeVisible;
    }

    public void resetTime() {
        timeVisible = startTime;
    }

    @Override
    public PowerUpMessage accept(PowerUpHandler puh) {
        return puh.handleFloorPowerUp(this);
    }

    public JsonObjectBuilder toJson() {
        JsonObjectBuilder job = Json.createObjectBuilder();
//        ShapeDimension shape = this.getShape();
        job.add("x", getX());
        job.add("y", getY());
        job.add("width", getWidth());
        job.add("height", getHeight());
        job.add("powerupname", getName());
        return job;
    }

    @Override
    public String getName() {
        return this.name;
    }

    @Override
    public PowerUpMessageType getType() {
        return PowerUpMessageType.ADDFLOOR;
    }

    void addTime(int othersTimeVisible) {
        this.timeVisible += othersTimeVisible;
    }

    @Override
    public void setPlayerId(int i) {
        this.playerId = i;
    }

    @Override
    public int getPlayerId() {
        return this.playerId;
    }

    @Override
    public BodyConfiguration getConfig() {
        BodyConfiguration floorConfig = BodyConfigurationFactory.getInstance().createFloorConfig(this.dimension);
        setBox2dConfig(floorConfig);
        
        return config;
    }
}
