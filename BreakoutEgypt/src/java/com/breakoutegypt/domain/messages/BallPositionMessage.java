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
public class BallPositionMessage extends GenericMessage {

    private final float x;
    private final float y;
    
    public BallPositionMessage(Ball b) {
        super(0, b.getName(), BallMessageType.POSITION);
        this.x = b.getPosition().x;
        this.y = b.getPosition().y;
    }

    @Override
    public JsonObjectBuilder toJson() {
        JsonObjectBuilder job = super.toJson();
        job.add("ball", getName());
        job.add("x", x);
        job.add("y", y);
        return job;
    }

    @Override
    public String toString() {
        return "BallPositionMessage{" + "ballName=" + getName() + ", x=" + x + ", y=" + y + ", messageType=" + getMessageType() + '}';
    }

}
