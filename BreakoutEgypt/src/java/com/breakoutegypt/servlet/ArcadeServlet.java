
/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.breakoutegypt.servlet;

import com.breakoutegypt.domain.GameDifficulty;
import com.breakoutegypt.domain.GameManager;
import com.breakoutegypt.domain.GameType;
import com.breakoutegypt.domain.Player;
import com.breakoutegypt.domain.User;
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

}
