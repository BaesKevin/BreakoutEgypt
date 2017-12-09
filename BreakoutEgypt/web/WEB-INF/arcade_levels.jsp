<%-- 
    Document   : arcade_levels
    Created on : Nov 23, 2017, 2:45:42 PM
    Author     : snc
--%>

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@page import="java.util.List"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Levels</title>
        <link rel="stylesheet" type="text/css" href="assets/css/bootstrap.min.css"/>
        <link rel="stylesheet" type="text/css" href="assets/css/font-awesome.min.css"/>
        <link rel="stylesheet" href="assets/css/screen.css"/>
        <link rel="stylesheet" href="assets/css/arcade_levels.css"/>
    </head>
    <body>
        <h1>Levels <c:out value="${difficulty}"/> </h1>
        <form action="arcade" method="GET" >            
            <select name="difficulty" id="difficulty">
                <option value="easy"   <c:out value="${difficulty.equals('easy')   ? 'selected=selected':''}"/>>easy</option>
                <option value="medium" <c:out value="${difficulty.equals('medium') ? 'selected=selected':''}"/>>medium</option>
                <option value="hard"   <c:out value="${difficulty.equals('hard')   ? 'selected=selected':''}"/>>hard</option>
            </select>

            <ul id="levels">

                <c:forEach begin="1" end="${totalLevels}" varStatus="loop">

                    <c:choose>
                        <c:when test="${loop.index <= levelReached}">
                                <li><a href=\"arcade?startLevel=${loop.index}&difficulty=${difficulty}\" data-level="${loop.index}">Level ${loop.index} of ${totalLevels} - unlocked</a></li>
                            </c:when>
                            <c:otherwise>
                                <li><a href=\"arcade?startLevel=${loop.index}&difficulty=${difficulty}\" data-level="${loop.index}">Level ${loop.index} of ${totalLevels} - locked</a></li>
                            </c:otherwise>
                        </c:choose>


                </c:forEach>

            </ul>
        </form>
        <script type="text/javascript" src="${pageContext.request.contextPath}/assets/js/jquery-3.2.1.min.js"></script>        
        <script type="text/javascript" src="${pageContext.request.contextPath}/assets/js/arcade_levels.js"></script>
    </body>
</html>
