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
        <link rel="stylesheet" type="text/css" href="assets/css/screen.css"/>
        <link rel="stylesheet" type="text/css" href="assets/css/tutorial.css"/>
    </head>
    <body>

    <body>
        <div class="container-fluid">
            <header class="row">
                <div class="col-lg-12 text-center text-capitalize">
                    <h1>Breakout Egypt</h1>
                </div>
            </header>
            <p class="back"><a href="index.jsp">Back</a></p>
            <div class="container">
                <h2>Here you can find all the info you need!</h2>
                <form action="#">
                    <legend>Brickinfo:</legend>
                    <label for="bricks">Select a brick type to get more info.</label>
                    <select name="bricks" id="bricks">
                        <option value="regular">Regular</option>
                        <option value="switch">Switch</option>
                        <option value="target">Target</option>
                        <option value="explosive">Explosive</option>
                        <option value="unbreakable">Unbreakable</option>
                    </select>
                </form>
                <div class="info">
                    <canvas id="brickExample" width="100" height="100"></canvas>
                    <p id="brickDescription" class="description"></p>
                </div>
            </div>
            <div class="container">
                <form action="#">
                    <legend>Powerupinfo:</legend>
                    <label for="powerups">Select a powerup to get more info.</label>
                    <select name="powerups" id="powerups">
                        <option value="questionmark">How to use them</option>
                        <option value="brokenpaddle">Brokenpaddle</option>
                        <option value="floor">Floor</option>
                        <option value="acidball">Acidball</option>
                    </select>
                </form>
                <div class="info">
                    <canvas id="powerupExample" width="100" height="100"></canvas>
                    <p id="powerupDescription" class="description"></p>
                </div>
            </div>
            <div class="container">
                <h3>Powerdowns <small>Yes you will encounter them to...</small></h3>
                <p>You won't be notified if a powerdown is triggered. This is a choice to make the game more challenging.</p>
            </div>
        </div>
        <script type="text/javascript" src="${pageContext.request.contextPath}/assets/js/jquery-3.2.1.min.js"></script>   
        <script type="text/javascript" src="${pageContext.request.contextPath}/assets/js/DrawingModule.js"></script>
        <script type="text/javascript" src="${pageContext.request.contextPath}/assets/js/ImageLoader.js"></script>
        <script type="text/javascript" src="${pageContext.request.contextPath}/assets/js/Brick.js"></script>
        <script type="text/javascript" src="${pageContext.request.contextPath}/assets/js/jsondata/DescriptionData.js"></script>
    </body>
</html>
