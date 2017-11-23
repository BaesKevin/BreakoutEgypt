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
        actionObjectBuilder.add("action", getMessageType().name().toLowerCase());
        actionObjectBuilder.add("name", getName());
        return actionObjectBuilder;
    }
}
