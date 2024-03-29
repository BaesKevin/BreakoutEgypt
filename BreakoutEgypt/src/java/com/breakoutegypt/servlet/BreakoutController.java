/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.breakoutegypt.servlet;

import com.breakoutegypt.data.Repositories;
import com.breakoutegypt.domain.GameManager;
import com.breakoutegypt.domain.GameType;
import com.breakoutegypt.domain.Player;
import com.breakoutegypt.domain.Score;
import com.breakoutegypt.domain.levelprogression.LevelProgress;
import com.breakoutegypt.exceptions.BreakoutException;
import java.io.IOException;
import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import com.breakoutegypt.data.HighscoreRepository;
import com.breakoutegypt.domain.BreakoutWorld;
import com.breakoutegypt.domain.levelprogression.Difficulty;
import java.util.ArrayList;

/**
 *
 * @author Bjarne Deketelaere
 */
@WebServlet(name = "BreakoutController", urlPatterns = {"/index.jsp",
    "/index",
    "/multiplayerMenu",
    "/multiplayer",
    "/arcade",
    "/login",
    "/register",
    "/highscores",
    "/explanation"})
public class BreakoutController extends HttpServlet {

    public static final String DIFFICULTY = "difficulty";
    public static final String DIFFICULTIES = "difficulties";
    public static final String LEVEL = "levelId";
    public static final String LEVELDIMENSION = "levelDimension";

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

        switch (path) {
            case "/index":
            case "/index.jsp":
                request.getRequestDispatcher("WEB-INF/pages/index.jsp").forward(request, response);
                break;
            case "/multiplayerMenu":
                request.getRequestDispatcher("WEB-INF/pages/multiplayerMenu.jsp").forward(request, response);
                break;
            case "/login":
                request.getRequestDispatcher("login.jsp").forward(request, response);
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
            case "/explanation":
                request.getRequestDispatcher("WEB-INF/pages/explanation.jsp").forward(request, response);
                break;
            default:
                break;
        }
    }

    private void handleArcade(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        List<String> errors = new ArrayList();
        try {
            int startingLevel = Integer.parseInt(request.getParameter("startLevel"));

            String gameDifficulty = request.getParameter("difficulty");

            GameManager gm = new GameManager();

            Player player = (Player) request.getSession().getAttribute("player");

            LevelProgress progress = player.getProgressions().getLevelProgressOrDefault(GameType.ARCADE, gameDifficulty);
            String gameId = gm.createGame(GameType.ARCADE, gameDifficulty);

            gm.getGame(gameId).initStartingLevel(startingLevel, progress);
            gm.addConnectingPlayer(gameId, player);
            request.getSession().setAttribute("player", player);

            request.setAttribute("gameId", gameId);
            request.setAttribute("level", startingLevel);
            request.setAttribute(LEVELDIMENSION, BreakoutWorld.DIMENSION);
            request.getRequestDispatcher("WEB-INF/pages/arcade.jsp").forward(request, response);
            return;
        } catch (BreakoutException boe) {
            errors.add(boe.getMessage());
        } catch (NumberFormatException ex) {
            errors.add("Level not unlocked");
        } catch (Exception ex) {
            errors.add("Something went wrong...");
        }
        request.setAttribute("errors", errors);
        request.getRequestDispatcher("showLevels?gameType=arcade").forward(request, response);
    }

    private void handleHighscores(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String levelId = request.getParameter("gameId");
        String difficulty = request.getParameter(DIFFICULTY);
        List<Difficulty> difficulties = Repositories.getDifficultyRepository().findAll();

        if (difficulty == null) {
            difficulty = "medium";
        }

        request.setAttribute(DIFFICULTY, difficulty);
        request.setAttribute(DIFFICULTIES, difficulties);

        if (levelId == null) {
            levelId = "1";
        }

        HighscoreRepository hr = Repositories.getHighscoreRepository();
        List<Score> scores = hr.getScoresByLevel(Integer.parseInt(levelId), difficulty); // TODO from querystring
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
