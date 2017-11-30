var redirectToMainMenu = function (e) {
    e.preventDefault();
    console.log("test");
    location.replace('index');
};

$(document).ready(function () {
    $("#returnToMain").on("click", redirectToMainMenu);
});

