/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.breakoutegypt.domain.messages;

import com.breakoutegypt.domain.shapes.Ball;
import java.util.Objects;
import javax.json.Json;
import javax.json.JsonObjectBuilder;

/**
 *
 * @author BenDB
 */
public class BallMessage implements Message {

    private String name;
    private BallMessageType messageType;
    private Ball ball;

    public BallMessage(String name, BallMessageType type) {
        this.name = name;
        this.messageType = type;
    }

    public BallMessage(Ball b, BallMessageType type) {
        this.name = b.getName();
        this.messageType = type;
        this.ball = b;
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
        actionObjectBuilder.add("ballaction", getMessageType().name().toLowerCase());
        if (ball != null) {
            actionObjectBuilder.add("name", getName());
            actionObjectBuilder.add("x", ball.getShape().getPosX());
            actionObjectBuilder.add("y", ball.getShape().getPosY());
            actionObjectBuilder.add("width", ball.getShape().getWidth());
            actionObjectBuilder.add("height", ball.getShape().getHeight());
        } else {
            actionObjectBuilder.add("ball", getName());
        }

        return actionObjectBuilder;
    }

    @Override
    public String toString() {
        return "BallMessage{" + "name=" + name + ", messageType=" + messageType + '}';
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
        final BallMessage other = (BallMessage) obj;
        if (!Objects.equals(this.name, other.name)) {
            return false;
        }
        if (this.messageType != other.messageType) {
            return false;
        }
        return true;
    }

}
