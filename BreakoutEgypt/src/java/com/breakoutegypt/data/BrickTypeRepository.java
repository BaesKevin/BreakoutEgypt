/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.breakoutegypt.data;

import com.breakoutegypt.domain.shapes.bricks.BrickType;
import java.util.List;

/**
 *
 * @author Bjarne Deketelaere
 */
public interface BrickTypeRepository {
    public List<BrickType> getBrickTypes();
    public BrickType getBrickTypeById(int id);
    public BrickType getBrickTypeByName(String name);
    public void addBrickType(BrickType bricktype);
}
