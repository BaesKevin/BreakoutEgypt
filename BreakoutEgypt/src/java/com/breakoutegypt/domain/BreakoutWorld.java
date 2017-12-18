/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.breakoutegypt.domain;

import com.breakoutegypt.domain.brickcollisionhandlers.BallBrickContact;
import com.breakoutegypt.domain.brickcollisionhandlers.BallGroundContact;
import com.breakoutegypt.domain.brickcollisionhandlers.BallPaddleContact;
import com.breakoutegypt.domain.brickcollisionhandlers.BrickCollisionDecider;
import com.breakoutegypt.domain.brickcollisionhandlers.Contact;
import com.breakoutegypt.domain.brickcollisionhandlers.ContactHandler;
import com.breakoutegypt.domain.brickcollisionhandlers.ProjectileGroundContact;
import com.breakoutegypt.domain.brickcollisionhandlers.ProjectilePaddleContact;
import com.breakoutegypt.domain.powers.AcidBallPowerUp;
import com.breakoutegypt.domain.messages.BrickMessageType;
import com.breakoutegypt.domain.messages.BrickMessage;
import com.breakoutegypt.domain.effects.EffectHandler;
import com.breakoutegypt.domain.effects.ExplosiveEffect;
import com.breakoutegypt.domain.messages.PowerDownMessage;
import com.breakoutegypt.domain.powers.PowerUp;
import com.breakoutegypt.domain.powers.PowerUpHandler;
import com.breakoutegypt.domain.messages.PowerUpMessage;
import com.breakoutegypt.domain.messages.PowerUpMessageType;
import com.breakoutegypt.domain.powers.PowerDown;
import com.breakoutegypt.domain.powers.PowerDownHandler;
import com.breakoutegypt.domain.shapes.Ball;
import com.breakoutegypt.domain.shapes.BodyConfiguration;
import com.breakoutegypt.domain.shapes.bricks.Brick;
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
public class BreakoutWorld implements ContactHandler {

    private World world;

    public static final int DIMENSION = 100;
    
    public static final float TIMESTEP_DEFAULT = 1f / 60f;
    private float timestepSeconds;
    private final int velocityIterations = 8;
    private final int positionIterations = 8;

    private BreakoutWorldEventListener worldEventListener;

    private List<Contact> contacts;
    private EffectHandler effectHandler;
    private PowerUpHandler powerupHandler;
    private PowerDownHandler powerdownHandler;
    private ServerClientMessageRepository messageRepo;

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
        powerdownHandler.removeInvertedControlIfTimedOut();
    }

    public void initContactListener(EffectHandler eventHandler, PowerUpHandler powerupHandler, PowerDownHandler powerdownHandler) {
        this.powerupHandler = powerupHandler;
        this.effectHandler = eventHandler;
        this.powerdownHandler = powerdownHandler;

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

    public ServerClientMessageRepository getMessageRepo() {
        return messageRepo;
    }

    @Override
    public void handle(BallBrickContact bbc) {
        Brick b = bbc.getBrick();
        Ball ball = bbc.getBall();
        AcidBallPowerUp abpu = ball.getAcidBall();
        if (!ball.isDecoy()) {
            if (abpu != null) {
                if (!b.hasToggleEffect()) {
                    b.addEffect(new ExplosiveEffect(b, abpu.getRange()));
                }
                ball.setAcidballPowerup(null);
                messageRepo.addPowerupMessages(new PowerUpMessage(abpu.getName(), abpu, PowerUpMessageType.REMOVEACIDBALL));
            }
            new BrickCollisionDecider(b, this.effectHandler).handleCollision();
        }
    }

    @Override
    public void handle(BallGroundContact bgc) {
        worldEventListener.ballOutOfBounds(bgc.getOutofbounds());
    }

    @Override
    public void handle(BallPaddleContact bpc) {
        worldEventListener.ballHitPaddle();
    }
    
    @Override
    public void handle(ProjectilePaddleContact ppc) {
        messageRepo.addPowerdownMessages(worldEventListener.destroyProjectile(ppc.getProjectile(), true));
    }

    @Override
    public void handle(ProjectileGroundContact pgc) {
        messageRepo.addPowerdownMessages(worldEventListener.destroyProjectile(pgc.getProjectile(), false));
    }

    public int countWorldObjects() {
        return world.getBodyCount();
    }

    public void setBreakoutWorldEventListener(BreakoutWorldEventListener listener) {
        this.worldEventListener = listener;
    }

    // add playerToGivePowerUps parameter
    public void destroyBricks(List<Brick> bricks) {
        String brickName;
        for (Brick brick : bricks) {
            brickName = brick.getName();
            if (brick.hasPowerUp()) {
                PowerUp pu = brick.getPowerUp();
                powerupHandler.addPowerUp(pu);
            } 
            if (brick.hasPowerDown()) {
                PowerDown pd = brick.getPowerDown();
                powerdownHandler.handle(pd);
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

}
