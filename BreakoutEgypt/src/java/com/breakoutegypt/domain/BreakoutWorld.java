/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.breakoutegypt.domain;

import com.breakoutegypt.domain.effects.BreakoutPowerUpHandler;
import com.breakoutegypt.domain.effects.BrokenPaddlePowerUp;
import com.breakoutegypt.domain.messages.BrickMessageType;
import com.breakoutegypt.domain.messages.BrickMessage;
import com.breakoutegypt.domain.messages.Message;
import com.breakoutegypt.domain.effects.EffectHandler;
import com.breakoutegypt.domain.effects.FloorPowerUp;
import com.breakoutegypt.domain.effects.PowerUp;
import com.breakoutegypt.domain.effects.PowerUpHandler;
import com.breakoutegypt.domain.messages.PowerUpMessage;
import com.breakoutegypt.domain.messages.PowerUpMessageType;
import com.breakoutegypt.domain.shapes.Ball;
import com.breakoutegypt.domain.shapes.BodyConfiguration;
import com.breakoutegypt.domain.shapes.BodyConfigurationFactory;
import com.breakoutegypt.domain.shapes.bricks.Brick;
import com.breakoutegypt.domain.shapes.Paddle;
import com.breakoutegypt.domain.shapes.RegularBody;
import java.util.ArrayList;
import java.util.List;
import org.jbox2d.collision.shapes.Shape;
import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.Body;
import org.jbox2d.dynamics.BodyDef;
import org.jbox2d.dynamics.FixtureDef;
import org.jbox2d.dynamics.World;

/**
 *
 * @author kevin
 */
public class BreakoutWorld {

    private World world;

    // TODO use these!
    public static final int WIDTH = 600;
    public static final int HEIGHT = 600;
    public static final float TIMESTEP_DEFAULT = 0.5f / 60f;
    private float timestepSeconds;
    private final int velocityIterations = 8;
    private final int positionIterations = 8;
//    private Level currentLevel;
    private BreakoutWorldEventListener listener;
    private PowerUpHandler poweruphandler;

    private boolean isBallOutOfBounds = false;

    // keep a seperate list of bodies to dstroy in the frontend since we only want to send this info once
    private List<Body> bodiesToDestroy;
    private List<String> keysOfBodiesToDestroy;
    private boolean ballHitPaddle = false;
    private List<Message> brickMessages;
    private List<Message> powerupMessages;

    private Ball ballToChangeDirectionOff;
    private Paddle paddleHitByBall;
    private Ball outOfBoundsBall;
    private FloorPowerUp floorToAdd;
    private FloorPowerUp floorInGame;
    private BrokenPaddlePowerUp brokenPaddleToAdd;
    private BrokenPaddlePowerUp brokenPaddle;

    public BreakoutWorld(/*Level level*/) {
        this(/*level, */TIMESTEP_DEFAULT);
    }

    public BreakoutWorld(/*Level level, */float timestepSeconds) {
        bodiesToDestroy = new ArrayList();
        keysOfBodiesToDestroy = new ArrayList();

        world = new World(new Vec2(0.0f, 0.0f));

        brickMessages = new ArrayList();
        powerupMessages = new ArrayList();

        this.timestepSeconds = timestepSeconds;
    }

    public void initContactListener(EffectHandler eventHandler, BallEventHandler ballEventHandler, PowerUpHandler powerupHandler) {
        this.poweruphandler = powerupHandler;
        world.setContactListener(new BreakoutContactListener(eventHandler, ballEventHandler, powerupHandler));
    }

    public long getTimeStepAsMs() {
        return Math.round(Math.floor(timestepSeconds * 1000));
    }

    public void spawn(RegularBody gameObject) {
        BodyConfiguration bodyConfig = gameObject.getConfig();
        BodyDef bodyDef = bodyConfig.getBodyDefinition().getBox2dBodyDef();
        FixtureDef fixtureDef = bodyConfig.getFixtureConfig().getBox2dFixtureDef();
        Shape shape = bodyConfig.getShape();

        fixtureDef.shape = shape;

        Body body = world.createBody(bodyDef);
        body.createFixture(fixtureDef);
        body.setUserData(gameObject);
        gameObject.setBody(body);
    }

    public void destroyBrick(Brick brick) {
        List<Brick> bricks = new ArrayList();
        bricks.add(brick);
        destroyBricks(bricks);
    }

    public void destroyBody(Body body) {
        world.destroyBody(body);
    }

    public void destroyBricks(List<Brick> bricks) {
        String brickName;
        for (Brick brick : bricks) {
            if (!bodiesToDestroy.contains(brick.getBody())) {
                brickName = brick.getName();
                listener.removeBrick(brick);
                bodiesToDestroy.add(brick.getBody());
                brickMessages.add(new BrickMessage(brickName, BrickMessageType.DESTROY));
            }
        }
    }

    // TODO this probably belongs in LevelState
    public void toggleBricks(List<Brick> switchBricks) {
        for (Brick switchBrick : switchBricks) {
            switchBrick.toggle();

            BrickMessageType toggleType;
            if (switchBrick.isVisible()) {
                toggleType = BrickMessageType.SHOW;
            } else {
                toggleType = BrickMessageType.HIDE;
            }

            brickMessages.add(new BrickMessage(switchBrick.getName(), toggleType));

        }
    }

