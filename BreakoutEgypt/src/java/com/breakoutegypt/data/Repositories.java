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
    
    private static final HighscoreRepository highscoreRepository = new StaticDummyHighscoreRepo();
    private static final DifficultyRepository difficultyRepository = new DummyDifficultyRepository();
    private static final DefaultShapeRepository defaultShapeRepository = new DefaultShapeRepository();
    
    public static HighscoreRepository getHighscoreRepository() {
        return highscoreRepository;
    }

    public static DifficultyRepository getDifficultyRepository() {
        return difficultyRepository;
    }

    public static DefaultShapeRepository getDefaultShapeRepository() {
        return defaultShapeRepository;
    }
    
}
