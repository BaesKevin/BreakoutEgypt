/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.breakoutegypt.test;

import com.breakoutegypt.domain.shapes.BodyConfigurationFactory;
import com.breakoutegypt.domain.shapes.Brick;
import com.breakoutegypt.domain.shapes.BrickType;
import com.breakoutegypt.domain.shapes.ShapeDimension;
import java.awt.Point;
import org.junit.Test;

/**
 *
 * @author kevin
 */
public class LevelObjectCreationTest {
    
    @Test
    public void testCreatingTriangle(){
        
        
        ShapeDimension shape = new ShapeDimension("triangle", 20,20, 20,20);
        Brick brick = new Brick(shape, BrickType.REGULAR, new Point(0,0));
    }
    
}
