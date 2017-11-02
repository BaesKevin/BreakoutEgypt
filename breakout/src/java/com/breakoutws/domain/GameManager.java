/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.breakoutws.domain;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Timer;
import javax.websocket.Session;

/**
 *
 * @author kevin
 */
public class GameManager {

    private static Map<Integer, Game> games = Collections.synchronizedMap(new HashMap());
    private static Map<Integer, Timer> simulations =  Collections.synchronizedMap(new HashMap());
    
    public int createGame(){
        Game game = new Game();
        
        games.put(game.getId(), game);
        simulations.put(game.getId(), new Timer());
        
        System.out.println("Created game " + game.getId());
        return game.getId();
    }

    public Game getGame(int gameId){
        return games.get(gameId);
    }
    
    public Level getLevel(int gameId) {
        Game game = games.get(gameId);
        
        if(game == null){
            System.out.println("Trying to get level for game that doesn't exist");
            return null;
        }
        
        return game.getCurrentLevel();
    }
    
    public void startGame(int gameId){
        Game game = games.get(gameId);
       
        
        if(game != null){
            Timer t = simulations.get(gameId);
            t.schedule(game, 0, 1000/60);
        }
        else{
            System.out.println("trying to start game that hasn't been created");
        }
        
    }
    
    public void stopGame(int gameId){
        Timer t = simulations.get(gameId);
        t.cancel();
        
        simulations.remove(gameId);
        games.remove(gameId);
        System.out.printf("Game %d stopped", gameId);
    }

    public void addPlayer(int gameId, Session peer) {
        
        Game game = games.get(gameId);
        
        if(game!=null)
            game.addPlayer(peer);
        else
            System.out.println("Trying to add player to game that doesn't exist");
    }

    public void removePlayer(int gameId, Session peer) {
        if(games != null){
            Game game = games.get(gameId);
            System.out.println("Removing peer from game " + gameId);    
            if(game != null){
                game.removePlayer(peer);
                if(game.hasNoPlayers()){
                    System.out.println("No more players");
                    stopGame(game.getId());
                }
            } else {
                System.out.println("Trying to remove player to game that doesn't exist");
            }
        }
        
    }
}
