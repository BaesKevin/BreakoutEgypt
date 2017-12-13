/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.breakoutegypt.domain.powers;

import com.breakoutegypt.domain.messages.PowerDownMessage;
import com.breakoutegypt.domain.messages.PowerDownMessageType;
import com.breakoutegypt.domain.shapes.Ball;
import com.breakoutegypt.domain.shapes.ShapeDimension;
import static java.lang.Math.abs;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import javax.json.Json;
import javax.json.JsonArrayBuilder;
import javax.json.JsonObject;
import javax.json.JsonObjectBuilder;

/**
 *
 * @author BenDB
 */
public class FloodPowerDown implements PowerDown {

    private final int noOfBalls;
    private final List<Ball> balls;
    private final Ball originalBall;
    private final int decoyBallSpeed = 150;

    public FloodPowerDown(Ball originalBall, int noOfBalls) {
        this.noOfBalls = noOfBalls;
        this.balls = new ArrayList();
        this.originalBall = originalBall;
    }

    public void initBalls() {
        float x = originalBall.getPosition().x;
        float y = originalBall.getPosition().y;
        int width = originalBall.getShape().getWidth();
        for (int i = 0; i < noOfBalls; i++) {
            ShapeDimension s = new ShapeDimension("decoy" + Math.ceil(x+i*x*x/y), x, y, width, width);
            Ball b = new Ball(s, true);
            balls.add(b);
        }
    }

    public List<Ball> getBalls() {
        return balls;
    }

    @Override
    public PowerDownMessage accept(PowerDownHandler pdh) {
        return pdh.handle(this);
    }

    @Override
    public JsonObject toJson() {
        JsonArrayBuilder jab = Json.createArrayBuilder();
        JsonObjectBuilder job = Json.createObjectBuilder();
        for (Ball b : balls) {
            jab.add(b.getShape().toJson());
        }
        job.add("decoyballs", jab.build());
        job.add("powerdownname", getName());

        return job.build();
    }

    @Override
    public String getName() {
        return "TODO";
    }

    @Override
    public PowerDownMessageType getType() {
        return PowerDownMessageType.FLOOD;
    }

    public void doPulse() {
        Random r = new Random();
        for (Ball b : balls) {
            int x = r.nextInt(2 * decoyBallSpeed) - decoyBallSpeed;
            boolean isYnegative = r.nextInt() <= 0.5;
            int y = decoyBallSpeed - abs(x);
            if (isYnegative) {
                y = -y;
            }
            b.setLinearVelocity(x, y);
        }
    }

}
