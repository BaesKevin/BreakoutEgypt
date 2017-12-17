/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.breakoutegypt.domain;

import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonObjectBuilder;

/**
 *
 * @author BenDB
 */
public class Score implements Comparable<Score> {

    private int level;
    private User user;
    private long score;
    private String difficulty;
    private int brickScore;

    public Score(int level, User user, long score, String difficulty, int brickScore) {
        this.level = level;
        this.user = user;
        this.score = score;
        this.difficulty = difficulty;
        this.brickScore = brickScore;
    }

    public String getUser() {
        return user.getUsername();
    }
    
    public int getUserId(){
        return user.getUserId();
    }
    
    public long getScore() {
        return score;
    }

    public int getLevel() {
        return level;
    }

    public String getDifficulty() {
        return difficulty;
    }
    
    public int getBrickScore () { return brickScore; }

    @Override
    public int compareTo(Score other) {
        if (this.score < other.score) {
            return -1;
        } else if (this.score == other.score) {
            return 0;
        } else {
            return 1;
        }
    }

    @Override
    public String toString() {
        return "Score{" + "level=" + level + ", user=" + user.getUsername() + ", score=" + score + "}\n";
    }

    public JsonObject toJson() {
        JsonObjectBuilder scoreObjectBuilder = Json.createObjectBuilder();
        scoreObjectBuilder.add("username", this.getUser());
        scoreObjectBuilder.add("score", this.getScore());
        scoreObjectBuilder.add("difficulty", this.getDifficulty());
        return scoreObjectBuilder.build();
    }

}
