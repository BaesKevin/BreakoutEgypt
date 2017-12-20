/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.breakoutegypt.data;

import com.breakoutegypt.domain.effects.Effect;
import com.breakoutegypt.domain.shapes.bricks.Brick;
import java.util.List;

/**
 *
 * @author Bjarne Deketelaere
 */
public interface EffectRepository {
    public void giveEffectsToBrick(Brick brick, List<Brick> allBricks);
    public void insertEffectsToBrick(int brickId,List<Effect> effects);
    public void removeEffectsOfBrick(int brickId);
}
