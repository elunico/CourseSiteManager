// DATA TO LOAD
var startHour;
var endHour;
var daysOfWeek;
var officeHours;
var undergradTAs;



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


function buildRecitations() {
    var dataFile = "./js/RecitationsData.json";
    loadData(dataFile, addRecitations);
}

function addRecitations(data) {
    var recTable = $("#rec_table");
    var rowParity = 0;
    for (var i = 0; i < data.recitations.length; i += 2) {
        var text = "<tr>";
        var rec = data.recitations[i];
        var cellParity = rowParity;
        text += buildRecCell(cellParity, rec);
        cellParity++;
        cellParity %= 2;
        if ((i + 1) < data.recitations.length) {
            rec = data.recitations[i + 1];
            text += buildRecCell(cellParity, rec);
        }
        else
            text += "<td></td>";
        text += "</tr>";
        recTable.append(text);
        rowParity++;
        rowParity %= 2;
    }
}
function buildRecCell(recClassNum, recData) {
    var text = "<td class='rec_" + recClassNum + "'>"
        + "<table><tr><td valign='top' class='rec_cell'>"
        + recData.section + "<br />"
        + recData.day_time + "<br />"
        + recData.location + "<br /></td></tr>"
        + "<tr>";

    // RECITATION TA #1
    text += "<td class='ta_cell'><strong>Supervising TA</strong><br />";
    if (recData.ta_1 != "none")
        text += "<img src='./images/tas/"
            + recData.ta_1.replace(/\s/g, '')
            + ".JPG' width='100' height='100' />"
            + "<br clear='both' />"
            + "(" + recData.ta_1 + ")<br />";
    else
        text += "TBA";
    text += "</td>";

    // RECITATION TA #2
    text += "<td class='ta_cell'><strong>Supervising TA</strong><br />";
    if (recData.ta_2 != "none")
        text += "<img src='./images/tas/"
            + recData.ta_2.replace(/\s/g, '')
            + ".JPG' width='100' height='100' />"
            + "<br clear='both' />"
            + "(" + recData.ta_2 + ")<br />";
    else
        text += "TBA";
    text += "</table></td>";
    return text;
}

