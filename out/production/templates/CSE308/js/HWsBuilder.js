// DATA TO LOAD
var hws;
var daysOfWeek;
var redInc;
var greenInc;
var blueInc;

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

function HW(hDate, hTime, hTitle, hTopic, hLink, hCriteria) {
    this.date = hDate;
    this.time = hTime;
    this.title = hTitle;
    this.topic = hTopic;
    this.link = hLink;
    this.criteria = hCriteria;
}
function ScheduleDate(sMonth, sDay) {
    this.month = sMonth;
    this.day = sDay;
}
function initHWs() {
    redInc = 10;
    greenInc = 10;
    blueInc = 5;

    daysOfWeek = new Array(7);
    daysOfWeek[0] = "Sunday";
    daysOfWeek[1] = "Monday";
    daysOfWeek[2] = "Tuesday";
    daysOfWeek[3] = "Wednesday";
    daysOfWeek[4] = "Thursday";
    daysOfWeek[5] = "Friday";
    daysOfWeek[6] = "Saturday";

    var dataFile = "./js/ScheduleData.json";
    loadData(dataFile);

    var dataFile = "./js/CourseInfoData.json";
    loadDataI(dataFile, loadImages);


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


function loadData(jsonFile) {
    $.getJSON(jsonFile, function (json) {
        loadJSONData(json);
        addHWs();
        loadCN(json);
    });
}
function loadJSONData(data) {
    // LOAD HWs DATA
    hws = new Array();
    for (var i = 0; i < data.hws.length; i++) {
        var hwData = data.hws[i];
        var hwDate = new ScheduleDate(hwData.month, hwData.day);
        var hw = new HW(hwDate, hwData.time, hwData.title, hwData.topic, hwData.link, hwData.criteria);
        hws[i] = hw;
    }
}

function addHWs() {
    var tBody = $("#hws");
    var red = 240;
    var green = 240;
    var blue = 255;
    for (var i = 0; i < hws.length; i++) {
        var hw = hws[i];
        var day = hw.date.day;
        var month = hw.date.month;
        var dayOfWeek = getDayOfWeek(day, month);

        // THE FIRST CELL
        var textToAppend = "<tr class=\"hw\" style=\"background-color:rgb(" + red + "," + green + "," + blue + ")\">"
            + "<td class=\"hw\" style=\"padding-right: 60px\">"
            + "<br />";
        if (hw.link.valueOf() === "none".valueOf()) {
            textToAppend += hw.title;
        }
        else {
            textToAppend += "<a href=\"" + hw.link + "\">" + hw.title + "</a>";
        }
        textToAppend += " - " + hw.topic + "<br /><br /></td>";

        // THE SECOND CELL
        textToAppend += "<td class=\"hw\" style=\"padding-right: 60px\">"
            + "<br />" + dayOfWeek + ", " + month + "/" + day
            + "<br /><br /><br />"
            + "</td>";
        if (hw.criteria.valueOf() === "none".valueOf()) {
            textToAppend += "<td class=\"hw\" style=\"padding-right: 60px\"><br />TBA<br /><br /><br /></td>";
        }
        else {
            textToAppend += "<td class=\"hw\" style=\"padding-right: 60px\">"
                + "<a href=\"" + hw.criteria + "\"><br />" + hw.title + " Grading Criteria</a><br /><br /><br /></td>";
        }

        textToAppend += "</tr>";
        tBody.append(textToAppend);
        red -= redInc;
        green -= greenInc;
        blue -= blueInc;
    }
}
function getDayOfWeek(gDay, gMonth) {
    var date = new Date();
    date.setDate(1);
    date.setMonth(gMonth - 1);
    date.setDate(gDay);
    return daysOfWeek[date.getDay()];
}
