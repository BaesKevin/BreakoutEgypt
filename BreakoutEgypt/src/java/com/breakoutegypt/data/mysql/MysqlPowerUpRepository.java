/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.breakoutegypt.data.mysql;

import com.breakoutegypt.data.PowerUpRepository;
import com.breakoutegypt.data.mysql.util.DbConnection;
import com.breakoutegypt.domain.powers.PowerDown;
import com.breakoutegypt.domain.shapes.Ball;
import com.breakoutegypt.domain.shapes.bricks.Brick;
import com.breakoutegypt.exceptions.BreakoutException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Bjarne Deketelaere
 */
public class MysqlPowerUpRepository implements PowerUpRepository{
    private final String DELETE_BRICKPOWERUPS = "delete from genericbrickpowerup where brickid = ?";
    private final String DELETE_BALLPOWERUPS = "delete from genericballpowerup where brickid = ?";

    @Override
    public void givePowerUpsToBricks(List<Brick> levelBricks, List<Ball> levelBalls) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void insertPowerUpsToBrick(int brickId, PowerDown powerdown) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void removePowerUpsOfBrick(int brickId) {
        this.removeBrickPowerups(brickId);
        this.removeBallPowerups(brickId);    
    }
    private void removeBrickPowerups(int brickId){
        try(
                Connection conn=DbConnection.getConnection();
                PreparedStatement prep=conn.prepareStatement(DELETE_BRICKPOWERUPS);
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
