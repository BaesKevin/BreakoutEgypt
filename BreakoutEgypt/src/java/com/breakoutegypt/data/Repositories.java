/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.breakoutegypt.data;

import com.breakoutegypt.domain.levelprogression.Difficulty;
import java.util.List;

/**
 *
 * @author BenDB
 */
public class Repositories {
    
    public static final HighscoreRepo highscoreRepository = new StaticDummyHighscoreRepo();
    private static final DifficultyRepository difficultyRepository = new DummyDifficultyRepository();
    
    public static HighscoreRepo getHighscoreRepository() {
        return highscoreRepository;
    }

    public static DifficultyRepository getDifficultyRepository() {
        return difficultyRepository;
    }
    
}
