/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.breakoutegypt.servlet;

import com.breakoutegypt.domain.GameType;
import com.breakoutegypt.domain.Player;
import com.breakoutegypt.domain.User;
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

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        // TODO remove the if when sessions are fully integrated
        Player player = (Player) request.getSession().getAttribute("player");
        if (player == null) {
            player = new Player(new User("player"));
            request.getSession().setAttribute("player", player);
        }

        GameType gameType;
        try {
            gameType = getGameTypeFromRequestOrThrow(request);
        } catch (BreakoutException boe) {
            request.setAttribute("error", boe.getMessage());
            return;
        }

        int levelReached = player.getProgressions().getHighestLevelReached(gameType);
        int totalLevels = new ArcadeLevelFactory(null).getTotalLevels();
        int defaultLevels = new ArcadeLevelFactory(null).getDefaultOpenLevels();

        if (levelReached < defaultLevels) {
            levelReached = defaultLevels;
        }

        request.setAttribute(TOTAL_LEVELS, totalLevels);
        request.setAttribute(LEVEL_REACHED, levelReached);

        request.getRequestDispatcher("WEB-INF/arcade_levels.jsp").forward(request, response);

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
