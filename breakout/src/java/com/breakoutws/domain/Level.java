/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.breakoutws.domain;

import com.breakoutws.domain.shapes.Ball;
import com.breakoutws.domain.shapes.Brick;
import com.breakoutws.domain.shapes.Paddle;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import org.jbox2d.dynamics.Body;

/**
 * keeps track of all the objects present in the level, only one level for now
 *
 * @author kevin
 */
public class Level extends TimerTask {

    private int id;

    private Timer timer;
    private Game game;

    private BreakoutWorld breakoutWorld;

    private int lives;

    private boolean isLastLevel;
    private LevelState levelState;

    public Level(int id, Game game) {

        this.id = id;
        this.game = game;
        this.isLastLevel = isLastLevel;

        breakoutWorld = new BreakoutWorld(this);
    }

    public Level(int id, Game game, Ball ball, Paddle paddle, List<Brick> bricks, int lives
    ) {

        this(id, game);
        levelState = new LevelState(breakoutWorld, ball, paddle, bricks);
       
        this.lives = lives;
    }

    public void movePaddle(int x, int y) {
        breakoutWorld.movePaddle(x, y);
    }

    public void addPaddle(Paddle p) {
        levelState.addPaddle(p);
    }

    // TODO based on bricktype it might be necessary to do more here
    // e.g. all surrounding bricks an explosive brick
    public void addBrick(Brick brick) {
        levelState.addBrick(brick);
    }

    public void addBall(Ball b) {
       levelState.addBall(b);
    }

    public int getId() {
        return id;
    }

    // SHOULD NOT BE USED, USE DELEGATION INSTEAD
    public LevelState getLevelState(){
        return levelState;
    }

    void resetBall() {
        System.out.println("LeveL: resetBall()");
        levelState.resetBall();
        
        lives--;
        game.notifyPlayersOfLivesLeft();
    }

    private int getTargetBricksLeft() {
        System.out.println("Targets left: " + levelState.getTargetBricksLeft());
        return levelState.getTargetBricksLeft();
    }

    public boolean allTargetBricksDestroyed() {
        return getTargetBricksLeft() == 0;
    }

    public void start() {
        timer = new Timer();
        System.out.printf("Level: start level %d", this.id);
        timer.schedule(this, 0, 1000 / 60);
    }

    public boolean noLivesLeft() {
        return lives == 0;
    }

    public int getLives() {
        return lives;
    }

    // it is necessary to check if the timer is null because when the server crashes the timer seems to be null before we get here
    public void stop() {
        System.out.printf("Level: stop level %d", this.id);

        if (timer != null) {
            timer.cancel();
        }
    }

    public void initNextLevel() {
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

     public Body getPaddle() {
        return levelState.getPaddle();
    }

    public  void removeBrick(Body brick) {
        levelState.removeBrick(brick);
    }

    public Body getBall() {
        return levelState.getBall();
    }

    public List<Body> getBricks() {
        return levelState.getBricks();
    }

    List<Body> getRangeOfBricksAroundBody(Body brick, int range) {
        return levelState.getRangeOfBricksAroundBody(brick, range);
    }
        
}
