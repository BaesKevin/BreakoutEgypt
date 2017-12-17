/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.breakoutegypt.servlet;

import com.breakoutegypt.servlet.util.HttpSessionConfigurator;
import com.breakoutegypt.connectionmanagement.WebsocketConnection;
import com.breakoutegypt.domain.Game;
import com.breakoutegypt.domain.GameManager;
import com.breakoutegypt.domain.Player;
import com.breakoutegypt.domain.User;
import com.breakoutegypt.servlet.util.JsonMoveCommand;
import com.breakoutegypt.servlet.util.MoveCommandDecoder;
import java.io.IOException;
import javax.servlet.http.HttpSession;
import javax.websocket.EncodeException;
import javax.websocket.EndpointConfig;
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
        value="/gameplay", configurator=HttpSessionConfigurator.class,
        decoders={MoveCommandDecoder.class}
)
public class GameplayEndpoint {
    private Game game;
//    private User user;
    private Player player;
    private String username;
    
    public GameplayEndpoint(){
        
    }
    
    @OnOpen
    public void onOpen(Session peer,EndpointConfig config) {
        
        GameManager gm = new GameManager();
        
        int gameId = Integer.parseInt(peer.getPathParameters().get("gameId"));
        HttpSession httpSession=(HttpSession) config.getUserProperties().get(HttpSession.class.getName());
//        user=(User)httpSession.getAttribute("user");
        player = (Player) httpSession.getAttribute("player");
        
        game = gm.getGame(gameId);
        username = player.getUsername();
        
        // TODO retrieve actual username from session/path parameter
        gm.addConnectionForPlayer(gameId, username, new WebsocketConnection(peer));
//        gm.setSessionForPlayer(name, session); 
    }

    @OnClose
    public void onClose(Session peer) {
        int gameId = Integer.parseInt(peer.getPathParameters().get("gameId"));
        new GameManager().removePlayer(gameId, username);
    }
    
    @OnMessage
    public void broadcastFigure(JsonMoveCommand moveCommand, Session session) throws IOException, EncodeException{
        int x = moveCommand.getJson().getInt("x");
        int y = moveCommand.getJson().getInt("y");
        
        if(game != null){
            game.movePaddle(username, x,y);
        }
    }
}
