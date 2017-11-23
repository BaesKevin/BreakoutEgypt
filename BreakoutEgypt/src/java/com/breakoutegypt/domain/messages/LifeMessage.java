/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.breakoutegypt.domain.messages;

import javax.json.Json;
import javax.json.JsonObjectBuilder;

/**
 *
 * @author BenDB
 */
public class LifeMessage implements Message {

    private int livesLeft;
    private LifeMessageType gameOver;
    private String playerName;

    public LifeMessage(String playerName, int livesLeft, LifeMessageType gameOver) {
        this.livesLeft = livesLeft;
        this.gameOver = gameOver;
        this.playerName = playerName;
    }

    @Override
    public String getName() {
        return playerName;
    }

    public int getLivesLeft() {
        return livesLeft;
    }

    @Override
    public LifeMessageType getMessageType() {
        return gameOver;
    }

    @Override
    public JsonObjectBuilder toJson() {
        JsonObjectBuilder job = Json.createObjectBuilder();
        job.add("name", getName());
        job.add("livesLeft", livesLeft);
        job.add("lifeaction", getMessageType().name().toLowerCase());
        return job;
    }

}
