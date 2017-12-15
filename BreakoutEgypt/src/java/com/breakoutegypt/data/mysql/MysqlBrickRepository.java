/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.breakoutegypt.data.mysql;

import com.breakoutegypt.data.BrickRepository;
import com.breakoutegypt.data.Repositories;
import com.breakoutegypt.data.mysql.util.DbConnection;
import com.breakoutegypt.domain.shapes.ShapeDimension;
import com.breakoutegypt.domain.shapes.bricks.Brick;
import com.breakoutegypt.domain.shapes.bricks.BrickType;
import com.breakoutegypt.exceptions.BreakoutException;
import java.awt.Point;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Bjarne Deketelaere
 */
public class MysqlBrickRepository implements BrickRepository{
    private final String SELECT_ALL_BRICKS = "select * from bricks";
    private final String SELECT_ALL_LEVELBRICKS_BYLEVELID = "select bricks.* from bricks inner join levelbricks on levelbricks.brickid=bricks.brickid where levelbricks.levelid = ?";
    List<Brick> bricks;
    
    @Override
    public List<Brick> getBricks() {
        this.bricks=new ArrayList<>();
        try(
                Connection conn=DbConnection.getConnection();
                PreparedStatement prep=conn.prepareStatement(SELECT_ALL_BRICKS);
                ResultSet rs=prep.executeQuery();
                ){
            while(rs.next()){
                boolean isVisible=rs.getBoolean("isvisible");
                boolean isBreakable=rs.getBoolean("isbreakable");
                boolean isTarget=rs.getBoolean("istarget");
                ShapeDimension dimension=Repositories.getShapeDimensionRepository().getShapeDimensionById(rs.getInt("shapedimensionid"));
                BrickType bricktype=Repositories.getBrickTypeRepository().getBrickTypeById(rs.getInt("typeid"));
                //initialize bricks, point?
                
            }
            return null;
        } catch (SQLException ex) {
            throw new BreakoutException("Couldn't load the bricks",ex);
        }
    }

    @Override
    public Brick getBrickById(int id) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public List<Brick> getBricksByLevel(int id) {
        this.bricks=new ArrayList<>();
        try(
                Connection conn=DbConnection.getConnection();
                PreparedStatement prep=conn.prepareStatement(SELECT_ALL_LEVELBRICKS_BYLEVELID);
                ){
            prep.setInt(1, id);
            try(
                    ResultSet rs=prep.executeQuery();
                    ){
                while(rs.next()){
                    boolean isVisible=rs.getBoolean("isvisible");
                    boolean isBreakable=rs.getBoolean("isbreakable");
                    boolean isTarget=rs.getBoolean("istarget");
                    ShapeDimension dimension=Repositories.getShapeDimensionRepository().getShapeDimensionById(rs.getInt("shapedimensionid"));
                    BrickType bricktype=Repositories.getBrickTypeRepository().getBrickTypeById(rs.getInt("typeid"));
                    //initialize bricks, point?
                }
                return null;
            }
        } catch (SQLException ex) {
            throw new BreakoutException("Couldn't load levelbricks",ex);
        }
    }
    
}
