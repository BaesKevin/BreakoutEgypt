
// inserts a hidden field with the level before submitting
function requestLevelWithDifficulty(e){
    e.preventDefault();
    
    let form = $(this).closest("form");
    let level = $(this).data("level");
    
    form.append("<input type='hidden' name='startLevel' value='" + level + "' />");
    form.submit();
}

$(document).ready(function () {
    $("#levels li a").on("click", requestLevelWithDifficulty);
});


