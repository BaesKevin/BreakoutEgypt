/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.breakoutegypt.domain.brickcollisionhandlers;

/**
 *
 * @author kevin
 */
public interface ContactHandler {
    void handle(BallBrickContact bbc);
    void handle(BallGroundContact bgc);
    void handle(BallPaddleContact bpc);
    void handle(ProjectilePaddleContact ppc);
    void handle(ProjectileGroundContact pgc);
}
