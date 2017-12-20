<%@include file="jspf/_head.jspf" %>
<%@include file="jspf/_header.jspf" %>
            <main class="row">
                <div class="col-lg-12 text-center">
                    <p class="back"><a href="index.jsp">Back</a>
                    <div id="menu">
                        <h2>Versus</h2>
                        <c:if test="${error != null}" >
                            <p class="error" ><c:out value="${error}"></c:out></p>
                        </c:if>
                        <form action="versus" id="joinVersusForm">
                            <input type="text" name="gameId" id="gameId" placeholder="Game id"/>
F                            <input type="submit" value="join" />
                        </form>
                        <p> Or </p>
                        <form action="versusLobby">
                            <input type="submit" value="Create game" class="btn"/>
                        </form>
                    </div>
                </div>
            </main>
        </div>
        <%@include file="jspf/_footer.jspf" %>
    </div>
    <%@include file="jspf/_defaultJS.jspf" %>
    <script type="text/javascript" src="assets/js/ModalModule.js"></script>
    <script type="text/javascript" src="assets/js/multiplayer.js"></script>
</body>
</html>
