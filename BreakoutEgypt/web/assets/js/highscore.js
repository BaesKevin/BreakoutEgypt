let redirectToMainMenu = function (e) {
    e.preventDefault();
    location.replace('index');
};

function doSubmit(e) {
    this.form.submit();
}

function makeArrayFromTable(table) {
    return highscoreObject = table.reduce(function (acc, element) {
        let rowData = element.children;
        acc.push(
                {name: rowData[0].innerHTML,
                time: rowData[1].innerHTML,
                brick: rowData[2].innerHTML}
        );
        return acc;
    }, []);
}

function sortBy(objToSort, sortOn) {
    
    switch (sortOn.trim()) {
        case "Timescore":
            return objToSort.sort(function (a, b) {
                return a.time - b.time;
            });
            break;
        case "Brickscore":
            return objToSort.sort(function (a, b) {
                return b.brick - a.brick;
            });
            break;
        default:
            return objToSort;
    }
}

function fillTable(objToPutInTable, sortOn) {
    let table = $("#highscoreTablePlaceholder table");
    table.empty();
    
    let Html = "";
    switch (sortOn.trim()) {
        case "Timescore":
            Html += "<tbody><tr><th>Username</th><th>Timescore <i class=\"fa fa-sort-asc\" aria-hidden=\"true\"></i></th><th>Brickscore <i class=\"fa fa-sort\" aria-hidden=\"true\"></i></th></tr>";
            break;
        case "Brickscore":
            Html += "<tbody><tr><th>Username</th><th>Timescore <i class=\"fa fa-sort\" aria-hidden=\"true\"></i></th><th>Brickscore <i class=\"fa fa-sort-desc\" aria-hidden=\"true\"></i></th></tr>";
            break;
        default:
            Html += "<tbody><tr><th>Username</th><th>Timescore <i class=\"fa fa-sort\" aria-hidden=\"true\"></i></th><th>Brickscore <i class=\"fa fa-sort\" aria-hidden=\"true\"></i></th></tr>";
            break;
    }
    
    objToPutInTable.forEach(function (row) {
        Html += `<tr><td>${row.name}</td><td>${row.time}</td><td>${row.brick}</td></tr>`;
    });
    
    table.append(Html);
}

function doSort(e) {
    let thClicked = $(this);
    console.log(thClicked.text())
    let HtmlTable = $("#highscoreTablePlaceholder table").find("tr");
    let table = [];
    HtmlTable.each(function () {
        table.push($(this)[0]);
    });
    table.splice(0, 1);

    let highscoreObject = makeArrayFromTable(table);

    highscoreObject = sortBy(highscoreObject, thClicked.text());
    
    fillTable(highscoreObject, thClicked.text());
}

$(document).ready(function () {
    $("#returnToMain").on("click", redirectToMainMenu);
    $("form input").on("change", doSubmit);
    $("table").on("click", "th", doSort);
});