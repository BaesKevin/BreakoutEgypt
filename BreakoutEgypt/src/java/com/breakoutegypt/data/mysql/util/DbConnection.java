/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.breakoutegypt.data.mysql.util;

import com.breakoutegypt.exceptions.BreakoutException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 *
 * @author Bjarne Deketelaere
 */
public class DbConnection {
    private static final String URL = "jdbc:mysql://localhost/dbbreakout?useSSL=false";
    private static final String UID = "root";
    private static final String PWD = "";
    
    static
    {
        try
        {
            Class.forName("com.mysql.jdbc.Driver");
        }
        catch (ClassNotFoundException ex)
        {
            throw new BreakoutException("Could not load the drivers", ex);
        }
    }
    
    public static Connection getConnection() throws SQLException
    {
        return DriverManager.getConnection(URL, UID, PWD);
    }
}
