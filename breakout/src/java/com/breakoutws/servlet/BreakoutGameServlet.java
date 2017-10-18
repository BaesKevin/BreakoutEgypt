/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.breakoutws.servlet;

import com.breakoutws.domain.JsonMoveCommand;
import com.breakoutws.domain.MoveCommandDecoder;
import java.io.IOException;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.Timer;
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
        value = "/breakoutendpoint",
        decoders = {MoveCommandDecoder.class}
)
public class BreakoutGameServlet{

    private GameSimulator simulator ;
    public static Set<Session> peers = Collections.synchronizedSet(new HashSet<Session>());
    private Timer timer;
    
    public BreakoutGameServlet() {
        simulator = new GameSimulator();
        System.out.println("BreakoutGameServlet created");
        timer = new Timer();
        timer.schedule(simulator, 0, 1000/60);
    }
    
    @OnOpen
    public void onOpen(Session peer) {
        System.out.println("Opening");
        peers.add(peer);
    }

    @OnClose
    public void onClose(Session peer) {
        System.out.println("Closing");
        timer.cancel();
        peers.remove(peer);
    }
    
    @OnMessage
    public void broadcastFigure(JsonMoveCommand moveCommand, Session session) throws IOException, EncodeException{
        int x = moveCommand.getJson().getInt("x");
        int y = moveCommand.getJson().getInt("y");
        
        simulator.movePaddle(x,y);
    }
    
}
