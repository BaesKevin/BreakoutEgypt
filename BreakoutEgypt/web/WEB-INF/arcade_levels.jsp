<%-- 
    Document   : arcade_levels
    Created on : Nov 23, 2017, 2:45:42 PM
    Author     : snc
--%>

<%@page import="com.breakoutegypt.domain.levelprogression.UserLevel"%>
<%@page import="java.util.List"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Levels</title>
    </head>
    <body>
        <h1>Levels</h1>
        
        <ul>
            <%
                List<UserLevel> userLevels = (List<UserLevel>) request.getAttribute("userlevels");
                String difficulty = (String) request.getAttribute("difficulty");
//Integer gameId = (Integer) request.getAttribute("gameid");
                
                for (UserLevel ul: userLevels) {
                    String item = String.format(
                        "<li><a href=\"arcade?startLevel=%d&difficulty=%s\">Level %d - %s</a></li>",
                        ul.getSeqNumber(),
                        difficulty,
                        ul.getSeqNumber(),
                        ul.isIsLocked() ? "LOCKED" : "OPEN"
                    );
                    
                    out.println(item);
                }
            %>
            
            <li><a href="arcade?startLevel=2">arcade</a></li>

        </ul>
    </body>
</html>
