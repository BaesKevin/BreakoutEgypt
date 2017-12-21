/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.breakoutegypt.domain.powers.generic;

import com.breakoutegypt.domain.messages.PowerUpMessage;
import com.breakoutegypt.domain.messages.PowerUpMessageType;
import com.breakoutegypt.domain.powers.PowerUp;
import com.breakoutegypt.domain.powers.PowerUpHandler;
import com.breakoutegypt.domain.shapes.RegularBody;
import javax.json.Json;
import javax.json.JsonObjectBuilder;

/**
 *
 * @author kevin
 */
public abstract class GenericPowerup implements PowerUp{

    private RegularBody baseBody;
    private int width;
    private int height;
    private int playerId;
    
    protected final String BALLTYPE = "ball";
    protected final String BRICKTYPE = "brick";
    protected final String PADDLETYPE = "paddle";

    public GenericPowerup(RegularBody baseBody, int width, int height) {
        this.baseBody = baseBody;
        this.width = width;
        this.height = height;
    }
    
    public RegularBody getBaseBody() { return baseBody; }

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
    


    @Override
    public JsonObjectBuilder toJson() {
        JsonObjectBuilder job = Json.createObjectBuilder();
        job.add("powerupname", baseBody.getName());
        job.add("width", getWidth());
        job.add("height", getHeight());
        return job;
    }

    @Override
    public String getName() {
        return baseBody.getName();
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
    
}
