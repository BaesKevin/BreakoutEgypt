<%-- 
    Document   : arcade_levels
    Created on : Nov 23, 2017, 2:45:42 PM
    Author     : snc
--%>

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
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
        <h1>Levels <c:out value="${difficulty}"/> </h1>
        <form action="showLevels" method="POST" id="difficulty">            
            <select name="difficulty">
                <option value="easy"   <c:out value="${difficulty.equals('easy')   ? 'selected=selected':''}"/>>easy</option>
                <option value="medium" <c:out value="${difficulty.equals('medium') ? 'selected=selected':''}"/>>medium</option>
                <option value="hard"   <c:out value="${difficulty.equals('hard')   ? 'selected=selected':''}"/>>hard</option>
            </select>
        </form>
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
            
           

        </ul>
    <script type="text/javascript" src="${pageContext.request.contextPath}/assets/js/jquery-3.2.1.min.js"></script>        
    <script type="text/javascript" src="${pageContext.request.contextPath}/assets/js/arcade_levels.js"></script>
    </body>
</html>
