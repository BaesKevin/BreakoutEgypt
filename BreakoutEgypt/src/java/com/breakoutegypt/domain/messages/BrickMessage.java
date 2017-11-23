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
 * @author kevin
 */
public class BrickMessage implements Message {

    private String name;
    private BrickMessageType messageType;

    public BrickMessage(String name, BrickMessageType messageType) {
        this.name = name;
        this.messageType = messageType;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public BrickMessageType getMessageType() {
        return messageType;
    }

    @Override
    public JsonObjectBuilder toJson() {
        JsonObjectBuilder actionObjectBuilder = Json.createObjectBuilder();
        actionObjectBuilder.add("brickaction", getMessageType().name().toLowerCase());
        actionObjectBuilder.add("name", getName());
        return actionObjectBuilder;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        return hash;
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
        final BrickMessage other = (BrickMessage) obj;
        if (!Objects.equals(this.name, other.name)) {
            return false;
        }
        if (this.messageType != other.messageType) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "BrickMessage{" + "name=" + name + ", messageType=" + messageType + '}';
    }
}
