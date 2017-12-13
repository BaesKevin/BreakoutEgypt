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

    private boolean isVisable;
    private int timeVisable = 1800; //TODO variable time in seconds (not steps)
    private int startTime;
    private String name;

    public FloorPowerUp(ShapeDimension s) {
        super(s);
        isVisable = false;
        startTime = timeVisable;
        BodyConfiguration floorConfig = new BodyConfigurationFactory().createWallConfig(s, false);
        this.setBox2dConfig(floorConfig);
    }

    public boolean isVisable() {
        return isVisable;
    }

    public void setIsVisable(boolean isVisable) {
        this.isVisable = isVisable;
    }

    public void setTimeVisable(int time) {
        this.timeVisable = time;
    }

    public int getTimeVisable() {
        return timeVisable;
    }
    
    public void resetTime() {
        timeVisable = startTime;
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

    void addTime(int othersTimeVisable) {
        this.timeVisable += othersTimeVisable;
    }
}
