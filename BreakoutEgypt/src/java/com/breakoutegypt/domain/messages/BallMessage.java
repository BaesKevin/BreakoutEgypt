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
public class BallMessage extends GenericMessage {
    private Ball ball;

    public BallMessage(Ball b, BallMessageType type) {
        super(0, b.getName(), type);
        this.ball = b;
    }

    @Override
    public JsonObjectBuilder toJson() {
        JsonObjectBuilder actionObjectBuilder = super.toJson();
        actionObjectBuilder.add("ballaction", getMessageType().name().toLowerCase());
        if (getMessageType() != BallMessageType.REMOVE) {
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
        return "BallMessage{" + "name=" + ball.getName() + ", messageType=" + getMessageType() + '}';
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
        if (!Objects.equals(ball.getName(), ball.getName())) {
            return false;
        }
        if (this.getMessageType() != other.getMessageType()) {
            return false;
        }
        return true;
    }

}
