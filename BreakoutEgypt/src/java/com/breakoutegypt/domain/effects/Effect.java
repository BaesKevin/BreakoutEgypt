/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.breakoutegypt.domain.effects;

/**
 *
 * @author kevin
 */
public interface Effect {
    void accept(EffectHandler eh);
}
