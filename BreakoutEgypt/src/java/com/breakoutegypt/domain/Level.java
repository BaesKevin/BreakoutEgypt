/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.breakoutegypt.domain;

import com.breakoutegypt.domain.effects.FloorPowerUp;
import com.breakoutegypt.data.StaticDummyHighscoreRepo;
import com.breakoutegypt.domain.messages.BallMessage;
import com.breakoutegypt.domain.messages.BallMessageType;
import com.breakoutegypt.domain.effects.BreakoutEffectHandler;
import com.breakoutegypt.domain.effects.BreakoutPowerUpHandler;
import com.breakoutegypt.domain.effects.BrokenPaddlePowerUp;
import com.breakoutegypt.domain.shapes.Ball;
import com.breakoutegypt.domain.shapes.bricks.Brick;
import com.breakoutegypt.domain.shapes.Paddle;
import com.breakoutegypt.domain.shapes.RegularBody;
import java.util.List;
import java.util.Timer;

/**
 * keeps track of all the objects present in the level, only one level for now
 *
 * @author kevin
 */
public class Level implements BreakoutWorldEventListener, BallEventHandler {

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
    private boolean runLevelManually;

    public Level(int id, Game game, LevelState initialObjects, int lives
    ) {
        this(id, game, initialObjects, lives, BreakoutWorld.TIMESTEP_DEFAULT);
    }

    public Level(int id, Game game, LevelState initialState, int lives, float worldTimeStepInMs) {
        this.id = id;
        this.game = game;
        this.isLastLevel = isLastLevel;

        this.levelStarted = false;

        scoreTimer = new ScoreTimer();

        breakoutWorld = new BreakoutWorld(/*this,*/worldTimeStepInMs);

        levelState = initialState;
        List<RegularBody> bodiesToSpawn = levelState.getAllObjectsToSpawn();
        for (RegularBody rb : bodiesToSpawn) {
            breakoutWorld.spawn(rb);
        }

        breakoutWorld.setBreakoutWorldEventListener(this);
        breakoutWorld.initContactListener(
                new BreakoutEffectHandler(this, levelState, breakoutWorld),
                this,
                new BreakoutPowerUpHandler(this, levelState, breakoutWorld));

        this.lives = lives;
        this.timer = new Timer();
        runLevelManually = false;
    }

    public ScoreTimer getScoreTimer() {
        return scoreTimer;
    }

    public boolean isLevelStarted() {
        return levelStarted;
    }

    public void setLevelStarted(boolean b) {
        this.levelStarted = b;
    }

    public void setRunManual(boolean b) {
        this.runLevelManually = b;
    }

    public void start() {
        if (!runLevelManually && levelTimerTask == null) {
            levelTimerTask = new LevelTimerTask(breakoutWorld, game, this);
            timer.schedule(levelTimerTask, 0, breakoutWorld.getTimeStepAsMs());
        }

    }

    public void startBall() {
        if (!levelStarted) {
            setLevelStarted(true);
            scoreTimer.start();
            List<Ball> balls = levelState.getBalls();
            for (Ball b : balls) {
                b.setLinearVelocity(0, 200);
            }
        }
    }

    // x coordinate is the center of the most left paddle
    public void movePaddle(Paddle paddle, int x, int y) {
        
        List<Paddle> paddles = levelState.getPaddles();
        int totalWidth = levelState.calculatePaddleWidthWithGaps();
        int paddleWidth = paddles.get(0).getShape().getWidth();
        
        int min = paddleWidth / 2;
        int max = 300 - totalWidth + (paddleWidth / 2);

        if (!levelStarted) {
            float yPos = levelState.getBall().getPosition().y;
            List<Ball> balls = levelState.getBalls();

            float temp = x;
            for (Ball ball : balls) {
                ball.moveTo(temp, yPos);
                temp += paddleWidth;
            }
            
            
        }
        
        float xpos = x;
        if( xpos < min){
            xpos = min;
        } else if (xpos > max) {
            xpos = max;
        }
        
        for(Paddle p : paddles){
            p.moveTo(xpos, p.getPosition().y);
            
            xpos += 2*paddleWidth;
        }

        
    }

    public int getId() {
        return id;
    }

    public LevelState getLevelState() {
        return levelState;
    }

    void resetBall(Ball ball) {
        if (this.getLevelState().getBalls().size() == 1) {
            setLevelStarted(false);
            levelState.removeBall(ball);
            levelState.resetBall(breakoutWorld);
            lives--;
        } else {
            levelState.removeBall(ball);
        }
        game.notifyPlayersOfBallAction();
        game.notifyPlayersOfLivesLeft();
    }

    private int getTargetBricksLeft() {
        return levelState.getTargetBricksLeft();
    }

    public boolean allTargetBricksDestroyed() {
        return getTargetBricksLeft() == 0;
    }

    public void step() {
        breakoutWorld.step();
        game.notifyPlayers(this, breakoutWorld);
    }

    public boolean noLivesLeft() {
        return lives == 0;
    }

    public int getLives() {
        return lives;
    }

    public void stop() {

        if (!runLevelManually && levelTimerTask != null) {
            levelTimerTask.cancel();
            levelTimerTask = null;
        }
    }

    public void togglePaused() {
        if (levelTimerTask == null) {
            start();
        } else {
            stop();
        }
    }

    public void initNextLevel() {
        stop();
        game.initNextLevel();
    }

    public boolean isLastLevel() {
        return isLastLevel;
    }

    @Override
    public void setResetBallFlag(Ball ball) {
        breakoutWorld.setResetBallFlag(ball);
    }

    @Override
    public void ballHitPaddle(Ball ball, Paddle paddle) {
        breakoutWorld.ballHitPaddle(ball, paddle);
    }

    @Override
    public void ballOutOfBounds(Ball ball) {
        breakoutWorld.destroyBody(ball.getBody());
        resetBall(ball);
        /*
        TODO
        probleem oplossen voor als meerdere ballen tegelijk out of bounds gaan
         */
    }

    @Override
    public void removeBrick(Brick brick) {
        levelState.removeBrick(brick);

        if (allTargetBricksDestroyed()) {
            getScoreTimer().stop();

            StaticDummyHighscoreRepo dummyRepo = new StaticDummyHighscoreRepo();

            Score scoreOfPlayer = new Score(getId(), new User("This is a new user"), getScoreTimer().getDuration(), "hard");
            dummyRepo.addScore(scoreOfPlayer);

            dummyRepo.getScoresByLevel(getId(), "hard");
            initNextLevel();
        }
    }

    public void addFloor(FloorPowerUp floor) {
        levelState.addFloor(floor);
        breakoutWorld.setFloorToAdd(floor);
    }

    public void setBrokenPaddle(BrokenPaddlePowerUp bppu) {
        for (Paddle p : bppu.getBrokenPaddle()) {
            levelState.addPaddle(p);
        }
        breakoutWorld.setBrokenPaddleToAdd(bppu);
    }

    public void deSpawn(Paddle p) {
        levelState.removePaddle(p);
    }

    public void addPaddle(Paddle basePaddle) {
        levelState.addPaddle(basePaddle);
    }
}
