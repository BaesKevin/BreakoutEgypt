/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.breakoutegypt.domain.powers;

import com.breakoutegypt.domain.BreakoutWorld;
import com.breakoutegypt.domain.messages.PowerUpMessage;
import com.breakoutegypt.domain.messages.PowerUpMessageType;
import com.breakoutegypt.domain.shapes.Paddle;
import com.breakoutegypt.domain.shapes.ShapeDimension;
import java.util.ArrayList;
import java.util.List;
import javax.json.Json;
import javax.json.JsonArrayBuilder;
import javax.json.JsonObject;
import javax.json.JsonObjectBuilder;

/**
 *
 * @author BenDB
 */
public class BrokenPaddlePowerUp implements PowerUp {
    
    private boolean isVisible;
    private int timeVisible; //TODO variable time in seconds (not steps)
    private int startTime;

    private Paddle base;
    private List<Paddle> brokenPaddle;
    public static final int GAP = 6;
    
    private String name;
    
    private int playerId;

    public BrokenPaddlePowerUp(Paddle p,  int i, int timeVisible) {
        this.name = "brokenpaddle" + i;
        this.timeVisible = timeVisible;
        base = p;
        brokenPaddle = new ArrayList();
        breakPaddle();
        startTime = timeVisible;
        this.playerId = 1;
    }

    public List<Paddle> getBrokenPaddle() {
        return brokenPaddle;
    }

    public Paddle getBasePaddle() {
        return base;
    }

    private void breakPaddle() {

        int basePaddleWidth = base.getWidth();
        int baseX = (int) base.getX();
        int baseY = (int) base.getY();

        int newWidth = (int) Math.round(basePaddleWidth * 0.6);
        
        int[] newXs = calculateNewX(baseX, newWidth, GAP);

        ShapeDimension leftPaddleShape = new ShapeDimension("leftpaddle", newXs[0], baseY, newWidth, 4);
        ShapeDimension rightPaddleShape = new ShapeDimension("rightpaddle", newXs[1], baseY, newWidth, 4);
        
        Paddle leftPaddle = new Paddle(leftPaddleShape);
        Paddle rightPaddle = new Paddle(rightPaddleShape);
        
        leftPaddle.setPlayerIndex(base.getPlayerIndex());
        rightPaddle.setPlayerIndex(base.getPlayerIndex());
        
        brokenPaddle.add(leftPaddle);
        brokenPaddle.add(rightPaddle);
    }
    
    public boolean isVisible() {
        return isVisible;
    }

    public void setIsVisible(boolean isVisible) {
        this.isVisible = isVisible;
    }

    public void setTimeVisible(int time) {
        this.timeVisible = time;
    }

    public int getTimeVisible() {
        return timeVisible;
    }
    
    public void resetTime() {
        timeVisible = startTime;
    }

    private int[] calculateNewX(int baseX, int newWidth, int gap) {

        // the new width is 60% of the old paddles width;
        // new x position is calculated for both of the paddles.
        // second paddle is first paddle x + the gap (width of a paddle) and the width of one paddle.
        
        int leftX = (int) Math.round(baseX - newWidth);
        if (leftX - newWidth / 2 < 0) {
            leftX = newWidth / 2;
        }
        int rightX = leftX + newWidth + gap;
        if (rightX + (newWidth / 2) > BreakoutWorld.DIMENSION) {
            rightX = BreakoutWorld.DIMENSION - (newWidth / 2);
            leftX = rightX - newWidth - gap;
        }
        
        int[] newXs = new int[2];
        newXs[0] = leftX;
        newXs[1] = rightX;
        return newXs;
    }

    @Override
    public PowerUpMessage accept(PowerUpHandler puh) {
        return puh.handleBrokenPaddle(this);
    }

    @Override
    public JsonObjectBuilder toJson() {
        JsonArrayBuilder jab = Json.createArrayBuilder();
        JsonObjectBuilder job = Json.createObjectBuilder();
        jab.add(brokenPaddle.get(0).toJson().build());
        jab.add(brokenPaddle.get(1).toJson().build());
        job.add("base", base.toJson().build());
        job.add("brokenpaddle", jab.build());
        job.add("powerupname", getName());
        job.add("gap", GAP);
        
        return job;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public PowerUpMessageType getType() {
        return PowerUpMessageType.ADDBROKENPADDLE;
    }

    void addTime(int othersTime) {
        this.timeVisible += othersTime;
    }
    
    @Override
    public void setPlayerId(int i) {
        this.playerId = i;
    }

    @Override
    public int getPlayerId() {
        return this.playerId;
    }

}
