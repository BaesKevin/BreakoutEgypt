/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.breakoutegypt.domain.effects;

import com.breakoutegypt.domain.shapes.Ball;
import com.breakoutegypt.domain.shapes.Paddle;

/**
 *
 * @author kevin
 */
public interface EffectHandler {

    void handle(ExplosiveEffect e);
    void handle(ToggleEffect e);
}
