/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.breakoutegypt.domain.shapes.bricks;

/**
 *
 * @author kevin
 */
public class BrickType {
    private String name;
    public BrickType(String name){
        this.setName(name);
    }

    public void setName(String name){
        this.name=name;
    }
    public String getName(){
        return this.name;
    }
    //REGULAR, UNBREAKABLE, EXPLOSIVE, SWITCH, TARGET
}