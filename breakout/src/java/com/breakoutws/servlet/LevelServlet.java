/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.breakoutws.servlet;

import com.breakoutws.domain.GameManager;
import com.breakoutws.domain.Level;
import com.breakoutws.domain.Shape;
import java.io.IOException;
import java.io.PrintWriter;
import javax.json.Json;
import javax.json.JsonArrayBuilder;
import javax.json.JsonObjectBuilder;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.jbox2d.dynamics.Body;

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

        JsonObjectBuilder job;

        boolean hasNextLevel = manager.hasNextLevel(gameId);
        //System.out.println("hasnextlevel: " + hasNextLevel);
        if (hasNextLevel) {
            Level level = manager.getLevel(gameId);
            job = Json.createObjectBuilder();
            if (level != null) {
                JsonArrayBuilder jab = Json.createArrayBuilder();
                levelToJson(level, jab, job);

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

    private void levelToJson(Level level, JsonArrayBuilder jab, JsonObjectBuilder job) {
        for (Body body : level.getBricks()) {
            Shape s = (Shape) body.getUserData();
            jab.add(s.toJson());
        }
        job.add("bricks", jab);
        job.add("ball", ((Shape) level.getBall().getUserData()).toJson());
        job.add("paddle", ((Shape) level.getPaddle().getUserData()).toJson());
        job.add("level", level.getId());
        job.add("lives", level.getLives());
    }
}
