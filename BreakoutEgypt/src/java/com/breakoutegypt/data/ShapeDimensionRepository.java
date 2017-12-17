/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.breakoutegypt.data;

import com.breakoutegypt.domain.shapes.ShapeDimension;
import java.util.List;

/**
 *
 * @author Bjarne Deketelaere
 */
public interface ShapeDimensionRepository {
    public List<ShapeDimension> getShapeDimensions();
    public ShapeDimension getShapeDimensionById(int id);
    public void addShapeDimension(ShapeDimension shapedimension);
    public void removeShapeDimension(ShapeDimension shapedimension);
}
