function buildCN() {
    var dataFile = "./js/CourseInfo.json";
    loadData(dataFile, loadCN);
}

function loadData(jsonFile) {
    $.getJSON(jsonFile, function (json) {
        loadCN(json);
    });
}

function loadCN(json) {
    var s = json.subject;
    var n = json.number;
    var sm = json.semester;
    var year = json.year;
    var title = json.title;

    $("#banner").append(s + " " + n + " - " + sm + " " + year + " <br />" + title);
    $("#tabtitle").append(s + " " + n);
}



