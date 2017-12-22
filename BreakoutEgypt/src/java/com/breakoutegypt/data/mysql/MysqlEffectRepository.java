/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.breakoutegypt.data.mysql;

import com.breakoutegypt.data.BrickRepository;
import com.breakoutegypt.data.EffectRepository;
import com.breakoutegypt.data.mysql.util.DbConnection;
import com.breakoutegypt.domain.effects.Effect;
import com.breakoutegypt.domain.effects.ExplosiveEffect;
import com.breakoutegypt.domain.effects.ToggleEffect;
import com.breakoutegypt.domain.shapes.bricks.Brick;
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
public class MysqlEffectRepository implements EffectRepository {

    private final String TOGGLE_EFFECTS = "select * from bricks join toggleeffects tog on tog.brickid=bricks.brickid where bricks.brickid = ?";
    private final String EXPLOSIVE_EFFECTS = "select * from bricks join explosiveeffects explos on explos.brickid=bricks.brickid where bricks.brickid = ?";
    private final String INSERT_TOGGLE_EFFECTS = "insert into toggleeffects(brickid,totogglebrickid) values(?,?)";
    private final String INSERT_EXPLOSIVE_EFFECT = "insert into explosiveeffects(brickid,centerbrickid,radius) values(?, ?, ?)";

    private final String DELETE_TOGGLE_EFFECTS = "delete from toggleeffects where brickid = ? or totogglebrickid = ?";
    private final String DELETE_EXPLOSIVE_EFFECTS = " delete from explosiveeffects where brickid = ? or centerbrickid = ?";

    @Override
    public void giveEffectsToBrick(Brick brick, List<Brick> allBricks) {
        this.getToggleEffects(brick, allBricks);
        this.getExplosiveEffects(brick);
    }

    private void getExplosiveEffects(Brick brick) {
        try (
                Connection conn = DbConnection.getConnection();
                PreparedStatement prep = conn.prepareStatement(EXPLOSIVE_EFFECTS);) {
            prep.setInt(1, brick.getBrickId());
            try (
                    ResultSet rs = prep.executeQuery();) {
                while (rs.next()) {
                    int brickid = rs.getInt("brickid");
                    int centerBrickId = rs.getInt("centerbrickid");
                    int radius = rs.getInt("radius");
                    ExplosiveEffect explosive = new ExplosiveEffect(brick, radius);
                    brick.addEffect(explosive);
                }
            }
        } catch (SQLException ex) {
            throw new BreakoutException("Couldn't get explosiveEffects for brick", ex);
        }
    }

    private void getToggleEffects(Brick brick, List<Brick> allBricks) {
        try (
                Connection conn = DbConnection.getConnection();
                PreparedStatement prep = conn.prepareStatement(TOGGLE_EFFECTS);) {
            prep.setInt(1, brick.getBrickId());
            try (
                    ResultSet rs = prep.executeQuery();) {
                List<Brick> toToggleBricks = new ArrayList<>();
                while (rs.next()) {
                    int brickId = rs.getInt("brickid");
                    int toToggleBrickId = rs.getInt("totogglebrickid");
                    toToggleBricks.add(findBrickById(toToggleBrickId, allBricks));
                }
                if (!toToggleBricks.isEmpty()) {
                    brick.addEffect(new ToggleEffect(toToggleBricks));
                }
            }
        } catch (SQLException ex) {
            throw new BreakoutException("Couldn't get toggleEffects for brick", ex);
        }
    }
    
    private Brick findBrickById(int id, List<Brick> bricks){
        Brick brick = null;
        
        for(Brick b : bricks){
            if(b.getBrickId() == id){
                return b;
            }
        }
        
        return brick;
    }

    @Override
    public void insertEffectsToBrick(int brickId, List<Effect> effects) {
        for (Effect effect : effects) {
            if (effect instanceof ToggleEffect) {
                this.insertToggleEffect(brickId, (ToggleEffect) effect);
            } else if (effect instanceof ExplosiveEffect) {
                if (((ExplosiveEffect) effect).getRadius() != 0) {
                    this.insertExplosiveEffect(brickId, (ExplosiveEffect) effect);
                }
            }
        }
    }

    private void insertExplosiveEffect(int brickId, ExplosiveEffect explosive) {
        try (
                Connection conn = DbConnection.getConnection();
                PreparedStatement prep = conn.prepareStatement(INSERT_EXPLOSIVE_EFFECT);) {
            prep.setInt(1, brickId);
            prep.setInt(2, explosive.getCentreBrick().getBrickId());
            prep.setInt(3, explosive.getRadius());
            prep.executeUpdate();
        } catch (SQLException ex) {
            throw new BreakoutException("Couldn't add explosiveEffect to brick", ex);
        }
    }

    private void insertToggleEffect(int brickId, ToggleEffect toggle) {
        for (Brick bricks : toggle.getBricksToToggle()) {
            try (
                    Connection conn = DbConnection.getConnection();
                    PreparedStatement prep = conn.prepareStatement(INSERT_TOGGLE_EFFECTS);) {
                prep.setInt(1, brickId);
                prep.setInt(2, bricks.getBrickId());
                prep.executeUpdate();

            } catch (SQLException ex) {
                throw new BreakoutException("Couldn't add toggleEffect to brick", ex);
            }
        }
    }

    @Override
    public void removeEffectsOfBrick(int brickId) {
        this.removeExplosiveEffects(brickId);
        this.removeToggleEffects(brickId);
    }

    private void removeToggleEffects(int brickId) {
        try (
                Connection conn = DbConnection.getConnection();
                PreparedStatement prep = conn.prepareStatement(DELETE_TOGGLE_EFFECTS);) {
            prep.setInt(1, brickId);
            prep.setInt(2, brickId);
            prep.executeUpdate();
        } catch (SQLException ex) {
            throw new BreakoutException("Couldn't remove toggleEffect", ex);
        }
    }

    private void removeExplosiveEffects(int brickId) {
        try (
                Connection conn = DbConnection.getConnection();
                PreparedStatement prep = conn.prepareStatement(DELETE_EXPLOSIVE_EFFECTS);) {
            prep.setInt(1, brickId);
            prep.setInt(2, brickId);
            prep.executeUpdate();
        } catch (SQLException ex) {
            throw new BreakoutException("Couldn't remove explosiveEffect", ex);
        }
    }
}
