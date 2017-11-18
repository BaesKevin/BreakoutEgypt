/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.breakoutegypt.data;

import com.breakoutegypt.domain.Score;
import java.util.List;

/**
 *
 * @author BenDB
 */
public interface HighscoreRepo {
    
    public List<Score> getScoresByLevel(int levelID, String diff);
    public void addScore(Score s);
    public int getRank(int levelID, Score s);
    
}
