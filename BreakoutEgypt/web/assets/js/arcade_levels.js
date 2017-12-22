
// inserts a hidden field with the level before submitting
function requestLevelWithDifficulty(e){
    e.preventDefault();
    
    let form = $(this).closest("form");
    let level = $(this).data("level");
    
    form.append("<input type='hidden' name='startLevel' value='" + level + "' />");
    form.submit();
}

function changeLevelDifficulty(){
    let difficulty = $("select").val();

    document.location = `showLevels?gameType=arcade&difficulty=${difficulty}`;
}

function setDifficultySelect(){
    let diffFromQueryString = UtilModule.getParameterByName("difficulty");
    let select = $("#difficulty");
    
    if(diffFromQueryString){
        select.val(diffFromQueryString);
    }
}
$(document).ready(function () {
    setDifficultySelect();
    $("#levels li a").on("click", requestLevelWithDifficulty);
    $("#difficulty").on("change", changeLevelDifficulty); //TODO ajax call
});


