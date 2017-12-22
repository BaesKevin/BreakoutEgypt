/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.breakoutegypt.domain;

import com.breakoutegypt.connectionmanagement.PlayerConnection;
import com.breakoutegypt.domain.levelprogression.LevelProgress;
import java.security.SecureRandom;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author kevin
 */
public class GameManager {

    private static Map<String, Game> games = Collections.synchronizedMap(new HashMap());
       
    public String createGame(GameType type, String difficulty){
        return createGame(type, difficulty, 1);
    }
    
    public String createGame(GameType type, String difficulty,int numberOfPlayers){
        String uniqueId = getUniqueId(4);
        Game game = new Game(numberOfPlayers, type, difficulty, uniqueId);
        
        games.put(game.getId(), game);
               
        return game.getId();
    }

    public Game getGame(String gameId){
        return games.get(gameId);
    }
    
    public void startGame(String gameId){
        Game game = games.get(gameId);
        
        game.startLevel();
       
    }
    
    public void stopGame(String gameId){
        games.get(gameId).stopLevel();        
    }

    public void addConnectingPlayer(String gameId, Player player) {
        
        Game game = games.get(gameId);
        
        if(game!=null)
            game.addConnectingPlayer(player);
    }
    
    public void addConnectionForPlayer(String gameId, String name, PlayerConnection conn){
         Game game = games.get(gameId);
        
        if(game!=null)
            game.addConnectionForPlayer(name, conn);
    }
    
//    public void assignPaddleToPlayer(int gameId, Player player) {
//        Game game = games.get(gameId);
//                
//        if(game!=null)
//            game.assignPaddleToPlayer(player);
//        
//    }

    public void removePlayer(String gameId, String name) {
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
    
    public boolean hasNextLevel(String gameId) {
        Game game = games.get(gameId);
        
        if( game != null ){
            return games.get(gameId).hasNextLevel();
        }
        
        return false;
    }
    
    private String getUniqueId(int length) {
        String uniqueId = null;
        do {
            uniqueId = generateUniqueId(length);
        } while (games.containsKey(uniqueId));
        return uniqueId;
    }
    
    private String generateUniqueId(int length) {        

        String alphabet = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz";
        SecureRandom rnd = new SecureRandom();

        StringBuilder sb = new StringBuilder( length );
            for( int i = 0; i < length; i++ ) 
                sb.append( alphabet.charAt( rnd.nextInt(alphabet.length()) ) );
       return sb.toString();
       
    }
}
