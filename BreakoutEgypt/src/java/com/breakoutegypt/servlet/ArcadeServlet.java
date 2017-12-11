
/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.breakoutegypt.servlet;

import com.breakoutegypt.domain.levelprogression.GameDifficulty;
import com.breakoutegypt.domain.GameManager;
import com.breakoutegypt.domain.GameType;
import com.breakoutegypt.domain.Player;
import com.breakoutegypt.domain.User;
import com.breakoutegypt.domain.levelprogression.LevelProgress;
import com.breakoutegypt.exceptions.BreakoutException;
import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author kevin
 */
@WebServlet(name = "ArcadeServlet", urlPatterns = {"/arcade"})
public class ArcadeServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        try {
            int startingLevel = Integer.parseInt(request.getParameter("startLevel"));
            int numberOfPlayers = 1;
            GameDifficulty gameDifficulty = getDifficultyFromRequest(request);

            GameManager gm = new GameManager();

            Player player = (Player) request.getSession().getAttribute("player");
            if (player == null) {
                player = new Player(new User("player"));
                request.getSession().setAttribute("player", player);
            }
            
            LevelProgress progress = player.getProgressions().getLevelProgressOrDefault(GameType.ARCADE, gameDifficulty);
            int gameId = gm.createGame(numberOfPlayers, startingLevel, GameType.ARCADE, gameDifficulty);
            gm.getGame(gameId).setCurrentLevel(startingLevel, progress);
            gm.addConnectingPlayer(gameId, player);

            response.sendRedirect(String.format("arcade.jsp?gameId=%d&level=%d", gameId, startingLevel));
        } catch (BreakoutException boe) {
            request.setAttribute("error", boe.getMessage());
        }
        //get the level progression from the user
        //the specific user can come out of the session
        //
        //LevelProgressionFactory lpf = new LevelProgressionFactory(gm.getGame(gameId));
        // TODO redirect to level choice page

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

}
