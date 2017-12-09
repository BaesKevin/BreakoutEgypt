/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.breakoutegypt.domain.messages;

import com.breakoutegypt.domain.TimeScore;
import javax.json.Json;
import javax.json.JsonObjectBuilder;

/**
 *
 * @author BenDB
 */
public class LevelMessage implements Message {

//    job.add("levelComplete", true);
//        job.add("scoreTimer", t.getDuration());
//        job.add("isLastLevel", isLastLevel);
    
    private final String name;
    private final boolean isLastLevel;
    private final long score;
    private final LevelMessageType messageType;

    public LevelMessage(String name, boolean isLastLevel, long score, LevelMessageType messageType) {
        this.name = name;
        this.isLastLevel = isLastLevel;
        this.score = score;
        this.messageType = messageType;
    }
    
    @Override
    public String getName() {
        return name;
    }

    @Override
    public LevelMessageType getMessageType() {
        return messageType;
    }

    @Override
    public JsonObjectBuilder toJson() {
        JsonObjectBuilder job = Json.createObjectBuilder();
        job.add("name", getName());
        job.add("levelComplete", true);
        job.add("scoreTimer", score);
        job.add("isLastLevel", isLastLevel);
        return job;
    }
    
}
