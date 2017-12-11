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
        <title>Breakout</title>
        <meta charset="UTF-8">
        <meta name="viewport" content="width=device-width, initial-scale=1.0">
        <link rel="stylesheet" type="text/css" href="assets/css/bootstrap.min.css"/>
        <link rel="stylesheet" type="text/css" href="assets/css/font-awesome.min.css"/>
        <link rel="stylesheet" href="assets/css/screen.css"/>
        <link rel="stylesheet" href="assets/css/arcade_levels.css"/>
    </head>
    <body>

    <body>
        <div class="container-fluid">
            <header class="row">
                <div class="col-lg-12 text-center text-capitalize">
                    <h1>Breakout Egypt</h1>
                </div>

            </header>

            <form action="arcade" method="GET" class="arcadelevels-container">
                <div class="arcadelevels-formheader">
                    <h2>Arcade levels <c:out value="${difficulty}"/></h2>
                        <select name="difficulty" id="difficulty">
                            <c:forEach items="${difficulties}" var="difficulty">
                                <option value="${difficulty.name}">${difficulty.name}</option>
                            </c:forEach>
                        </select>
                </div>

                <ul id="levels">

                    <c:forEach begin="1" end="${totalLevels}" varStatus="loop">

                        <c:choose>
                            <c:when test="${loop.index <= levelReached}">
                                    <li><a href=\"arcade?startLevel=${loop.index}&difficulty=${difficulty}\" data-level="${loop.index}">Level ${loop.index}</a></li>
                                </c:when>
                                <c:otherwise>
                                    <li class="locked">Level ${loop.index}</li>
                                </c:otherwise>
                            </c:choose>


                    </c:forEach>

                </ul>
            </form>
        </div>
        <script type="text/javascript" src="${pageContext.request.contextPath}/assets/js/jquery-3.2.1.min.js"></script>   
        <script type="text/javascript" src="${pageContext.request.contextPath}/assets/js/UtilModule.js"></script>
        <script type="text/javascript" src="${pageContext.request.contextPath}/assets/js/arcade_levels.js"></script>
    </body>
</html>
