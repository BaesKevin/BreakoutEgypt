/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.breakoutegypt.domain.levelprogression;

import com.breakoutegypt.domain.Level;

/**
 *
 * @author snc
 */
public class UserLevel {
    
    private boolean isLocked;
    private int seqNumber;
    private String name;
    
    public UserLevel(int sequenceNumber, boolean isLocked, String name) {
        this.seqNumber = sequenceNumber;
        this.isLocked = isLocked;
        this.name = name;      
    }

    public boolean isIsLocked() {
        return isLocked;
    }

    public String getName() {
        return name;
    }

    public int getSeqNumber() {
        return seqNumber;
    }  
    
    
}
