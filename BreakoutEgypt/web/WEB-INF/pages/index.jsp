<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@page import="com.breakoutegypt.domain.User"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <title>Breakout</title>
        <meta charset="UTF-8">
        <meta name="viewport" content="width=device-width, initial-scale=1.0">
        <link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/assets/css/bootstrap.min.css"/>
        <link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/assets/css/font-awesome.min.css"/>
        <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/screen.css"/>
    </head>
    <body>
        <div class="container-fluid">
            <header class="row">
                <div class="col-lg-12 text-center text-capitalize">
                    <h1>Breakout Egypt</h1>
                </div>
            </header>
            <main class="row">
                <div class="col-lg-12 text-center">
                    <noscript class="error">JavaScript is disabled or your browser does not support JavaScript! <br/>
                        You won't be able to do anything while it's disabled.
                    </noscript>
                    <h2 class="user">Welcome <c:out value="${player.username}"></c:out></h2>
                    <div id="menu">
                        <ul class="list-group">
                            <li class="list-group-item">
                                <button  type="button" class="btn" id="arcade" data-toggle="modal" data-target="#modal">Arcade</button>
                            </li>
                            <li class="list-group-item">
                                <button type="button" id="toMultiplayer" class="btn">Multiplayer</button>
                            </li>
                            <li class="list-group-item">
                                <button type="button" id="toHighscores" class="btn">Highscores</button>
                            </li>
                            <li class="list-group-item">
                                <button type="button" class="btn" id="toExplanation">Explanation</button>
                            </li>
                            <li class="list-group-item">
                                <button type="button" class="btn" id="logout" data-toggle="modal" data-target="#modal">Logout</button>
                            </li>
                        </ul>
                    </div>
                </div>
                <div id="modalPlaceholder"></div>
                <script type="text/javascript" src="${pageContext.request.contextPath}/assets/js/jquery-3.2.1.min.js"></script>
                <script type="text/javascript" src="${pageContext.request.contextPath}/assets/js/popper.min.js"></script>
                <script type="text/javascript" src="${pageContext.request.contextPath}/assets/js/bootstrap.min.js"></script>
                <script type="text/javascript" src="${pageContext.request.contextPath}/assets/js/ModalModule.js"></script>
                <script type="text/javascript" src="${pageContext.request.contextPath}/assets/js/index.js"></script>
                </body>
                </html>
