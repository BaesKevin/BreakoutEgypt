/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.breakoutegypt.data.mysql;

import com.breakoutegypt.data.LevelProgressionRepository;
import com.breakoutegypt.data.Repositories;
import com.breakoutegypt.data.mysql.util.DbConnection;
import com.breakoutegypt.domain.GameType;
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
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author BenDB
 */
public class MysqlLevelProgressionRepository implements LevelProgressionRepository {

    private final String GET_LEVELPACKPROGRESS_BY_USER_DIFFICULTY_LEVELPACK = "select * from levelprogression where userid = ? and difficultyid = ? and levelpackid = ?";
    private final String GET_LEVELPACKPROGRESS_BY_USER = "select * from levelprogression where userid = ?";
    private final String UPDATE_HIGHESTLEVELREACHED = "update levelprogression set `highest_level_reached` = highest_level_reached + 1 where userid = ? and difficultyid = ? and levelpackid = ?";
    private final String SET_HIGHESTLEVELREACHED = "update levelprogression set `highest_level_reached` = ? where userid = ? and difficultyid = ? and levelpackid = ?";
    private final String INSERT_NEW_LEVELPROGRESS = "insert into levelprogression(userid, levelpackid, difficultyid, highest_level_reached, isCampaign)"
            + "value(?, ?, ?, ?, ?)";
    private final String DELETE_LEVELPROGRESSION = "delete from levelprogression where userid = ? and difficultyid = ? and levelpackid = ?";

    @Override
    public void addNewLevelProgression(int userid, LevelPack lp, Difficulty d, int highestLevelReached, boolean isCampaign) {
        try (
                Connection conn = DbConnection.getConnection();
                PreparedStatement prep = conn.prepareStatement(INSERT_NEW_LEVELPROGRESS);) {

            prep.setInt(1, userid);
            prep.setInt(2, lp.getId());
            prep.setInt(3, d.getId());
            prep.setInt(4, highestLevelReached);
            prep.setBoolean(5, false);
            prep.execute();

        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
            throw new BreakoutException("Could not add new progress", ex);
        }

    }

    @Override
    public void incrementHighestLevelReached(Player p, LevelPack lp, Difficulty d) {
        try (
                Connection conn = DbConnection.getConnection();
                PreparedStatement prep = conn.prepareStatement(UPDATE_HIGHESTLEVELREACHED);) {

            prep.setInt(1, p.getUserId());
            prep.setInt(2, d.getId());
            prep.setInt(3, lp.getId());
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
            prep.setInt(2, d.getId());
            prep.setInt(3, levelpack.getId());
            LevelPackProgress lpp = null;
            ResultSet rs = prep.executeQuery();
            while (rs.next()) {
                int highestLevel = rs.getInt("highest_level_reached");

                LevelProgress lp = new LevelProgress(levelpack.getTotalLevels(), highestLevel);
                GameType gt = getGameTypeFromString(levelpack.getName());
                lpp = new LevelPackProgress(gt, d.getName(), lp);
            }
            return lpp;
        } catch (SQLException ex) {
            throw new BreakoutException("Couldn't get the desired levelprogress", ex);
        }
    }

    private GameType getGameTypeFromString(String name) {
        GameType[] types = GameType.values();
        
        for (GameType gt : types) {
            if (gt.name().toLowerCase().equals(name.toLowerCase())) {
                return gt;
            }
        }
        return GameType.TEST;
    }

    @Override
    public void removeLevelPackProgress(int playerId, LevelPack lp, Difficulty d) {
        try (
                Connection conn = DbConnection.getConnection();
                PreparedStatement prep = conn.prepareStatement(DELETE_LEVELPROGRESSION);) {

            prep.setInt(1, playerId);
            prep.setInt(2, d.getId());
            prep.setInt(3, lp.getId());
            prep.execute();

        } catch (SQLException ex) {
            throw new BreakoutException("Could not add new progress", ex);
        }
    }

    @Override
    public List<LevelPackProgress> getAllForPlayer(int userId) {

        try (
                Connection conn = DbConnection.getConnection();
                PreparedStatement prep = conn.prepareStatement(GET_LEVELPACKPROGRESS_BY_USER);) {

            prep.setInt(1, userId);

            List<LevelPackProgress> lpp = new ArrayList();
            ResultSet rs = prep.executeQuery();
            while (rs.next()) {
                int highestLevel = rs.getInt("highest_level_reached");
                int levelpackid = rs.getInt("levelpackid");
                int difficultyid = rs.getInt("difficultyid");
                LevelPack lp = Repositories.getLevelPackRepo().getById(levelpackid);
                GameType gt = getGameTypeFromString(lp.getName());
                Difficulty d = Repositories.getDifficultyRepository().getById(difficultyid);
                lpp.add(new LevelPackProgress(gt, d.getName(), new LevelProgress(lp.getTotalLevels(), highestLevel)));
            }
            return lpp;
        } catch (SQLException ex) {
            throw new BreakoutException("Couldn't get the desired levelprogress", ex);
        }

    }

    @Override
    public void initDefaults(int playerid) {

        List<Difficulty> diffs = Repositories.getDifficultyRepository().findAll();

        try {
            LevelPack lp = Repositories.getLevelPackRepo().getByName("arcade");
            for (Difficulty d : diffs) {
                addNewLevelProgression(playerid, lp, d, 1, false);
            }

        } catch (Exception e) {
            System.out.println(e.getMessage());
            throw new BreakoutException(e.getMessage());
        }
    }

    @Override
    public void setHighestLevelReached(int levelid, Player p, LevelPack lp, Difficulty difficulty) {
        try (
                Connection conn = DbConnection.getConnection();
                PreparedStatement prep = conn.prepareStatement(SET_HIGHESTLEVELREACHED);) {

            prep.setInt(1, levelid);
            prep.setInt(2, p.getUserId());
            prep.setInt(3, difficulty.getId());
            prep.setInt(4, lp.getId());
            prep.executeUpdate();

        } catch (SQLException ex) {
             throw new BreakoutException("Could not update highest level reached");
        }
    }

}
