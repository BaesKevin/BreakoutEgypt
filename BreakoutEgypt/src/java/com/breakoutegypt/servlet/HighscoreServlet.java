/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.breakoutegypt.servlet;

import com.breakoutegypt.data.HighscoreRepo;
import com.breakoutegypt.data.Repositories;
import com.breakoutegypt.domain.Score;
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
 * @author BenDB
 */
@WebServlet(name = "HighscoreServlet", urlPatterns = {"/highscore"})
public class HighscoreServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        int gameId = Integer.parseInt(request.getParameter("gameId"));

        HighscoreRepo hr = Repositories.getHighscoreRepository();

        List<Score> scores = hr.getScoresByLevel(gameId, "hard");

        JsonObjectBuilder job = Json.createObjectBuilder();
        JsonArrayBuilder jab = Json.createArrayBuilder();
        for (Score s : scores) {
            jab.add(s.toJson());
        }
        job.add("scores", jab);

        try (PrintWriter out = response.getWriter()) {
            out.print(job.build().toString());
        }

    }

}
