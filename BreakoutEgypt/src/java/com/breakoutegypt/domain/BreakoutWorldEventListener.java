/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.breakoutegypt.domain;

import com.breakoutegypt.domain.shapes.Ball;
import com.breakoutegypt.domain.shapes.Brick;

/**
 *
 * @author kevin
 */
public interface BreakoutWorldEventListener {
    void removeBrick(Brick brick);
    void ballOutOfBounds(Ball ball);
}
