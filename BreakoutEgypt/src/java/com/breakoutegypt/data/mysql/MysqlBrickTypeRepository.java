/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.breakoutegypt.data.mysql;

import com.breakoutegypt.data.BrickTypeRepository;
import com.breakoutegypt.data.mysql.util.DbConnection;
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
public class MysqlBrickTypeRepository implements BrickTypeRepository{
    
    private final String SELECT_ALL_BRICKTYPES = "select * from brick_types";
    private final String SELECT_BRICKTYPE_BYID = "select * from brick_types where id = ?";
    private final String SELECT_BRICKTYPE_BYNAME = "select * from brick_types where typename = ?";
    private final String INSERT_BRICKTYPE = "insert into brick_types(typename) values(?)";
    
    private List<BrickType> bricktypes;

    @Override
    public List<BrickType> getBrickTypes() {
        this.bricktypes=new ArrayList<>();
        try(
                Connection conn=DbConnection.getConnection();
                PreparedStatement prep=conn.prepareStatement(SELECT_ALL_BRICKTYPES);
                ResultSet rs=prep.executeQuery();
                ){
            while(rs.next()){
                String typeName=rs.getString("typename").toUpperCase();
                this.bricktypes.add(new BrickType(typeName));
                
            }
            return this.bricktypes;
        } catch (SQLException ex) {
            throw new BreakoutException("Couldn't load bricktypes", ex);
        }
    }

    @Override
    public BrickType getBrickTypeById(int id) {
        try(
                Connection conn=DbConnection.getConnection();
                PreparedStatement prep=conn.prepareStatement(SELECT_BRICKTYPE_BYID);
                ){
            prep.setInt(1, id);
            try(
                    ResultSet rs=prep.executeQuery();
                    ){
                BrickType bricktype=null;
                while(rs.next()){
                    String typeName=rs.getString("typename").toUpperCase();
                    int bricktypeId=rs.getInt("id");
                    bricktype=new BrickType(bricktypeId,typeName);
                }
                return bricktype;
            }
        } catch (SQLException ex) {
            throw new BreakoutException("Couldn't load bricktype",ex);
        }
    }

    @Override
    public BrickType getBrickTypeByName(String name) {
        try(
                Connection conn=DbConnection.getConnection();
                PreparedStatement prep=conn.prepareStatement(SELECT_BRICKTYPE_BYNAME);
                ){
            prep.setString(1, name);
            try(
                    ResultSet rs=prep.executeQuery();
                    ){
                BrickType bricktype=null;
                while(rs.next()){
                    String typeName=rs.getString("typename").toUpperCase();
                    int bricktypeId=rs.getInt("id");
                    bricktype=new BrickType(bricktypeId,typeName);        
                }
                return bricktype;
            }
        } catch (SQLException ex) {
            throw new BreakoutException("Couldn't load bricktype",ex);
        }
    }

    @Override
    public void addBrickType(BrickType bricktype) {
        try(
                Connection conn=DbConnection.getConnection();
                PreparedStatement prep=conn.prepareStatement(INSERT_BRICKTYPE,PreparedStatement.RETURN_GENERATED_KEYS);
                ){
            prep.setString(1, bricktype.getName());
            prep.executeUpdate();
            try(ResultSet rs=prep.getGeneratedKeys()){
                int bricktypeId = -1;
                if(rs.next()){
                    bricktypeId=rs.getInt(1);
                }
                if(bricktypeId<0){
                    throw new BreakoutException("Unable to add Bricktype");
                }
                bricktype.setBrickTypeId(bricktypeId);
            }
        } catch (SQLException ex) {
            throw new BreakoutException("Couldn't add Bricktype",ex);
        }
    }
    
}
