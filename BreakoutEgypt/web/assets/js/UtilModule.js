const UtilModule = (function(){
    function getParameterByName(name, url) {
        if (!url) url = window.location.href;
        name = name.replace(/[\[\]]/g, "\\$&");
        var regex = new RegExp("[?&]" + name + "(=([^&#]*)|&|#|$)"),
            results = regex.exec(url);
        if (!results) return null;
        if (!results[2]) return '';
        return decodeURIComponent(results[2].replace(/\+/g, " "));
    }

    function scoreTimerFormatter(millisecs) {

        var secs = Math.round(millisecs / 1000);

        var mins = parseInt(secs / 60);
        secs = secs % 60;

        return prenull(mins) + ":" + prenull(secs);
    }

    function prenull(number) {

        return number < 10 ? "0" + number : "" + number;

    }
    ;

    return {
        getParameterByName,
        scoreTimerFormatter
    }
})();

