/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.breakoutegypt.servlet;

import com.breakoutegypt.data.StaticUserRepository;
import com.breakoutegypt.data.UserRepository;
import com.breakoutegypt.domain.User;
import java.io.IOException;
import java.io.PrintWriter;
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
            HttpSession session=request.getSession();
            UserRepository userRepo=StaticUserRepository.getInstance();
            userRepo.addUser(new User("Bjarne","bjarne.deketelaere@student.howest.be","Bjarne"));
            

            
            String register=request.getParameter("register");
            String login=request.getParameter("login");
            
            String email=request.getParameter("email");
            
            String password=request.getParameter("password");
            if(login!=null){
                User loginUser=new User(email,password);
                
                if(userRepo.inList(loginUser)){
                    session.setAttribute("user",loginUser);
                    response.sendRedirect("index.jsp?message=you are logged in");
                } else {
                    response.sendRedirect("login.jsp?error=could not find user");
                }
            } else if(register!=null){
                String username=request.getParameter("username");
                User registerUser=new User(username,email,password);
                if(!userRepo.alreadyExists(registerUser)){
                    userRepo.addUser(registerUser);
                    response.sendRedirect("login.jsp?message=now you can login");
                } else {
                    response.sendRedirect("registration.jsp?error=user already exists");
                }
            }
            
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
