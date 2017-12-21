/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.breakoutegypt.data;

import com.breakoutegypt.data.mysql.MysqlBrickTypeRepository;
import com.breakoutegypt.data.mysql.MysqlEffectRepository;
import com.breakoutegypt.data.mysql.MysqlHighscoreRepository;
import com.breakoutegypt.data.mysql.MysqlLevelPackRepository;
import com.breakoutegypt.data.mysql.MysqlLevelRepository;
import com.breakoutegypt.data.mysql.MysqlShapeDimensionRepository;
import com.breakoutegypt.data.mysql.MysqlUserRepository;

/**
 *
 * @author BenDB
 */
public class Repositories {
    
    private static final HighscoreRepository highscoreRepository = new MysqlHighscoreRepository();
    private static final DifficultyRepository difficultyRepository = new DummyDifficultyRepository();
    private static final BrickTypeRepository bricktypeRepository = new MysqlBrickTypeRepository();
    private static final EffectRepository effectRepository = new MysqlEffectRepository();
    private static final ShapeDimensionRepository shapedimensionRepository=new MysqlShapeDimensionRepository();
    private static DefaultShapeRepository defaultShapeRepository = DefaultShapeRepository.getInstance();
    private static UserRepository userRepository = null;
    private static LevelRepository levelRepo = new MysqlLevelRepository();
    
    public static HighscoreRepository getHighscoreRepository() {
        return highscoreRepository;
    }

    public static DifficultyRepository getDifficultyRepository() {
        return difficultyRepository;
    }

    public static DefaultShapeRepository getDefaultShapeRepository() {
        return defaultShapeRepository;
    }
    
    public static BrickTypeRepository getBrickTypeRepository() {
        return bricktypeRepository;
    }
    
    public static ShapeDimensionRepository getShapeDimensionRepository(){
        return shapedimensionRepository;
    }
    
    public static UserRepository getUserRepository() {
        
        //lazy loading
        if (userRepository == null)
            userRepository = new MysqlUserRepository();
        
        return userRepository;
    }

    public static LevelRepository getLevelRepository() {
        return levelRepo;
    }

    public static EffectRepository getEffectRepository() {
        return effectRepository;
    }
    
    
    
}
