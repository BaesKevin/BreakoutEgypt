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
    
    private boolean isVisable;
    private int timeVisable = 1800; //TODO variable time in seconds (not steps)
    private int startTime;

    private Paddle base;
    private List<Paddle> brokenPaddle;
    public static final int GAP = 6;
    
    private String name;

    public BrokenPaddlePowerUp(Paddle p,  int i) {
        this.name = "brokenpaddle" + i;
        base = p;
        brokenPaddle = new ArrayList();
        breakPaddle();
        startTime = timeVisable;
    }

    public List<Paddle> getBrokenPaddle() {
        return brokenPaddle;
    }

    public Paddle getBasePaddle() {
        return base;
    }

    private void breakPaddle() {

        int basePaddleWidth = base.getShape().getWidth();
        int baseX = (int) base.getShape().getPosX();
        int baseY = (int) base.getShape().getPosY();

        int newWidth = (int) Math.round(basePaddleWidth * 0.6);
        
        int[] newXs = calculateNewX(baseX, newWidth, GAP);

        ShapeDimension leftPaddleShape = new ShapeDimension("leftpaddle", newXs[0], baseY, newWidth, 4);
        ShapeDimension rightPaddleShape = new ShapeDimension("rightpaddle", newXs[1], baseY, newWidth, 4);
        
        brokenPaddle.add(new Paddle(leftPaddleShape));
        brokenPaddle.add(new Paddle(rightPaddleShape));
    }
    
    public boolean isVisable() {
        return isVisable;
    }

    public void setIsVisable(boolean isVisable) {
        this.isVisable = isVisable;
    }

    public void setTimeVisable(int time) {
        this.timeVisable = time;
    }

    public int getTimeVisable() {
        return timeVisable;
    }
    
    public void resetTime() {
        timeVisable = startTime;
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
        return puh.handleAddBrokenPaddle(this);
    }

    @Override
    public JsonObject toJson() {
        JsonArrayBuilder jab = Json.createArrayBuilder();
        JsonObjectBuilder job = Json.createObjectBuilder();
        jab.add(brokenPaddle.get(0).getShape().toJson().build());
        jab.add(brokenPaddle.get(1).getShape().toJson().build());
        job.add("base", base.getShape().toJson().build());
        job.add("brokenpaddle", jab.build());
        job.add("powerupname", getName());
        job.add("gap", GAP);
        
        return job.build();
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
        this.timeVisable += othersTime;
    }

}
