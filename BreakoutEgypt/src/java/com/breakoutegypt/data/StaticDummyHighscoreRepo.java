/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.breakoutegypt.data;

import com.breakoutegypt.domain.Score;
import com.breakoutegypt.domain.User;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 *
 * @author BenDB
 */
public class StaticDummyHighscoreRepo implements HighscoreRepository {
    
    private final List<Score> scores;
    
    public StaticDummyHighscoreRepo () {
        
        scores = new ArrayList();
        
        User dummy = new User("Ben");
        
        scores.add(new Score(0, 1, dummy, 2500, "hard", 16545));
        scores.add(new Score(1, 1, dummy, 4548, "hard", 24324));
        scores.add(new Score(2, 1, dummy, 2580, "hard", 24234));
        scores.add(new Score(3 ,2, dummy, 48648, "hard", 46756));
        scores.add(new Score(4, 1, dummy, 2500, "hard", 63245));
        scores.add(new Score(5, 1, dummy, 25448, "hard", 2364754));
        scores.add(new Score(6, 2, dummy, 25486, "hard", 42357));
        scores.add(new Score(7, 1, dummy, 12345, "hard", 54767));
        scores.add(new Score(8, 1, dummy, 486, "hard", 54774));
        scores.add(new Score(9, 2, dummy, 2560, "hard", 74576));
        scores.add(new Score(10, 1, dummy, 46, "hard", 57472));
        scores.add(new Score(11, 2, dummy, 8686, "hard", 76543));
        scores.add(new Score(12, 2, dummy, 486, "hard", 56732));
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

    @Override
    public void removeScore(int id) {
        Score scoreToRemove = null;
        for (Score s : scores) {
            if (s.getScoreId() == id) {
                scoreToRemove = s;
                break;
            }
        }
        scores.remove(scoreToRemove);
    }
        
}
