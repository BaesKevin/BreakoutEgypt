/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.breakoutegypt.data.mysql;

import com.breakoutegypt.data.DifficultyRepository;
import com.breakoutegypt.data.mysql.util.DbConnection;
import com.breakoutegypt.domain.levelprogression.Difficulty;
import com.breakoutegypt.domain.levelprogression.GameDifficulty;
import com.breakoutegypt.exceptions.BreakoutException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Bjarne Deketelaere
 */
public class MysqlDifficultyRepository implements DifficultyRepository{
    
    private final String SELECT_ALL_DIFFICULTIES="select * from difficulties";
    private final String SELECT_DIFFICULTY_BYNAME="select * from difficulties where difficultyname = ?";
    private Map<GameDifficulty, Difficulty> difficulties;

    private GameDifficulty selectGameDifficulty(String name){
        switch(name){
            case "EASY":
                return GameDifficulty.EASY;
            case "MEDIUM":
                return GameDifficulty.MEDIUM;
            case "HARD":
                return GameDifficulty.HARD;
            case "BRUTAL":
                return GameDifficulty.BRUTAL;
        }
        return null;
    }
    @Override
    public List<Difficulty> findAll() {
        this.difficulties=new HashMap();
        try(
                Connection conn=DbConnection.getConnection();
                PreparedStatement prep=conn.prepareStatement(SELECT_ALL_DIFFICULTIES);
                ResultSet rs=prep.executeQuery();
                ){
            while(rs.next()){
                String difficultyname=rs.getString("difficultyname");
                int ballspeed=rs.getInt("ball_speed");
                int livesPerLevel=rs.getInt("lives_per_level");
                boolean livesRegeneration=rs.getBoolean("liveregeneration");
                int pointsPerBlock=rs.getInt("points_per_block");
                int powerupRatio=rs.getInt("powerup_ratio");
                int powerupDuration=rs.getInt("powerup_duration");
                
                Difficulty difficulty=new Difficulty(difficultyname,ballspeed,livesPerLevel,livesRegeneration,pointsPerBlock,powerupRatio,powerupDuration);
                
                difficulties.put(selectGameDifficulty(difficultyname.toUpperCase()), difficulty);
            }
            List<Difficulty> difficultyList=new ArrayList<>();
            difficultyList.addAll(difficulties.values());
            return difficultyList;
        } catch (SQLException ex) {
            throw new BreakoutException("Couldn't load difficulties",ex);
        }
    }
    
    @Override
    public Difficulty findByName(GameDifficulty type) {
        this.difficulties=new HashMap();
        try(
                Connection conn=DbConnection.getConnection();
                PreparedStatement prep=conn.prepareStatement(SELECT_DIFFICULTY_BYNAME);
                ){
            prep.setString(1, type.name());
            try(
                    ResultSet rs=prep.executeQuery();
                    ){
                Difficulty difficulty=null;
                while(rs.next()){
                String difficultyname=rs.getString("difficultyname");
                int ballspeed=rs.getInt("ball_speed");
                int livesPerLevel=rs.getInt("lives_per_level");
                boolean livesRegeneration=rs.getBoolean("liveregeneration");
                int pointsPerBlock=rs.getInt("points_per_block");
                int powerupRatio=rs.getInt("powerup_ratio");
                int powerupDuration=rs.getInt("powerup_duration");
                
                difficulty=new Difficulty(difficultyname,ballspeed,livesPerLevel,livesRegeneration,pointsPerBlock,powerupRatio,powerupDuration);
                }
                return difficulty;
            }
            
            
        } catch (SQLException ex) {
            throw new BreakoutException("Couldn't load difficulties",ex);
        }
    }
    
}
