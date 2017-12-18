/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.breakoutegypt.domain;

import com.breakoutegypt.data.Repositories;
import com.breakoutegypt.domain.powers.FloorPowerUp;
import com.breakoutegypt.domain.effects.BreakoutEffectHandler;
import com.breakoutegypt.domain.levelprogression.Difficulty;
import com.breakoutegypt.domain.messages.PowerDownMessageType;
import com.breakoutegypt.domain.powers.BreakoutPowerUpHandler;
import com.breakoutegypt.domain.powers.BrokenPaddlePowerUp;
import com.breakoutegypt.domain.powers.PowerUp;
import com.breakoutegypt.domain.messages.PowerUpMessage;
import com.breakoutegypt.domain.messages.PowerUpMessageType;
import com.breakoutegypt.domain.messages.ProjectilePositionMessage;
import com.breakoutegypt.domain.powers.BreakoutPowerDownHandler;
import com.breakoutegypt.domain.shapes.Ball;
import com.breakoutegypt.domain.shapes.bricks.Brick;
import com.breakoutegypt.domain.shapes.Paddle;
import com.breakoutegypt.domain.shapes.Projectile;
import com.breakoutegypt.domain.shapes.RegularBody;
import java.util.List;
import java.util.Timer;
import com.breakoutegypt.data.HighscoreRepository;

/**
 * keeps track of all the objects present in the level, only one level for now
 *
 * @author kevin
 */
public class Level implements BreakoutWorldEventListener {

    private int id;

    private Timer timer;
    private Game game;

    private BreakoutWorld breakoutWorld;

    private int lives;

    // TODO use isLastLevel so frontend doesn't have to get 2 messagse, it can just know when loading the last level that it's the last level
    private boolean isLastLevel;
    private LevelState levelState;

    private boolean levelStarted;

    private TimeScore scoreTimer;
    private LevelTimerTask levelTimerTask;
    private boolean runLevelManually;

    private BrickScoreCalculator brickScoreCalc;

    private boolean invertedControls;
    private BreakoutPowerUpHandler bpuh;
    private final BreakoutPowerDownHandler bpdh;
    public Level(int id, Game game, LevelState initialObjects) {
        this(id, game, initialObjects, BreakoutWorld.TIMESTEP_DEFAULT);
    
    
  }

    private Level(int id, Game game, LevelState initialState, float worldTimeStepInMs) {
        this.id = id;
        this.game = game;
        this.isLastLevel = isLastLevel;

        this.levelStarted = false;

        scoreTimer = new TimeScore();
        this.brickScoreCalc = new BrickScoreCalculator(game.getDifficulty().getPointsPerBlock());

        breakoutWorld = new BreakoutWorld(worldTimeStepInMs);

        levelState = initialState;
        List<RegularBody> bodiesToSpawn = levelState.getAllObjectsToSpawn();
        for (RegularBody rb : bodiesToSpawn) {
            breakoutWorld.spawn(rb);
        }

        bpuh = new BreakoutPowerUpHandler(this, levelState, breakoutWorld);
        bpdh = new BreakoutPowerDownHandler(this, levelState, breakoutWorld);

        breakoutWorld.setBreakoutWorldEventListener(this);
        breakoutWorld.initContactListener(
                new BreakoutEffectHandler(this, levelState, breakoutWorld),
                bpuh, bpdh);

        this.lives = game.getInitialLives();
        this.timer = new Timer();
        runLevelManually = false;
        this.invertedControls = false;
    }

    public boolean isInvertedControls() {
        return invertedControls;
    }

    public void invertControls() {
        this.invertedControls = !this.invertedControls;
    }

    public TimeScore getScoreTimer() {
        return scoreTimer;
    }

    public boolean isLevelStarted() {
        return levelStarted;
    }

