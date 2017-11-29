/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.breakoutegypt.domain;

import com.breakoutegypt.domain.messages.BrickMessageType;
import com.breakoutegypt.domain.messages.BrickMessage;
import com.breakoutegypt.domain.messages.Message;
import com.breakoutegypt.domain.effects.EffectHandler;
import com.breakoutegypt.domain.shapes.Ball;
import com.breakoutegypt.domain.shapes.BodyConfiguration;
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
    
    private boolean isBallOutOfBounds = false;

    // keep a seperate list of bodies to dstroy in the frontend since we only want to send this info once
    private List<Body> bodiesToDestroy;
    private List<String> keysOfBodiesToDestroy;
    private boolean ballHitPaddle = false;
    private List<Message> messages;

    private Ball ballToChangeDirectionOff;
    private Paddle paddleHitByBall;
    private Ball outOfBoundsBall;
    
    public BreakoutWorld(/*Level level*/) {
        this(/*level, */TIMESTEP_DEFAULT);
    }
    
    public BreakoutWorld(/*Level level, */float timestepSeconds){
        bodiesToDestroy = new ArrayList();
        keysOfBodiesToDestroy = new ArrayList();
        
        world = new World(new Vec2(0.0f, 0.0f));
        

//        this.currentLevel = level;
        messages = new ArrayList();
        
        this.timestepSeconds = timestepSeconds;
    }
    
    public void initContactListener(EffectHandler eventHandler, BallEventHandler ballEventHandler){
        world.setContactListener(new BreakoutContactListener(eventHandler, ballEventHandler));
    }
    
    public long getTimeStepAsMs(){
        return Math.round(Math.floor(timestepSeconds * 1000));
    }
    
    public void spawn(RegularBody gameObject){
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

    public void destroyBody(Body body){
        world.destroyBody(body);
    }
    
    public void destroyBricks(List<Brick> bricks) {
        String brickName;
        for (Brick brick : bricks) {
            if (!bodiesToDestroy.contains(brick.getBody())) {
                brickName = brick.getName();
//                currentLevel.removeBrick(brick);
                listener.removeBrick(brick);
                bodiesToDestroy.add(brick.getBody());
                messages.add(new BrickMessage(brickName, BrickMessageType.DESTROY));
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

            messages.add(new BrickMessage(switchBrick.getName(), toggleType));

        }
    }

    void ballHitPaddle(Ball ball, Paddle paddle) {
        ballHitPaddle = true;
        ballToChangeDirectionOff = ball;
        paddleHitByBall = paddle;
    }

    public void addMessage(Message m) {
        messages.add(m);
    }
    
    public List<Message> getMessages() {
        return messages;
    }

    public void clearMessages() {
        messages.clear();
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
    }

    private void adjustBallDirection() {
        float width = paddleHitByBall.getShape().getWidth();
        float adjustedX = paddleHitByBall.getBody().getPosition().x - width / 2;
        float relativeDistance = (ballToChangeDirectionOff.getBody().getPosition().x - adjustedX) / width;

        float newX = -100 + relativeDistance * 200;
        ballToChangeDirectionOff.setLinearVelocity(newX, ballToChangeDirectionOff.getLinearVelocity().y);
    }

    public int countWorldObjects(){
        return world.getBodyCount();
    }

    public void setResetBallFlag(Ball ball) {
        isBallOutOfBounds = true;
        outOfBoundsBall = ball;
    }

    public void setBreakoutWorldEventListener(BreakoutWorldEventListener listener){
        this.listener = listener;
    }
    
}
