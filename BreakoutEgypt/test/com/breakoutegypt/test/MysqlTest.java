/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.breakoutegypt.test;

import com.breakoutegypt.data.mysql.MysqlBrickTypeRepository;
import com.breakoutegypt.data.mysql.MysqlDifficultyRepository;
import com.breakoutegypt.domain.levelprogression.Difficulty;
import com.breakoutegypt.domain.levelprogression.GameDifficulty;
import com.breakoutegypt.domain.shapes.bricks.BrickType;
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
        MysqlDifficultyRepository repository=new MysqlDifficultyRepository();
        List<Difficulty> difficulties=repository.findAll();
        assertNotNull(difficulties);
    }
    @Test
    public void DifficultyRepoFindByNameTest(){
        MysqlDifficultyRepository repository=new MysqlDifficultyRepository();
        Difficulty difficulty=repository.findByName(GameDifficulty.EASY);
        assertNotNull(difficulty);
    }
    @Test
    public void brickTypeRepoFindByNameTest(){
        MysqlBrickTypeRepository repository=new MysqlBrickTypeRepository();
        BrickType bricktype=repository.getBrickTypeByName("EXPLOSIVE");
        assertNotNull(bricktype);
        assertEquals(bricktype.getName(), "EXPLOSIVE");
    }
}
