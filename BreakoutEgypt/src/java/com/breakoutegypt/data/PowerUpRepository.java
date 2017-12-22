/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.breakoutegypt.data;

import com.breakoutegypt.domain.levelprogression.Difficulty;
import com.breakoutegypt.domain.powers.PowerUp;
import com.breakoutegypt.domain.shapes.Ball;
import com.breakoutegypt.domain.shapes.Paddle;
import com.breakoutegypt.domain.shapes.bricks.Brick;
import java.util.List;

/**
 *
 * @author Bjarne Deketelaere
 */
public interface PowerUpRepository {
    public void givePowerUpsToBricks(List<Brick> levelBricks, List<Ball> levelBalls, List<Paddle> levelPaddles, Difficulty difficulty);
    public void insertPowerUpsToBrick(int brickId,PowerUp powerdown);
    public void removePowerUpsOfBrick(int brickId);
}
