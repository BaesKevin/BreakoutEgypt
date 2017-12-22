/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.breakoutegypt.data.mysql;

import com.breakoutegypt.data.DifficultyRepository;
import com.breakoutegypt.data.mysql.util.DbConnection;
import com.breakoutegypt.domain.levelprogression.Difficulty;
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
public class MysqlDifficultyRepository implements DifficultyRepository {

    private final String SELECT_ALL_DIFFICULTIES = "select * from difficulties";
    private final String SELECT_DIFFICULTY_BYNAME = "select * from difficulties where difficultyname = ?";
    private final String SELECT_DIFFICULTY_BYID = "select * from difficulties where difficultyid = ?";

    @Override
    public List<Difficulty> findAll() {
        try (
                Connection conn = DbConnection.getConnection();
                PreparedStatement prep = conn.prepareStatement(SELECT_ALL_DIFFICULTIES);
                ResultSet rs = prep.executeQuery();) {
            List<Difficulty> difficultyList = new ArrayList<>();

            while (rs.next()) {
                int difficultyId = rs.getInt("difficultyid");
                String difficultyname = rs.getString("difficultyname");
                int ballspeed = rs.getInt("ball_speed");
                int livesPerLevel = rs.getInt("lives_per_level");
                boolean livesRegeneration = rs.getBoolean("liveregeneration");
                int pointsPerBlock = rs.getInt("points_per_block");
                int powerupRatio = rs.getInt("powerup_ratio");
                int powerupDuration = rs.getInt("powerup_duration");

                Difficulty difficulty = new Difficulty(difficultyId, difficultyname, ballspeed, livesPerLevel, livesRegeneration, pointsPerBlock, powerupRatio, powerupDuration);
                difficultyList.add(difficulty);

            }

            return difficultyList;
        } catch (SQLException ex) {
            throw new BreakoutException("Couldn't load difficulties", ex);
        }
    }

    @Override
    public Difficulty findByName(String type) {

        try (
                Connection conn = DbConnection.getConnection();
                PreparedStatement prep = conn.prepareStatement(SELECT_DIFFICULTY_BYNAME);) {
            prep.setString(1, type);
            try (
                    ResultSet rs = prep.executeQuery();) {
                Difficulty difficulty = null;
                while (rs.next()) {
                    int difficultyId = rs.getInt("difficultyid");
                    String difficultyname = rs.getString("difficultyname");
                    int ballspeed = rs.getInt("ball_speed");
                    int livesPerLevel = rs.getInt("lives_per_level");
                    boolean livesRegeneration = rs.getBoolean("liveregeneration");
                    int pointsPerBlock = rs.getInt("points_per_block");
                    int powerupRatio = rs.getInt("powerup_ratio");
                    int powerupDuration = rs.getInt("powerup_duration");

                    difficulty = new Difficulty(difficultyId, difficultyname, ballspeed, livesPerLevel, livesRegeneration, pointsPerBlock, powerupRatio, powerupDuration);
                }
                return difficulty;
            }

        } catch (SQLException ex) {
            throw new BreakoutException("Couldn't load difficulties", ex);
        }
    }

    @Override
    public Difficulty getById(int id) {
        try (
                Connection conn = DbConnection.getConnection();
                PreparedStatement prep = conn.prepareStatement(SELECT_DIFFICULTY_BYID);) {
            prep.setInt(1, id);
            try (
                    ResultSet rs = prep.executeQuery();) {
                Difficulty difficulty = null;
                while (rs.next()) {
                    int difficultyId = rs.getInt("difficultyid");
                    String difficultyname = rs.getString("difficultyname");
                    int ballspeed = rs.getInt("ball_speed");
                    int livesPerLevel = rs.getInt("lives_per_level");
                    boolean livesRegeneration = rs.getBoolean("liveregeneration");
                    int pointsPerBlock = rs.getInt("points_per_block");
                    int powerupRatio = rs.getInt("powerup_ratio");
                    int powerupDuration = rs.getInt("powerup_duration");

                    difficulty = new Difficulty(difficultyId, difficultyname, ballspeed, livesPerLevel, livesRegeneration, pointsPerBlock, powerupRatio, powerupDuration);
                }
                return difficulty;
            }

        } catch (SQLException ex) {
            throw new BreakoutException("Couldn't load difficulties", ex);
        }
    }

}
