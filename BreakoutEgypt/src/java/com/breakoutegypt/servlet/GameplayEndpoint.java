/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.breakoutegypt.servlet;

import com.breakoutegypt.connectionmanagement.WebsocketConnection;
import com.breakoutegypt.domain.Game;
import com.breakoutegypt.domain.GameManager;
import com.breakoutegypt.domain.Player;
import com.breakoutegypt.domain.User;
import com.breakoutegypt.servlet.util.JsonMoveCommand;
import com.breakoutegypt.servlet.util.MoveCommandDecoder;
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
        
        GameManager gm = new GameManager();
        
        int gameId = Integer.parseInt(peer.getPathParameters().get("gameId"));
        
        game = gm.getGame(gameId);
        
        // TODO retrieve actual username from session/path parameter
        gm.addConnectionForPlayer(gameId, "player", new WebsocketConnection(peer));
//        gm.setSessionForPlayer(name, session); 
    }

    @OnClose
    public void onClose(Session peer) {
        int gameId = Integer.parseInt(peer.getPathParameters().get("gameId"));
        new GameManager().removePlayer(gameId, "player");
    }
    
    @OnMessage
    public void broadcastFigure(JsonMoveCommand moveCommand, Session session) throws IOException, EncodeException{
        int x = moveCommand.getJson().getInt("x");
        int y = moveCommand.getJson().getInt("y");
        
        if(game != null){
            game.movePaddle("player", x,y);
        }
    }
}
