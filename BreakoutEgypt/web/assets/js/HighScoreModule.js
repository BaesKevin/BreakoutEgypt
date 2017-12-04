const HighScoresModule = (function(){
    function getHighscoresByLevel(levelId) {
        fetch("highscore?gameId=" + levelId).then(function (response) {
            let json = response.json();
            return json;
        }).then(function (response) {
            let scoreTable = generateHighscoreTable(response.scores);

            $("#highscoreTablePlaceholder").empty().append(scoreTable);

        });
    }

    function generateHighscoreTable (scoresAsJson) {

        var HTMLTable = "<table><tr><th>Username</th><th>Score</th></tr>";

        scoresAsJson.forEach(function (score) {
            HTMLTable += "<tr><td>";
            HTMLTable += score.username;
            HTMLTable += "</td><td>";
            HTMLTable += score.score;
            HTMLTable += "</td></tr>";
        })

        HTMLTable += "</table>"

        return HTMLTable;
    }

    function getHighscores(e) {
        e.preventDefault();
        let levelId = $("#levelId").val();
        getHighscoresByLevel(levelId);
    }

    return {getHighscoresByLevel, getHighscores}
})();


$(document).ready(function () {
    HighScoresModule.getHighscoresByLevel(1);
    $("#returnToMain").on("click", function () {
        UtilModule.redirect("index.jsp");
    });
    $("form").on("submit", HighScoresModule.getHighscores);
})