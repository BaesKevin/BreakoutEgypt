/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.breakoutegypt.domain.brickcollisionhandlers;

import com.breakoutegypt.domain.effects.ExplosiveEffect;
import com.breakoutegypt.domain.effects.ToggleEffect;
import com.breakoutegypt.domain.shapes.Ball;
import com.breakoutegypt.domain.shapes.Paddle;

/**
 *
 * @author kevin
 */
public interface CollisionEventHandler {
    void setResetBallFlag(Ball ball);
    void ballHitPaddle(Ball ball, Paddle paddle);
    
    void handleExplosiveEffect(ExplosiveEffect effect);
    void handleToggleEffect(ToggleEffect effect);
}
