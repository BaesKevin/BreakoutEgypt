/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.breakoutegypt.test.persistence;

import com.breakoutegypt.data.LevelProgressionRepository;
import com.breakoutegypt.data.LevelRepository;
import com.breakoutegypt.data.Repositories;
import com.breakoutegypt.domain.Game;
import com.breakoutegypt.domain.GameManager;
import com.breakoutegypt.domain.GameType;
import com.breakoutegypt.domain.Level;
import com.breakoutegypt.domain.levelprogression.Difficulty;
import com.breakoutegypt.domain.levelprogression.LevelProgress;
import java.util.List;
import junit.framework.Assert;
import org.junit.Test;

/**
 *
 * @author kevin
 */
public class levelPackTest {
    
    @Test
    public void createArcadeGameCreatesArcadeFactoryWithAllArcadeLevels(){
        GameManager gm = new GameManager();
        int gameId = gm.createGame(GameType.ARCADE, Difficulty.HARD);
        
        LevelProgress progress = LevelProgressionRepository.getDefault(GameType.ARCADE);
        Game game = gm.getGame(gameId);
        
        List<Level> levels = game.getLevelFactory().getLevels();
        LevelRepository levelRepo = Repositories.getLevelRepository();
        
        for(Level level : levels){
            levelRepo.addLevel(level);
        }
        
        Level level2 = levelRepo.getLevelByNumber(2, game);
        String expectedName = "unbreakable and explosive";
        Assert.assertEquals(2, level2.getLevelNumber());
        Assert.assertEquals(expectedName, level2.getLevelName());
    }
}
