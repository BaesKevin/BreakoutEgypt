/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.breakoutegypt.servlet;

import com.breakoutegypt.data.Repositories;
import com.breakoutegypt.domain.GameType;
import com.breakoutegypt.domain.Player;
import com.breakoutegypt.domain.User;
import com.breakoutegypt.domain.levelprogression.GameDifficulty;
import com.breakoutegypt.exceptions.BreakoutException;
import com.breakoutegypt.levelfactories.ArcadeLevelFactory;
import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author snc
 */
@WebServlet(name = "showLevels", urlPatterns = {"/showLevels"})
public class ShowLevelsServlet extends HttpServlet {

    public static final String TOTAL_LEVELS = "totalLevels";
    public static final String LEVEL_REACHED = "levelReached";
    public static final String GAMETYPE = "gameType";
    public static final String DIFFICULTY = "difficulty";

    public static final String EASY = "easy";
    public static final String MEDIUM = "medium";
    public static final String HARD = "hard";
    public static final String BRUTAL = "brutal";
    
    public static String DIFFICULTIES = "difficulties";
    
    public static final GameDifficulty DEFAULT_DIFFICULTY = GameDifficulty.MEDIUM;
    
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        // TODO remove the if when sessions are fully integrated
        Player player = (Player) request.getSession().getAttribute("player");

        try {
            GameType gameType = getGameTypeFromRequestOrThrow(request);
            GameDifficulty difficulty = getDifficultyFromRequest(request);
            
            int levelReached = player.getProgressions().getHighestLevelReached(gameType, difficulty);
            int totalLevels = new ArcadeLevelFactory(null, null).getTotalLevels();
            int defaultLevels = new ArcadeLevelFactory(null, null).getDefaultOpenLevels();

            if (levelReached < defaultLevels) {
                levelReached = defaultLevels;
            }

            request.setAttribute(TOTAL_LEVELS, totalLevels);
            request.setAttribute(LEVEL_REACHED, levelReached);
            request.setAttribute(DIFFICULTIES, Repositories.getDifficultyRepository().findAll());
            
            request.getRequestDispatcher("WEB-INF/arcade_levels.jsp").forward(request, response);
        } catch (BreakoutException boe) {
            request.setAttribute("error", boe.getMessage());
        }

    }

    private GameType getGameTypeFromRequestOrThrow(HttpServletRequest request) {
        String gameType = request.getParameter(GAMETYPE);

        if (gameType == null) {
            throw new BreakoutException("Unknown gametype");
        }

        switch (gameType) {
            case "arcade":
                return GameType.ARCADE;
            default:
                throw new BreakoutException("Unknown gametype");
        }
    }

    private GameDifficulty getDifficultyFromRequest(HttpServletRequest request) {
        String difficulty = request.getParameter(DIFFICULTY);

        if (difficulty == null) {
            return DEFAULT_DIFFICULTY;
        }

        switch (difficulty) {
            case EASY:
                return GameDifficulty.EASY;
            case MEDIUM:
                return GameDifficulty.MEDIUM;
            case HARD:
                return GameDifficulty.HARD;
            case BRUTAL:
                return GameDifficulty.BRUTAL;
            default:
                return DEFAULT_DIFFICULTY;
        }
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        processRequest(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
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