    void ballHitPaddle(Ball ball, Paddle paddle) {
        ballHitPaddle = true;
        ballToChangeDirectionOff = ball;
        paddleHitByBall = paddle;
    }

    public void addBrickMessage(Message m) {
        brickMessages.add(m);
    }

    public List<Message> getBrickMessages() {
        return brickMessages;
    }

    public void clearBrickMessages() {
        brickMessages.clear();
    }

    public void addPowerupMessages(Message m) {
        powerupMessages.add(m);
    }

    public void clearPowerupMessages() {
        powerupMessages.clear();
    }

    public List<Message> getPowerupMessages() {
        return powerupMessages;
    }

    // any changes to the world state must be made here to try to avoid concurrency issues where the game is 
    // updating some state while we are changing it too
    public void step() {
        world.step(timestepSeconds, velocityIterations, positionIterations);
        for (Body brick : bodiesToDestroy) {

            world.destroyBody(brick);
        }
        bodiesToDestroy.clear();

        if (ballHitPaddle) {
//            adjustBallDirection();
            ballHitPaddle = false;
        } else if (isBallOutOfBounds) {
            listener.ballOutOfBounds(outOfBoundsBall);
            isBallOutOfBounds = false;
        }
        addBrokenPaddleIfNecessary();
        addFloorIfNecessary();
    }

    private void addBrokenPaddleIfNecessary() {

        if (brokenPaddleToAdd != null) {
            this.deSpawn(brokenPaddleToAdd.getBasePaddle().getBody());
            for (Paddle p : brokenPaddleToAdd.getBrokenPaddle()) {
                this.spawn(p);
            }
            this.addPowerupMessages(new PowerUpMessage("brokenPaddle", brokenPaddleToAdd, PowerUpMessageType.ADDBROKENPADDLE));
            this.brokenPaddle = brokenPaddleToAdd;
            brokenPaddleToAdd = null;
        } else if (brokenPaddle != null) {
            int timeLeft = brokenPaddle.getTimeVisable();
            if (timeLeft > 0) {
                brokenPaddle.setTimeVisable(timeLeft - 1);
            } else {
                Paddle base = brokenPaddle.getBasePaddle();

                poweruphandler.handleRemoveBrokenPaddle(brokenPaddle);
                
                for (Paddle p : brokenPaddle.getBrokenPaddle()) {
                    this.deSpawn(p.getBody());
                }

                addPowerupMessages(new PowerUpMessage("name", brokenPaddle, PowerUpMessageType.REMOVEBROKENPADDLE));
                brokenPaddle = null;
                spawn(base);
            }
        }
    }

    private void addFloorIfNecessary() {
        if (floorToAdd != null && floorToAdd.isVisable()) {
            if (floorInGame != null) {
                world.destroyBody(floorInGame.getBody());
                floorInGame = null;
            }
            spawn(floorToAdd);
            floorInGame = floorToAdd;
            floorToAdd = null;
            addPowerupMessages(new PowerUpMessage("name", floorInGame, PowerUpMessageType.ADDFLOOR));
        } else if (floorInGame != null) {
            int timeLeft = floorInGame.getTimeVisable();
            if (timeLeft > 0) {
                floorInGame.setTimeVisable(timeLeft - 1);
            } else {
                world.destroyBody(floorInGame.getBody());
                addPowerupMessages(new PowerUpMessage("name", floorInGame, PowerUpMessageType.REMOVEFLOOR));
                floorInGame = null;
            }
        }
    }

    private void adjustBallDirection() {
        float width = paddleHitByBall.getShape().getWidth();
        float adjustedX = paddleHitByBall.getBody().getPosition().x - width / 2;
        float relativeDistance = (ballToChangeDirectionOff.getBody().getPosition().x - adjustedX) / width;

        float newX = -100 + relativeDistance * 200;
        ballToChangeDirectionOff.setLinearVelocity(newX, ballToChangeDirectionOff.getLinearVelocity().y);
    }

    public int countWorldObjects() {
        return world.getBodyCount();
    }

    public void setResetBallFlag(Ball ball) {
        isBallOutOfBounds = true;
        outOfBoundsBall = ball;
    }

    public void setBreakoutWorldEventListener(BreakoutWorldEventListener listener) {
        this.listener = listener;
    }

    void setFloorToAdd(FloorPowerUp floorToAdd) {
        this.floorToAdd = floorToAdd;
    }

    public void destroyBricks(Level level, LevelState levelState, List<Brick> bricks) {
        String brickName;
        for (Brick brick : bricks) {
            if (!bodiesToDestroy.contains(brick.getBody())) {
                brickName = brick.getName();

                if (brick.hasPowerUp()) {
                    PowerUp pu = brick.getPowerUp();
                    pu.accept(new BreakoutPowerUpHandler(level, levelState, this));
                }

                listener.removeBrick(brick);
                bodiesToDestroy.add(brick.getBody());
                brickMessages.add(new BrickMessage(brickName, BrickMessageType.DESTROY));
            }
        }
    }

    void deSpawn(Body rb) {
        world.destroyBody(rb);
    }

    void setBrokenPaddleToAdd(BrokenPaddlePowerUp bppu) {
        brokenPaddleToAdd = bppu;
    }
}
