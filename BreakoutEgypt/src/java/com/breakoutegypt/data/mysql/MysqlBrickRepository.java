/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.breakoutegypt.data.mysql;

import com.breakoutegypt.data.BrickRepository;
import com.breakoutegypt.data.BrickTypeRepository;
import com.breakoutegypt.data.EffectRepository;
import com.breakoutegypt.data.PowerDownRepository;
import com.breakoutegypt.data.PowerUpRepository;
import com.breakoutegypt.data.Repositories;
import com.breakoutegypt.data.ShapeDimensionRepository;
import com.breakoutegypt.data.mysql.util.DbConnection;
import com.breakoutegypt.domain.effects.Effect;
import com.breakoutegypt.domain.shapes.ShapeDimension;
import com.breakoutegypt.domain.shapes.bricks.Brick;
import com.breakoutegypt.domain.shapes.bricks.BrickType;
import com.breakoutegypt.exceptions.BreakoutException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

/**
 *
 * @author Bjarne Deketelaere
 */
public class MysqlBrickRepository implements BrickRepository {

    private final String SELECT_ALL_BRICKS = "select * from bricks";
    private final String SELECT_ALL_LEVELBRICKS_BYLEVELID = "select bricks.* from bricks inner join levelbricks on levelbricks.brickid=bricks.brickid where levelbricks.levelid = ?";
    private final String SELECT_BRICK_BY_ID = "select * from bricks where brickId=?";
    private final String INSERT_BRICK = "insert into bricks(shapedimensionid,typeid,isbreakable,isvisible,istarget, isInverted, playerIndex) values(?, ?, ?, ?, ?, ?, ? )";
    private final String DELETE_BRICK = "delete from bricks where brickid = ?";
    private final String DELETE_LEVELBRICKS = "delete from levelbricks where levelid = ?";
    private final String INSERT_LEVELBRICKS = "insert into levelbricks(levelid,brickid) values(?,?)";

    List<Brick> bricks;
    Map<Integer, List<Effect>> effects = new HashMap();
    
    EffectRepository effectRepo = Repositories.getEffectRepository();
    PowerUpRepository powerupRepo = Repositories.getPowerUpRepository();
    
    @Override
    public List<Brick> getBricks() {
        this.bricks = new ArrayList<>();
        try (
                Connection conn = DbConnection.getConnection();
                PreparedStatement prep = conn.prepareStatement(SELECT_ALL_BRICKS);
                ResultSet rs = prep.executeQuery();) {
            while (rs.next()) {
                initializeBricks(rs);
            }
            
        } catch (SQLException ex) {
            throw new BreakoutException("Couldn't load the bricks", ex);
        }
        
        for (Brick brick : bricks) {
            effectRepo.giveEffectsToBrick(brick, bricks);
        }
        
        return this.bricks;
    }

    @Override
    public Brick getBrickById(int id) {
        this.bricks = new ArrayList<>();
        try (
                Connection conn = DbConnection.getConnection();
                PreparedStatement prep = conn.prepareStatement(SELECT_BRICK_BY_ID);) {

            prep.setInt(1, id);
            
            try (ResultSet rs = prep.executeQuery();) {
                while (rs.next()) {
                    initializeBricks(rs);
                }
                if (this.bricks.isEmpty()) {
                    return null;
                }
                
            }

        } catch (SQLException ex) {
            throw new BreakoutException("Couldn't load the bricks", ex);
        }
        
         for (Brick brick : bricks) {
            effectRepo.giveEffectsToBrick(brick, bricks);
        }
         
         return this.bricks.get(0);
    }

    @Override
    public List<Brick> getBricksByLevel(int id) {
        this.bricks = new ArrayList<>();
        try (
                Connection conn = DbConnection.getConnection();
                PreparedStatement prep = conn.prepareStatement(SELECT_ALL_LEVELBRICKS_BYLEVELID);) {
            prep.setInt(1, id);
            try (
                    ResultSet rs = prep.executeQuery();) {
                while (rs.next()) {
                    initializeBricks(rs);
                }
            }
        } catch (SQLException ex) {
            throw new BreakoutException("Couldn't load levelbricks", ex);
        }
        
         for (Brick brick : bricks) {
            effectRepo.giveEffectsToBrick(brick, bricks);
        }
         
         return this.bricks;
    }

