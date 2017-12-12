var redirectToMainMenu = function (e) {
    e.preventDefault();
    console.log("test");
    location.replace('index');
};
$(document).ready(function(){
    console.log("test");
    $("#returnToMain").on("click", redirectToMainMenu);
});