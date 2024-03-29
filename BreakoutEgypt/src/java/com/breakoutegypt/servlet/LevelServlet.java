
/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.breakoutegypt.servlet;

import com.breakoutegypt.domain.BreakoutWorld;
import com.breakoutegypt.domain.Game;
import com.breakoutegypt.domain.GameManager;
import com.breakoutegypt.domain.GameType;
import com.breakoutegypt.domain.Level;
import com.breakoutegypt.domain.LevelState;
import com.breakoutegypt.domain.Player;
import com.breakoutegypt.domain.User;
import com.breakoutegypt.domain.levelprogression.LevelProgress;
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
import javax.servlet.http.HttpSession;

/**
 *
 * @author kevin
 */
@WebServlet(name = "LevelServlet", urlPatterns = {"/level"})
public class LevelServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession session = request.getSession();
//        User user = (User)session.getAttribute("user");
        Player playerFromSession = (Player) session.getAttribute("player");
        String gameId = request.getParameter("gameId");

        GameManager manager = new GameManager();
        Game game = manager.getGame(gameId);

        JsonObjectBuilder job;

        boolean hasNextLevel = manager.hasNextLevel(gameId);

        if (hasNextLevel) {
            if (!game.getCurrentLevel().isComplete()) {
                Level level = game.getLevel();

                // already initialize player and give him a paddle
                if (playerFromSession != null) {
                    String name = playerFromSession.getUsername();
                    Player connectingPlayer = game.getPlayer(name);
                    System.out.printf("Game: %s Level: %d Player: %s", game.getId(), level.getId(), name);

                    job = Json.createObjectBuilder();
                    if (level != null) {
                        JsonArrayBuilder jab = Json.createArrayBuilder();
                        levelToJson(level, jab, job, connectingPlayer);

                        manager.startGame(gameId);
                    } else {
                        job.add("error", "Tried to get level for game that doesn't exist");
                    }
                } else {
                    job = Json.createObjectBuilder();
                    job.add("error", "No user in the session");
                }
            } else {
                job = Json.createObjectBuilder();
                job.add("levelLoading", true);
            }

        } else if (game == null) {
            job = Json.createObjectBuilder();
            job.add("error", "Connection lost...");
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

        String gameId = request.getParameter("gameId");

        GameManager manager = new GameManager();

        JsonObjectBuilder job;

        Game game = manager.getGame(gameId);

        Level level = manager.getGame(gameId).getLevel();

        Player player = (Player) request.getSession().getAttribute("player");
        player = manager.getGame(gameId).getPlayer(player.getUsername());

        game.startBall(player.getIndex());
        response.setContentType("application/json");

    }

    private void levelToJson(Level level, JsonArrayBuilder jab, JsonObjectBuilder job, Player player) {
        LevelState state = level.getLevelState();
        for (Brick brick : state.getBricks()) {
            jab.add(brick.toJson().build());
        }
        job.add("bricks", jab);
        List<Ball> balls = state.getBalls();
        JsonArrayBuilder ballBuilder = Json.createArrayBuilder();
        for (int i = 0; i < balls.size(); i++) {
            ballBuilder.add(balls.get(i).toJson().build());
        }
        job.add("balls", ballBuilder.build());

        JsonArrayBuilder paddleBuilder = Json.createArrayBuilder();
        List<Paddle> paddles = state.getPaddles();
        for (int i = 0; i < paddles.size(); i++) {
            paddleBuilder.add(paddles.get(i).toJson().build());
        }

        Paddle myPaddle = null;
        for (Paddle p : paddles) {
            if (p.getPlayerIndex() == player.getIndex()) {
                myPaddle = p;
            }
        }

        job.add("paddles", paddleBuilder.build());
        job.add("mypaddle", myPaddle.getName()); //TODO when one player leaves and reconnects he doesn't get a paddle
        job.add("playerIndex", player.getIndex());
        job.add("level", level.getLevelNumber());
        job.add("lives", player.getLives());

        job.add("levelDimension", BreakoutWorld.DIMENSION);
    }
}
