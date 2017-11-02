/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.breakoutws.domain;

import java.util.TimerTask;
import javax.websocket.Session;

/**
 *
 * @author kevin
 */
public class Game extends TimerTask {

    private static int ID = 0;
    private int id;
    private Level currentLevel;
    private BreakoutWorld world;
    private SessionManager manager;
    

    public Game() {
        id = ID++;
        world = new BreakoutWorld();
        manager = new SessionManager();

        currentLevel = new LevelFactory(world.getBox2dWorld()).getLevel1();
        world.setLevel(currentLevel);
    }

    public int getId() {
        return id;
    }

    public Level getCurrentLevel() {
        return currentLevel;
    }

    public void movePaddle(int x, int y) {
        world.movePaddle(x, y);
    }
    
    public void addPlayer(Session peer) {
        manager.addPlayer(peer);
    }

    public void removePlayer(Session peer) {
        manager.removePlayer(peer);
    }
    
    public boolean hasNoPlayers() {
        return manager.hasNoPlayers();
    }

    @Override
    public void run() {
        world.step();
        manager.notifyPlayers(currentLevel, world);
    }

    



}
