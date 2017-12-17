/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.breakoutegypt.domain.shapes;

import java.awt.Color;
import javax.json.Json;
import javax.json.JsonObjectBuilder;

/**
 * Used to provide inital values for Box2d objects. Changing these values will
 * not influence the box2d world. To change position or dimensions you must use
 * the Body object
 *
 * @author kevin
 */
public class ShapeDimension {
    
    private int shapeId=0;
    private float posX;
    private float posY;
    private int width, height;
    //Ball radius in pixels
    private Color color;
    private String name;

    public ShapeDimension(String name, float x, float y, int width, int height) {
        this(name, x, y, width, height, Color.BLACK);
    }
    
    public ShapeDimension(float posX, float posY, int width, int height){
        this("shape",posX,posY,width,height);
    }
    
    public ShapeDimension(String name, float posX, float posY, int width, int height, Color color) {
        this.name = name;
        this.posX = posX;
        this.posY = posY;
        this.width = width;
        this.height = height;
        this.color = color;
    }

    public ShapeDimension(ShapeDimension s) {
        this(s.name, s.posX, s.posY, s.width, s.height, s.color);
    }
    
    public int getShapeId(){
        return this.shapeId;
    }
    
    public void setShapeId(int id){
        this.shapeId=id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public float getPosX() {
        return posX;
    }

    public void setPosX(float posX) {
        this.posX = posX;
    }

    public float getPosY() {
        return posY;
    }

    public void setPosY(float posY) {
        this.posY = posY;
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public Color getColor() {
        return color;
    }

    public void setColor(Color color) {
        this.color = color;
    }

    public JsonObjectBuilder toJson() {
        JsonObjectBuilder brickkObjectBuilder = Json.createObjectBuilder();
        brickkObjectBuilder.add("name", this.name);
        brickkObjectBuilder.add("x", this.posX);
        brickkObjectBuilder.add("y", posY);
        brickkObjectBuilder.add("width", this.width);
        brickkObjectBuilder.add("height", this.height);
        brickkObjectBuilder.add("color", String.format("rgb(%d,%d,%d)", this.color.getRed(), this.color.getGreen(), this.color.getBlue()));
        return brickkObjectBuilder;
    }

}
