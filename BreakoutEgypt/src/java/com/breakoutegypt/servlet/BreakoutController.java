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
import com.breakoutegypt.domain.levelprogression.GameDifficulty;
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
import com.breakoutegypt.domain.Game;
import com.breakoutegypt.domain.levelprogression.Difficulty;

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
    "/highscores"})
public class BreakoutController extends HttpServlet {

    public static final String DIFFICULTY = "difficulty";
    public static final String DIFFICULTIES = "difficulties";
    public static final String LEVEL = "levelId";

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
            case "/multiplayer":
                handleMultiplayer(request, response);
                break;
            default:
        }
    }

    private void handleArcade(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            int startingLevel = Integer.parseInt(request.getParameter("startLevel"));

            GameDifficulty gameDifficulty = getDifficultyFromRequest(request);

            GameManager gm = new GameManager();

            Player player = (Player) request.getSession().getAttribute("player");

            LevelProgress progress = player.getProgressions().getLevelProgressOrDefault(GameType.ARCADE, gameDifficulty);
            int gameId = gm.createGame(GameType.ARCADE, gameDifficulty);

            gm.getGame(gameId).initStartingLevel(startingLevel, progress);
            gm.addConnectingPlayer(gameId, player);
            request.getSession().setAttribute("player", player);

            request.setAttribute("gameId", gameId);
            request.setAttribute("level", startingLevel);

        } catch (BreakoutException boe) {
            request.setAttribute("error", boe.getMessage());
        }

        request.getRequestDispatcher("WEB-INF/pages/arcade.jsp").forward(request, response);
    }

    private GameDifficulty getDifficultyFromRequest(HttpServletRequest request) throws BreakoutException {
        GameDifficulty gameDifficulty;
        switch (request.getParameter("difficulty")) {
            case ShowLevelsServlet.EASY:
                gameDifficulty = GameDifficulty.EASY;
                break;
            case ShowLevelsServlet.MEDIUM:
                gameDifficulty = GameDifficulty.MEDIUM;
                break;
            case ShowLevelsServlet.HARD:
                gameDifficulty = GameDifficulty.HARD;
                break;
            case ShowLevelsServlet.BRUTAL:
                gameDifficulty = GameDifficulty.BRUTAL;
                break;
            default:
                throw new BreakoutException("unknown difficulty");
        }
        return gameDifficulty;
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

    private void handleMultiplayer(HttpServletRequest request, HttpServletResponse response) 
        throws ServletException, IOException{
        try {
//            int startingLevel = Integer.parseInt(request.getParameter("startLevel"));
            String gameId = request.getParameter("gameId");

            if (gameId == null) {
                createMultiplayerGame(request, response);
            } else {
                int id = Integer.parseInt(gameId);
                joinMultiplayer(request, response, id);
            }
            request.getRequestDispatcher("WEB-INF/pages/arcade.jsp").forward(request, response);
        } catch (BreakoutException boe) {
            request.setAttribute("error", boe.getMessage());
        }

        
    }

    private void createMultiplayerGame(HttpServletRequest request, HttpServletResponse response) 
    throws ServletException, IOException{
        System.out.println("creating multiplayer");
        GameDifficulty gameDifficulty = GameDifficulty.EASY;

        GameManager gm = new GameManager();

        Player player = (Player) request.getSession().getAttribute("player");
        if (player == null) {
            player = new Player("player");
            request.getSession().setAttribute("player", player);
        }

        LevelProgress progress = player.getProgressions().getLevelProgressOrDefault(GameType.MULTIPLAYER, gameDifficulty);
//            int gameId = gm.createGame(GameType.ARCADE, gameDifficulty);

        int gameId = gm.createGame(GameType.MULTIPLAYER, gameDifficulty, 2);

        gm.getGame(gameId).initStartingLevel(1, progress);
        gm.addConnectingPlayer(gameId, player);

        request.setAttribute("gameId", gameId);
        request.setAttribute("level", 1);
    }

    private void joinMultiplayer(HttpServletRequest request, HttpServletResponse response, int id) 
    throws ServletException, IOException{
        System.out.println("joining multiplayer");
        GameManager gm = new GameManager();
        
        Player player = (Player) request.getSession().getAttribute("player");
        gm.addConnectingPlayer(id, player);
        
        request.setAttribute("gameId", id);
        request.setAttribute("level", 1);
    }

}
