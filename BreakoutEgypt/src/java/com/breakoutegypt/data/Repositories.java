/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.breakoutegypt.data;

import com.breakoutegypt.data.mysql.MysqlBallRepository;
import com.breakoutegypt.data.mysql.MysqlBrickTypeRepository;
import com.breakoutegypt.data.mysql.MysqlEffectRepository;
import com.breakoutegypt.data.mysql.MysqlHighscoreRepository;
import com.breakoutegypt.data.mysql.MysqlLevelPackRepository;
import com.breakoutegypt.data.mysql.MysqlLevelProgressionRepository;
import com.breakoutegypt.data.mysql.MysqlLevelRepository;
import com.breakoutegypt.data.mysql.MysqlPowerDownRepository;
import com.breakoutegypt.data.mysql.MysqlPowerUpRepository;
import com.breakoutegypt.data.mysql.MysqlShapeDimensionRepository;
import com.breakoutegypt.data.mysql.MysqlUserRepository;

/**
 *
 * @author BenDB
 */
public class Repositories {
    
    private static boolean isTesting = false;
    
    private static final HighscoreRepository highscoreRepository = new MysqlHighscoreRepository();
    private static final DifficultyRepository difficultyRepository = new DummyDifficultyRepository();
    private static final BrickTypeRepository bricktypeRepository = new MysqlBrickTypeRepository();
    private static final EffectRepository effectRepository = new MysqlEffectRepository();
    private static final ShapeDimensionRepository shapedimensionRepository=new MysqlShapeDimensionRepository();
    private static DefaultShapeRepository defaultShapeRepository = DefaultShapeRepository.getInstance();
    private static UserRepository userRepository = null;
    private static LevelRepository levelRepo = new MysqlLevelRepository();
    private static LevelPackRepository levelPackRepo = new MysqlLevelPackRepository();
    private static PowerDownRepository powerdownRepo = new MysqlPowerDownRepository();
    private static BallRepository ballRepo = new MysqlBallRepository();
    private static LevelProgressionRepository levelProgressRepo = new MysqlLevelProgressionRepository();
    private static PowerUpRepository powerupRepo = new MysqlPowerUpRepository();
    
    public static HighscoreRepository getHighscoreRepository() {
        if (isTesting) {
            return new StaticDummyHighscoreRepo();
        }
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

    public static LevelPackRepository getLevelPackRepo() {
        return levelPackRepo;
    }

    public static PowerDownRepository getPowerdownRepo() {
        return powerdownRepo;
    }

    public static BallRepository getBallRepo() {
        return ballRepo;
    }  
    
    public static PowerUpRepository getPowerUpRepository(){
        return powerupRepo;
    }
    
    public static LevelProgressionRepository getLevelProgressionRepository() {
        if (isTesting) {
            return new DummyLevelProgressionRepository();
        }
        return levelProgressRepo;
    }
    
    public static void isTesting(boolean testing) {
        isTesting = testing;
    }
    
}
