/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.breakoutegypt.data.mysql;

import com.breakoutegypt.data.LevelRepository;
import com.breakoutegypt.data.Repositories;
import com.breakoutegypt.data.mysql.util.DbConnection;
import com.breakoutegypt.domain.Game;
import com.breakoutegypt.domain.GameType;
import com.breakoutegypt.domain.Level;
import com.breakoutegypt.domain.LevelState;
import com.breakoutegypt.domain.levelprogression.Difficulty;
import com.breakoutegypt.domain.powers.FloodPowerDown;
import com.breakoutegypt.domain.shapes.Ball;
import com.breakoutegypt.domain.shapes.Paddle;
import com.breakoutegypt.domain.shapes.bricks.Brick;
import com.breakoutegypt.exceptions.BreakoutException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

/**
 *
 * @author Bjarne Deketelaere
 */
public class MysqlLevelRepository implements LevelRepository {

    MysqlBrickRepository brickRepo = new MysqlBrickRepository();
    MysqlPaddleRepository paddleRepo = new MysqlPaddleRepository();
    MysqlBallRepository ballRepo = new MysqlBallRepository();

    private final String SELECT_ALL_LEVELS = "select * from level";
    private final String SELECT_LEVEL_BYID = "select * from level where levelid = ?";
    private final String SELECT_LEVEL_BY_PACKID = "select * from level where levelpackid = ?";
    private final String SELECT_LEVEL_BYNUMBER = "select * from level where levelNumber = ? and levelpackid = ?";
    private final String INSERT_LEVEL = "insert into level(levelpackid,name,description, levelNumber, isMultiplayer) values(?,?,?,?,?)";
    private final String DELETE_LEVEL = "delete from level where levelid = ?";

    List<Level> levels;

    @Override
    public List<Level> getLevels(Game game) {
        this.levels = new ArrayList<>();
        try (
                Connection conn = DbConnection.getConnection();
                PreparedStatement prep = conn.prepareStatement(SELECT_ALL_LEVELS);
                ResultSet rs = prep.executeQuery();) {
            while (rs.next()) {
                int levelid = rs.getInt("levelid");
                int levelpackid = rs.getInt("levelpackid");
                int levelNumber = rs.getInt("levelNumber");
                String name = rs.getString("name");
                String description = rs.getString("description");
                List<Brick> levelBricks = brickRepo.getBricksByLevel(levelid);
                List<Paddle> levelPaddles = paddleRepo.getPaddlesByLevelId(levelid);
                List<Ball> levelBalls = ballRepo.getBallsByLevelId(levelid);
                LevelState levelstate = new LevelState(levelBalls, levelPaddles, levelBricks);
                
//                Game game = new Game(GameType.ARCADE, Difficulty.MEDIUM); //todo
                Level level = new Level(levelid, game, levelstate);
                level.setLevelName(name);
                level.setLevelNumber(levelpackid);
                this.levels.add(level);
            }
            return this.levels;
        } catch (SQLException ex) {
            throw new BreakoutException("Couldn't load levels", ex);
        }
    }

    @Override
    public Level getLevelById(int id, Game game) {
        try (
                Connection conn = DbConnection.getConnection();
                PreparedStatement prep = conn.prepareStatement(SELECT_LEVEL_BYID);) {
            prep.setInt(1, id);
            try (
                    ResultSet rs = prep.executeQuery();) {
                Level level=null;
                while (rs.next()) {
                    int levelid = rs.getInt("levelid");
                    int levelpackid = rs.getInt("levelpackid");
                    int levelNumber = rs.getInt("levelNumber");
                    String name = rs.getString("name");
                    String description = rs.getString("description");
                    List<Brick> levelBricks = brickRepo.getBricksByLevel(levelid);
                    List<Paddle> levelPaddles = paddleRepo.getPaddlesByLevelId(levelid);
                    List<Ball> levelBalls = ballRepo.getBallsByLevelId(levelid);
                    
                    Repositories.getPowerdownRepo().givePowerDownToBricks(levelBricks, levelBalls);
                    
                    LevelState levelstate = new LevelState(levelBalls, levelPaddles, levelBricks);
//                    Game game = new Game(GameType.ARCADE, Difficulty.MEDIUM); //todo
                    level = new Level(levelid, game, levelstate);
                }
                return level;
            }

        } catch (SQLException ex) {
            throw new BreakoutException("Couldn't load level", ex);
        }
    }

