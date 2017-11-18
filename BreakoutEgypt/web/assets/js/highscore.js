function getHighscoresByLevel(levelId) {
    fetch("highscore?gameId=" + levelId).then(function (response) {
        var json = response.json();
        return json;
    }).then(function (response) {
        var scoreTable = generateHighscoreTable(response.scores);

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

var redirectToMainMenu = function (e) {
    e.preventDefault();
    location.replace('index.html');
};

function getHighscores(e) {
    e.preventDefault();
    var levelId = $("#levelId").val();
    getHighscoresByLevel(levelId);
}

$(document).ready(function () {
    getHighscoresByLevel(1);
    $("#returnToMain").on("click", redirectToMainMenu);
    $("form").on("submit", getHighscores);
})