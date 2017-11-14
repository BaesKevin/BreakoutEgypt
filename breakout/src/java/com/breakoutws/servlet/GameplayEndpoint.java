/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.breakoutws.servlet;

import com.breakoutws.domain.Game;
import com.breakoutws.domain.GameManager;
import com.breakoutws.domain.Player;
import com.breakoutws.domain.User;
import com.breakoutws.servlet.util.JsonMoveCommand;
import com.breakoutws.servlet.util.MoveCommandDecoder;
import java.io.IOException;
import javax.websocket.EncodeException;
import javax.websocket.OnClose;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.ServerEndpoint;

/**
 *
 * @author kevin
 */
@ServerEndpoint(
        value="/gameplay",
        decoders={MoveCommandDecoder.class}
)
public class GameplayEndpoint {
    private Game game;
    
    public GameplayEndpoint(){
        
    }
    @OnOpen
    public void onOpen(Session peer) {
        System.out.println("GamePlayEndpoint: Opening socket connection");
        GameManager gm = new GameManager();
        
        int gameId = Integer.parseInt(peer.getPathParameters().get("gameId"));
        
        game = gm.getGame(gameId);
        
        Player player = new Player(new User("player"));
        gm.addPlayer(gameId, peer, player);
    }

    @OnClose
    public void onClose(Session peer) {
        int gameId = Integer.parseInt(peer.getPathParameters().get("gameId"));
        new GameManager().removePlayer(gameId, peer);
    }
    
    @OnMessage
    public void broadcastFigure(JsonMoveCommand moveCommand, Session session) throws IOException, EncodeException{
        int x = moveCommand.getJson().getInt("x");
        int y = moveCommand.getJson().getInt("y");
        
        if(game != null){
            game.movePaddle(session, x,y);
        }
        else
        {
            System.out.println("GamePlayEndpoint: Trying to move paddle for game that doesn't exist");
        }
    }
}
