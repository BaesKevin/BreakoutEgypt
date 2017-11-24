/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.breakoutegypt.levelfactories;

import com.breakoutegypt.domain.Game;
import com.breakoutegypt.domain.levelprogression.UserLevel;
import java.util.Arrays;
import java.util.List;

/**
 *
 * @author wouter
 */
public class EasyArcadeLevelFactory extends LevelFactory{
    
    private ArcadeLevelFactory arcadeLevelFactory;
    private Game game;

    public EasyArcadeLevelFactory(Game game) {        
        super(game, 3);
        arcadeLevelFactory = new ArcadeLevelFactory(game);
    }

    @Override
    protected void createCurrentLevel() {
        System.out.println("EasyArcadeLevelFactory: creating level " + currentLevelId);
        switch (currentLevelId) {
            case 1:
                currentLevel = arcadeLevelFactory.getSimpleTestLevel();
                break;
            case 2:
                currentLevel = arcadeLevelFactory.getLevelWithUnbreakableAndExplosive();
                break;
            case 3:
                currentLevel = arcadeLevelFactory.getSimpleTestLevel();
                break;
        }
    
    }
    
    
    
    
}
