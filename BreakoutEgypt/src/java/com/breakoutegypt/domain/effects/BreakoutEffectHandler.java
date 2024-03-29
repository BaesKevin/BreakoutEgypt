/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.breakoutegypt.domain.effects;

import com.breakoutegypt.domain.BreakoutWorld;
import com.breakoutegypt.domain.Level;
import com.breakoutegypt.domain.LevelState;
import com.breakoutegypt.domain.ServerClientMessageRepository;
import com.breakoutegypt.domain.messages.BrickMessage;
import com.breakoutegypt.domain.messages.BrickMessageType;
import com.breakoutegypt.domain.shapes.bricks.Brick;
import java.util.ArrayList;
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

        // all bricks now belong to the person that destroyed them
        for(Brick b : bricks){
            b.setPlayerIndex(e.getCentreBrick().getPlayerIndex());
        }
        
        breakoutWorld.destroyBricks(bricks);
    }

    @Override
    public void handle(ToggleEffect e) {
        List<Brick> bricksToToggle = e.getBricksToToggle();
        ServerClientMessageRepository repo = breakoutWorld.getMessageRepo();

        List<Brick> bricksToRemove = new ArrayList();
        
        for (Brick brick : bricksToToggle) {
            if (brick.getBody().getFixtureList() != null) {
                brick.toggle();

                BrickMessageType toggleType;
                if (brick.isVisible()) {
                    toggleType = BrickMessageType.SHOW;
                } else {
                    toggleType = BrickMessageType.HIDE;
                }
                repo.addBrickMessage(new BrickMessage(brick.getName(), toggleType));
            } else {
                bricksToRemove.add(brick);
            }
        }
        e.removeBricksFromToggleList(bricksToRemove);
    }

}
