/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.breakoutegypt.test.persistence;

import com.breakoutegypt.data.BrickRepository;
import com.breakoutegypt.data.LevelPackRepository;
import com.breakoutegypt.data.LevelProgressionRepository;
import com.breakoutegypt.data.LevelRepository;
import com.breakoutegypt.data.Repositories;
import com.breakoutegypt.data.mysql.MysqlBrickRepository;
import com.breakoutegypt.domain.Game;
import com.breakoutegypt.domain.GameManager;
import com.breakoutegypt.domain.GameType;
import com.breakoutegypt.domain.Level;
import com.breakoutegypt.domain.LevelPack;
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
        game.initStartingLevel(4, LevelProgressionRepository.getDefault(GameType.ARCADE));
        
        Level level4 = game.getCurrentLevel();
        String expectedName = "Level by ben";
        Assert.assertEquals(4, level4.getLevelNumber());
        Assert.assertEquals(expectedName, level4.getLevelName());
        
    }
    
}
