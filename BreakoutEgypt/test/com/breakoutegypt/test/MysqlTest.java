/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.breakoutegypt.test;

import com.breakoutegypt.data.mysql.MysqlDifficultyRepository;
import com.breakoutegypt.domain.levelprogression.Difficulty;
import com.breakoutegypt.domain.levelprogression.GameDifficulty;
import java.util.List;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author Bjarne Deketelaere
 */
public class MysqlTest {
    
    public MysqlTest() {
    }
    @Test
    public void DifficultyRepoFindAllTest(){
        MysqlDifficultyRepository repository=MysqlDifficultyRepository.getInstance();
        List<Difficulty> difficulties=repository.findAll();
        assertNotNull(difficulties);
    }
    @Test
    public void DifficultyRepoFindByNameTest(){
        MysqlDifficultyRepository repository=MysqlDifficultyRepository.getInstance();
        Difficulty difficulty=repository.findByName(GameDifficulty.EASY);
        assertNotNull(difficulty);
    }
}
