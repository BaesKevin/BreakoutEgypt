/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.breakoutegypt.domain;

import com.breakoutegypt.data.StaticDummyHighscoreRepo;
import com.breakoutegypt.domain.actionmessages.BallMessage;
import com.breakoutegypt.domain.actionmessages.BallMessageType;
import com.breakoutegypt.domain.effects.BreakoutEffectHandler;
import com.breakoutegypt.domain.shapes.Ball;
import com.breakoutegypt.domain.shapes.bricks.Brick;
import com.breakoutegypt.domain.shapes.Paddle;
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
        levelState.spawnAllObjects(breakoutWorld);

        breakoutWorld.setBreakoutWorldEventListener(this);
        breakoutWorld.initContactListener(
                new BreakoutEffectHandler(levelState, breakoutWorld),
                this);

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
            System.out.printf("Level: start level %d", this.id);
            levelTimerTask = new LevelTimerTask(breakoutWorld, game, this);
            System.out.printf("Expected timestep: %d, actual timestep: %d", 1000 / 60, breakoutWorld.getTimeStepAsMs());
            timer.schedule(levelTimerTask, 0, breakoutWorld.getTimeStepAsMs());
        } else {
            System.out.println("Level: trying to start the level twice, ignoring call");
        }

    }

    public void startBall() {
        if (!levelStarted) {
            setLevelStarted(true);
            scoreTimer.start();
            List<Ball> balls = levelState.getBalls();
            for (Ball b : balls) {
                b.setLinearVelocity(0, 100);
            }
            System.out.println("Level: startBall()");
        }
    }

    public void movePaddle(Paddle paddle, int x, int y) {
        if (!levelStarted) {
            float yPos = levelState.getBall().getPosition().y;
            List<Ball> balls = levelState.getBalls();
            if (balls.size() > 0) {
                balls.get(0).moveTo(x, yPos);
            }
        }

        paddle.moveTo(x, y);
    }

    public int getId() {
        return id;
    }

    public LevelState getLevelState() {
        return levelState;
    }

    void resetBall(Ball ball) {
        System.out.println("Level: resetBall()");
        if (this.getLevelState().getBalls().size() == 1) {
            setLevelStarted(false);
            levelState.removeBall(ball);
            levelState.resetBall(breakoutWorld);
            lives--;
        } else {
            levelState.removeBall(ball);
            levelState.getMessages().add(new BallMessage(ball.getName(), BallMessageType.REMOVE));
        }
        System.out.println("Balls left: " + this.getLevelState().getBalls().size());
        game.notifyPlayersOfBallAction();
        game.notifyPlayersOfLivesLeft();
    }

    private int getTargetBricksLeft() {
        System.out.println("Targets left: " + levelState.getTargetBricksLeft());
        return levelState.getTargetBricksLeft();
    }

    public boolean allTargetBricksDestroyed() {
        return getTargetBricksLeft() == 0;
    }

    public void step() {
        breakoutWorld.step();
    }

    public boolean noLivesLeft() {
        return lives == 0;
    }

    public int getLives() {
        return lives;
    }

    public void stop() {
        System.out.printf("Level: stop level %d", this.id);

        if (!runLevelManually) {
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
            System.out.println("BreakoutWorld: all brick destroyed");
            getScoreTimer().stop();

            StaticDummyHighscoreRepo dummyRepo = new StaticDummyHighscoreRepo();

            Score scoreOfPlayer = new Score(getId(), new User("This is a new user"), getScoreTimer().getDuration(), "hard");
            dummyRepo.addScore(scoreOfPlayer);

            dummyRepo.getScoresByLevel(getId(), "hard");
            initNextLevel();
        }
    }
}
