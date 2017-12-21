<%@include file="../jspf/_head.jspf" %>
        <link rel="stylesheet" href="assets/css/arcade.css"/>
    </head>
    <body>
        <div class="container-fluid">
            <main class="row">
                <div id="gameMain" class="clearfix">
                    <aside>
                        <p id="level"></p>
                        <p id="pause"><a>Pause (P)</a></p>
                        <p class="quit"><a>Quit (Q)</a></p>
                    </aside>
                    <canvas id="stationaryParts" height="300" width="300"></canvas>
                    <canvas id="effectCanvas" height="300" width="300"></canvas>
                    <canvas id="movingParts" height="300" width="300"></canvas>
                </div>
            </main>
        </div>
        <%
            String id = (String) request.getAttribute("gameId");
            int levelId = Integer.parseInt(request.getAttribute("level").toString());
            out.println("<input type='hidden' value='" + id + "' id='gameId'/>");
            out.println("<input type='hidden' value='" + levelId + "' id='levelid'/>");
        %>

        <div id="modalPlaceholder"></div>
        <%@include file="../jspf/_defaultJS.jspf" %>
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
        <script type="text/javascript" src="assets/js/PowerDownModule.js"></script>
    </body>
</html>
