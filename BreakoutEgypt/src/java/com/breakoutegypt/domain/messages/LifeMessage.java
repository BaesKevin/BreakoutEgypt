/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.breakoutegypt.domain.messages;

import java.util.Objects;
import javax.json.Json;
import javax.json.JsonObjectBuilder;

/**
 *
 * @author BenDB
 */
public class LifeMessage implements Message {

    private String playerName;
    private int livesLeft;
    private LifeMessageType gameOver;

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

    @Override
    public int hashCode() {
        int hash = 3;
        return hash;
    }

    @Override
    public String toString() {
        return "LifeMessage{" + "name=" + playerName + ", livesLeft=" + livesLeft + ", lifeaction=" + gameOver + '}';
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final LifeMessage other = (LifeMessage) obj;
        if (this.livesLeft != other.livesLeft) {
            return false;
        }
        if (!Objects.equals(this.playerName, other.playerName)) {
            return false;
        }
        if (this.gameOver != other.gameOver) {
            return false;
        }
        return true;
    }

}
