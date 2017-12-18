/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.breakoutegypt.data.mysql;

import com.breakoutegypt.data.BrickRepository;
import com.breakoutegypt.data.BrickTypeRepository;
import com.breakoutegypt.data.Repositories;
import com.breakoutegypt.data.ShapeDimensionRepository;
import com.breakoutegypt.data.mysql.util.DbConnection;
import com.breakoutegypt.domain.shapes.ShapeDimension;
import com.breakoutegypt.domain.shapes.bricks.Brick;
import com.breakoutegypt.domain.shapes.bricks.BrickType;
import com.breakoutegypt.exceptions.BreakoutException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Bjarne Deketelaere
 */
public class MysqlBrickRepository implements BrickRepository {

    private final String SELECT_ALL_BRICKS = "select * from bricks";
    private final String SELECT_ALL_LEVELBRICKS_BYLEVELID = "select bricks.* from bricks inner join levelbricks on levelbricks.brickid=bricks.brickid where levelbricks.levelid = ?";
    private final String INSERT_BRICK = "insert into bricks(shapedimensionid,typeid,isbreakable,isvisible,istarget) values(?, ?, ?, ?, ? )";
    private final String DELETE_BRICK = "delete from bricks where brickid = ?";
    private final String INSERT_LEVELBRICKS = "insert into levelbricks(levelid,brickid) values(?,?)";
    
    List<Brick> bricks;

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
            return this.bricks;
        } catch (SQLException ex) {
            throw new BreakoutException("Couldn't load the bricks", ex);
        }
    }

    @Override
    public Brick getBrickById(int id) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
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
                return this.bricks;
            }
        } catch (SQLException ex) {
            throw new BreakoutException("Couldn't load levelbricks", ex);
        }
    }

    public void initializeBricks(ResultSet rs) throws SQLException {
        int brickId=rs.getInt("brickid");
        boolean isVisible = rs.getBoolean("isvisible");
        boolean isBreakable = rs.getBoolean("isbreakable");
        boolean isTarget = rs.getBoolean("istarget");
        ShapeDimension dimension = Repositories.getShapeDimensionRepository().getShapeDimensionById(rs.getInt("shapedimensionid"));
        BrickType bricktype = Repositories.getBrickTypeRepository().getBrickTypeById(rs.getInt("typeid"));
        Brick brick = new Brick(dimension, isTarget, isVisible, isBreakable);
        brick.setBrickId(brickId);
        this.bricks.add(brick);
    }

    @Override
    public void addBrick(Brick brick) {
        ShapeDimensionRepository shapedimensionRepo=new MysqlShapeDimensionRepository();
        shapedimensionRepo.addShapeDimension(brick.getShape());
        BrickTypeRepository bricktypeRepo=new MysqlBrickTypeRepository();
        BrickType bricktype=bricktypeRepo.getBrickTypeByName("REGULAR");
        try(
                Connection conn=DbConnection.getConnection();
                PreparedStatement prep=conn.prepareStatement(INSERT_BRICK,PreparedStatement.RETURN_GENERATED_KEYS);
                ){
            prep.setInt(1,brick.getShape().getShapeId());
            prep.setInt(2, bricktype.getBrickTypeId());
            prep.setBoolean(3, brick.isBreakable());
            prep.setBoolean(4, brick.isVisible());
            prep.setBoolean(5,brick.isTarget());
            prep.executeUpdate();
            try(ResultSet rs=prep.getGeneratedKeys()){
                int brickId = -1;
                if(rs.next()){
                    brickId=rs.getInt(1);
                }
                if(brickId<0){
                    throw new BreakoutException("Unable to add brick");
                }
                brick.setBrickId(brickId);
            }
        } catch (SQLException ex) {
            throw new BreakoutException("Couldn't add brick",ex);
        }
    }

    @Override
    public void removeBrick(Brick brick) {
        try(
                Connection conn=DbConnection.getConnection();
                PreparedStatement prep=conn.prepareStatement(DELETE_BRICK);
                ){
            prep.setInt(1, brick.getBrickId());
            prep.executeUpdate();
            
            ShapeDimensionRepository shapedimensionRepo=new MysqlShapeDimensionRepository();
            shapedimensionRepo.removeShapeDimension(brick.getShape());
        } catch (SQLException ex) {
            throw new BreakoutException("Couldn't remove brick",ex);
        }
    }

    @Override
    public void addBricksForLevel(int levelId,List<Brick> bricks) {
        for(Brick brick:bricks){
            this.addBrick(brick);
            populateLevelBricks(levelId,brick);
        }
    }
    private void populateLevelBricks(int levelId,Brick brick){
        try (
                Connection conn=DbConnection.getConnection();
                PreparedStatement prep=conn.prepareStatement(INSERT_LEVELBRICKS);
                ){
            prep.setInt(1, levelId);
            prep.setInt(2, brick.getBrickId());
            prep.executeUpdate();
            
        } catch (SQLException ex) {
            throw new BreakoutException("Couldn't add brick",ex);
        }
    }

}
