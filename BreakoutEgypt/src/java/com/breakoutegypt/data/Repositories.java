/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.breakoutegypt.data;

import com.breakoutegypt.data.mysql.MysqlBrickTypeRepository;
import com.breakoutegypt.data.mysql.MysqlShapeDimensionRepository;
import com.breakoutegypt.domain.levelprogression.Difficulty;
import java.util.List;

/**
 *
 * @author BenDB
 */
public class Repositories {
    
    private static final HighscoreRepository highscoreRepository = new StaticDummyHighscoreRepo();
    private static final DifficultyRepository difficultyRepository = new DummyDifficultyRepository();
    private static final BrickTypeRepository bricktypeRepository = new MysqlBrickTypeRepository();
    private static final ShapeDimensionRepository shapedimensionRepository=new MysqlShapeDimensionRepository();
    public static HighscoreRepository getHighscoreRepository() {
        return highscoreRepository;
    }

    public static DifficultyRepository getDifficultyRepository() {
        return difficultyRepository;
    }
    
    public static BrickTypeRepository getBrickTypeRepository() {
        return bricktypeRepository;
    }
    
    public static ShapeDimensionRepository getShapeDimensionRepository(){
        return shapedimensionRepository;
    }
}
