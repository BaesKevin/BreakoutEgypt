/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.breakoutws.servlet;

import com.breakoutws.domain.Game;
import com.breakoutws.domain.GameManager;
import com.breakoutws.domain.Level;
import com.breakoutws.domain.Player;
import com.breakoutws.domain.User;
import com.breakoutws.domain.shapes.Brick;
import com.breakoutws.domain.shapes.Paddle;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;
import javax.json.Json;
import javax.json.JsonArrayBuilder;
import javax.json.JsonObjectBuilder;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author kevin
 */
@WebServlet(name = "LevelServlet", urlPatterns = {"/level"})
public class LevelServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        int gameId = Integer.parseInt(request.getParameter("gameId"));
        GameManager manager = new GameManager();
        Game game = manager.getGame(gameId);
        
        JsonObjectBuilder job;
        boolean hasNextLevel = manager.hasNextLevel(gameId);
        //System.out.println("hasnextlevel: " + hasNextLevel);
        if (hasNextLevel) {
            Level level = game.getLevel();
            
            // already initialize player and give him a paddle
            String name = "player";
            Player player = new Player(new User(name));
            
            if(game.isPlayerInSessionManager(player)){
                player = game.getPlayer(player);
            }
            else
            {
                manager.addConnectingPlayer(gameId, player);
            }
            manager.assignPaddleToPlayer(gameId, player);
            
            job = Json.createObjectBuilder();
            if (level != null) {
                JsonArrayBuilder jab = Json.createArrayBuilder();
                levelToJson(level, jab, job, player);

                manager.startGame(gameId);
            } else {
                job.add("error", "Tried to get level for game that doesn't exist");
            }
        } else {
            job = Json.createObjectBuilder();
            job.add("allLevelsComplete", true);
        }

        response.setContentType("application/json");

        try (PrintWriter out = response.getWriter()) {
            out.print(job.build().toString());
        }
    }
    
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
       
        int gameId = Integer.parseInt(request.getParameter("gameId"));
        
        GameManager manager = new GameManager();

        JsonObjectBuilder job;       
        
        Level level = manager.getGame(gameId).getLevel();
        level.startBall();
        response.setContentType("application/json");

        try (PrintWriter out = response.getWriter()) {
            out.print("level STARTED");
        }
        
    }

    private void levelToJson(Level level, JsonArrayBuilder jab, JsonObjectBuilder job, Player player) {
        for (Brick brick : level.getBricks()) {
            jab.add(brick.toJson().build());
        }
        job.add("bricks", jab);
        job.add("ball", level.getBall().getShape().toJson());
        
        JsonArrayBuilder paddleBuilder = Json.createArrayBuilder();
        List<Paddle> paddles = level.getPaddles();
        for(int i = 0; i < paddles.size(); i++){
            paddleBuilder.add( paddles.get(i).getShape().toJson().build());
        }
        job.add("paddles", paddleBuilder.build());
        job.add("mypaddle", player.getPaddle().getName());
        job.add("level", level.getId());
        job.add("lives", level.getLives());
    }
}
