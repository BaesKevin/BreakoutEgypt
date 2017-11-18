/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.breakoutegypt.domain;

import java.util.TimerTask;

/**
 *
 * @author kevin
 */
public class LevelTimerTask extends TimerTask {
    private BreakoutWorld breakoutWorld;
    private Game game;
    private Level level;

    public LevelTimerTask(BreakoutWorld breakoutWorld, Game game, Level level) {
        this.breakoutWorld = breakoutWorld;
        this.game = game;
        this.level = level;
    }
    
    @Override
    public void run() {
        breakoutWorld.step();

        game.notifyPlayers(level, breakoutWorld);

    }

}
