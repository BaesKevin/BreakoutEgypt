/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.breakoutegypt.domain;

import com.breakoutegypt.data.Repositories;
import com.breakoutegypt.domain.powers.FloorPowerUp;
import com.breakoutegypt.domain.effects.BreakoutEffectHandler;
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
import java.util.HashMap;
import java.util.Map;

/**
 * keeps track of all the objects present in the level, only one level for now
 *
 * @author kevin
 */
public class Level implements BreakoutWorldEventListener {

    private int id;
    private int levelNumber;
    private String levelName="";
    private String levelDescription="";
    private int levelPackId;
    
    private Timer timer;
    private Game game;

    private BreakoutWorld breakoutWorld;

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
    private Map<Integer, Boolean> startedLevelMap;
    
    public Level(int id, Game game, LevelState initialObjects) {
        this(id, game, initialObjects, BreakoutWorld.TIMESTEP_DEFAULT);
    
    }
    
    public Level(int id,String name,String description, Game game, LevelState initialObjects){
        this(id, game, initialObjects);
        this.levelName=name;
        this.levelDescription=description;
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

        this.timer = new Timer();
        runLevelManually = false;
        this.invertedControls = false;
        startedLevelMap = new HashMap();
        
        this.levelName = "level" + id;
        this.levelDescription = "description";
    }

    public void setId(int id){ this.id = id; }
    
    public String getLevelName() {
        return levelName;
    }

    public String getLevelDescription() {
        return levelDescription;
    }

    public void setLevelName(String levelName) {
        this.levelName = levelName;
    }

    public void setLevelDescription(String levelDescription) {
        this.levelDescription = levelDescription;
    }
    
    public int getLevelNumber(){ return levelNumber; }
    public void setLevelNumber( int levelNumber ) { this.levelNumber = levelNumber; } 

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

    public int getLevelPackId() {
        return levelPackId;
    }

    public void setLevelPackId(int levelPackId) {
        this.levelPackId = levelPackId;
    }

    
//    public void setLevelNumber(int id) {
//        this.id = id;
//    }

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

    // should not be used by real classes, still here for tests
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

    public void startBall(int playerIndex) {
        if (startedLevelMap.get(playerIndex) == null || startedLevelMap.get(playerIndex) == false) {
            startedLevelMap.put(playerIndex, true);
            List<Ball> balls = levelState.getBalls();

            for (Ball ball : balls) {
                if (ball.getPlayerIndex() == playerIndex) {
                    scoreTimer.start();
                    ball.setLinearVelocity(0, game.getDifficulty().getBallspeed());
                }
            }
        }

    }

    // x coordinate is the center of the most left paddle
    public synchronized void movePaddle(int playerIndex, int firstPaddleCenter, int y) {
        List<Paddle> paddles = levelState.getPaddlesForPlayer(playerIndex);

        int totalWidth = levelState.calculatePaddleWidthWithGaps(paddles);
        int paddleWidth = paddles.get(0).getWidth();
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

//        if (!levelStarted) {
//            float yPos = levelState.getBall().getPosition().y;
//            List<Ball> balls = levelState.getBalls();
//
//            float temp = firstPaddleCenter;
//            for (Ball ball : balls) {
//                ball.moveTo(temp, yPos);
//                temp += paddleWidth;
//            }
//
//        }
    }

    public int getId() {
        return id;
    }

    public LevelState getLevelState() {
        return levelState;
    }

    void resetBall(Ball ball) {
        int playerIndex = ball.getPlayerIndex();

        if (!ball.isDecoy()) {
            game.loseLife(playerIndex);
        }

        levelState.removeBall(ball);
        if (levelState.noMoreBallsForPlayer(ball.getPlayerIndex())) {
            this.startedLevelMap.put(playerIndex, false);

            levelState.resetBall(breakoutWorld, ball.getPlayerIndex());
        }

        game.notifyPlayersOfBallAction();
        game.notifyPlayersOfLivesLeft(ball.getPlayerIndex());
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

    public void initNextLevel(int winnerIndex) {
        stop();
        game.initNextLevel(winnerIndex);
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
            int winnerIndex = brick.getPlayerIndex();
            getScoreTimer().stop();

            HighscoreRepository highScoreRepo = Repositories.getHighscoreRepository();

            int brickScore = brickScoreCalc.getScore();
            //TODO user with playerindex x
            Score scoreOfPlayer = new Score(getId(), new User("This is a new user"), getScoreTimer().getDuration(), game.getDifficulty().getName(), brickScore - (int) getScoreTimer().getDuration());
            highScoreRepo.addScore(scoreOfPlayer);

            initNextLevel(winnerIndex);
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

    public PowerUpMessage triggerPowerup(String powerup, int playerIndex) {
        PowerUp p = bpuh.getPowerupByName(powerup, playerIndex);

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
            game.loseLife(projectile.getPlayerIndex());
            game.notifyPlayersOfLivesLeft(projectile.getPlayerIndex());
        }
        return new ProjectilePositionMessage(projectile, PowerDownMessageType.REMOVEPROJECTILE);
    }
}
