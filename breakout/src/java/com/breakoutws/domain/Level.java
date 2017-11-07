/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.breakoutws.domain;

import com.breakoutws.domain.shapes.Ball;
import com.breakoutws.domain.shapes.Brick;
import com.breakoutws.domain.shapes.Paddle;
import com.breakoutws.domain.shapes.Shape;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import org.jbox2d.dynamics.Body;

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
    
    private Ball startingBall;
    
    private boolean isLastLevel;
    
    public Level(int id, Game game){
    
        this.id = id;
        bricks = new ArrayList();        
        
        breakoutWorld = new BreakoutWorld(this);
        factory = new BodyFactory(breakoutWorld.getWorld());
                
        this.game = game; 
        this.isLastLevel = isLastLevel;
             
        createBounds();        
    }  
    
    public Level(int id, Game game, Ball ball, Paddle paddle, List<Brick> bricks, int lives
           ){
             
        this(id, game);
        addBall(ball);
        addPaddle(paddle);
        
        for(Brick brick : bricks){
            addBrick(brick);
        }
        
        this.lives = lives;
    }
    
    public void movePaddle(int x, int y) {
        breakoutWorld.movePaddle(x, y);
    }
    
    public void addPaddle(Paddle p){
        paddle = factory.createPaddle(p);
    }
    
    public void addBrick(Brick brick){
        bricks.add(factory.createBrick(brick));
    }
    
    public void addBall(Ball b){
        this.startingBall = b;
        ball = factory.createCircle(b);
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
        System.out.println("LeveL: resetBall()");
        ball = new BodyFactory(breakoutWorld.getBox2dWorld()).createCircle(startingBall);        
        lives--;
        game.notifyPlayersOfLivesLeft();
    }
    
    private int getTargetBricksLeft() {
        int targetsLeft = 0;
        for(Body body : bricks) {
            Brick brick = (Brick) body.getUserData();
            
            if (brick.isTarget())
               ++targetsLeft;           
        }
        return targetsLeft;
    }
    
    public boolean allTargetBricksDestroyed() {
        return getTargetBricksLeft() == 0;
    }
    
    public void start() {
        timer = new Timer();
        System.out.printf("Level: start level %d", this.id);
        timer.schedule(this, 0, 1000/60);
    }
    
    public boolean noLivesLeft(){
        return lives == 0;
    }

    public int getLives() {
        return lives;
    }
    
    
      
    // it is necessary to check if the timer is null because when the server crashes the timer seems to be null before we get here
    public void stop() {
        System.out.printf("Level: stop level %d", this.id);
        
        if( timer != null ){
            timer.cancel();
        }
    }
    
    public void initNextLevel(){
        stop();
        game.initNextLevel();
    }
    
    @Override
    public void run() {
        breakoutWorld.step();      
       
        game.notifyPlayers(this, breakoutWorld);
       
        
    }

    public boolean isLastLevel() {
        return isLastLevel;
    }

}
