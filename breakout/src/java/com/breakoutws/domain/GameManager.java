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
       
    public int createGame(int numberOfPlayers, GameType type){
        Game game = new Game(numberOfPlayers, type);
        
        games.put(game.getId(), game);
               
        System.out.println("GameManager: Created game " + game.getId());
        return game.getId();
    }

    public Game getGame(int gameId){
        return games.get(gameId);
    }
    
    public Level getLevel(int gameId) {
        Game game = games.get(gameId);
        
        if(game == null){
            System.out.println("GameManager: Trying to get level for game that doesn't exist");
            return null;
        }
        
        return game.getCurrentLevel();
    }
    
    public void startGame(int gameId){
        Game game = games.get(gameId);
        
        game.startLevel();
       
    }
    
    public void stopGame(int gameId){
        System.out.println("GameManager: stopping game " + gameId );
        games.get(gameId).stopLevel();        
    }

    public void addPlayer(int gameId, Player player) {
        
        Game game = games.get(gameId);
        
        if(game!=null)
            game.addConnectingPlayer(player);
        else
            System.out.println("GameManager: Trying to add player to game that doesn't exist");
    }
    
    public void addSessionForPlayer(int gameId, String name, Session session){
         Game game = games.get(gameId);
        
        if(game!=null)
            game.addSessionForPlayer(name, session);
        else
            System.out.println("GameManager: Trying to add player to game that doesn't exist");
    }

    public void removePlayer(int gameId, Session peer) {
        if(games != null){
            Game game = games.get(gameId);

            if(game != null){
                game.removePlayer(peer);
                if(game.hasNoPlayers()){
                    
                    stopGame(game.getId());
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
