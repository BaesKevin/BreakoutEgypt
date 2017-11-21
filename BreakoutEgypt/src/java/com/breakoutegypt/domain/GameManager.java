/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.breakoutegypt.domain;

import com.breakoutegypt.connectionmanagement.PlayerConnection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author kevin
 */
public class GameManager {

    private static Map<Integer, Game> games = Collections.synchronizedMap(new HashMap());
       
    public int createGame(int numberOfPlayers, int startingLevel, GameType type){
        Game game = new Game(numberOfPlayers, startingLevel, type);
        
        games.put(game.getId(), game);
               
        System.out.println("GameManager: Created game " + game.getId());
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
        System.out.println("GameManager: stopping game " + gameId );
        games.get(gameId).stopLevel();        
    }

    public void addConnectingPlayer(int gameId, Player player) {
        
        Game game = games.get(gameId);
        
        if(game!=null)
            game.addConnectingPlayer(player);
        else
            System.out.println("GameManager: Trying to add player to game that doesn't exist");
    }
    
    public void addConnectionForPlayer(int gameId, String name, PlayerConnection conn){
         Game game = games.get(gameId);
        
        if(game!=null)
            game.addConnectionForPlayer(name, conn);
        else
            System.out.println("GameManager: Trying to add player to game that doesn't exist");
    }
    
    public void assignPaddleToPlayer(int gameId, Player player) {
        Game game = games.get(gameId);
                
        if(game!=null)
            game.assignPaddleToPlayer(player);
        else
            System.out.println("GameManager: Trying to add player to game that doesn't exist");
        
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
            } else {
//                System.out.println("TGameManager: rying to remove player to game that doesn't exist");
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
