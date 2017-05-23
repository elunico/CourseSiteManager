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


function buildImgs() {
    var dataFile = "./js/CourseInfo.json";
    loadDataI(dataFile, loadImages);
}

function loadDataI(jsonFile) {
    $.getJSON(jsonFile, function (json) {
        loadImages(json);
    });
}

function loadImages(json) {
    var l = json.left_image;
    var r = json.right_image;
    var b = json.banner_image;

    $("#lfimg").append("<img class=\"sunysb\" style=\"float:left\" src=\"./images/left_footer.jpg\" alt=\"SBU\" />");
    $("#rfimg").append("<img style=\"float:right\" src=\"./images/right_footer.png\" alt=\"CS\" />");
    $("#bimg").append("<img alt=\"Stony Brook University\" class=\"sbu_navbar\" src=\"./images/banner_image.png\">")

}





