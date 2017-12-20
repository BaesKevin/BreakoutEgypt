<%@include file="../jspf/_head.jspf" %>
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
                <div class="card-header">Registration</div>
                <div class="card-body">
                    <c:forEach var="error" items="${errors}" >
                        <p class="error"><c:out value="${error}"></c:out></p>
                    </c:forEach>
                    <form action="LoginSystem" method="POST">
                        <div class="form-group">
                            <label for="username">Username:</label>
                            <input type="text" value="<c:out value="${username}"></c:out>" id="username" class="form-control" name="username" placeholder="Username"/>
                            </div>
                            <div class="form-group">
                                <label for="email">Email:</label>
                                <input type="email" value="<c:out value="${email}"></c:out>" id="email" class="form-control" name="email" placeholder="example@domain.com"/>
                            </div>
                            <div class="form-group">
                                <label for="password">Passphrase</label>
                                <input type="password" id="password" class="form-control" name="password" placeholder="******"/>
                            </div>
                            <input type="submit" class="btn" name="register" value="Register"/>
                            <p class="back btn"><a href="login">Back</a></p>
                        </form>
                    </div>
                </div>
            </div>
        <%@include file="../jspf/_footer.jspf" %>
    </div>
</body>
</html>
