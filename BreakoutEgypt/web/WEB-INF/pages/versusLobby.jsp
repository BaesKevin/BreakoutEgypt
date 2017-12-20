<%@include file="../jspf/_head.jspf" %>
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
                    <p class="back"><a href="index.jsp">Back</a>

                    <div id="menu">
                        <h2>Versus lobby</h2>

                        <p>Send the number below to a friend so he can join your game by entering it on the previous page.</p>
                        <p><input type="text" value="${gameId}" onClick="this.select();"/></p>

                        <input type="hidden" id="gameId" value="${gameId}" />
                        <button  class="btn btn-primary" id="startVersus">Start game</button>
                        <!--<a href="versus?gameId=${gameId}" class="btn">Go to game</a>-->
                    </div>
                </div>

            </main>
        </div>
    </main>
    <%@include file="../jspf/_footer.jspf" %>
</div>
<script type="text/javascript" src="assets/js/jquery-3.2.1.min.js"></script>
<script type="text/javascript" src="assets/js/popper.min.js"></script>
<script type="text/javascript" src="assets/js/bootstrap.min.js"></script>
<script type="text/javascript" src="assets/js/ModalModule.js"></script>
<script type="text/javascript" src="assets/js/multiplayer.js"></script>
</body>
</html>
