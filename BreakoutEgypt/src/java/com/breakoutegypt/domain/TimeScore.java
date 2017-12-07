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
public class TimeScore {
    
    private long beginTime;
    private long endTime;
    private long totalPausedTime;
    private long beginPause;
    private boolean running;
    private boolean paused;
    
    public TimeScore () {
        running = false;
        paused = false;
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
    
    public void pauseTimer() {
        if (!paused) {
            paused = true;
            beginPause = new Date().getTime();
        } else {
            paused = false;
            long pauseTime = new Date().getTime() - beginPause;
            totalPausedTime += pauseTime;
        }
    }
    
    public long getDuration () {
        return (endTime - beginTime) - totalPausedTime;
    }
}
