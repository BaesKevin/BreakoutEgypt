/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.breakoutegypt.domain;

import com.breakoutegypt.domain.effects.ExplosiveEffect;
import com.breakoutegypt.domain.effects.ToggleEffect;
import com.breakoutegypt.domain.shapes.Ball;
import com.breakoutegypt.domain.shapes.Brick;
import com.breakoutegypt.domain.shapes.Paddle;
import java.util.Arrays;
import java.util.List;
import java.util.Timer;

/**
 * keeps track of all the objects present in the level, only one level for now
 *
 * @author kevin
 */
public class Level {

    private int id;

    private Timer timer;
    private Game game;

    private BreakoutWorld breakoutWorld;

    private int lives;

    private boolean isLastLevel;
    private LevelState levelState;

    private boolean levelStarted;

    private ScoreTimer scoreTimer;
    private LevelTimerTask levelTimerTask;

    public Level(int id, Game game) {

        this.id = id;
        this.game = game;
        this.isLastLevel = isLastLevel;

        this.levelStarted = false;

        scoreTimer = new ScoreTimer();

        breakoutWorld = new BreakoutWorld(this);
    }

    public ScoreTimer getScoreTimer() {
        return scoreTimer;
    }

    public Level(int id, Game game, Ball ball, Paddle paddle, List<Brick> bricks, int lives
    ) {
        this(id, game, ball, Arrays.asList(new Paddle[]{paddle}), bricks, lives);
    }

    public Level(int id, Game game, Ball ball, List<Paddle> paddles, List<Brick> bricks, int lives
    ) {
        this(id, game);
        levelState = new LevelState(breakoutWorld, ball, paddles, bricks);

        this.lives = lives;
        this.timer = new Timer();
    }

    public boolean isLevelStarted() {
        return levelStarted;
    }

    public void setLevelStarted(boolean b) {
        this.levelStarted = b;
    }

    public void startBall() {
        if (!levelStarted) {
            setLevelStarted(true);
            scoreTimer.start();
            getBall().setLinearVelocity(0, 100);
            System.out.println("Level: startBall()");
        }
    }

    public void movePaddle(Paddle paddle, int x, int y) {
        if (!levelStarted) {
            float yPos = this.getBall().getPosition().y;
            this.getBall().moveTo(x, yPos);
        }

        paddle.moveTo(x, y);
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
    public LevelState getLevelState() {
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
        if (levelTimerTask == null) {
            System.out.printf("Level: start level %d", this.id);
            levelTimerTask = new LevelTimerTask(breakoutWorld, game, this);
            timer.schedule(levelTimerTask, 0, 1000 / 60);
        } else {
            System.out.println("Level: trying to start the level twice, ignoring call");
        }

    }
    
    public boolean noLivesLeft() {
        return lives == 0;
    }

    public int getLives() {
        return lives;
    }

    public void stop() {
        System.out.printf("Level: stop level %d", this.id);
        levelTimerTask.cancel();
        levelTimerTask = null;
    }

    public void initNextLevel() {
        stop();
        game.initNextLevel();
    }

    public boolean isLastLevel() {
        return isLastLevel;
    }

    public List<Paddle> getPaddles() {
        return levelState.getPaddles();
    }

    public void removeBrick(Brick brick) {
        levelState.removeBrick(brick);
    }

    public Ball getBall() {
        return levelState.getBall();
    }

    public List<Brick> getBricks() {
        return levelState.getBricks();
    }

    BreakoutWorld getBreakoutWorld() {
        return breakoutWorld;
    }

    public void handleExplosiveEffect(ExplosiveEffect effect) {
        List<Brick> bricks = levelState.getRangeOfBricksAroundBody(effect.getCentreBrick(), effect.getRadius());

        breakoutWorld.destroyBricks(bricks);
    }

    public void handleToggleEffect(ToggleEffect effect) {
        breakoutWorld.toggleBricks(effect.getBricksToToggle());
    }
}
