/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.breakoutegypt.data;

import com.breakoutegypt.domain.shapes.Paddle;
import java.util.List;

/**
 *
 * @author Bjarne Deketelaere
 */
public interface PaddleRepository {
    public List<Paddle> getPaddles();
    public List<Paddle> getPaddlesByLevelId(int id);
    public void addPaddle(Paddle paddle);
    public void addPaddlesForLevel(int levelId,List<Paddle> paddles);
    public void removePaddle(Paddle paddle);
    
}
