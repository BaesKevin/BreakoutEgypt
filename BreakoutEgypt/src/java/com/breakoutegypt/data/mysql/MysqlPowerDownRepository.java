/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.breakoutegypt.data.mysql;

import com.breakoutegypt.data.PowerDownRepository;
import com.breakoutegypt.data.mysql.util.DbConnection;
import com.breakoutegypt.domain.powers.FloodPowerDown;
import com.breakoutegypt.domain.powers.PowerDown;
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
public class MysqlPowerDownRepository implements PowerDownRepository {

    private final String INSERT_FLOODPOWERDOWN_FORBRICK = "insert into spawnballeffect(brickid,amountofballs) values(?, ?)";
    private final String DELETE_FLOODPOWERDOWN_FORBRICK = "delete from spawnballeffect where brickid = ?";
    private final String SELECT_FLOODPOWERDOWN = "select * from spawnballeffect where brickid = ?";

    @Override
    public void givePowerDownToBrick(Brick brick) {
        MysqlBallRepository ballRepo = new MysqlBallRepository();

        try (
                Connection conn = DbConnection.getConnection();
                PreparedStatement prep = conn.prepareStatement(SELECT_FLOODPOWERDOWN);) {
            prep.setInt(1, brick.getBrickId());
            try (ResultSet rs = prep.executeQuery()) {
                while (rs.next()) {
                    int amount = rs.getInt("amountofballs");
                    //only the amount is known, levelpaddle will be added in levelRepo
                    FloodPowerDown floodpowerdown = new FloodPowerDown(amount);
                    brick.setPowerdown(floodpowerdown);
                }
            }
        } catch (SQLException ex) {
            throw new BreakoutException("Couldn't get powerdowns for brick");
        }
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
            prep.setInt(2, floodPowerDown.getNoOfBalls());
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
