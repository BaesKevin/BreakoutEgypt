/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.breakoutegypt.data;

import com.breakoutegypt.domain.shapes.bricks.Brick;
import java.util.List;

/**
 *
 * @author Bjarne Deketelaere
 */
public interface BrickRepository {
    public List<Brick> getBricks();
    public Brick getBrickById(int id);
    public List<Brick> getBricksByLevel(int id);
}
