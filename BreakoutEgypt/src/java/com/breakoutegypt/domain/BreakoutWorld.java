/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.breakoutegypt.domain;

import com.breakoutegypt.domain.brickcollisionhandlers.BallBrickContact;
import com.breakoutegypt.domain.brickcollisionhandlers.BallGroundContact;
import com.breakoutegypt.domain.brickcollisionhandlers.BrickCollisionDecider;
import com.breakoutegypt.domain.brickcollisionhandlers.Contact;
import com.breakoutegypt.domain.brickcollisionhandlers.ContactHandler;
import com.breakoutegypt.domain.messages.BrickMessageType;
import com.breakoutegypt.domain.messages.BrickMessage;
import com.breakoutegypt.domain.effects.EffectHandler;
import com.breakoutegypt.domain.effects.ExplosiveEffect;
import com.breakoutegypt.domain.effects.PowerUp;
import com.breakoutegypt.domain.effects.PowerUpHandler;
import com.breakoutegypt.domain.shapes.BodyConfiguration;
import com.breakoutegypt.domain.shapes.bricks.Brick;
import com.breakoutegypt.domain.shapes.RegularBody;
import com.breakoutegypt.domain.shapes.bricks.BrickType;
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
public class BreakoutWorld implements ContactHandler {

    private World world;

    // TODO use these!
    public static final int WIDTH = 600;
    public static final int HEIGHT = 600;
    public static final float TIMESTEP_DEFAULT = 0.5f / 60f;
    private float timestepSeconds;
    private final int velocityIterations = 8;
    private final int positionIterations = 8;
    
    private BreakoutWorldEventListener worldEventListener;

    private List<Contact> contacts;
    private EffectHandler effectHandler;
    private PowerUpHandler powerupHandler;
    private ServerClientMessageRepository messageRepo;
    private boolean acidBall = false;
    
    public BreakoutWorld() {
        this(TIMESTEP_DEFAULT);
    }

    public BreakoutWorld(float timestepSeconds) {
        world = new World(new Vec2(0.0f, 0.0f));
        contacts = new ArrayList();

        messageRepo = new ServerClientMessageRepository();
        this.timestepSeconds = timestepSeconds;
    }

    // any changes to the world state must be made here to try to avoid concurrency issues where the game is 
    // updating some state while we are changing it too
    public void step() {
        world.step(timestepSeconds, velocityIterations, positionIterations);

        for (Contact contact : contacts) {
            contact.accept(this);
        }
        contacts = new ArrayList();
        
        powerupHandler.removePowerupsIfTimedOut();
    }

    public void initContactListener(EffectHandler eventHandler, PowerUpHandler powerupHandler) {
        this.powerupHandler = powerupHandler;
        this.effectHandler = eventHandler;

        world.setContactListener(new BreakoutContactListener(this));
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

    public void deSpawn(Body rb) {
        world.destroyBody(rb);
    }

    public ServerClientMessageRepository getMessageRepo(){
        return messageRepo;
    }

    @Override
    public void handle(BallBrickContact bbc) {
        Brick b = bbc.getBrick();
        if (acidBall) {
            b.setType(BrickType.EXPLOSIVE);
            b.addEffect(new ExplosiveEffect(b, 1));
            acidBall = false;
        }
        new BrickCollisionDecider(b, this.effectHandler).handleCollision();
    }

    @Override
    public void handle(BallGroundContact bgc) {
        worldEventListener.ballOutOfBounds(bgc.getOutofbounds());
    }

    public int countWorldObjects() {
        return world.getBodyCount();
    }

    public void setBreakoutWorldEventListener(BreakoutWorldEventListener listener) {
        this.worldEventListener = listener;
    }

    public void destroyBricks(List<Brick> bricks) {
        String brickName;
        for (Brick brick : bricks) {
            brickName = brick.getName();

            if (brick.hasPowerUp()) {
                PowerUp pu = brick.getPowerUp();
                pu.accept(powerupHandler);
            }

            worldEventListener.removeBrick(brick);
            this.deSpawn(brick.getBody());
            messageRepo.addBrickMessage(new BrickMessage(brickName, BrickMessageType.DESTROY));
        }
    }

    void addContact(Contact contact) {
        this.contacts.add(contact);
    }

    public long getTimeStepAsMs() {
        return Math.round(Math.floor(timestepSeconds * 1000));
    }

    public void setAcidBall() {
        acidBall = true;
    }

}
