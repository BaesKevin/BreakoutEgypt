<%-- 
    Document   : highscore
    Created on : 23-nov-2017, 15:52:56
    Author     : Bjarne Deketelaere
--%>

<%@page import="com.breakoutegypt.domain.Score"%>
<%@page import="java.util.List"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
    <head>
        <meta charset="UTF-8">
        <meta name="viewport" content="width=device-width, initial-scale=1.0">
        <link rel="stylesheet" type="text/css" href="assets/css/bootstrap.min.css"/>
        <link rel="stylesheet" type="text/css" href="assets/css/font-awesome.min.css"/>
        <link rel="stylesheet" href="assets/css/screen.css"/>
        <link href="assets/css/highscore.css" rel="stylesheet" type="text/css"/>
    </head>
    <body>
        <header class="row">
            <div class="col-lg-12 text-center text-capitalize">
                <h1>Breakout Egypt</h1>
            </div>
        </header>
        <main>
            <form action="highscores" method="POST">

                <label>Difficulty</label>
                <select name="difficulty">
                    <c:forEach items="${difficulties}" var="difficultyFromRepo">
                        <c:choose>
                            <c:when test="${difficultyFromRepo.getName().equals(difficulty)}">
                                <option selected="true" value="${difficultyFromRepo.name}">${difficultyFromRepo.name}</option>
                            </c:when>
                            <c:otherwise>
                                <option value="${difficultyFromRepo.name}">${difficultyFromRepo.name}</option>
                            </c:otherwise>
                        </c:choose>

                    </c:forEach>
                </select>

                <label for="gameId">level:</label> <input type="number" value="<c:out value="${gameIdentification}"/>" min="1" max="100" name="gameId" id="gameId"/>
                <input type="submit" value="show">
            </form>
            <div id="highscoreTablePlaceholder">
                <table>
                    <tr>
                        <th>Username</th>
                        <th>Timescore</th>
                        <th>Brickscore</th>
                    </tr>
                    <c:forEach var="score" items="${scores}">
                        <tr>
                            <td><c:out value="${score.getUser()}"/></td>
                            <td><c:out value="${score.getScore()}"/></td>
                            <td><c:out value="${score.brickScore}"/></td>
                        </tr>
                    </c:forEach>
                </table>
                <button type="button" id="returnToMain" class="btn">Main Menu</button>
            </div>
        </main>
        <footer class="row">
            <div class="col-lg-8 col-md-10 mx-auto text-center">
                <p>Find us on:</p>
                <ul class="list-inline text-center">
                    <li class="list-inline-item">
                        <a href="#">
                            <span class="fa-stack fa-lg">
                                <i class="fa fa-circle fa-stack-2x"></i>
                                <i class="fa fa-facebook fa-stack-1x fa-inverse"></i>
                            </span>
                        </a>
                    </li>
                    <li class="list-inline-item">
                        <a href="https://github.com/HowestBachelorTI/2017_S3_Groep_25">
                            <span class="fa-stack fa-lg">
                                <i class="fa fa-circle fa-stack-2x"></i>
                                <i class="fa fa-github fa-stack-1x fa-inverse"></i>
                            </span>
                        </a>
                    </li>
                </ul>
                <p class="copyright text-muted text-center">Copyright &copy; Breakout Egypt 2017</p>
            </div>
        </footer>
        <script type="text/javascript" src="${pageContext.request.contextPath}/assets/js/jquery-3.2.1.min.js"></script>
        <script type="text/javascript" src="${pageContext.request.contextPath}/assets/js/popper.min.js"></script>
        <script type="text/javascript" src="${pageContext.request.contextPath}/assets/js/bootstrap.min.js"></script>
        <script type="text/javascript" src="${pageContext.request.contextPath}/assets/js/ModalModule.js"></script>
        <script type="text/javascript" src="${pageContext.request.contextPath}/assets/js/highscore.js"></script>
    </body>
</html>
