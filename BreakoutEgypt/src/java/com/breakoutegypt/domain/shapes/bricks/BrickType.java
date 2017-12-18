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
    private int bricktypeId;
    public BrickType(String name){
        this(0,name);
    }
    public BrickType(int bricktypeId,String name){
        this.setBrickTypeId(bricktypeId);
        this.setName(name);
    }

    public void setName(String name){
        this.name=name;
    }
    public String getName(){
        return this.name;
    }
    //REGULAR, UNBREAKABLE, EXPLOSIVE, SWITCH, TARGET

    public void setBrickTypeId(int bricktypeId) {
        this.bricktypeId=bricktypeId;
    }
    
    public int getBrickTypeId(){
        return this.bricktypeId;
    }
}