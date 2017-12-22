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

    private int levelId;
    private int levelNumber;
    private Player player;
    private long timeScore;
    private String difficulty;
    private int brickScore;
    private int scoreId;

    public Score(int id, int level, int levelNumber, Player player, long score, String difficulty, int brickScore) {
        this.levelId = level;
        this.levelNumber = levelNumber;
        this.scoreId = id;
        this.player = player;
        this.timeScore = score;
        this.difficulty = difficulty;
        this.brickScore = brickScore;
    }

    public int getScoreId() {
        return scoreId;
    }

    public void setScoreId(int scoreId) {
        this.scoreId = scoreId;
    }

    public String getUser() {
        return player.getUsername();
    }
    
    public int getUserId(){
        return player.getUserId();
    }
    
    public long getTimeScore() {
        return timeScore;
    }

    public int getLevelId() {
        return levelId;
    }

    public int getLevelNumber() {
        return levelNumber;
    }
    
    

    public String getDifficulty() {
        return difficulty;
    }
    
    public int getBrickScore () { return brickScore; }

    @Override
    public int compareTo(Score other) {
        if (this.timeScore < other.timeScore) {
            return -1;
        } else if (this.timeScore == other.timeScore) {
            return 0;
        } else {
            return 1;
        }
    }

    @Override
    public String toString() {
        return "Score{" + "level=" + levelId + ", user=" + player.getUsername() + ", score=" + timeScore + "}\n";
    }

    public JsonObject toJson() {
        JsonObjectBuilder scoreObjectBuilder = Json.createObjectBuilder();
        scoreObjectBuilder.add("username", this.getUser());
        scoreObjectBuilder.add("score", this.getTimeScore());
        scoreObjectBuilder.add("difficulty", this.getDifficulty());
        return scoreObjectBuilder.build();
    }

}
