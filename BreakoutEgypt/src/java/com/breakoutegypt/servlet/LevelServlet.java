/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.breakoutegypt.servlet;

import com.breakoutegypt.domain.Game;
import com.breakoutegypt.domain.GameManager;
import com.breakoutegypt.domain.Level;
import com.breakoutegypt.domain.LevelState;
import com.breakoutegypt.domain.Player;
import com.breakoutegypt.domain.User;
import com.breakoutegypt.domain.shapes.Ball;
import com.breakoutegypt.domain.shapes.bricks.Brick;
import com.breakoutegypt.domain.shapes.Paddle;
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

        if (hasNextLevel) {
            Level level = game.getLevel();
            
            String name = "player";
            Player player = game.getPlayer(name);
            
            if(player == null){
                player = new Player(new User(name));
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
        LevelState state = level.getLevelState();
        for (Brick brick : state.getBricks()) {
            jab.add(brick.toJson().build());
        }
        job.add("bricks", jab);
        List<Ball> balls = state.getBalls();
        JsonArrayBuilder ballBuilder = Json.createArrayBuilder();
        for(int i = 0; i < balls.size(); i++) {
            ballBuilder.add( balls.get(i).getShape().toJson().build());
        }
        job.add("balls", ballBuilder.build());
        
        JsonArrayBuilder paddleBuilder = Json.createArrayBuilder();
        List<Paddle> paddles = state.getPaddles();
        for(int i = 0; i < paddles.size(); i++){
            paddleBuilder.add( paddles.get(i).getShape().toJson().build());
        }
        job.add("paddles", paddleBuilder.build());
        job.add("mypaddle", player.getPaddle().getName());
        job.add("level", level.getId());
        job.add("lives", level.getLives());
    }
}
