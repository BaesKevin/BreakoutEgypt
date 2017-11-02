/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.breakoutws.domain;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;
import org.jbox2d.dynamics.World;

/**
 *
 * @author kevin
 */
public class LevelFactory {
    private World world;
    
    public LevelFactory(World world){
        this.world = world;
    }
    
    public Level getLevel1(){
        
        Shape paddleShape = new Shape("paddle", 45, 250, 100, 4, Color.BLUE);
        Shape ballShape = new Shape("ball", 60, 90, BodyFactory.BALL_RADIUS, BodyFactory.BALL_RADIUS, Color.GREEN);
        List<Shape> bricks = new ArrayList();
        
        int row = 1;
        int col = 1;
        int rows = 3;
        int cols = 5;
        int width = 30;
        int height = 10;
        
        Shape brickShape;
        for(int x = 45; x < 45 + (( width + 1) * cols); x+=width + 1){
            for(int y = 45; y < 45 + (( height + 1) * rows ); y += height + 1){
                brickShape = new Shape("brick" + col++ + "" + row, x, y, width, height, Color.PINK);
                bricks.add(brickShape);
            }
            row++;
        }
        
        Level level = new Level(1, world, ballShape, paddleShape, bricks);
        
        return level;
    }
}
