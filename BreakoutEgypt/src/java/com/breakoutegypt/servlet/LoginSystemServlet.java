/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.breakoutegypt.servlet;

import com.breakoutegypt.data.Repositories;
import com.breakoutegypt.data.StaticUserRepository;
import com.breakoutegypt.data.UserRepository;
import com.breakoutegypt.domain.Player;
import com.breakoutegypt.domain.User;
import com.breakoutegypt.domain.levelprogression.LevelPackProgress;
import com.breakoutegypt.domain.levelprogression.LevelProgressManager;
import com.breakoutegypt.servlet.util.Validator;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Arrays;
import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 *
 * @author Bjarne Deketelaere
 */
@WebServlet(name = "LoginSystemServlet", urlPatterns = {"/LoginSystem"})
public class LoginSystemServlet extends HttpServlet {

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
        response.setContentType("text/html;charset=UTF-8");
        try (PrintWriter out = response.getWriter()) {
            HttpSession session = request.getSession();
            UserRepository userRepo = Repositories.getUserRepository();

            String register = request.getParameter("register");
            String login = request.getParameter("login");

            String email = request.getParameter("email");

            String passphrase = request.getParameter("password");
            if (login != null) {
                doLogin(userRepo, email, passphrase, session, request, response);
            } else if (register != null) {
                doRegister(request, passphrase, email, userRepo, response);
            }

        }
    }

    private void doRegister(HttpServletRequest request, String passphrase, String email, UserRepository userRepo, HttpServletResponse response) throws IOException, ServletException {
        Validator v = new Validator();
        String username = request.getParameter("username");
        List<String> errors = v.isValidForm(username, passphrase, email);
        if (errors.size() <= 0) {
            
            User registerUser = new User(username, email, passphrase);
            if (userRepo.alreadyExists(registerUser)) {
                request.setAttribute("email", email);
                request.setAttribute("username", username);
                request.setAttribute("errors", Arrays.asList(new String[]{"User already exists"}));
                request.getRequestDispatcher("WEB-INF/pages/registration.jsp").forward(request, response);
            } else {
                userRepo.addUser(registerUser);
                Repositories.getLevelProgressionRepository().initDefaults(registerUser.getUserId());
                request.getRequestDispatcher("login.jsp").forward(request, response);
            
            }
        } else {
            
            request.setAttribute("errors", errors);
            request.setAttribute("email", email);
            request.setAttribute("username", username);
            request.getRequestDispatcher("WEB-INF/pages/registration.jsp").forward(request, response);
        
        }
    }

    private void doLogin(UserRepository userRepo, String email, String passphrase, HttpSession session, HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        User user = userRepo.getUser(email, passphrase);
        if (user != null) {
            
            Player player = new Player(user.getUserId(), user.getUsername(), user.getEmail(), user.getHash(), new LevelProgressManager());            
            
            List<LevelPackProgress> allForPlayer = Repositories.getLevelProgressionRepository().getAllForPlayer(player.getUserId());
            
            player.getProgressions().setProgressions(allForPlayer);
            
            session.setAttribute("player", player);
            
            request.getRequestDispatcher("WEB-INF/pages/index.jsp").forward(request, response);
        } else {
            request.setAttribute("error", "This combination does not exist!");
            request.getRequestDispatcher("login.jsp").forward(request, response);
        }
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
