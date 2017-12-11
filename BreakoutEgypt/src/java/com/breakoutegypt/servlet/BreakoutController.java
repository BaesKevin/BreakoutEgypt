/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.breakoutegypt.servlet;

import com.breakoutegypt.data.HighscoreRepo;
import com.breakoutegypt.data.LevelProgressionRepository;
import com.breakoutegypt.data.Repositories;
import com.breakoutegypt.domain.GameDifficulty;
import com.breakoutegypt.domain.GameManager;
import com.breakoutegypt.domain.GameType;
import com.breakoutegypt.domain.Player;
import com.breakoutegypt.domain.Score;
import com.breakoutegypt.domain.User;
import com.breakoutegypt.exceptions.BreakoutException;
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
 * @author Bjarne Deketelaere
 */
@WebServlet(name = "BreakoutController", urlPatterns = {"/index.jsp",
    "/index",
    "/multiplayerMenu",
    "/arcade",
    "/login",
    "/register",
    "/highscores"})
public class BreakoutController extends HttpServlet {

    /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code>
     * methods.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String path = request.getServletPath();
        System.out.println("PATH: " + path);
        switch (path) {
            case "/index.jsp":
                request.getRequestDispatcher("WEB-INF/pages/index.jsp").forward(request, response);
                break;
            case "/index":
                request.getRequestDispatcher("WEB-INF/pages/index.jsp").forward(request, response);
                break;
            case "/multiplayerMenu":
                request.getRequestDispatcher("WEB-INF/pages/multiplayerMenu.jsp").forward(request, response);
                break;
            case "/login":
                request.getRequestDispatcher("WEB-INF/pages/login.jsp").forward(request, response);
                break;
            case "/register":
                request.getRequestDispatcher("WEB-INF/pages/registration.jsp").forward(request, response);
                break;
            case "/highscores":
                handleHighscores(request, response);
                break;
            case "/arcade":
                handleArcade(request, response);
                break;
            default:
        }
    }

    private void handleArcade(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        System.out.println("ArcadeServlet: Creating game");

        int startingLevel = Integer.parseInt(request.getParameter("startLevel"));
        System.out.println("startingLevel : " + startingLevel);

        GameManager gm = new GameManager();

        // TODO get from querystring
        int numberOfPlayers = 1;
        GameDifficulty gameDifficulty;
        switch (request.getParameter("difficulty")) {
            case "hard":
                gameDifficulty = GameDifficulty.HARD;
                break;
            case "medium":
                gameDifficulty = GameDifficulty.MEDIUM;
                break;
            default:
                gameDifficulty = GameDifficulty.EASY;
        }

        Player player = (Player) request.getSession().getAttribute("player");
        if (player == null) {
            player = new Player(new User("player"));
            request.getSession().setAttribute("player", player);
        }

        try {
            int gameId = gm.createGame(numberOfPlayers, startingLevel, GameType.ARCADE, gameDifficulty, player.getProgressions().getProgressionOrDefault(GameType.ARCADE));
            gm.addConnectingPlayer(gameId, player);

            
        request.setAttribute("gameId", gameId);
        request.setAttribute("level", startingLevel);
        request.getRequestDispatcher("WEB-INF/pages/arcade.jsp").forward(request, response);
//            response.sendRedirect(String.format("arcade.jsp?gameId=%d&level=%d", gameId, startingLevel));
        } catch (BreakoutException boe) {
            request.setAttribute("error", boe.getMessage());
        }
        //get the level progression from the user
        //the specific user can come out of the session
        //
        //LevelProgressionFactory lpf = new LevelProgressionFactory(gm.getGame(gameId));
        // TODO redirect to level choice page

    }

    private void handleHighscores(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String levelId = request.getParameter("gameId");

        if (levelId == null) {
            levelId = "1";
        }
        HighscoreRepo hr = Repositories.getHighscoreRepository();
        List<Score> scores = hr.getScoresByLevel(Integer.parseInt(levelId), "hard");
        request.getSession().setAttribute("gameIdentification", levelId);
        request.getSession().setAttribute("scores", scores);
        request.getRequestDispatcher("WEB-INF/pages/highscore.jsp").forward(request, response);
    }

    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    /**
     * Handles the HTTP <code>GET</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Handles the HTTP <code>POST</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Returns a short description of the servlet.
     *
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>

}
