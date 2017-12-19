/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.breakoutegypt.domain.messages;

import com.breakoutegypt.domain.shapes.Paddle;
import javax.json.JsonObjectBuilder;

/**
 *
 * @author kevin
 */
public class PaddlePositionMessage extends GenericMessage {

    private Paddle paddle;
    
    public PaddlePositionMessage(Paddle paddle) {
        super(0, paddle.getName(), PaddleMessageType.POSITION);
        this.paddle = paddle;
    }

    @Override
    public JsonObjectBuilder toJson() {
        JsonObjectBuilder job = super.toJson();
        job.add("name", paddle.getName());
        job.add("x", paddle.getX());
        job.add("y", paddle.getY());
        return job;
    }
}