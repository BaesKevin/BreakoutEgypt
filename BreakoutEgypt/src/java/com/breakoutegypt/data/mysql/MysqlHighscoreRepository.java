/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.breakoutegypt.data.mysql;

import com.breakoutegypt.data.HighscoreRepository;
import com.breakoutegypt.data.Repositories;
import com.breakoutegypt.data.mysql.util.DbConnection;
import com.breakoutegypt.domain.Score;
import com.breakoutegypt.domain.User;
import com.breakoutegypt.domain.levelprogression.Difficulty;
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
public class MysqlHighscoreRepository implements HighscoreRepository {

    private final String INSERT_SCORE = "insert into level_scores(levelid,userid,difficultyid,time,points) values(?, ?, ?, ?, ?)";
    private final String GET_SCORES_BY_LEVEL = "select * from level_scores where levelid = ? and difficultyid = ?";
    private final String REMOVE_SCORE = "delete from level_scores where scoreId = ?";
    
    @Override
    public List<Score> getScoresByLevel(int levelID, String diff) {
        try (
                Connection conn = DbConnection.getConnection();
                PreparedStatement prep = conn.prepareStatement(GET_SCORES_BY_LEVEL);) {
            Difficulty d = Repositories.getDifficultyRepository().findByName(diff);
            prep.setInt(1, levelID);
            prep.setInt(2, d.getId());

            ResultSet rs = prep.executeQuery();
            List<Score> scores = new ArrayList();
            Score s = null;
            while (rs.next()) {

                //scoreId, levelid, userid, difficultyid, time, points
                int scoreid = rs.getInt("scoreId");
                int levelid = rs.getInt("levelid");
                int userid = rs.getInt("userid");
                long time = rs.getLong("time");
                int points = rs.getInt("points");

                User user = Repositories.getUserRepository().getUserById(userid);

                s = new Score(scoreid, levelid, user, time, diff, points);
                scores.add(s);
            }
            return scores;
        } catch (SQLException ex) {
            throw new BreakoutException("Can not get scores for this level", ex);
        }
    }

    @Override
    public void addScore(Score s) {
        try (
                Connection conn = DbConnection.getConnection();
                PreparedStatement prep = conn.prepareStatement(INSERT_SCORE, PreparedStatement.RETURN_GENERATED_KEYS);) {

            prep.setInt(1, s.getLevel());
            prep.setInt(2, s.getUserId());
            prep.setInt(3, Repositories.getDifficultyRepository().findByName(s.getDifficulty()).getId());
            prep.setLong(4, s.getTimeScore());
            prep.setInt(5, s.getBrickScore());

            prep.execute();

            ResultSet rs = prep.getGeneratedKeys();
            while (rs.next()) {
                int scoreId = -1;
                
                scoreId = rs.getInt(1);
                
                if (scoreId < 0) {
                    throw new BreakoutException("Unable to add score to database.");
                }
                
                s.setScoreId(scoreId);

            }
        } catch (SQLException ex) {
            throw new BreakoutException(ex.getMessage(), ex);
        }
    }

    @Override
    public int getRank(int levelID, Score s) {
        throw new BreakoutException("not supported yet. Maybe never");
    }

    @Override
    public void removeScore(int id) {
        try (
                Connection conn = DbConnection.getConnection();
                PreparedStatement prep = conn.prepareStatement(REMOVE_SCORE);) {

            prep.setInt(1, id);

            prep.execute();
        } catch (SQLException ex) {
            throw new BreakoutException(ex.getMessage(), ex);
        }
    }

}
