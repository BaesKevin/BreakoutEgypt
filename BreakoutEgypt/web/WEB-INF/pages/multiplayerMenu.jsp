<%-- 
    Document   : multiplayerMenu
    Created on : 23-nov-2017, 15:49:05
    Author     : Bjarne Deketelaere
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta charset="UTF-8">
    <title>Multiplayer Menu</title>
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
            <div id="menu">
                <ul class="list-group">
                    <li class="list-group-item">
                        <button type="button" class="btn">Versus</button>
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
