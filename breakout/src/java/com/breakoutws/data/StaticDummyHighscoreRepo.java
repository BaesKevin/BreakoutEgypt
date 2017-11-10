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
public class StaticDummyHighscoreRepo implements HighscoreRepo {
    
    private final List<Score> scores;
    
    public StaticDummyHighscoreRepo () {
        
        scores = new ArrayList();
        
        User dummy = new User("Ben");
        
        scores.add(new Score(1, dummy, 2500, "hard"));
        scores.add(new Score(1, dummy, 4548, "hard"));
        scores.add(new Score(1, dummy, 2580, "hard"));
        scores.add(new Score(2, dummy, 48648, "hard"));
        scores.add(new Score(1, dummy, 2500, "hard"));
        scores.add(new Score(1, dummy, 25448, "hard"));
        scores.add(new Score(2, dummy, 25486, "hard"));
        scores.add(new Score(1, dummy, 12345, "hard"));
        scores.add(new Score(1, dummy, 486, "hard"));
        scores.add(new Score(2, dummy, 2560, "hard"));
        scores.add(new Score(1, dummy, 46, "hard"));
        scores.add(new Score(2, dummy, 8686, "hard"));
        scores.add(new Score(2, dummy, 486, "hard"));
    }
    
    @Override
    public List<Score> getScoresByLevel(int levelID, String difficulty) {
        
        List<Score> scoresByLevel = new ArrayList();
        
        for (Score s : scores) {
            if (s.getLevel() == levelID && s.getDifficulty().equals(difficulty)) {
                scoresByLevel.add(s);
            }
        }
        
        Collections.sort(scoresByLevel);
        System.out.println("Scores for Level: " + levelID);
        System.out.println(scoresByLevel);
        System.out.println("");
        return scoresByLevel;
    }
    
    @Override
    public void addScore(Score s) {
        scores.add(s);
    }
    
    @Override
    public int getRank(int levelID, Score s) {
        
        List<Score> temp = getScoresByLevel(levelID, s.getDifficulty());
        return temp.indexOf(s) + 1;
        
    }
        
}
