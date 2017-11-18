/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.breakoutegypt.domain;

import java.util.Date;

/**
 *
 * @author BenDB
 */
public class ScoreTimer {
    
    private long beginTime;
    private long endTime;
    private boolean running;
    
    public ScoreTimer () {
        running = false;
    }
    
    public void start () {
        if (!running) {
            beginTime = new Date().getTime();
            running = true;
        }
    }
    
    public void stop () {
        endTime = new Date().getTime();
        running = false;
    }
    
    public long getDuration () {
        return endTime - beginTime;
    }
}
