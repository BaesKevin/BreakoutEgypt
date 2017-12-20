<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@page import="java.util.List"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <title>Login</title>
        <meta charset="UTF-8">
        <meta name="viewport" content="width=device-width, initial-scale=1.0">
        <link rel="stylesheet" type="text/css" href="assets/css/bootstrap.min.css"/>
        <link rel="stylesheet" type="text/css" href="assets/css/font-awesome.min.css"/>
        <link rel="stylesheet" type="text/css" href="assets/css/loginsystem.css"/>
    </head>
    <body class="background">
        <div class="container-fluid">
            <header class="row">
                <div class="col-lg-12 text-center text-capitalize">
                    <h1>Breakout Egypt</h1>
                </div>
            </header>
            <div class="row">
                <div class="card mx-auto mt-5">
                    <div class="card-header">Login</div>
                    <div class="card-body">
                        <form action="LoginSystem" method="POST">
                            <c:if test="${error != null}"><p class="error"><c:out value="${error}" ></c:out></p></c:if>
                            <div class="form-group">
                                <label for="email">Email:</label>
                                <input type="email" id="email" class="form-control" name="email" placeholder="example@domain.com"/>
                            </div>
                            <div class="form-group">
                                <label for="password">Passphrase:</label>
                                <input type="password" id="password" class="form-control" name="password" placeholder="******"/>
                            </div>
                            <input type="submit" class="btn" name="login" value="Login"/>
                            <p>
                                <a class="d-block small mt-3" href="register">No account? Register here!</a>
                            </p>
                        </form>
                    </div>
                </div>
            </div>
        </div>
    </body>
</html>