    public void initializeBricks(ResultSet rs) throws SQLException {
        int brickId = rs.getInt("brickid");
        boolean isVisible = rs.getBoolean("isVisible");
        boolean isBreakable = rs.getBoolean("isbreakable");
        boolean isTarget = rs.getBoolean("istarget");
        boolean isInverted = rs.getBoolean("isInverted");
        int playerIndex = rs.getInt("playerIndex");
        
        ShapeDimension dimension = Repositories.getShapeDimensionRepository().getShapeDimensionById(rs.getInt("shapedimensionid"));
        BrickType bricktype = Repositories.getBrickTypeRepository().getBrickTypeById(rs.getInt("typeid"));
        Brick brick = new Brick(dimension, isTarget, isVisible, isBreakable, isInverted);
        brick.setBrickId(brickId);
        brick.setPlayerIndex(playerIndex);
       
//        EffectRepository effectRepo = new MysqlEffectRepository();
//        PowerDownRepository powerdownRepo = new MysqlPowerDownRepository();
//        effectRepo.giveEffectsToBrick(brick);
//        powerdownRepo.givePowerDownToBrick(brick);
        this.bricks.add(brick);
    }

    @Override
    public void addBrick(Brick brick) {
        ShapeDimensionRepository shapedimensionRepo = new MysqlShapeDimensionRepository();
        shapedimensionRepo.addShapeDimension(brick.getShape());
        BrickTypeRepository bricktypeRepo = new MysqlBrickTypeRepository();
        BrickType bricktype = bricktypeRepo.getBrickTypeByName("REGULAR");

//        List<Effect> effectsToSave = new ArrayList();
        try (
                Connection conn = DbConnection.getConnection();
                PreparedStatement prep = conn.prepareStatement(INSERT_BRICK, PreparedStatement.RETURN_GENERATED_KEYS);) {
            prep.setInt(1, brick.getShape().getShapeId());
            prep.setInt(2, bricktype.getBrickTypeId());
            prep.setBoolean(3, brick.isBreakable());
            prep.setBoolean(4, brick.isVisible());
            prep.setBoolean(5, brick.isTarget());
            prep.setBoolean(6, brick.isInverted());
            prep.setInt(7, brick.getPlayerIndex());
            
            prep.executeUpdate();
            try (ResultSet rs = prep.getGeneratedKeys()) {
                int brickId = -1;
                if (rs.next()) {
                    brickId = rs.getInt(1);
                }
                if (brickId < 0) {
                    throw new BreakoutException("Unable to add brick");
                }
                brick.setBrickId(brickId);
//                effectsToSave.addAll(brick.getEffects());

                new MysqlPowerDownRepository().insertPowerDownsToBrick(brickId, brick.getPowerDown());
                powerupRepo.insertPowerUpsToBrick(brick.getBrickId(), brick.getPowerUp());

            }
        } catch (SQLException ex) {
            throw new BreakoutException("Couldn't add brick", ex);
        }

    }

    @Override
    public void removeBrick(Brick brick) {
        effectRepo.removeEffectsOfBrick(brick.getBrickId());
        powerupRepo.removePowerUpsOfBrick(brick.getBrickId());
        PowerDownRepository powerdownRepo = new MysqlPowerDownRepository();
        powerdownRepo.removePowerDownsOfBrick(brick.getBrickId());
        try (
                Connection conn = DbConnection.getConnection();
                PreparedStatement prep = conn.prepareStatement(DELETE_BRICK);) {
            prep.setInt(1, brick.getBrickId());
            prep.executeUpdate();

            ShapeDimensionRepository shapedimensionRepo = new MysqlShapeDimensionRepository();
            shapedimensionRepo.removeShapeDimension(brick.getShape());
        } catch (SQLException ex) {
            throw new BreakoutException("Couldn't remove brick", ex);
        }
    }

    @Override
    public void addBricksForLevel(int levelId, List<Brick> bricks) {
        
        for (Brick brick : bricks) {
            this.addBrick(brick);
            effects.put(brick.getBrickId(), brick.getEffects());
            populateLevelBricks(levelId, brick);
        }

        addEffectsForBricks();
    }
    private void addEffectsForBricks(){
        for (Entry<Integer, List<Effect>> entry : effects.entrySet()) {
            effectRepo.insertEffectsToBrick(entry.getKey(), entry.getValue());
        }
        
        effects = new HashMap();
    }

    private void populateLevelBricks(int levelId, Brick brick) {
        try (
                Connection conn = DbConnection.getConnection();
                PreparedStatement prep = conn.prepareStatement(INSERT_LEVELBRICKS);) {
            prep.setInt(1, levelId);
            prep.setInt(2, brick.getBrickId());
            prep.executeUpdate();

        } catch (SQLException ex) {
            throw new BreakoutException("Couldn't add brick", ex);
        }
    }

    @Override
    public void removeLevelBricks(int levelId, List<Brick> bricks) {
        try (
                Connection conn = DbConnection.getConnection();
                PreparedStatement prep = conn.prepareStatement(DELETE_LEVELBRICKS);) {
            prep.setInt(1, levelId);
            prep.executeUpdate();
            for (Brick brick : bricks) {
                this.removeBrick(brick);
            }
        } catch (SQLException ex) {
            throw new BreakoutException("Couldn't remove bricks", ex);
        }
    }
}
