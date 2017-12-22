/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.breakoutegypt.servlet;

import com.breakoutegypt.domain.Game;
import com.breakoutegypt.domain.GameManager;
import com.breakoutegypt.domain.GameType;
import com.breakoutegypt.domain.Player;
import com.breakoutegypt.domain.levelprogression.Difficulty;
import com.breakoutegypt.domain.levelprogression.LevelProgress;
import com.breakoutegypt.exceptions.BreakoutException;
import com.breakoutegypt.servlet.util.Validator;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author kevin
 */
@WebServlet(name = "VersusServlet", urlPatterns = {"/versus", "/versusLobby"})
public class VersusServlet extends HttpServlet {

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
            case "/versusLobby":
                createVersusLobby(request, response);
                break;
            case "/versus":
                joinVersusGame(request, response);
                break;
        }
    }

    private void joinVersusGame(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        List<String> errors = new ArrayList();
        try {
            String gameId = request.getParameter("gameId");
            Validator validator = new Validator();
            if(! validator.isInteger(gameId)){
                throw new BreakoutException("Game does not exist.");
            }
            int id = Integer.parseInt(gameId);
            
            GameManager gm = new GameManager();
            Game game = gm.getGame(id);
            
            if(game == null){
                throw new BreakoutException("Game does not exist.");
            }
            
            Player player = (Player) request.getSession().getAttribute("player");
            gm.addConnectingPlayer(id, player);

            request.setAttribute("gameId", id);
            request.setAttribute("level", gm.getGame(id).getLevel().getId());
            request.getRequestDispatcher("WEB-INF/pages/arcade.jsp").forward(request, response);
            return;
        } catch (BreakoutException boe) {
            System.out.println(boe.getMessage());
            errors.add(boe.getMessage());
        } catch (Exception ex) {
            errors.add("Something went wrong...");
        }
        request.setAttribute("errors", errors);
        request.getRequestDispatcher("versus.jsp").forward(request, response);

    }

    private void createVersusLobby(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        GameManager gm = new GameManager();

        Player player = (Player) request.getSession().getAttribute("player");

        LevelProgress progress = player.getProgressions().getLevelProgressOrDefault(GameType.MULTIPLAYER, Difficulty.MEDIUM);

        int gameId = gm.createGame(GameType.MULTIPLAYER, Difficulty.MEDIUM, 2);
        Game game = gm.getGame(gameId);
        game.initStartingLevel(1, progress);
        
        request.setAttribute("gameId", gameId);
        request.getRequestDispatcher("WEB-INF/pages/versusLobby.jsp").forward(request, response);
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
