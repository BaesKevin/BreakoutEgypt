/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.breakoutegypt.domain;

import com.breakoutegypt.domain.shapes.bricks.Brick;

/**
 *
 * @author BenDB
 */
public class BrickScoreCalculator {

    private int score;
    private int multiplier;
    private int hitsWithoutHittingPaddle;
    private final int HITS_TO_INCREASE_MULTIPLIER = 2;
    private final int MAX_MULTIPLIER = 5;
    private final int pointsPerBlock;
    
    public BrickScoreCalculator(int pointsPerBlock) {
        this.score = 0;
        this.multiplier = 1;
        this.hitsWithoutHittingPaddle = 0;
        this.pointsPerBlock = pointsPerBlock;
    }

    public int getScore() {
        return score;
    }

    public int getMultiplier() {
        return multiplier;
    }

    private void incrMultiplier() {
        if (this.multiplier < MAX_MULTIPLIER) {
            this.multiplier++;
        }
    }

    public void resetMultiplier() {
        this.multiplier = 1;
        resetHitsWithoutHittingPaddle();
    }

    public int getHitsWithoutHittingPaddle() {
        return hitsWithoutHittingPaddle;
    }

    public void incrHitsWithoutHittingPaddle() {
        if (this.hitsWithoutHittingPaddle <= HITS_TO_INCREASE_MULTIPLIER) {
            this.hitsWithoutHittingPaddle++;
        }
        if (this.hitsWithoutHittingPaddle >= HITS_TO_INCREASE_MULTIPLIER) {
            resetHitsWithoutHittingPaddle();
            incrMultiplier();
        }
    }

    private void resetHitsWithoutHittingPaddle() {
        this.hitsWithoutHittingPaddle = 0;
    }

    void addPointsToScore() {
        score += (pointsPerBlock * this.multiplier);
        incrHitsWithoutHittingPaddle();
    }

}
