/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.breakoutegypt.data.mysql;

import com.breakoutegypt.data.BallRepository;
import com.breakoutegypt.data.PowerDownRepository;
import com.breakoutegypt.data.Repositories;
import com.breakoutegypt.data.mysql.util.DbConnection;
import com.breakoutegypt.domain.powers.FloodPowerDown;
import com.breakoutegypt.domain.powers.PowerDown;
import com.breakoutegypt.domain.shapes.Ball;
import com.breakoutegypt.domain.shapes.bricks.Brick;
import com.breakoutegypt.exceptions.BreakoutException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

/**
 *
 * @author Bjarne Deketelaere
 */
public class MysqlPowerDownRepository implements PowerDownRepository {

    private final String INSERT_FLOODPOWERDOWN_FORBRICK = "insert into spawnballeffect(brickid,ballid,amountofballs) values(?, ?, ?)";
    private final String DELETE_FLOODPOWERDOWN_FORBRICK = "delete from spawnballeffect where brickid = ?";
    private final String SELECT_FLOODPOWERDOWN = "select * from spawnballeffect where brickid = ?";

    @Override
    public void givePowerDownToBricks(List<Brick> bricks, List<Ball> balls) {
        for (Brick brick : bricks) {
            try (
                    Connection conn = DbConnection.getConnection();
                    PreparedStatement prep = conn.prepareStatement(SELECT_FLOODPOWERDOWN);) {
                prep.setInt(1, brick.getBrickId());
                try (ResultSet rs = prep.executeQuery()) {
                    while (rs.next()) {
                        int amount = rs.getInt("amountofballs");
                        int ballId = rs.getInt("ballid");
                        Ball defaultBall = findBallById(balls, ballId);
                        FloodPowerDown floodpowerdown = new FloodPowerDown(defaultBall, amount);
                        brick.setPowerdown(floodpowerdown);
                    }
                }
            } catch (SQLException ex) {
                throw new BreakoutException("Couldn't get powerdowns for brick");
            }
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

    @Override
    public void insertPowerDownsToBrick(int brickId, PowerDown powerdown) {
        if (powerdown instanceof FloodPowerDown) {
            this.insertFloodPowerDown(brickId, (FloodPowerDown) powerdown);
        }

    }

    private void insertFloodPowerDown(int brickId, FloodPowerDown floodPowerDown) {
        try (
                Connection conn = DbConnection.getConnection();
                PreparedStatement prep = conn.prepareStatement(INSERT_FLOODPOWERDOWN_FORBRICK);) {
            prep.setInt(1, brickId);
            prep.setInt(2, floodPowerDown.getOriginalBall().getBallId());
            prep.setInt(3, floodPowerDown.getNoOfBalls());
            prep.executeUpdate();
        } catch (SQLException ex) {
            throw new BreakoutException("Couldn't insert powerdowns for brick");
        }
    }

    @Override
    public void removePowerDownsOfBrick(int brickId) {
        try (
                Connection conn = DbConnection.getConnection();
                PreparedStatement prep = conn.prepareStatement(DELETE_FLOODPOWERDOWN_FORBRICK);) {
            prep.setInt(1, brickId);
            prep.executeUpdate();
        } catch (SQLException ex) {
            throw new BreakoutException("Couldn't delete powerdowns of brick");
        }
    }
}
