/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.breakoutegypt.test;

import com.breakoutegypt.data.BrickTypeRepository;
import com.breakoutegypt.data.Repositories;
import com.breakoutegypt.domain.LevelState;
import com.breakoutegypt.domain.effects.ExplosiveEffect;
import com.breakoutegypt.domain.shapes.ShapeDimension;
import com.breakoutegypt.domain.shapes.bricks.Brick;
import com.breakoutegypt.domain.shapes.bricks.BrickType;
import java.awt.Color;
import java.awt.Point;
import java.util.ArrayList;
import java.util.List;
import static org.junit.Assert.assertTrue;
import org.junit.Test;

/**
 *
 * @author kevin
 */
public class LevelStateTest {

    @Test
    public void testRangeAroundBrick() {
        List<Brick> bricks = getBricks();

        LevelState ls = new LevelState(new ArrayList(), new ArrayList(), bricks);
        bricks = ls.getBricks();
        bricks.get(5).addEffect(new ExplosiveEffect(bricks.get(5), 1));
        bricks.get(4).addEffect(new ExplosiveEffect(bricks.get(4), 1));
        
        List<Brick> bricksToRemove = ls.getRangeOfBricksAroundBody(bricks.get(5), 1);
        assertTrue(bricksToRemove.size() == 9);
    }
    
    @Test
    public void testRangeAroundBrick2() {
        List<Brick> bricks = getBricks();

        LevelState ls = new LevelState(new ArrayList(), new ArrayList(), bricks);
        bricks = ls.getBricks();
        bricks.get(5).addEffect(new ExplosiveEffect(bricks.get(5), 1));
        
        List<Brick> bricksToRemove = ls.getRangeOfBricksAroundBody(bricks.get(5), 1);
        assertTrue(bricksToRemove.size() == 6);
    }
    
    @Test
    public void testRangeAroundBrick3() {
        List<Brick> bricks = getBricks();

        LevelState ls = new LevelState(new ArrayList(), new ArrayList(), bricks);
        bricks = ls.getBricks();
        bricks.get(5).addEffect(new ExplosiveEffect(bricks.get(5), 1));
        
        List<Brick> bricksToRemove = ls.getRangeOfBricksAroundBody(bricks.get(1), 0);
        assertTrue(bricksToRemove.size() == 1);
    }
    
    @Test
    public void testRangeAroundBrick4() {
        List<Brick> bricks = getBricks();

        LevelState ls = new LevelState(new ArrayList(), new ArrayList(), bricks);
        bricks = ls.getBricks();
        bricks.get(5).addEffect(new ExplosiveEffect(bricks.get(5), 1));
        bricks.get(1).addEffect(new ExplosiveEffect(bricks.get(1), 1));
        
        
        List<Brick> bricksToRemove = ls.getRangeOfBricksAroundBody(bricks.get(1), 1);
        assertTrue(bricksToRemove.size() == 8);
    }
    
    @Test
    public void testRangeAroundBrick5() {
        List<Brick> bricks = getBricks();

        LevelState ls = new LevelState(new ArrayList(), new ArrayList(), bricks);
        bricks = ls.getBricks();
        bricks.get(5).addEffect(new ExplosiveEffect(bricks.get(5), 1));
        bricks.get(0).addEffect(new ExplosiveEffect(bricks.get(0), 1));
        
        
        List<Brick> bricksToRemove = ls.getRangeOfBricksAroundBody(bricks.get(5), 1);
        assertTrue(bricksToRemove.size() == 6);
        
        bricksToRemove = ls.getRangeOfBricksAroundBody(bricks.get(0), 1);
        assertTrue(bricksToRemove.size() == 4);
    }
    
    private List<Brick> getBricks() {
        BrickTypeRepository bricktypeRepo=Repositories.getBrickTypeRepository();
        int row = 1;
        int col = 1;
        int rows = 3;
        int cols = 3;
        int width = 30;
        int height = 30;
        ShapeDimension brickShape;
        Brick brick;
        List<Brick> bricks = new ArrayList();
        String id;
        for (int x = 10; x < 10 + ((width + 1) * cols); x += width + 1) {
            for (int y = 5; y < 5 + ((height + 1) * rows); y += height + 1) {
                int colPadding = cols / 10 + 1;
                int rowPadding = rows / 10 + 1;

                id = String.format("brick%0" + rowPadding + "d%0" + colPadding + "d", col, row); //altijd genoeg padding 0en zetten zodat id's uniek zijn

                brickShape = new ShapeDimension(id, x, y, width, height, Color.PINK);

                brick = new Brick(brickShape, new Point(row, col));
                brick.setType(bricktypeRepo.getBrickTypeByName("REGULAR"));

                bricks.add(brick);
                col++;
            }
            row++;
            col = 1;
        }
        return bricks;
    }
}
