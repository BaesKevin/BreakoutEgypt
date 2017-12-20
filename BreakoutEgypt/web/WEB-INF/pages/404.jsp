<%@include file="../jspf/_head.jspf" %>
<style>
    .container {
        display: flex;
        flex-wrap: wrap;
        justify-content: center;
        align-items: center;
        height: 100vh;
    }
    
    small {
        align-self: center;
    }
    
    .btn {
        padding: 10px;
        border: 1px solid black;
        vertical-align: middle;
        align-self: flex-end; 
        white-space: normal; 
    }

    #menu {
        display: grid;
        text-align: center;
        justify-content: center;
        grid-template-rows: 1fr 1fr 1fr;
        padding: 5%;
    }
    
    footer {
        width: 100%;
    }
</style>
</head>
<body>
    <div class="container">
        <div id="menu">
            <h1>404</h1>
            <small>That means: page not found</small>
            <button onclick="location.replace('index.jsp')" class="btn">Take me to a page that does exist</button>
        </div>
        <%@include file="../jspf/_footer.jspf" %>
    </div>
</body>
</html>
