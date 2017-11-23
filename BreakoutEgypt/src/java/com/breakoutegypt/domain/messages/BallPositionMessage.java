/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.breakoutegypt.domain.messages;

import com.breakoutegypt.domain.shapes.Ball;
import javax.json.Json;
import javax.json.JsonObjectBuilder;

/**
 *
 * @author BenDB
 */
public class BallPositionMessage implements Message {

    private final String ballName;
    private final float x;
    private final float y;
    private final BallMessageType messageType;

    public BallPositionMessage(String ballName, float x, float y) {
        this.ballName = ballName;
        this.x = x;
        this.y = y;
        messageType = BallMessageType.POSITION;
    }

    public BallPositionMessage(Ball b) {
        this.ballName = b.getName();
        this.x = b.getPosition().x;
        this.y = b.getPosition().y;
        this.messageType = BallMessageType.POSITION;
    }

    @Override
    public String getName() {
        return ballName;
    }

    @Override
    public BallMessageType getMessageType() {
        return messageType;
    }

    @Override
    public JsonObjectBuilder toJson() {
        JsonObjectBuilder job = Json.createObjectBuilder();
        job.add("name", getName());
        job.add("x", x);
        job.add("y", y);
        return job;
    }

}
