<%--
    Document   : highscore
    Created on : 23-nov-2017, 15:52:56
    Author     : Bjarne Deketelaere
--%>

<%@page import="com.breakoutegypt.domain.Score" %>
<%@page import="java.util.List" %>
<%@page contentType="text/html" pageEncoding="UTF-8" %>
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
        <title>Highscores</title>
    </head>
    <body>
        <header class="row">
            <div class="col-lg-12 text-center text-capitalize">
                <h1>Breakout Egypt</h1>
            </div>
        </header>
        <main>
            <p class="back"><a href="index.jsp">Back</a></p>
            <div class='text-center'>
                <noscript class="error">JavaScript is disabled or your browser does not support JavaScript! <br/>
                        You won't be able to do anything while it's disabled.
                    </noscript>
            </div>
            <form action="highscores" method="POST">
                <label>Difficulty</label>
                <div id='difficulties'>
                    <c:forEach items="${difficulties}" var="difficultyFromRepo">
                        <c:choose>
                            <c:when test="${difficultyFromRepo.getName().equals(difficulty)}">
                                <label class="radioButtonContainer" for="radio${difficultyFromRepo.name}">${difficultyFromRepo.name}
                                    <input checked="checked" type="radio" name="difficulty" value="${difficultyFromRepo.name}"
                                           id="radio${difficultyFromRepo.name}">
                                    <span class="checkmark"></span>
                                </label>
                            </c:when>
                            <c:otherwise>
                                <label class="radioButtonContainer" for="radio${difficultyFromRepo.name}">${difficultyFromRepo.name}
                                    <input type="radio" name="difficulty" value="${difficultyFromRepo.name}"
                                           id="radio${difficultyFromRepo.name}">
                                    <span class="checkmark"></span>
                                </label>
                            </c:otherwise>
                        </c:choose>
                    </c:forEach>
                </div>

                <label for="gameId">Level:</label><input type="number" value="<c:out value="${gameIdentification}"/>" min="1"
                                                         max="50" name="gameId" id="gameId"/>
            </form>
            <div id="highscoreTablePlaceholder">
                <table>
                    <tr>
                        <th>Username</th>
                        <th>Timescore <i class="fa fa-sort-asc" aria-hidden="true"></i></th>
                        <th>Brickscore <i class="fa fa-sort" aria-hidden="true"></i></th>
                    </tr>
                    <c:if test="${empty scores}">
                        <tr>
                            <td>No scores for this combination yet!</td>
                            <td>You can be the first!</td>
                            <td>Go do your best!</td>
                        </tr>
                    </c:if>
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
