/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.breakoutegypt.data.mysql;

import com.breakoutegypt.data.LevelPackRepository;
import com.breakoutegypt.data.LevelRepository;
import com.breakoutegypt.data.Repositories;
import com.breakoutegypt.data.mysql.util.DbConnection;
import com.breakoutegypt.domain.Game;
import com.breakoutegypt.domain.Level;
import com.breakoutegypt.domain.LevelPack;
import com.breakoutegypt.exceptions.BreakoutException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author kevin
 */
public class MysqlLevelPackRepository implements LevelPackRepository{
    private LevelRepository levelRepo = Repositories.getLevelRepository();
    private final String SELECT_BY_NAME = "select * from levelpacks where name = ?";
    private final String INSERT = "insert into levelpacks(name, description, default_open_levels, total_levels) values(?,?,?,?)";
    
    @Override
    public void add(LevelPack pack) {
         try(
                Connection conn=DbConnection.getConnection();
                PreparedStatement prep=conn.prepareStatement(INSERT,PreparedStatement.RETURN_GENERATED_KEYS);
                ){
             prep.setString(1, pack.getName());
            prep.setString(2, pack.getDescription());
            prep.setInt(3, pack.getDefaultOpenLevels());
            prep.setInt(4, pack.getTotalLevels());
            
            prep.executeUpdate();
            try(ResultSet rs=prep.getGeneratedKeys();){
                int packId = -1;
                if(rs.next()){
                    packId=rs.getInt(1);
                }
                if(packId<0){
                    throw new BreakoutException("Unable to add level");
                }
                pack.setId(packId);
//                level.setLevelNumber(levelId); 
            }
            
             for(Level level : pack.getLevels()){
                 levelRepo.addLevel(level);
             }
        } catch (SQLException ex) {
            throw new BreakoutException("Couldn't add level", ex);
        }
    }
    
   

    @Override
    public LevelPack getByName(String name, Game game) {
        try (
                Connection conn = DbConnection.getConnection();
                PreparedStatement prep = conn.prepareStatement(SELECT_BY_NAME);) {
            prep.setString(1, name);
            try (
                    ResultSet rs = prep.executeQuery();) {
                LevelPack pack = null;
                while (rs.next()) {
                    int packId = rs.getInt("id");
                    String packName = rs.getString("name");
                    String description = rs.getString("description");
                    int openLevels = rs.getInt("default_open_levels");
                    
                    List<Level> levels = levelRepo.getLevelsByPackId(packId, game);
                    
                    pack = new LevelPack(packId, name, description, levels, openLevels, levels.size());
                }
                return pack;
            }

        } catch (SQLException ex) {
            throw new BreakoutException("Couldn't load level", ex);
        }
    }

    @Override
    public LevelPack getByNameWithoutLevels(String name) {
        try (
                Connection conn = DbConnection.getConnection();
                PreparedStatement prep = conn.prepareStatement(SELECT_BY_NAME);) {
            prep.setString(1, name);
            try (
                    ResultSet rs = prep.executeQuery();) {
                LevelPack pack = null;
                while (rs.next()) {
                    int packId = rs.getInt("id");
                    String packName = rs.getString("name");
                    String description = rs.getString("description");
                    int openLevels = rs.getInt("default_open_levels");                    
                    int totalLevels = rs.getInt("total_levels");

                    
                    pack = new LevelPack(packId, name, description, new ArrayList(), openLevels, totalLevels);
                }
                return pack;
            }

        } catch (SQLException ex) {
            throw new BreakoutException("Couldn't load level", ex);
        }
    }
    
    
    
}
