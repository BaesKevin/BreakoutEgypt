/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.breakoutws.domain;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import org.jbox2d.dynamics.Body;
import org.jbox2d.dynamics.World;

/**
 * keeps track of all the objects present in the level, only one level for now
 * @author kevin
 */
public class Level extends TimerTask {
    private int id;
    private List<Body> bricks;
    private Body ball;
    private Body paddle;
    
    private Timer timer;
    
    private Game game;
    
    private BodyFactory factory;
    
    private BreakoutWorld breakoutWorld;
    
    private int lives;
    
    private Shape startingBall;
    
    public Level(int id, Game game){
    
        this.id = id;
        bricks = new ArrayList();        
        
        breakoutWorld = new BreakoutWorld(this);
        factory = new BodyFactory(breakoutWorld.getWorld());
                
        this.game = game;
        
        this.timer = new Timer();
        
        createBounds();
    }
    
    
    
    public Level(int id, Game game, Shape ball, Shape paddle, List<Shape> bricks, int lives){
        this(id, game);
        
        addBall(ball);
        addPaddle(paddle);
        
        for(Shape brick : bricks){
            addBrick(brick);
        }
        
        this.lives = lives;
    }
    
    public void movePaddle(int x, int y) {
        breakoutWorld.movePaddle(x, y);
    }
    
    public void addPaddle(Shape s){
        paddle = factory.createPaddle(s);
    }
    
    public void addBrick(Shape s){
        bricks.add(factory.createBrick(s));
    }
    
    public void addBall(Shape s){
        this.startingBall = new Shape(s);
        ball = factory.createCircle(s);
    }
    
    private void createBounds(){
        factory.addGround(300, 300);
        
        factory.addWall(0, 0, 1, 300); //Left wall
        factory.addWall(300, 0, 1, 300); //Right wall, keep in mind 
        
        factory.addWall(0, 0,300, 1); //roof 
    }
    
    public int getId() {
        return id;
    }

    public List<Body> getBricks() {
        return bricks;
    }

    public Body getBall() {
        return ball;
    }

    public Body getPaddle() {
        return paddle;
    }

    public void removeBrick(Body brick){
        bricks.remove(brick);
    }
    
    void resetBall() {
        ball = new BodyFactory(breakoutWorld.getBox2dWorld()).createCircle(startingBall);
        
        lives--;
    }
    
    private int getTargetBricksLeft() {
        int targetsLeft = 0;
        for(Body b : bricks) {
            Shape s = (Shape) b.getUserData();
            
            if (s.isTarget())
               ++targetsLeft;           
        }
        return targetsLeft;
    }
    
    public boolean allTargetBricksDestroyed() {
        return getTargetBricksLeft() == 0;
    }
    
    public boolean noLivesLeft(){
        return lives == 0;
    }
    
    public void startLevel() {
        timer.schedule(this, 0, 1000/60);
    }
    
    public void stopLevel() {
        timer.cancel();
    }
    
    @Override
    public void run() {
        breakoutWorld.step();
        
        if( noLivesLeft() ){
            game.notifyPlayersLevelEnded();
        } else {
            game.notifyPlayers(this, breakoutWorld);
        }
        
    }

}
