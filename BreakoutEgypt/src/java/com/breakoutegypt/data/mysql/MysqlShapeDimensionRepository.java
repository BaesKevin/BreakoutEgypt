/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.breakoutegypt.data.mysql;

import com.breakoutegypt.data.ShapeDimensionRepository;
import com.breakoutegypt.data.mysql.util.DbConnection;
import com.breakoutegypt.domain.shapes.ShapeDimension;
import com.breakoutegypt.exceptions.BreakoutException;
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
public class MysqlShapeDimensionRepository implements ShapeDimensionRepository{
    
    private final String SELECT_ALL_SHAPEDIMENSIONS = "select * from shapedimensions";
    private final String SELECT_SHAPEDIMENSION_BYID = "select * from shapedimensions where idshapedimension = ?";
    private final String INSERT_SHAPEDIMENSION = "insert into shapedimensions(x,y,width,height) values(?, ?, ?, ?)";
    private final String DELETE_SHAPEDIMENSION = "delete from shapedimensions where idshapedimension = ?";
    private List<ShapeDimension> shapedimensions;

    @Override
    public List<ShapeDimension> getShapeDimensions() {
        this.shapedimensions=new ArrayList<>();
        try(
                Connection conn=DbConnection.getConnection();
                PreparedStatement prep=conn.prepareStatement(SELECT_ALL_SHAPEDIMENSIONS);
                ResultSet rs=prep.executeQuery();
                ){
            while(rs.next()){
                float xPos=rs.getFloat("x");
                float yPos=rs.getFloat("y");
                int width=rs.getInt("width");
                int height=rs.getInt("height");
                ShapeDimension dimension=new ShapeDimension(xPos, yPos, width, height);
                this.shapedimensions.add(dimension);
            }
            return this.shapedimensions;
        } catch (SQLException ex) {
            throw new BreakoutException("Couldn't load shapedimensions",ex);
        }
    }

    @Override
    public ShapeDimension getShapeDimensionById(int id) {
        try(
                Connection conn=DbConnection.getConnection();
                PreparedStatement prep=conn.prepareStatement(SELECT_SHAPEDIMENSION_BYID);
                ){
            prep.setInt(1, id);
            try(
                    ResultSet rs=prep.executeQuery();
                    ){
                ShapeDimension dimension=null;
                while(rs.next()){
                    float xPos=rs.getFloat("x");
                    float yPos=rs.getFloat("y");
                    int width=rs.getInt("width");
                    int height=rs.getInt("height");
                    dimension=new ShapeDimension(xPos, yPos, width, height);
                    
                }
                return dimension;
            }
        } catch (SQLException ex) {
            throw new BreakoutException("Couldn't load shapedimension",ex);
        }
    }

    @Override
    public void addShapeDimension(ShapeDimension shapedimension) {
        try(
                Connection conn=DbConnection.getConnection();
                PreparedStatement prep=conn.prepareStatement(INSERT_SHAPEDIMENSION,PreparedStatement.RETURN_GENERATED_KEYS);
                ){
            prep.setFloat(1, shapedimension.getPosX());
            prep.setFloat(2, shapedimension.getPosY());
            prep.setInt(3, shapedimension.getWidth());
            prep.setInt(4, shapedimension.getHeight());
            prep.executeUpdate();
            try(ResultSet rs=prep.getGeneratedKeys()){
                int shapeId = -1;
                if(rs.next()){
                    shapeId=rs.getInt(1);
                }
                if(shapeId<0){
                    throw new BreakoutException("Unable to add shapedimension");
                }
                shapedimension.setShapeId(shapeId);               
            }
        } catch (SQLException ex) {
            throw new BreakoutException("Unable to add shapedimension",ex);
        }
    }

    @Override
    public void removeShapeDimension(ShapeDimension shapedimension) {
        try(
                Connection conn=DbConnection.getConnection();
                PreparedStatement prep=conn.prepareStatement(DELETE_SHAPEDIMENSION);
                ){
            prep.setInt(1, shapedimension.getShapeId());
            prep.executeUpdate();
        } catch (SQLException ex) {
            throw new BreakoutException("Unable to remove shapedimension",ex);
        }
    }
    
}
