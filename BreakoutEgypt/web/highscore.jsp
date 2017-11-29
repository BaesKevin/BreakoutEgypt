<%-- 
    Document   : highscore
    Created on : 23-nov-2017, 15:52:56
    Author     : Bjarne Deketelaere
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta charset="UTF-8">
        <meta name="viewport" content="width=device-width, initial-scale=1.0">
        <link rel="stylesheet" type="text/css" href="assets/css/bootstrap.min.css"/>
        <link rel="stylesheet" type="text/css" href="assets/css/font-awesome.min.css"/>
        <link rel="stylesheet" href="assets/css/screen.css"/>
        <link href="assets/css/highscore.css" rel="stylesheet" type="text/css"/>
    </head>
    <body>
        <header class="row">
            <div class="col-lg-12 text-center text-capitalize">
                <h1>Breakout Egypt</h1>
            </div>
        </header>
        <main>
            <form>
                level: <input type="number" value="1" min="1" max="100" id="levelId"/>
                <input type="submit" value="show">
            </form>
            <div id="highscoreTablePlaceholder">

            </div>
            <button type="button" id="returnToMain" class="btn">Main Menu</button>
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
        <script type="text/javascript" src="assets/js/jquery-3.2.1.min.js"></script>
        <script type="text/javascript" src="assets/js/popper.min.js"></script>
        <script type="text/javascript" src="assets/js/bootstrap.min.js"></script>
        <script type="text/javascript" src="assets/js/HighScoreModule.js"></script>
        <script type="text/javascript" src="assets/js/ModalModule.js"></script>
    </body>
</html>
