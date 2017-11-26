/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.breakoutegypt.servlet;

import com.breakoutegypt.domain.GameManager;
import com.breakoutegypt.domain.GameType;
import com.breakoutegypt.domain.Player;
import com.breakoutegypt.domain.User;
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
public class ArcadeServlet extends HttpServlet{

    
    
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        GameManager gm = new GameManager();
        
        // TODO get from querystring
        int numberOfPlayers = 1;
        int startingLevel = 4;
        int gameId = gm.createGame(numberOfPlayers, startingLevel, GameType.ARCADE);
        
        // TODO redirect to level choice page
        response.sendRedirect(String.format("arcade.html?gameId=%d&level=%d",gameId, 1));
    }

}
