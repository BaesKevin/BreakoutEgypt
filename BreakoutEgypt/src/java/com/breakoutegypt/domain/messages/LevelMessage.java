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
public class LevelMessage extends GenericMessage {
    private final boolean isLastLevel;
    private final long score;
    private int brickScore;

    public LevelMessage(String name, boolean isLastLevel, long score, int brickScore, LevelMessageType messageType) {
        super(0, name, messageType);
        this.isLastLevel = isLastLevel;
        this.score = score;
        this.brickScore = brickScore;
    }
    
    @Override
    public JsonObjectBuilder toJson() {
        JsonObjectBuilder job = super.toJson();
        job.add("name", getName());
        job.add("levelComplete", true);
        job.add("scoreTimer", score);
        job.add("brickScore", brickScore);
        job.add("isLastLevel", isLastLevel);
        return job;
    }
    
}