    public void setLevelNumber(int id) {
        this.id = id;
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
                b.setLinearVelocity(0, game.getDifficulty().getBallspeed());
            }
        }
    }

    // x coordinate is the center of the most left paddle
    public void movePaddle(Paddle paddle, int firstPaddleCenter, int y) {
        
        List<Paddle> paddles = levelState.getPaddles();
        int totalWidth = levelState.calculatePaddleWidthWithGaps();
        int paddleWidth = paddles.get(0).getWidth();
        // x is the center of the most left paddle
        int min = paddleWidth / 2;
        int max = BreakoutWorld.DIMENSION - totalWidth + (paddleWidth / 2);

        if (invertedControls) {
            firstPaddleCenter = BreakoutWorld.DIMENSION - firstPaddleCenter;
        }
        
        float paddleCenter = firstPaddleCenter;
        if (paddleCenter < min) {
            paddleCenter = min;
        } else if (paddleCenter > max) {
            paddleCenter = max;
        }

        for (Paddle p : paddles) {
            p.moveTo(paddleCenter, p.getPosition().y);
            paddleCenter += (paddleWidth + BrokenPaddlePowerUp.GAP);
        }

        if (!levelStarted) {
            float yPos = levelState.getBall().getPosition().y;
            List<Ball> balls = levelState.getBalls();

            float temp = firstPaddleCenter;
            for (Ball ball : balls) {
                ball.moveTo(temp, yPos);
                temp += paddleWidth;
            }

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

            loseLifeBasedOnDifficulty();
        } else {
            levelState.removeBall(ball);
        }
        game.notifyPlayersOfBallAction();
        game.notifyPlayersOfLivesLeft();
    }
    
    private void loseLifeBasedOnDifficulty(){
        Difficulty diff = game.getDifficulty();
       
        if(diff.getLives() != Difficulty.INFINITE_LIVES){
            lives--;
        }
    }

    private int getTargetBricksLeft() {
        return levelState.getTargetBricksLeft();
    }

    public boolean allTargetBricksDestroyed() {
        return getTargetBricksLeft() == 0;
    }

    public void step() {
        breakoutWorld.step();
        game.notifyPlayers(this, breakoutWorld.getMessageRepo());
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
        scoreTimer.pauseTimer();
    }

    public void initNextLevel() {
        stop();
        game.initNextLevel();
    }

    public boolean isLastLevel() {
        return isLastLevel;
    }

    @Override
    public void ballOutOfBounds(Ball ball) {
        breakoutWorld.deSpawn(ball.getBody());
        resetBall(ball);
    }

    @Override
    public void removeBrick(Brick brick) {
        levelState.removeBrick(brick);
        brickScoreCalc.addPointsToScore();

        if (allTargetBricksDestroyed()) {
            getScoreTimer().stop();

            HighscoreRepository highScoreRepo = Repositories.getHighscoreRepository();

            int brickScore = brickScoreCalc.getScore();
            Score scoreOfPlayer = new Score(getId(), new User("This is a new user"), getScoreTimer().getDuration(), game.getDifficulty().getName(), brickScore - (int)getScoreTimer().getDuration());
            highScoreRepo.addScore(scoreOfPlayer);
            
            initNextLevel();
        }
    }

    @Override
    public void ballHitPaddle() {
        brickScoreCalc.resetMultiplier();
    }

    public void addFloor(FloorPowerUp floor) {
        levelState.addFloor(floor);
    }

    public void addPaddle(Paddle basePaddle) {
        levelState.addPaddle(basePaddle);
    }

    public int getBrickScore() {
        return brickScoreCalc.getScore();
    }

    public PowerUpMessage triggerPowerup(String powerup) {
        PowerUp p = bpuh.getPowerupByName(powerup);

        PowerUpMessage msg = new PowerUpMessage("You are trying to trigger a powerup that doesn't exist", null, PowerUpMessageType.NOSUCHPOWERUP);

        if (p != null) {
            msg = p.accept(bpuh);
        }
        return msg;
    }

    public BreakoutPowerUpHandler getPoweruphandler() {
        return bpuh;
    }

    @Override
    public ProjectilePositionMessage destroyProjectile(Projectile projectile, boolean lostLife) {
        levelState.removeProjectile(projectile);
        breakoutWorld.deSpawn(projectile.getBody());
        if (lostLife) {
            lives--;
            game.notifyPlayersOfLivesLeft();
        }
        return new ProjectilePositionMessage(projectile, PowerDownMessageType.REMOVEPROJECTILE);
    }
}
