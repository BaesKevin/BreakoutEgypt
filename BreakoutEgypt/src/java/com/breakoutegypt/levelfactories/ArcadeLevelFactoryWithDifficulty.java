/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.breakoutegypt.levelfactories;

import com.breakoutegypt.domain.Game;
import com.breakoutegypt.domain.GameDifficulty;

/**
 *
 * @author wouter
 */
public class ArcadeLevelFactoryWithDifficulty extends LevelFactory{
    
    private ArcadeLevelFactory arcadeLevelFactory;
    private Game game;
    private GameDifficulty difficulty;
    
    private static final float[] TIMESTEP_WITH_DIFFICULTY =
            new float[]{ 
                1.0f / 60.0f, //EASY
                1.0f / 180.f, //MEDIUM
                1.0f / 360.0f //HARD
            };
    
    private float timeStep;

    public ArcadeLevelFactoryWithDifficulty(Game game, GameDifficulty diff) {        
        super(game, 3);
        difficulty = diff;
        timeStep = TIMESTEP_WITH_DIFFICULTY[diff.ordinal()];
        arcadeLevelFactory = new ArcadeLevelFactory(game);
    }

    @Override
    protected void createCurrentLevel() {
        System.out.println("ArcadeLevelFactoryWithDifficulty: creating " 
                + difficulty.name() + " level " + currentLevelId);
        switch (currentLevelId) {
            case 1:
                currentLevel = arcadeLevelFactory.getSimpleTestLevel(timeStep);
                break;
            case 2:
                currentLevel = arcadeLevelFactory.getLevelWithUnbreakableAndExplosive(timeStep);
                break;
            case 3:
                currentLevel = arcadeLevelFactory.getSimpleTestLevel(timeStep);
                break;
        }
        currentLevel.setLevelNumber(currentLevelId);
    
    }
    
    
    
    
}
