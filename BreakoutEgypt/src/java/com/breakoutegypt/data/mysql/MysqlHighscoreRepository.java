/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.breakoutegypt.data.mysql;

import com.breakoutegypt.data.HighscoreRepository;
import com.breakoutegypt.data.mysql.util.DbConnection;
import com.breakoutegypt.domain.Score;
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
public class MysqlHighscoreRepository implements HighscoreRepository{
    private final String INSERT_SCORE = "insert into level_scores(levelid,userid,difficultyid,time,points) values(?, ?, ?, ?, ?,)";
    
    @Override
    public List<Score> getScoresByLevel(int levelID, String diff) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void addScore(Score s) {
        try(
                Connection conn=DbConnection.getConnection();
                PreparedStatement prep=conn.prepareStatement(INSERT_SCORE);
                ){
            prep.setInt(1, s.getLevel());
            prep.setInt(2, s.getUserId());
            //Difficulty refactoring
        } catch (SQLException ex) {
            throw new BreakoutException("Couldn't add Score",ex);
        }
    }

    @Override
    public int getRank(int levelID, Score s) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
}