    @Override
    public Level getLevelByNumber(int number, int packId, Game game) {
         try (
                Connection conn = DbConnection.getConnection();
                PreparedStatement prep = conn.prepareStatement(SELECT_LEVEL_BYNUMBER);) {
            prep.setInt(1, number);
            prep.setInt(2, packId);
            try (
                    ResultSet rs = prep.executeQuery();) {
                Level level=null;
                while (rs.next()) {
                    int levelid = rs.getInt("levelid");
                    int levelpackid = rs.getInt("levelpackid");
                    int levelNumber = rs.getInt("levelNumber");
                    boolean isMultiplayer = rs.getBoolean("isMultiplayer");
                    
                    String name = rs.getString("name");
                    String description = rs.getString("description");
                    List<Brick> levelBricks = brickRepo.getBricksByLevel(levelid);
                    List<Paddle> levelPaddles = paddleRepo.getPaddlesByLevelId(levelid);
                    List<Ball> levelBalls = ballRepo.getBallsByLevelId(levelid);
                    LevelState levelstate = new LevelState(levelBalls, levelPaddles, levelBricks, game.getDifficulty(), true, isMultiplayer);
                    
                    Repositories.getPowerdownRepo().givePowerDownToBricks(levelBricks, levelBalls);
                    
                    level = new Level(levelid, game, levelstate);
                    level.setLevelNumber(levelNumber);
                    level.setLevelName(name);
                    level.setLevelPackId(levelpackid);
                }
                return level;
            }

        } catch (SQLException ex) {
            throw new BreakoutException("Couldn't load level", ex);
        }
    }

    @Override
    public List<Level> getLevelsByPackId(int packId, Game game) {
          try (
                Connection conn = DbConnection.getConnection();
                PreparedStatement prep = conn.prepareStatement(SELECT_LEVEL_BY_PACKID);) {
            prep.setInt(1, packId);
            try (
                    ResultSet rs = prep.executeQuery();) {
               List<Level> levels = new ArrayList<>();
               
                while (rs.next()) {
                    int levelid = rs.getInt("levelid");
                    int levelpackid = rs.getInt("levelpackid");
                    int levelNumber = rs.getInt("levelNumber");
                    String name = rs.getString("name");
                    String description = rs.getString("description");
                    List<Brick> levelBricks = brickRepo.getBricksByLevel(levelid);
                    List<Paddle> levelPaddles = paddleRepo.getPaddlesByLevelId(levelid);
                    List<Ball> levelBalls = ballRepo.getBallsByLevelId(levelid);
                    LevelState levelstate = new LevelState(levelBalls, levelPaddles, levelBricks);
//                    Game game = new Game(GameType.ARCADE, Difficulty.MEDIUM); //todo
                    Level level =new Level(levelid, game, levelstate);
                    level.setLevelName(name);
                    level.setLevelNumber(levelNumber);
                    level.setLevelDescription(description);
                    levels.add(level);
                    
                }
                return levels;
            }

        } catch (SQLException ex) {
            throw new BreakoutException("Couldn't load level", ex);
        }
    }
    
    

    @Override
    public void addLevel(Level level) {
        try(
                Connection conn=DbConnection.getConnection();
                PreparedStatement prep=conn.prepareStatement(INSERT_LEVEL,PreparedStatement.RETURN_GENERATED_KEYS);
                ){
            prep.setInt(1, level.getLevelPackId());
            prep.setString(2, level.getLevelName());
            prep.setString(3, level.getLevelDescription());
            prep.setInt(4, level.getLevelNumber());
            prep.setBoolean(5, level.getLevelState().isMultiplayer());
            prep.executeUpdate();
            try(ResultSet rs=prep.getGeneratedKeys();){
                int levelId = -1;
                if(rs.next()){
                    levelId=rs.getInt(1);
                }
                if(levelId<0){
                    throw new BreakoutException("Unable to add level");
                }
                level.setId(levelId);
//                level.setLevelNumber(levelId); 
            }
            ballRepo.addBallsForLevel(level.getId(), level.getLevelState().getBalls());
            brickRepo.addBricksForLevel(level.getId(), level.getLevelState().getBricks());
            paddleRepo.addPaddlesForLevel(level.getId(), level.getLevelState().getPaddles());
        } catch (SQLException ex) {
            throw new BreakoutException("Couldn't add level", ex);
        }
    }
    
    @Override
    public void removeLevel(Level level) {
        brickRepo.removeLevelBricks(level.getId(), level.getLevelState().getBricks());
        paddleRepo.removeLevelPaddles(level.getId(), level.getLevelState().getPaddles());
        ballRepo.removeLevelBalls(level.getId(), level.getLevelState().getBalls());
        try (
                Connection conn=DbConnection.getConnection();
                PreparedStatement prep=conn.prepareStatement(DELETE_LEVEL);
                ){
            prep.setInt(1, level.getId());
            prep.executeUpdate();
        } catch (SQLException ex) {
            throw new BreakoutException("Couldn't remove level", ex);
        }
    }
}