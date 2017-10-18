/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.breakoutws.servlet;

import com.breakoutws.domain.Ball;
import com.breakoutws.domain.BreakoutWorld;
import com.breakoutws.domain.Brick;
import java.util.TimerTask;

/**
 *
 * @author kevin
 */
public class GameSimulator extends TimerTask {

    private Ball ball;
    private Brick brick;
    
    private ClientUpdater updater;
    private static BreakoutWorld world;
    
    public GameSimulator() {
        world = new BreakoutWorld();
        updater = new ClientUpdater(this);
    }
    
    public Ball getBall() {
        return ball;
    }
    
    public Brick getBrick(){
        return brick;
    }
    
    public BreakoutWorld getBreakoutWorld(){
        return world;
    }
    
    public void movePaddle(int x, int y){
        world.movePaddle(x, y);
    }

    @Override
    public void run() {
        try{
            world.step();
            updater.notifyClients();
        } catch(NullPointerException e){
            e.printStackTrace();
        }
        
    }

}
