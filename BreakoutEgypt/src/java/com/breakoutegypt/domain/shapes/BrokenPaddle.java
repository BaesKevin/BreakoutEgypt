/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.breakoutegypt.domain.shapes;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author BenDB
 */
public class BrokenPaddle {

    private Paddle base;
    private List<Paddle> brokenPaddle;

    public BrokenPaddle(Paddle p) {
        base = p;
        brokenPaddle = new ArrayList();
        breakPaddle();
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

}
