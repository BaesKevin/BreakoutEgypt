// TODO webpack bundle
$(document).ready(function(){
    $("#arcade").on("click", () => window.location = "showLevels?gameType=arcade");
    ModalModule.doDocumentLoaded();
});