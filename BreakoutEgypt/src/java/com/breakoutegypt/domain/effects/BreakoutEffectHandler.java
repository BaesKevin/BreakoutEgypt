/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.breakoutegypt.domain.effects;

import com.breakoutegypt.domain.BreakoutWorld;
import com.breakoutegypt.domain.Level;
import com.breakoutegypt.domain.LevelState;
import com.breakoutegypt.domain.shapes.bricks.Brick;
import java.util.List;

/**
 *
 * @author kevin
 */
public class BreakoutEffectHandler implements EffectHandler {

    private Level level;
    private LevelState levelState;
    private BreakoutWorld breakoutWorld;

    public BreakoutEffectHandler(Level level, LevelState levelState, BreakoutWorld breakoutWorld) {
        this.level = level;
        this.levelState = levelState;
        this.breakoutWorld = breakoutWorld;
    }

    @Override
    public void handle(ExplosiveEffect e) {
        List<Brick> bricks = levelState.getRangeOfBricksAroundBody(e.getCentreBrick(), e.getRadius());

        breakoutWorld.destroyBricks(level, levelState, bricks);
    }

    @Override
    public void handle(ToggleEffect e) {
        breakoutWorld.toggleBricks(e.getBricksToToggle());
    }

}
