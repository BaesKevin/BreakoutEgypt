<%@include file="WEB-INF/jspf/_head.jspf" %>
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
            <main class="card mx-auto mt-5">
                <h2 class="card-header">Login</h2>
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
                    </main>
                </div>
        <%@include file="WEB-INF/jspf/_footer.jspf" %>
    </div>
</body>
</html>
