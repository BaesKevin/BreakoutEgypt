/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.breakoutegypt.data;

import com.breakoutegypt.domain.shapes.Ball;
import java.util.List;

/**
 *
 * @author Bjarne Deketelaere
 */
public interface BallRepository {
    public List<Ball> getBalls();
    public List<Ball> getBallsByLevelId(int id);
    public void addBall(Ball ball);
    public void removeBall(Ball ball);
}
