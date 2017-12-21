/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.breakoutegypt.data.mysql;

import com.breakoutegypt.data.BallRepository;
import com.breakoutegypt.data.mysql.util.DbConnection;
import com.breakoutegypt.domain.shapes.Ball;
import com.breakoutegypt.domain.shapes.ShapeDimension;
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
public class MysqlBallRepository implements BallRepository {

    private final String SELECT_ALL_BALLS = "select * from balls";
    private final String SELECT_BALLBYID = "select * from balls where ballid = ?";
    private final String SELECT_BALLS_BYLEVELID = "select * from balls join levelballs on levelballs.idball=balls.ballid where levelid = ?";
    private final String INSERT_BALL = "insert into balls(shapedimensionid,xspeed,yspeed, isStartingBall, playerIndex) values(?, ?, ?,?, ?)";
    private final String DELETE_BALL = "delete from balls where ballid = ?";
    private final String INSERT_LEVELBALLS = "insert into levelballs(levelid,idball) values(?, ?)";
    private final String DELETE_LEVELBALLS = "delete from levelballs where levelid = ?";

    private List<Ball> balls;

    @Override
    public List<Ball> getBalls() {
        this.balls = new ArrayList<>();
        try (
                Connection conn = DbConnection.getConnection();
                PreparedStatement prep = conn.prepareStatement(SELECT_ALL_BALLS);
                ResultSet rs = prep.executeQuery();) {
            while (rs.next()) {
                int xSpeed = rs.getInt("xspeed");
                int ySpeed = rs.getInt("yspeed");
                int shapedimensionId = rs.getInt("shapedimensionid");
                boolean isStartingBall = rs.getBoolean("isStartingBall");
                ShapeDimension shapeDimension = new MysqlShapeDimensionRepository().getShapeDimensionById(shapedimensionId);
                Ball ball = new Ball(shapeDimension, xSpeed, ySpeed);
                ball.setStartingBall(isStartingBall);
                this.balls.add(ball);
            }
            return this.balls;
        } catch (SQLException ex) {
            throw new BreakoutException("Couldn't load balls", ex);
        }
    }

    @Override
    public List<Ball> getBallsByLevelId(int id) {
        this.balls = new ArrayList<>();
        try (
                Connection conn = DbConnection.getConnection();
                PreparedStatement prep = conn.prepareStatement(SELECT_BALLS_BYLEVELID);) {
            prep.setInt(1, id);
            try (
                    ResultSet rs = prep.executeQuery();) {
                while (rs.next()) {
                    int ballId = rs.getInt("ballid");
                    int xSpeed = rs.getInt("xspeed");
                    int ySpeed = rs.getInt("yspeed");
                    int shapedimensionId = rs.getInt("shapedimensionid");
                    boolean isStartingBall = rs.getBoolean("isStartingBall");
                    int playerIndex = rs.getInt("playerIndex");
                    ShapeDimension shapeDimension = new MysqlShapeDimensionRepository().getShapeDimensionById(shapedimensionId);
                    Ball ball = new Ball(shapeDimension, xSpeed, ySpeed);
                    ball.setBallId(ballId);
                    ball.setStartingBall(isStartingBall);
                    ball.setPlayerIndex(playerIndex);
                    this.balls.add(ball);
                }
                return this.balls;
            }
        } catch (SQLException ex) {
            throw new BreakoutException("Couldn't load balls", ex);
        }
    }

    @Override
    public void addBall(Ball ball) {
        MysqlShapeDimensionRepository shapedimensionRepo = new MysqlShapeDimensionRepository();
        shapedimensionRepo.addShapeDimension(ball.getShape());
        try (
                Connection conn = DbConnection.getConnection();
                PreparedStatement prep = conn.prepareStatement(INSERT_BALL, PreparedStatement.RETURN_GENERATED_KEYS);) {
            prep.setInt(1, ball.getShape().getShapeId());
            prep.setInt(2, (int) ball.getXspeed());
            prep.setInt(3, (int) ball.getYspeed());
            prep.setBoolean(4, ball.isStartingBall());
            prep.setInt(5, ball.getPlayerIndex());
            prep.executeUpdate();
            try (ResultSet rs = prep.getGeneratedKeys();) {
                int ballId = -1;
                if (rs.next()) {
                    ballId = rs.getInt(1);
                }
                if (ballId < 0) {
                    throw new BreakoutException("Unable to add ball");
                }
                ball.setBallId(ballId);
            }

        } catch (SQLException ex) {
            throw new BreakoutException("Couldn't add ball", ex);
        }
    }

    @Override
    public void removeBall(Ball ball) {
        try (
                Connection conn = DbConnection.getConnection();
                PreparedStatement prep = conn.prepareStatement(DELETE_BALL);) {
            prep.setInt(1, ball.getBallId());
            prep.executeUpdate();
            MysqlShapeDimensionRepository shapedimensionRepo = new MysqlShapeDimensionRepository();
            shapedimensionRepo.removeShapeDimension(ball.getShape());
        } catch (SQLException ex) {
            throw new BreakoutException("Couldn't remove ball", ex);
        }
    }

    @Override
    public void addBallsForLevel(int levelId, List<Ball> balls) {
        for (Ball ball : balls) {
            this.addBall(ball);
            this.populateLevelBalls(levelId, ball);
        }
    }

    private void populateLevelBalls(int levelId, Ball ball) {
        try (
                Connection conn = DbConnection.getConnection();
                PreparedStatement prep = conn.prepareStatement(INSERT_LEVELBALLS);) {
            prep.setInt(1, levelId);
            prep.setInt(2, ball.getBallId());
            prep.executeUpdate();
        } catch (SQLException ex) {
            throw new BreakoutException("Couldn't add ball", ex);
        }
    }

    @Override
    public void removeLevelBalls(int levelId, List<Ball> balls) {
        try (
                Connection conn = DbConnection.getConnection();
                PreparedStatement prep = conn.prepareStatement(DELETE_LEVELBALLS);) {
            prep.setInt(1, levelId);
            prep.executeUpdate();
            for (Ball ball : balls) {
                this.removeBall(ball);
            }
        } catch (SQLException ex) {
            throw new BreakoutException("Couldn't remove balls", ex);
        }
    }

    @Override
    public Ball getBallById(int id) {
        try (
                Connection conn = DbConnection.getConnection();
                PreparedStatement prep = conn.prepareStatement(SELECT_BALLBYID);) {
            prep.setInt(1, id);
            try (ResultSet rs = prep.executeQuery()) {
                Ball ball = null;
                while (rs.next()) {
                    int xSpeed = rs.getInt("xspeed");
                    int ySpeed = rs.getInt("yspeed");
                    int shapedimensionId = rs.getInt("shapedimensionid");
                    boolean isStartingBall = rs.getBoolean("isStartingBall");
                    ShapeDimension shapeDimension = new MysqlShapeDimensionRepository().getShapeDimensionById(shapedimensionId);
                    ball = new Ball(shapeDimension, xSpeed, ySpeed);
                    ball.setStartingBall(isStartingBall);
                }
                return ball;
            }
        } catch (SQLException ex) {
            throw new BreakoutException("Couldn't get ball", ex);
        }
    }
}
