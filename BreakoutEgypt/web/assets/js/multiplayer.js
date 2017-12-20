var redirectToMainMenu = function (e) {
    e.preventDefault();
    console.log("test");
    location.replace('index');
};

function toVersus(e){
    location.replace('versus.jsp');
}

function joinVersusGame(e){
    e.preventDefault();

    let gameId = $(this).find("#gameId").val();

    location.replace(`versus?gameId=${gameId}`);
}

function startVersus(e){
    e.preventDefault();
    
    let gameId = $("#gameId").val();
    location.replace(`versus?gameId=${gameId}`);
}

$(document).ready(function () {
    $("#returnToMain").on("click", redirectToMainMenu);
    $("#versus").on("click", toVersus);
    $("#joinVersusForm").on("submit", joinVersusGame);
    $("#startVersus").on("click", startVersus)
});

