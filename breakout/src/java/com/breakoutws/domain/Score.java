/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.breakoutws.domain;

/**
 *
 * @author BenDB
 */
public class Score implements Comparable<Score> {
    
    private int level;
    private User user;
    private long score;
    
    public Score (int level, User user, long score) {   
        this.level = level;
        this.user = user;
        this.score = score;   
    }

    public User getUser() {
        return user;
    }

    public long getScore() {
        return score;
    }
    
    public int getLevel() {
        return level;
    }

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
        return "Score{" + "level=" + level + ", user=" + user.getName() + ", score=" + score + "}\n";
    }
    
}
