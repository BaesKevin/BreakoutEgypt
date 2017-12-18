/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.breakoutegypt.domain;

import com.breakoutegypt.connectionmanagement.PlayerConnection;
import com.breakoutegypt.domain.levelprogression.LevelProgress;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author kevin
 */
public class GameManager {

    private static Map<Integer, Game> games = Collections.synchronizedMap(new HashMap());
       
    public int createGame(GameType type, String difficulty){
        return createGame(type, difficulty, 1);
    }
    
    private int createGame(GameType type, String difficulty,int numberOfPlayers){
        Game game = new Game(numberOfPlayers, type, difficulty);
        
        games.put(game.getId(), game);
               
        return game.getId();
    }

    public Game getGame(int gameId){
        return games.get(gameId);
    }
    
    public void startGame(int gameId){
        Game game = games.get(gameId);
        
        game.startLevel();
       
    }
    
    public void stopGame(int gameId){
        games.get(gameId).stopLevel();        
    }

    public void addConnectingPlayer(int gameId, Player player) {
        
        Game game = games.get(gameId);
        
        if(game!=null)
            game.addConnectingPlayer(player);
    }
    
    public void addConnectionForPlayer(int gameId, String name, PlayerConnection conn){
         Game game = games.get(gameId);
        
        if(game!=null)
            game.addConnectionForPlayer(name, conn);
    }
    
    public void assignPaddleToPlayer(int gameId, Player player) {
        Game game = games.get(gameId);
                
        if(game!=null)
            game.assignPaddleToPlayer(player);
        
    }

    public void removePlayer(int gameId, String name) {
        if(games != null){
            Game game = games.get(gameId);

            if(game != null){
                game.removePlayer(name);
                if(game.hasNoPlayers()){
                    stopGame(game.getId());
                    games.remove(game.getId());
                }
            }
        }
        
    }
    
    public boolean hasNextLevel(int gameId) {
        Game game = games.get(gameId);
        
        if( game != null ){
            return games.get(gameId).hasNextLevel();
        }
        
        return false;
    }
}
