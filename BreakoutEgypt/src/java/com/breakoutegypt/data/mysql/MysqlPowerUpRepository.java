/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.breakoutegypt.data.mysql;

import com.breakoutegypt.data.PowerUpRepository;
import com.breakoutegypt.data.mysql.util.DbConnection;
import com.breakoutegypt.domain.powers.FloodPowerDown;
import com.breakoutegypt.domain.powers.PowerUp;
import com.breakoutegypt.domain.powers.generic.BallPowerup;
import com.breakoutegypt.domain.powers.generic.BrickPowerup;
import com.breakoutegypt.domain.powers.generic.PaddlePowerup;
import com.breakoutegypt.domain.shapes.Ball;
import com.breakoutegypt.domain.shapes.Paddle;
import com.breakoutegypt.domain.shapes.bricks.Brick;
import com.breakoutegypt.exceptions.BreakoutException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Bjarne Deketelaere
 */
public class MysqlPowerUpRepository implements PowerUpRepository{
    private final String DELETE_PADDLEPOWERUPS = "delete from genericpaddlepowerup where brickid = ?";
    private final String DELETE_BALLPOWERUPS = "delete from genericballpowerup where brickid = ?";
    private final String INSERT_PADDLEPOWERUP = "insert into genericpaddlepowerup(brickid,paddleid,width,height) values(?, ?, ?, ?)";
    private final String INSERT_BALLPOWERUP = "insert into genericballpowerup(brickid,ballid,width,height) values(?, ?, ?, ?)";
    private final String SELECT_PADDLEPOWERUPS = "select * from genericpaddlepowerup where brickid = ?";
    private final String SELECT_BALLPOWERUPS = "select * from genericballpowerup where brickid = ?";
    
    @Override
    public void givePowerUpsToBricks(List<Brick> levelBricks, List<Ball> levelBalls, List<Paddle> levelPaddles) {
        for (Brick brick : levelBricks) {
            this.giveBallPowerupToBrick(brick, levelBalls);
            this.givePaddlePowerupToBrick(brick,levelPaddles);
        }
    }
    
    private void givePaddlePowerupToBrick(Brick brick, List<Paddle> paddles){
        try (
                    Connection conn = DbConnection.getConnection();
                    PreparedStatement prep = conn.prepareStatement(SELECT_PADDLEPOWERUPS);) {
                prep.setInt(1, brick.getBrickId());
                try (ResultSet rs = prep.executeQuery()) {
                    while (rs.next()) {
                        int brickId = rs.getInt("brickid");
                        int paddleId = rs.getInt("paddleid");
                        int width = rs.getInt("width");
                        int height = rs.getInt("height");
                        Paddle defaultPaddle = findPaddleById(paddles, paddleId);
                        PaddlePowerup paddlepowerup = new PaddlePowerup(defaultPaddle, width, height);
                        brick.setPowerUp(paddlepowerup);
                    }
                }
            } catch (SQLException ex) {
                throw new BreakoutException("Couldn't give powerups to brick");
            }
    }
    
    private void giveBallPowerupToBrick(Brick brick, List<Ball> balls){
        try (
                    Connection conn = DbConnection.getConnection();
                    PreparedStatement prep = conn.prepareStatement(SELECT_BALLPOWERUPS);) {
                prep.setInt(1, brick.getBrickId());
                try (ResultSet rs = prep.executeQuery()) {
                    while (rs.next()) {
                        int brickId = rs.getInt("brickid");
                        int ballId = rs.getInt("ballid");
                        int width = rs.getInt("width");
                        int height = rs.getInt("height");
                        Ball defaultBall = findBallById(balls, ballId);
                        BallPowerup ballpowerup = new BallPowerup(defaultBall, width, height);
                        brick.setPowerUp(ballpowerup);
                    }
                }
            } catch (SQLException ex) {
                throw new BreakoutException("Couldn't give powerups to brick");
            }
    }
    private Ball findBallById(List<Ball> balls, int id){
        Ball ball = null;
        
        for(Ball b : balls){
            if(b.getBallId() == id){
                ball = b;
                break;
            }
        }
        
        return ball;
    }
    
    private Paddle findPaddleById(List<Paddle> paddles, int id){
        Paddle paddle = null;
        
        for(Paddle p : paddles){
            if(p.getPaddleId() == id){
                paddle = p;
                break;
            }
        }
        
        return paddle;
    }

    @Override
    public void insertPowerUpsToBrick(int brickId, PowerUp powerup) {
        if(powerup instanceof BallPowerup){
            this.insertBallPowerup(brickId, (BallPowerup)powerup);
        } else if(powerup instanceof PaddlePowerup){
            this.insertPaddlePowerup(brickId, (PaddlePowerup)powerup);
        }
    }
    private void insertBallPowerup(int brickId,BallPowerup powerup){
        try(
                Connection conn=DbConnection.getConnection();
                PreparedStatement prep=conn.prepareStatement(INSERT_BALLPOWERUP);
                ){
            prep.setInt(1, brickId);
            prep.setInt(2, ((Ball)powerup.getBaseBody()).getBallId());
            prep.setInt(3, powerup.getWidth());
            prep.setInt(4, powerup.getHeight());
            prep.executeUpdate();
        } catch (SQLException ex) {
            System.out.println(ex);
            throw new BreakoutException("Couldn't insert powerup");
        }
    }
    
    private void insertPaddlePowerup(int brickId,PaddlePowerup powerup){
        try(
                Connection conn=DbConnection.getConnection();
                PreparedStatement prep=conn.prepareStatement(INSERT_PADDLEPOWERUP);
                ){
            prep.setInt(1, brickId);
            prep.setInt(2, ((Paddle)powerup.getBaseBody()).getPaddleId());
            prep.setInt(3, powerup.getWidth());
            prep.setInt(4, powerup.getHeight());
            prep.executeUpdate();
        } catch (SQLException ex) {
            throw new BreakoutException("Couldn't insert powerup");
        }
    }

    @Override
    public void removePowerUpsOfBrick(int brickId) {
        this.removeBrickPowerups(brickId);
        this.removeBallPowerups(brickId);    
    }
    private void removeBrickPowerups(int brickId){
        try(
                Connection conn=DbConnection.getConnection();
                PreparedStatement prep=conn.prepareStatement(DELETE_PADDLEPOWERUPS);
                ){
            prep.setInt(1, brickId);
            prep.executeUpdate();
        } catch (SQLException ex) {
            throw new BreakoutException("Couldn't delete powerups");
        }
    }
    private void removeBallPowerups(int brickId){
        try(
                Connection conn=DbConnection.getConnection();
                PreparedStatement prep=conn.prepareStatement(DELETE_BALLPOWERUPS);
                ){
            prep.setInt(1, brickId);
            prep.executeUpdate();
        } catch (SQLException ex) {
            throw new BreakoutException("Couldn't delete powerups");
        }
    }
    
}
