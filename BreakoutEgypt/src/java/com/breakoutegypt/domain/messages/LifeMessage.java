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
public class LifeMessage extends GenericMessage {

    private int livesLeft;

    public LifeMessage(String playerName, int livesLeft, LifeMessageType gameOver) {
        super(0, playerName, gameOver);
        this.livesLeft = livesLeft;
    }

    public int getLivesLeft() {
        return livesLeft;
    }
    
    @Override
    public JsonObjectBuilder toJson() {
        JsonObjectBuilder job = super.toJson();
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
        return "LifeMessage{" + "name=" + getName() + ", livesLeft=" + livesLeft + ", lifeaction=" + getMessageType() + '}';
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
        if (!Objects.equals(this.getName(), other.getName())) {
            return false;
        }
        if (this.getMessageType() != other.getMessageType()) {
            return false;
        }
        return true;
    }

}
