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
                    <p class="back"><a href="index.jsp">Back</a></p>
                    <c:if test="${error != null}" >
                        <p class="error" ><c:out value="${error}"></c:out></p>
                    </c:if>
                    <div id="menu">
                        <ul class="list-group">
                            <li class="list-group-item">
                                <button type="button" class="btn" id="versus">Versus</button>
                            </li>
                            <li class="list-group-item">
                                <button type="button" class="btn">Cooperative</button>
                            </li>
                            <li class="list-group-item">
                                <button type="button" id="returnToMain" class="btn">Main menu</button>
                            </li>
                        </ul>
                    </div>
                </div>
            </main>
        </div>
    <%@include file="../jspf/_footer.jspf" %>
</div>
<script type="text/javascript" src="assets/js/jquery-3.2.1.min.js"></script>
<script type="text/javascript" src="assets/js/popper.min.js"></script>
<script type="text/javascript" src="assets/js/bootstrap.min.js"></script>
<script type="text/javascript" src="assets/js/ModalModule.js"></script>
<script type="text/javascript" src="assets/js/multiplayer.js"></script>
</body>
</html>
