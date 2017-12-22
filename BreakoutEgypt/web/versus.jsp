<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
    <head>
        <meta charset="UTF-8">
        <title>Versus</title>
        <meta name="viewport" content="width=device-width, initial-scale=1.0">
        <link rel="stylesheet" type="text/css" href="assets/css/bootstrap.min.css"/>
        <link rel="stylesheet" type="text/css" href="assets/css/font-awesome.min.css"/>
        <link rel="stylesheet" href="assets/css/screen.css"/>
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
                        <c:if test="${errors != null}" >
                            <c:forEach var="error" items="${errors}" >
                            <p class="error"><c:out value="${error}"></c:out></p>
                        </c:forEach>
                    </c:if>
                    <div id="menu">
                        <h2>Versus</h2>
                        <form action="versus" id="joinVersusForm">
                            <input type="text" name="gameId" id="gameId" placeholder="Game id"/>


                            <input type="submit" value="join" />
                        </form>
                        <p> Or </p>
                        <form action="versusLobby">
                            <input type="submit" value="Create game" class="btn"/>
                        </form>
                    </div>
                </div>

            </main>
        </div>
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
    </div>
    <script type="text/javascript" src="assets/js/jquery-3.2.1.min.js"></script>
    <script type="text/javascript" src="assets/js/popper.min.js"></script>
    <script type="text/javascript" src="assets/js/bootstrap.min.js"></script>
    <script type="text/javascript" src="assets/js/ModalModule.js"></script>
    <script type="text/javascript" src="assets/js/multiplayer.js"></script>
</body>
</html>
