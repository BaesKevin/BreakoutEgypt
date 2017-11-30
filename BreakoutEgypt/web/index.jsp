<%-- 
    Document   : index
    Created on : 23-nov-2017, 14:51:32
    Author     : Bjarne Deketelaere
--%>

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
                    <%
                    User user=(User)request.getSession().getAttribute("user");
                    //Development purpose
                    if(user==null){
                        user=new User("test","test@hotmail.com","test");
                        request.getSession().setAttribute("user",user);
                    }
                    out.println("Username"+user.getEmail());
                    %>
                    <div id="menu">
                        <ul class="list-group">
                            <li class="list-group-item">
                                <button type="button" class="btn" id="arcade" data-toggle="modal" data-target="#modal">Arcade</button>
                            </li>
                            <li class="list-group-item">
                                <button type="button" id="toMultiplayer" class="btn">Multiplayer</button>
                            </li>
                            <li class="list-group-item">
                                <button type="button" id="toHighscores" class="btn">Highscores</button>
                            </li>
                            <li class="list-group-item">
                                <button type="button" class="btn" id="logout" data-toggle="modal" data-target="#modal">Logout</button>
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
        <div id="modalPlaceholder"></div>
        <script type="text/javascript" src="${pageContext.request.contextPath}/assets/js/jquery-3.2.1.min.js"></script>
        <script type="text/javascript" src="${pageContext.request.contextPath}/assets/js/popper.min.js"></script>
        <script type="text/javascript" src="${pageContext.request.contextPath}/assets/js/bootstrap.min.js"></script>
        <script type="text/javascript" src="${pageContext.request.contextPath}/assets/js/ModalModule.js"></script>
        <script type="text/javascript" src="${pageContext.request.contextPath}/assets/js/index.js"></script>
    </body>
</html>
