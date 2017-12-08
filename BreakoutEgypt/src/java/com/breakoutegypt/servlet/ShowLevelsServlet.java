/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.breakoutegypt.servlet;

import com.breakoutegypt.domain.levelprogression.UserLevelsFactory;
import java.io.IOException;
import java.io.PrintWriter;
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

        
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        String diff = null;
        
        if ( request.getMethod().equals("GET")) {
            //get last difficulty from db
            diff = "medium";
        } else if (request.getMethod().equals("POST")) {
            System.out.println("got a post");
            diff = request.getParameter("difficulty");
        }
        System.out.println("diff is " + diff);
        
        if (diff == null) return;
        
        request.setAttribute("difficulty", diff);
        
        
        UserLevelsFactory ulf = new UserLevelsFactory();
        request.setAttribute("userlevels", ulf.getAllUserLevelsForEasyForSomeUser());
        request.getRequestDispatcher("WEB-INF/arcade_levels.jsp").forward(request, response);
        
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
