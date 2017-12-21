/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.breakoutegypt.data.mysql;

import com.breakoutegypt.data.LevelProgressionRepository;
import com.breakoutegypt.data.mysql.util.DbConnection;
import com.breakoutegypt.domain.GameType;
import com.breakoutegypt.domain.Level;
import com.breakoutegypt.domain.LevelPack;
import com.breakoutegypt.domain.Player;
import com.breakoutegypt.domain.levelprogression.Difficulty;
import com.breakoutegypt.domain.levelprogression.LevelPackProgress;
import com.breakoutegypt.domain.levelprogression.LevelProgress;
import com.breakoutegypt.exceptions.BreakoutException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 *
 * @author BenDB
 */
public class MysqlLevelProgressionRepository implements LevelProgressionRepository {

    private final String GET_LEVELPACKPROGRESS_BY_USER_DIFFICULTY_LEVELPACK = "select * from levelprogression where userid = ? and difficulty = ? and levelpackid = ?";
    private final String UPDATE_HIGHESTLEVELREACHED = "update levelprogression set `highest_level_reached` = highest_level_reached + 1 where levelprogressionid = ?";
    private final String INSERT_NEW_LEVELPROGRESS = "insert into levelprgression(userid, levelpackid, difficultyid, highest_level_reached, isCampaign)"
                                                    + "value(?, ?, ?, ?, ?)";

    @Override
    public void addNewLevelProgression(Player p, LevelPack lp, Difficulty d, int highestLevelReached, boolean isCampaign) {
        try (
                Connection conn = DbConnection.getConnection();
                PreparedStatement prep = conn.prepareStatement(UPDATE_HIGHESTLEVELREACHED);) {

            prep.setInt(1, p.getUserId());
            prep.setInt(2, lp.getId());
            prep.setInt(3, d.getId());
            prep.setInt(4, highestLevelReached);
            prep.setBoolean(5, true);
            prep.execute();

        } catch (SQLException ex) {
            throw new BreakoutException("Could not update highest level reached");
        }
        
    }

    @Override
    public void incrementHighestLevelReached(int levelProgressId) {
        try (
                Connection conn = DbConnection.getConnection();
                PreparedStatement prep = conn.prepareStatement(UPDATE_HIGHESTLEVELREACHED);) {

            prep.setInt(1, levelProgressId);
            prep.executeUpdate();

        } catch (SQLException ex) {
            throw new BreakoutException("Could not update highest level reached");
        }
    }

    @Override
    public LevelPackProgress getLevelPackProgress(int playerId, LevelPack levelpack, Difficulty d) {
        try (
                Connection conn = DbConnection.getConnection();
                PreparedStatement prep = conn.prepareStatement(GET_LEVELPACKPROGRESS_BY_USER_DIFFICULTY_LEVELPACK);) {

            prep.setInt(1, playerId);
            prep.setInt(2, levelpack.getId());
            prep.setInt(3, d.getId());
            LevelPackProgress lpp = null;
            ResultSet rs = prep.executeQuery();
            while (rs.next()) {
                int id = rs.getInt("levelprogressionid");
                int highestLevel = rs.getInt("highest_level_reached");

                LevelProgress lp = new LevelProgress(levelpack.getTotalLevels(), highestLevel);
                GameType gt = getGameTypeFromString(levelpack.getName());
                lpp = new LevelPackProgress(id, gt, d.getName(), lp);
            }
            return lpp;
        } catch (SQLException ex) {
            throw new BreakoutException("Couldn't get the desired levelprogress", ex);
        }
    }

    private GameType getGameTypeFromString(String name) {

        for (GameType gt : GameType.values()) {
            if (gt.name().equals(name)) {
                return gt;
            }
        }
        return GameType.TEST;
    }

}
