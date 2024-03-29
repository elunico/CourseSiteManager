// DATA TO LOAD
var startHour;
var endHour;
var daysOfWeek;
var officeHours;
var undergradTAs;
var gradTAs;

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

function buildOfficeHoursGrid() {
    var dataFile = "./js/OfficeHoursGridData.json";
    loadData(dataFile, loadOfficeHours);
    var dataFile = "./js/CourseInfoData.json";
    loadDataI(dataFile, loadImages);
}

function loadData(jsonFile, callback) {
    $.getJSON(jsonFile, function (json) {
        callback(json);
    });
}

function loadOfficeHours(json) {
    initDays(json);
    addUndergradTAs(json);
    addGradTAs(json);
    addOfficeHours(json);
    loadCN(json);
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


function initDays(data) {
    // GET THE START AND END HOURS
    startHour = parseInt(data.startHour);
    endHour = parseInt(data.endHour);

    // THEN MAKE THE TIMES
    daysOfWeek = new Array();
    daysOfWeek[0] = "MONDAY";
    daysOfWeek[1] = "TUESDAY";
    daysOfWeek[2] = "WEDNESDAY";
    daysOfWeek[3] = "THURSDAY";
    daysOfWeek[4] = "FRIDAY";
}

function addUndergradTAs(data) {
    var tas = $("#undergrad_tas");
    var tasPerRow = 4;
    var numTAs = data.undergrad_tas.length;
    for (var i = 0; i < data.undergrad_tas.length;) {
        var text = "";
        text = "<tr>";
        for (var j = 0; j < tasPerRow; j++) {
            text += buildTACell(i, numTAs, data.undergrad_tas[i]);
            i++;
        }
        text += "</tr>";
        tas.append(text);
    }
}

function addGradTAs(data) {
    var tas = $("#grad_tas");
    var tasPerRow = 4;
    var numTAs = data.grad_tas.length;
    for (var i = 0; i < data.grad_tas.length;) {
        var text = "";
        text = "<tr>";
        for (var j = 0; j < tasPerRow; j++) {
            text += buildTACell(i, numTAs, data.grad_tas[i]);
            i++;
        }
        text += "</tr>";
        tas.append(text);
    }
}
function buildTACell(counter, numTAs, ta) {
    if (counter >= numTAs)
        return "<td></td>";

    var name = ta.name;
    var abbrName = name.replace(/\s/g, '');
    var email = ta.email;
    var text = "<td class='tas'><img width='100' height='100'"
        + " src='./images/tas/" + abbrName + ".JPG' "
        + " alt='" + name + "' /><br />"
        + "<strong>" + name + "</strong><br />"
        + "<span class='email'>" + email + "</span><br />"
        + "<br /><br /></td>";
    return text;
}
function addOfficeHours(data) {
    for (var i = startHour; i < endHour; i++) {
        // ON THE HOUR
        var textToAppend = "<tr>";
        var amPm = getAMorPM(i);
        var displayNum = i;
        if (i > 12)
            displayNum = displayNum - 12;
        textToAppend += "<td>" + displayNum + ":00" + amPm + "</td>"
            + "<td>" + displayNum + ":30" + amPm + "</td>";
        for (var j = 0; j < 5; j++) {
            textToAppend += "<td id=\"" + daysOfWeek[j]
                + "_" + displayNum
                + "_00" + amPm
                + "\" class=\"open\"></td>";
        }
        textToAppend += "</tr>";

        // ON THE HALF HOUR
        var altAmPm = amPm;
        if (displayNum === 11)
            altAmPm = "pm";
        var altDisplayNum = displayNum + 1;
        if (altDisplayNum > 12)
            altDisplayNum = 1;

        textToAppend += "<tr>";
        textToAppend += "<td>" + displayNum + ":30" + amPm + "</td>"
            + "<td>" + altDisplayNum + ":00" + altAmPm + "</td>";

        for (var j = 0; j < 5; j++) {
            textToAppend += "<td id=\"" + daysOfWeek[j]
                + "_" + displayNum
                + "_30" + amPm
                + "\" class=\"open\"></td>";
        }

        textToAppend += "</tr>";
        var cell = $("#office_hours_table");
        cell.append(textToAppend);
    }

    // NOW SET THE OFFICE HOURS
    for (var i = 0; i < data.officeHours.length; i++) {
        var id = data.officeHours[i].day + "_" + data.officeHours[i].time;
        var name = data.officeHours[i].name;
        var cell = $("#" + id);
        if (name === "Lecture") {
            cell.removeClass("open");
            cell.addClass("lecture");
            cell.html("Lecture");
        }
        else {
            cell.removeClass("open");
            cell.addClass("time");
            cell.append("<br />" + name);
        }
    }
}
function getAMorPM(testTime) {
    if (testTime >= 12)
        return "pm";
    else
        return "am";
}
