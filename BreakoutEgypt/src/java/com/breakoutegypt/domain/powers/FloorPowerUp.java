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

    public FloorPowerUp(ShapeDimension s, int timeVisible) {
        super(s);
        this.timeVisible = timeVisible;
        isVisible = false;
        startTime = this.timeVisible;
        BodyConfiguration floorConfig = new BodyConfigurationFactory().createWallConfig(s, false);
        this.setBox2dConfig(floorConfig);
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

    @Override
    public JsonObject toJson() {
        JsonObjectBuilder job = Json.createObjectBuilder();
        ShapeDimension shape = this.getShape();
        job.add("x", shape.getPosX());
        job.add("y", shape.getPosY());
        job.add("width", shape.getWidth());
        job.add("height", shape.getHeight());
        job.add("powerupname", getName());
        return job.build();
    }

    @Override
    public String getName() {
        return super.getName();
    }

    @Override
    public PowerUpMessageType getType() {
        return PowerUpMessageType.ADDFLOOR;
    }

    void addTime(int othersTimeVisible) {
        this.timeVisible += othersTimeVisible;
    }
}
