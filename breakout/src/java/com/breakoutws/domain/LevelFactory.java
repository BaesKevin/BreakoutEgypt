/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.breakoutws.domain;

import com.breakoutws.domain.shapes.Ball;
import com.breakoutws.domain.shapes.Brick;
import com.breakoutws.domain.shapes.BrickType;
import com.breakoutws.domain.shapes.Paddle;
import com.breakoutws.domain.shapes.Shape;
import java.awt.Color;
import java.awt.Point;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author kevin
 */
public class LevelFactory {
    
    private Game game;
    private int currentLevel;
    private int totalLevels;

    public LevelFactory(Game game) {
        this.currentLevel = 1;
        this.totalLevels = 2;
        this.game = game;
    }
    
    public boolean hasNextLevel() {

        return currentLevel <= totalLevels;
    }
    
    public Level getCurrentLevel() {
        System.out.printf("LevelFactory: Get level %d of %d", currentLevel, totalLevels);
        return getLevelWithUnbreakableAndExplosive();
//        return getSimpleTestLevel(currentLevel);        
    }
    
    public Level getNextLevel() {
        System.out.println("LevelFactory: Get next level");
        currentLevel++;
        return getCurrentLevel();
    }
    
    public Level getSimpleTestLevel(int targetBlocks){
        Shape paddleShape = new Shape("paddle", 45, 250, 100, 4, Color.BLUE);
        Shape ballShape = new Shape("ball", 60, 90, BodyFactory.BALL_RADIUS, BodyFactory.BALL_RADIUS, Color.GREEN);
        
        Paddle paddle = new Paddle(paddleShape);
        Ball ball = new Ball(ballShape);
        List<Brick> bricks = new ArrayList();
        
        int row = 1;
        int col = 1;
        int rows = 1;
        int cols = 5;
        int width = 30;
        int height = 10;
        
        Shape brickShape;
        Brick brick;
        
        String id;
        for(int x = 45; x < 45 + (( width + 1) * cols); x+=width + 1){
            for(int y = 45; y < 45 + (( height + 1) * rows ); y += height + 1){
                int colPadding = cols / 10 + 1;
                int rowPadding = rows / 10 + 1;
             
                id = String.format("brick%0" + rowPadding+ "d%0" + colPadding + "d", col, row); //altijd genoeg padding 0en zetten zodat id's uniek zijn
               
                
                
                brickShape = new Shape(id, x, y, width, height, Color.PINK);
                brick = new Brick(brickShape, BrickType.REGULAR, new Point(row, col));
                bricks.add(brick);
                col++;
            }
            row++;
            col = 1;
        }
        
//        ArrayList<Integer> list = new ArrayList<Integer>();
//        for (int i=0; i<bricks.size(); i++) {
//            list.add(new Integer(i));
//        }
//        Collections.shuffle(list);
//        for (int i=0; i<targetBlocks; i++) {
//            Shape b = bricks.get(list.get(i));
//            b.setTarget(true);
//            b.setColor(Color.BLACK);
//        }

//        for (int i = 0; i < targetBlocks; i++) {
//            Shape b = bricks.get(i);
//            b.setTarget(true);
//            b.setColor(Color.BLACK);
//        }

        for (int i=0; i<targetBlocks; i++) {
            bricks.get(i).setIsTarget(true);
            bricks.get(i).getShape().setColor(Color.BLACK);
        }
                
        Level level = new Level(currentLevel, game, ball, paddle, bricks, 3);
        
        return level;
    }
    
    public Level getLevelWithUnbreakableAndExplosive(){
        Shape paddleShape = new Shape("paddle", 45, 250, 100, 4, Color.BLUE);
        Shape ballShape = new Shape("ball", 60, 90, BodyFactory.BALL_RADIUS, BodyFactory.BALL_RADIUS, Color.GREEN);
        
        Paddle paddle = new Paddle(paddleShape);
        Ball ball = new Ball(ballShape);
        List<Brick> bricks = new ArrayList();
        
        int row = 1;
        int col = 1;
        int rows = 1;
        int cols = 5;
        int width = 30;
        int height = 10;
        
        Shape brickShape;
        Brick brick;
        
        String id;
        for(int x = 45; x < 45 + (( width + 1) * cols); x+=width + 1){
            for(int y = 45; y < 45 + (( height + 1) * rows ); y += height + 1){
                int colPadding = cols / 10 + 1;
                int rowPadding = rows / 10 + 1;
             
                id = String.format("brick%0" + rowPadding+ "d%0" + colPadding + "d", col, row); //altijd genoeg padding 0en zetten zodat id's uniek zijn
               
                
                
                brickShape = new Shape(id, x, y, width, height, Color.PINK);
                brick = new Brick(brickShape, BrickType.REGULAR, new Point(row,col));
                bricks.add(brick);
                col++;
            }
            row++;
            col = 1;
        }

        for (int i=0; i<1; i++) {
            bricks.get(i).setIsTarget(true);
            bricks.get(i).getShape().setColor(Color.BLACK);
        }
        
        bricks.get(1).setType(BrickType.UNBREAKABLE);
        bricks.get(2).setType(BrickType.EXPLOSIVE);
                
        Level level = new Level(currentLevel, game, ball, paddle, bricks, 3);
        
        return level;
    }
}
