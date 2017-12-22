<%@include file="jspf/_head.jspf" %>
        <link rel="stylesheet" href="assets/css/arcade_levels.css"/>
    </head>
    <body>

    <body>
        <div class="container-fluid">
            <header class="row">
                <div class="col-lg-12 text-center text-capitalize">
                    <h1>Breakout Egypt</h1>
                </div>
            </header>
            <p class="back"><a href="index.jsp">Back</a></p>
            <form action="arcade" method="GET" class="arcadelevels-container">
                <div class="arcadelevels-formheader">
                    <h2>Arcade levels <c:out value="${difficulty}"/></h2>
                    <select name="difficulty" id="difficulty">
                        <c:forEach items="${difficulties}" var="difficulty">
                            <option value="${difficulty.name}">${difficulty.name}</option>
                        </c:forEach>
                    </select>
                </div>

                <ul id="levels">

                    <c:forEach begin="1" end="${totalLevels}" varStatus="loop">
                        <c:choose>
                            <c:when test="${loop.index <= levelReached}">
                                <li><a href="arcade?startLevel=${loop.index}&difficulty=${difficulty}" data-level="${loop.index}">Level ${loop.index}</a></li>
                                </c:when>
                                <c:otherwise>
                                <li class="locked"><a>Level ${loop.index}</a></li>
                                </c:otherwise>
                            </c:choose>
                    </c:forEach>
                </ul>
                <c:if test="${errors != null}" >
                    <c:forEach var="error" items="${errors}" >
                        <p class="error"><c:out value="${error}"></c:out></p>
                    </c:forEach>
                </c:if>
            </form>
            <%@include file="jspf/_footer.jspf" %>
        </div>
        <script type="text/javascript" src="${pageContext.request.contextPath}/assets/js/jquery-3.2.1.min.js"></script>   
        <script type="text/javascript" src="${pageContext.request.contextPath}/assets/js/UtilModule.js"></script>
        <script type="text/javascript" src="${pageContext.request.contextPath}/assets/js/arcade_levels.js"></script>
    </body>
</html>
