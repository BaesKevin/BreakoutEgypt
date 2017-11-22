/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.breakoutegypt.domain.actionmessages;

import javax.json.Json;
import javax.json.JsonObjectBuilder;

/**
 *
 * @author BenDB
 */
public class BallMessage implements Message {

    private String name;
    private BallMessageType messageType;

    public BallMessage(String name, BallMessageType type) {
        this.name = name;
        this.messageType = type;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public BallMessageType getMessageType() {
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
