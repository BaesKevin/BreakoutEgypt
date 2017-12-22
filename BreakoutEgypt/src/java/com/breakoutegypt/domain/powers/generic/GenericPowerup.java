/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.breakoutegypt.domain.powers.generic;

import com.breakoutegypt.domain.messages.PowerUpMessageType;
import com.breakoutegypt.domain.powers.PowerUp;
import com.breakoutegypt.domain.shapes.RegularBody;
import java.util.concurrent.atomic.AtomicInteger;
import javax.json.Json;
import javax.json.JsonObjectBuilder;

/**
 *
 * @author kevin
 */
public abstract class GenericPowerup implements PowerUp {
    private static AtomicInteger ai = new AtomicInteger(1);
            
    private int timeVisible;
    private int startTime;

    private String name;
    private RegularBody baseBody;
    private int width;
    private int height;
    private int originalWidth;
    private int originalHeight;
    
    private int playerId;

    protected final String BALLTYPE = "ball";
    protected final String BRICKTYPE = "brick";
    protected final String PADDLETYPE = "paddle";

    public GenericPowerup(String name, RegularBody baseBody, int width, int height, int timeVisible) {
        this.baseBody = baseBody;
        this.width = width;
        this.height = height;
        this.originalWidth = baseBody.getWidth();
        this.originalHeight = baseBody.getHeight();
        this.name = name + ai.getAndIncrement();
        
        this.timeVisible = timeVisible;
        startTime = timeVisible;
    }

    public RegularBody getBaseBody() {
        return baseBody;
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public int getOriginalWidth() {
        return originalWidth;
    }

    public int getOriginalHeight() {
        return originalHeight;
    }

    @Override
    public JsonObjectBuilder toJson() {
        JsonObjectBuilder job = Json.createObjectBuilder();
        job.add("name", name);
        job.add("bodyname", this.baseBody.getName());
        job.add("width", getWidth());
        job.add("height", getHeight());
        job.add("originalWidth", originalWidth);
        job.add("originalHeight", originalHeight);
        return job;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public PowerUpMessageType getType() {
        return PowerUpMessageType.GENERICPOWERUP;
    }

    @Override
    public void setPlayerId(int i) {
        this.playerId = i;
    }

    @Override
    public int getPlayerId() {
        return playerId;
    }

    public void addTime(int othersTime) {
        this.timeVisible += othersTime;
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

}
