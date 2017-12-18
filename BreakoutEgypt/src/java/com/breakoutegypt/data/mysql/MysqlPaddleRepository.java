/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.breakoutegypt.data.mysql;

import com.breakoutegypt.data.PaddleRepository;
import com.breakoutegypt.data.Repositories;
import com.breakoutegypt.data.ShapeDimensionRepository;
import com.breakoutegypt.data.mysql.util.DbConnection;
import com.breakoutegypt.domain.shapes.Paddle;
import com.breakoutegypt.domain.shapes.ShapeDimension;
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
public class MysqlPaddleRepository implements PaddleRepository{
    private final String SELECT_ALL_PADDLES = "select paddleid, shapedimensions.* from paddles join shapedimensions on shapedimensions.idshapedimension=paddles.shapedimensionid";
    private final String SELECT_ALL_PADDLES_BYLEVELID = "select levelpaddles.levelid,paddles.paddleid, shapedimensions.* "
            + "from paddles join levelpaddles on levelpaddles.idpaddle=paddles.paddleid "
            + "join shapedimensions on shapedimensions.idshapedimension=paddles.shapedimensionid where levelpaddles.levelid = ?";
    private final String INSERT_PADDLE = "Insert into paddles(shapedimensionid) values(?)";
    private final String DELETE_PADDLE = "Delete from paddles where paddleid = ?";
    private List<Paddle> paddles;
    @Override
    public List<Paddle> getPaddles() {
        this.paddles=new ArrayList<>();
        try (
                Connection conn=DbConnection.getConnection();
                PreparedStatement prep=conn.prepareStatement(SELECT_ALL_PADDLES);
                ResultSet rs=prep.executeQuery();
                ){
            while(rs.next()){
                initializePaddles(rs);
            }
            return this.paddles;
            
        } catch (SQLException ex) {
            throw new BreakoutException("Couldn't load paddles",ex);
        }
    }

    @Override
    public List<Paddle> getPaddlesByLevelId(int id) {
        this.paddles=new ArrayList<>();
        try(
                Connection conn=DbConnection.getConnection();
                PreparedStatement prep=conn.prepareStatement(SELECT_ALL_PADDLES_BYLEVELID);
                ){
            prep.setInt(1, id);
            try(
                    ResultSet rs=prep.executeQuery();
                    ){
                while(rs.next()){
                    initializePaddles(rs);
                }
                return this.paddles;
            }
        } catch (SQLException ex) {
            throw new BreakoutException("Couldn't load paddles",ex);
        }
    }
    private void initializePaddles(ResultSet rs) throws SQLException{
        int paddleId=rs.getInt("paddleid");
        int xPos=rs.getInt("x");
        int yPos=rs.getInt("y");
        int width=rs.getInt("width");
        int height=rs.getInt("height");
        Paddle paddle=new Paddle(new ShapeDimension("paddle",xPos,yPos,width,height));
        paddle.setPaddleId(paddleId);
        this.paddles.add(paddle);
    }

    @Override
    public void addPaddle(Paddle paddle) {
        ShapeDimensionRepository shapedimensionRepo=Repositories.getShapeDimensionRepository();
        shapedimensionRepo.addShapeDimension(paddle.getShapeDimension());
        try(
                Connection conn=DbConnection.getConnection();
                PreparedStatement prep=conn.prepareStatement(INSERT_PADDLE,PreparedStatement.RETURN_GENERATED_KEYS);
                ){
            prep.setInt(1, paddle.getShapeDimension().getShapeId());
            prep.executeUpdate();
            try(ResultSet rs=prep.getGeneratedKeys()){
                int paddleId = -1;
                if(rs.next()){
                    paddleId=rs.getInt(1);
                }
                if(paddleId<0){
                    throw new BreakoutException("Unable to add paddle");
                }
                paddle.setPaddleId(paddleId);               
            }
            
        } catch (SQLException ex) {
            throw new BreakoutException("Unable to add paddle");
        }
    }

    @Override
    public void removePaddle(Paddle paddle) {
        
        try(
                Connection conn=DbConnection.getConnection();
                PreparedStatement prep=conn.prepareStatement(DELETE_PADDLE);
                ){
            prep.setInt(1, paddle.getPaddleId());
            prep.executeUpdate();
            Repositories.getShapeDimensionRepository().removeShapeDimension(paddle.getShapeDimension());
        } catch (SQLException ex) {
            throw new BreakoutException("Unable to remove paddle");
        }
    }
    
}
