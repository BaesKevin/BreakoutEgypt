/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.breakoutws.data;

import com.breakoutws.domain.Score;
import com.breakoutws.domain.User;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 *
 * @author BenDB
 */
public class StaticDummyHighscoreRepo {
    
    private List<Score> scores;
    
    public StaticDummyHighscoreRepo () {
        
        scores = new ArrayList();
        
        User dummy = new User("Ben");
        
        scores.add(new Score(1, dummy, 2500));
        scores.add(new Score(1, dummy, 4548));
        scores.add(new Score(1, dummy, 2580));
        scores.add(new Score(2, dummy, 48648));
        scores.add(new Score(1, dummy, 2500));
        scores.add(new Score(1, dummy, 25448));
        scores.add(new Score(2, dummy, 25486));
        scores.add(new Score(1, dummy, 12345));
        scores.add(new Score(1, dummy, 486));
        scores.add(new Score(2, dummy, 2560));
        scores.add(new Score(1, dummy, 46));
        scores.add(new Score(2, dummy, 8686));
        scores.add(new Score(2, dummy, 486));
    }
    
    public List<Score> getScoresByLevel(int levelID) {
        
        List<Score> scoresByLevel = new ArrayList();
        
        for (Score s : scores) {
            if (s.getLevel() == levelID) {
                scoresByLevel.add(s);
            }
        }
        
        Collections.sort(scoresByLevel);
        System.out.println("Scores for Level: " + levelID);
        System.out.println(scoresByLevel);
        System.out.println("");
        return scoresByLevel;
    }
    
    public void addScore(Score s) {
        scores.add(s);
    }
    
    public int getRank(int levelID, Score s) {
        
        List<Score> temp = getScoresByLevel(levelID);
        return temp.indexOf(s) + 1;
        
    }
        
}
