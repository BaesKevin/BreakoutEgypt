/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.breakoutegypt.domain.effects;

import com.breakoutegypt.domain.shapes.Paddle;
import com.breakoutegypt.domain.shapes.ShapeDimension;
import java.util.ArrayList;
import java.util.List;
import javax.json.Json;
import javax.json.JsonArrayBuilder;
import javax.json.JsonObject;

/**
 *
 * @author BenDB
 */
public class BrokenPaddlePowerUp implements PowerUp {

    private Paddle base;
    private List<Paddle> brokenPaddle;

    public BrokenPaddlePowerUp(Paddle p) {
        base = p;
        brokenPaddle = new ArrayList();
        breakPaddle();
    }

    public BrokenPaddlePowerUp() {
        brokenPaddle = new ArrayList();
    }

    public List<Paddle> getBrokenPaddle() {
        return brokenPaddle;
    }
    
    public List<Paddle> getBrokenPaddle(Paddle p) {
        base = p;
        breakPaddle();
        return brokenPaddle;
    }

    public Paddle getBasePaddle() {
        return base;
    }

    private void breakPaddle() {

        int basePaddleWidth = base.getShape().getWidth();
        int baseX = (int) base.getShape().getPosX();
        int baseY = (int) base.getShape().getPosY();
        double spaceBetween = basePaddleWidth * 0.4;

        int newWidth = (int) Math.round(basePaddleWidth * 0.6);
        
        int[] newXs = calculateNewX(basePaddleWidth, baseX, spaceBetween, newWidth);

        ShapeDimension leftPaddleShape = new ShapeDimension("leftpaddle", newXs[0], (int) baseY, newWidth, newWidth);
        ShapeDimension rightPaddleShape = new ShapeDimension("rightpaddle", newXs[1], (int) baseY, newWidth, newWidth);
        brokenPaddle.add(new Paddle(leftPaddleShape));
        brokenPaddle.add(new Paddle(rightPaddleShape));
    }

    private int[] calculateNewX(int basePaddleWidth, int baseX, double spaceBetween, int newWidth) {

        int leftX = (int) Math.round(baseX - spaceBetween);
        if (leftX - newWidth / 2 < 0) {
            leftX = newWidth / 2;
        }
        int rightX = leftX + (newWidth*2);
        if (rightX + (newWidth / 2) > 300) {
            rightX = 300 - (newWidth / 2);
            leftX = rightX - (newWidth*2);
        }
        
        int[] newXs = new int[2];
        newXs[0] = leftX;
        newXs[1] = rightX;
        return newXs;
    }

    @Override
    public void accept(PowerUpHandler puh) {
        puh.handle(this);
    }

    @Override
    public JsonObject toJson() {
        JsonArrayBuilder jab = Json.createArrayBuilder();
        return null;
    }

}
