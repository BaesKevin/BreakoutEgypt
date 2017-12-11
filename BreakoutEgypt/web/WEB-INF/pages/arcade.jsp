<%-- 
    Document   : arcade
    Created on : 23-nov-2017, 15:53:56
    Author     : Bjarne Deketelaere
--%>

<%@page import="com.breakoutegypt.domain.User"%>
<%@page import="com.breakoutegypt.domain.User"%>
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
        <link rel="stylesheet" href="assets/css/arcade.css"/>
    </head>
    <body>
        <div class="container-fluid">
            <main class="row">
                <div id="gameMain" class="clearfix">
                    <canvas id="stationaryParts" height="300" width="300"></canvas>
                    <canvas id="effectCanvas" height="300" width="300"></canvas>
                    <canvas id="movingParts" height="300" width="300"></canvas>
                    <p id="level"></p>
                    <p id="pause"><a>Pause (P)</a></p>
                    <p class="quit"><a>Quit (Q)</a></p>
                </div>
        </main>
    </div>
        <%
            int id=Integer.parseInt(request.getAttribute("gameId").toString());
            int levelId=Integer.parseInt(request.getAttribute("level").toString());
            out.println("<input type='hidden' value='"+id+"' id='gameId'/>");
            out.println("<input type='hidden' value='"+levelId+"' id='level'/>");
        %>
        
        <div id="modalPlaceholder"></div>
        <script type="text/javascript" src="assets/js/jquery-3.2.1.min.js"></script>
        <script type="text/javascript" src="assets/js/popper.min.js"></script>
        <script type="text/javascript" src="assets/js/bootstrap.min.js"></script>
        <script type="text/javascript" src="assets/js/effects/vector.js"></script>
        <script type="text/javascript" src="assets/js/effects/Particle.js"></script>
        <script type="text/javascript" src="assets/js/effects/Explosion.js"></script>
        <script type="text/javascript" src="assets/js/ScalingModule.js"></script>
        <script type="text/javascript" src="assets/js/Level.js"></script>
        <script type="text/javascript" src="assets/js/Brick.js"></script>
        <script type="text/javascript" src="assets/js/UtilModule.js"></script>
        <script type="text/javascript" src="assets/js/ModalModule.js"></script>
        <script type="text/javascript" src="assets/js/ArcadeWebSocket.js"></script>
        <script type="text/javascript" src="assets/js/ImageLoader.js"></script>
        <script type="text/javascript" src="assets/js/ArcadeIndex.js"></script>
        <script type="text/javascript" src="assets/js/DrawingModule.js"></script>
        <script type="text/javascript" src="assets/js/arcade.js"></script>
        <script type="text/javascript" src="assets/js/PowerUpModule.js"></script>
    </body>
</html>
