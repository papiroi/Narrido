/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

function showMonitoring() {
    var monitoringPane = $("#daily-monitoring");
    
    var container = $("<div/>", {class: "container"}).appendTo(monitoringPane);
    var form = $("<form/>").appendTo(container);
    var row = $("<div/>", {class: "form-group row"}).appendTo(form);

    var comboDiv = $("<div/>", {class: "col-xs-4"}).appendTo(row);
    var labCombo = $("<select/>", {
        name: "labId",
        id: "lab-id",
        class: "form-control"
    }).on("change", viewMonitoring).appendTo(comboDiv);
    
    for(var i = 0; i < 9; i++) {
        $("<option/>", {value: i + 1})
                .html("Computer Laboratory " + (i + 1)).appendTo(labCombo);
    }
    
    var buttonDiv2 = $("<div/>", {class: "col-xs-3 offset-xs-2"}).appendTo(row);
    var button2 = $("<button/>", {
        type: "button",
        class: "form-control btn btn-primary"
    }).html("Generate Report")
            .on("click", function() {
                showModal($("<p/>").text("Generating Report..."), "Generate Report");
                $.ajax({
                    type: "GET",
                    url: "/Narrido-1.0-SNAPSHOT/api/it/monitoring/" + labCombo.val() + "/report",
                    success: function (response) {
                        showModal($("<p/>").text(response), "Generate Report");
                    },
                    error: function (xhr) {
                        showModal($("<p/>").text(xhr.responseText), "Generate Report");
                    }
                });
            })
            .appendTo(buttonDiv2);
    
    if(userObj.type === "it-staff") {
        var buttonDiv = $("<div/>", {class: "col-xs-3"}).appendTo(row);
        var button = $("<button/>", {
            type: "button",
            class: "form-control btn btn-primary"
        }).html("+ New Monitoring")
                .on("click", function() {addMonitoring(labCombo.val());})
                .appendTo(buttonDiv);
    }
    
    
    $("<div/>", {class: "container", id: "monitoring-content"}).appendTo(monitoringPane);
    viewMonitoring();
}

function viewMonitoring() {
    var content = $("#monitoring-content").empty();
    
    $.ajax({
       type: "GET",
       url: "/Narrido-1.0-SNAPSHOT/api/it/monitoring/" + $("#lab-id").val(),
       dataType: "json",
       success: function (data) {
           console.log("Data got!");
           content.append(tablifyMonitoring(data));
       },
       error: function(xhr) {
           showModal($("<p/>").text("Error: " + xhr.responseText), "Error");
           console.log(xhr.responseText);
       }
    });
}

function tablifyMonitoring(data) {
    var frag = document.createDocumentFragment();
    
    var laboratoryId;
    if(data[0]) {
        laboratoryId = data[0].laboratory.labId;
    }
    
    var table = $("<table/>", {class: "table"}).appendTo(frag);
    var thead = $("<thead/>").appendTo(table);
    var theadrow = $("<tr/>").appendTo(thead);
    
    $("<th/>").text("Laboratory").appendTo(theadrow);
    $("<th/>").text("Date").appendTo(theadrow);
    $("<th/>").text("Instructor").appendTo(theadrow);
    $("<th/>").text("Course/Section").appendTo(theadrow);
    $("<th/>").text("System Unit").appendTo(theadrow);
    $("<th/>").text("Monitor").appendTo(theadrow);
    $("<th/>").text("Keyboard").appendTo(theadrow);
    $("<th/>").text("Mouse").appendTo(theadrow);
    $("<th/>").text("AVR").appendTo(theadrow);
    $("<th/>").text("Remarks").appendTo(theadrow);
    $("<th/>").text("Comment").appendTo(theadrow);
    $("<th/>").text("Inspected By").appendTo(theadrow);
    
    var tbody = $("<tbody/>", {id: "table-lab-" + laboratoryId}).appendTo(table);
    data.forEach(function(data) {
        var bodyrow = $("<tr/>").appendTo(tbody);
        $("<th/>", {scope: "row"}).text(data.laboratory.labId).appendTo(bodyrow);
        var dateObj = new Date(data.date);
        var date = dateObj.toDateString() + " " + dateObj.toLocaleTimeString();
        $("<td/>").text(date).appendTo(bodyrow);
        $("<td/>").text(data.instructor).appendTo(bodyrow);
        $("<td/>").text(data.courseSection).appendTo(bodyrow);
        $("<td/>").text(data.systemUnit).appendTo(bodyrow);
        $("<td/>").text(data.monitor).appendTo(bodyrow);
        $("<td/>").text(data.keyboard).appendTo(bodyrow);
        $("<td/>").text(data.mouse).appendTo(bodyrow);
        $("<td/>").text(data.avr).appendTo(bodyrow);
        $("<td/>").text(data.remarks).appendTo(bodyrow);
        $("<td/>").text(data.comment).appendTo(bodyrow);
        $("<td/>").append(nameHeader(data.user, "sm")).appendTo(bodyrow);
    });
    
    return frag;
}

function addMonitoring(labId) {
    showModal(addMonitoringBody(labId), "Add Monitoring");
}

function addMonitoringBody(labId) {
    
    var container = $("<div/>", {class: "container"});
    var form = $("<form/>")
            .on("submit", function(event) {
                event.preventDefault();
                var monitoringDate = $("#monitoring-date").val();
                var monitoring = {
                    date: monitoringDate ? new Date(monitoringDate) : new Date(),
                    instructor: $("#monitoring-instructor").val(),
                    courseSection: $("#monitoring-course-section").val(),
                    systemUnit: $("#monitoring-system-unit").val(),
                    monitor: $("#monitoring-monitor").val(),
                    keyboard: $("#monitoring-keyboard").val(),
                    mouse: $("#monitoring-mouse").val(),
                    avr: $("#monitoring-avr").val(),
                    remarks: $("#monitoring-remarks").val(),
                    comment: $("#monitoring-comments").val()
                };
                
                $.ajax({
                    type: "POST",
                    url: "/Narrido-1.0-SNAPSHOT/api/it/monitoring/" + labId,
                    data: JSON.stringify(monitoring),
                    contentType: "application/json",
                    success: function(response) {
                        showModal($("<p/>").text(response), "Submit Monitoring");
                    },
                    error: function (xhr) {
                        showModal($("<p/>").text(xhr.responsText), "Submit Monitoring");
                    }
                });
            })
            .appendTo(container);
    
    var titleRow = $("<div/>", {class: "form-group row"})
            .append($("<h3/>").text("Computer Laboratory " + labId))
            .appendTo(form);
    
    var dateRow = $("<div/>", {class: "form-group row"})
            .appendTo(form);
    
    var dateDiv = $("<div/>", {class: "col-xs-12"})
            .appendTo(dateRow);
    
    $("<input/>", {
        type: "datetime-local",
        id: "monitoring-date",
        class: "form-control"
    }).appendTo(dateDiv);
    
    var instructorRow = $("<div/>", {class: "form-group row"})
            .appendTo(form);
    
    var instructorDiv = $("<div/>", {class: "col-xs-12"})
            .appendTo(instructorRow);
    
    $("<input/>", {
        type: "text",
        id: "monitoring-instructor",
        placeholder: "Instructor",
        required: true,
        class: "form-control"
    }).appendTo(instructorDiv);
    
    
    var courseSecRow = $("<div/>", {class: "form-group row"})
            .appendTo(form);
    
    var courseSecDiv = $("<div/>", {class: "col-xs-12"})
            .appendTo(courseSecRow);
    
    $("<input/>", {
        type: "text",
        id: "monitoring-course-section",
        placeholder: "Course/Year/Section",
        required: true,
        class: "form-control"
    }).appendTo(courseSecDiv);
    

    var systemUnitRow = $("<div/>", {class: "form-group row"})
            .appendTo(form);
    
    var systemUnitDiv = $("<div/>", {class: "col-xs-12"})
            .appendTo(systemUnitRow);
    
    $("<input/>", {
        type: "text",
        id: "monitoring-system-unit",
        placeholder: "System Unit",
        class: "form-control"
    }).appendTo(systemUnitDiv);
    

    var monitorRow = $("<div/>", {class: "form-group row"})
            .appendTo(form);
    
    var monitorDiv = $("<div/>", {class: "col-xs-12"})
            .appendTo(monitorRow);
    
    $("<input/>", {
        type: "text",
        id: "monitoring-monitor",
        placeholder: "Monitor",
        class: "form-control"
    }).appendTo(monitorDiv);
    
    
    var keyboardRow = $("<div/>", {class: "form-group row"})
            .appendTo(form);
    
    var keyboardDiv = $("<div/>", {class: "col-xs-12"})
            .appendTo(keyboardRow);
    
    $("<input/>", {
        type: "text",
        id: "monitoring-keyboard",
        placeholder: "Keyboard",
        class: "form-control"
    }).appendTo(keyboardDiv);
    
    
    var mouseRow = $("<div/>", {class: "form-group row"})
            .appendTo(form);
    
    var mouseDiv = $("<div/>", {class: "col-xs-12"})
            .appendTo(mouseRow);
    
    $("<input/>", {
        type: "text",
        id: "monitoring-mouse",
        placeholder: "Mouse",
        class: "form-control"
    }).appendTo(mouseDiv);
    
    
    var avrRow = $("<div/>", {class: "form-group row"})
            .appendTo(form);
    
    var avrDiv = $("<div/>", {class: "col-xs-12"})
            .appendTo(avrRow);
    
    $("<input/>", {
        type: "text",
        id: "monitoring-avr",
        placeholder: "AVR",
        class: "form-control"
    }).appendTo(avrDiv);
    
    
    var remarksRow = $("<div/>", {class: "form-group row"})
            .appendTo(form);
    
    var remarksDiv = $("<div/>", {class: "col-xs-12"})
            .appendTo(remarksRow);
    
    $("<textarea/>", {
        id: "monitoring-remarks",
        placeholder: "Remarks",
        class: "form-control",
        required: true
    }).appendTo(remarksDiv);
    
    
    var commentRow = $("<div/>", {class: "form-group row"})
            .appendTo(form);
    
    var commentDiv = $("<div/>", {class: "col-xs-12"})
            .appendTo(commentRow);
    
    $("<textarea/>", {
        id: "monitoring-comments",
        placeholder: "Comment",
        class: "form-control",
        required: true
    }).appendTo(commentDiv);
    
    var buttonRow = $("<div/>", {class: "form-group row"})
            .appendTo(form);
    
    var buttonDiv = $("<div/>", {class: "col-xs-12"})
            .appendTo(buttonRow);
    
    $("<button/>", {
        type: "submit",
        value: "Submit",
        class: "btn btn-primary"
    }).text("Submit").appendTo(buttonDiv);
    
    return container;
}

function showPcs() {
    var pcPane = $("#pc");
    
    var container = $("<div/>", {class: "container"}).appendTo(pcPane);
    var form = $("<form/>").appendTo(container);
    var row = $("<div/>", {class: "form-group row"}).appendTo(form);

    var comboDiv = $("<div/>", {class: "col-xs-4"}).appendTo(row);
    var labCombo = $("<select/>", {
        name: "labId",
        id: "lab-id-pc",
        class: "form-control"
    }).on("change", viewPcs).appendTo(comboDiv);
    
    for(var i = 0; i < 9; i++) {
        $("<option/>", {value: i + 1})
                .html("Computer Laboratory " + (i + 1)).appendTo(labCombo);
    }
    
    if(userObj.type === "property-supply") {
        var buttonDiv = $("<div/>", {class: "col-xs-3 offset-xs-1"}).appendTo(row);
        var button = $("<button/>", {
            type: "button",
            class: "form-control btn btn-primary"
        }).html("+ New PC")
                .on("click", function() {addPc(labCombo.val());})
                .appendTo(buttonDiv);
    }
    
    var buttonDiv2 = $("<div/>", {class: "col-xs-2"}).appendTo(row);
    var button2 = $("<button/>", {
        type: "button",
        class: "form-control btn btn-primary"
    }).html("Generate Report")
            .on("click", function() {
                $.ajax({
                    type: "GET",
                    url: "/Narrido-1.0-SNAPSHOT/api/it/pc/" + labCombo.val() + "/report",
                    success: function (response) {
                        showModal($("<p/>").text(response), "Generate Report");
                    },
                    error: function (xhr) {
                        showModal($("<p/>").text(xhr.responseText), "Generate Report");
                    }
                });
            })
            .appendTo(buttonDiv2);
    
    if(userObj.type === "property-supply") {
        var buttonDiv3 = $("<div/>", {class: "col-xs-2"}).appendTo(row);
        var button3 = $("<button/>", {
            type: "button",
            class: "form-control btn btn-primary"
        }).html("Print Re-MR")
                .on("click", function() {
                    showModal(reMrBody(), "Print Re-MR");
                })
                .appendTo(buttonDiv3);
    }
    
    $("<div/>", {class: "container", id: "pc-content"}).appendTo(pcPane);
    viewPcs();
}

function viewPcs() {
    var content = $("#pc-content").empty();
    
    $.ajax({
       type: "GET",
       url: "/Narrido-1.0-SNAPSHOT/api/it/pc/" + $("#lab-id-pc").val(),
       dataType: "json",
       success: function (data) {
           console.log("Data got!");
           content.append(tablifyPc(data));
       },
       error: function(xhr) {
           showModal($("<p/>").text("Error: " + xhr.responseText), "Error");
           console.log(xhr.responseText);
       }
    });
}

function tablifyPc(data) {
    var frag = document.createDocumentFragment();
    
    var laboratoryId;
    if(data[0]) {
        laboratoryId = data[0].laboratory.labId;
    }
    
    var table = $("<table/>", {class: "table"}).appendTo(frag);
    var thead = $("<thead/>").appendTo(table);
    var theadrow = $("<tr/>").appendTo(thead);
    
    $("<th/>").text("Laboratory").appendTo(theadrow);
    $("<th/>").text("PC Nr.").appendTo(theadrow);
    $("<th/>").text("Description").appendTo(theadrow);
    $("<th/>").text("Date Acquired").appendTo(theadrow);
    $("<th/>").text("Serial Nr.").appendTo(theadrow);
    $("<th/>").text("Property Nr.").appendTo(theadrow);
    $("<th/>").text("Unit Value").appendTo(theadrow);
    $("<th/>").text("Status").appendTo(theadrow);
    $("<th/>").text("PAR Assigned To").appendTo(theadrow);
    $("<th/>").text("Re-MR To").appendTo(theadrow);
    
    var tbody = $("<tbody/>", {id: "table-pc-lab-" + laboratoryId}).appendTo(table);
    data.forEach(function(data) {
        var bodyrow = $("<tr/>", {class: "narrido-it-row"})
                .on("click", function(event) {
                    openPcPage(data, event);
                })
                .appendTo(tbody);
        
        $("<th/>", {scope: "row"}).text(data.laboratory.labId).appendTo(bodyrow);
        $("<td/>").text(data.pcNumber).appendTo(bodyrow);
        $("<td/>").text(data.pcDescription).appendTo(bodyrow);
        $("<td/>").text(data.dateAcquired).appendTo(bodyrow);
        $("<td/>").text(data.serialNumber).appendTo(bodyrow);
        $("<td/>").text(data.propertyNumber).appendTo(bodyrow);
        $("<td/>").text(data.unitValue).appendTo(bodyrow);
        $("<td/>").text(data.status).appendTo(bodyrow);
        $("<td/>").text(data.mr).appendTo(bodyrow);
        $("<td/>").text(data.reMr).appendTo(bodyrow);
    });
    
    return frag;
}

function addPc(labId) {
    showModal(addPcBody(labId), "Add PC");
}

function addPcBody(labId) {
    var container = $("<div/>", {class: "container"});
    var form = $("<form/>")
            .on("submit", function(event) {
                event.preventDefault();
                var pc = {
                    pcNumber: $("#pc-pc-number").val(),
                    pcName: $("#pc-pc-name").val(),
                    pcDescription: $("#pc-pc-description").val(),
                    dateAcquired: new Date(),
                    serialNumber: $("#pc-serial-number").val(),
                    propertyNumber: $("#pc-property-number").val(),
                    unitValue: $("#pc-unit-value").val(),
                    status: "WORKING",
                    mr: $("#pc-mr").val(),
                    reMr: $("#pc-re-mr").val()
                };
                
                $.ajax({
                    type: "POST",
                    url: "/Narrido-1.0-SNAPSHOT/api/it/pc/" + labId,
                    data: JSON.stringify(pc),
                    contentType: "application/json",
                    success: function(response) {
                        showModal($("<p/>").text(response), "New PC");
                    },
                    error: function (xhr) {
                        showModal($("<p/>").text(xhr.responsText), "New PC");
                    }
                });
            })
            .appendTo(container);
    
    var titleRow = $("<div/>", {class: "form-group row"})
            .append($("<h3/>").text("Add PC to Computer Laboratory " + labId))
            .appendTo(form);
    
    var pcNumberRow = $("<div/>", {class: "form-group row"})
            .appendTo(form);
    
    var pcNumberDiv = $("<div/>", {class: "col-xs-12"})
            .appendTo(pcNumberRow);
    
    $("<input/>", {
        type: "text",
        id: "pc-pc-number",
        placeholder: "PC Number",
        required: true,
        class: "form-control"
    }).appendTo(pcNumberDiv);
    
    
    var pcNameRow = $("<div/>", {class: "form-group row"})
            .appendTo(form);
    
    var pcNameDiv = $("<div/>", {class: "col-xs-12"})
            .appendTo(pcNameRow);
    
    $("<input/>", {
        type: "text",
        id: "pc-pc-name",
        placeholder: "PC Name",
        required: true,
        class: "form-control"
    }).appendTo(pcNameDiv);
    
    
    var pcDescriptionRow = $("<div/>", {class: "form-group row"})
            .appendTo(form);
    
    var pcDescriptionDiv = $("<div/>", {class: "col-xs-12"})
            .appendTo(pcDescriptionRow);
    
    $("<textarea/>", {
        id: "pc-pc-description",
        placeholder: "PC Description",
        required: true,
        class: "form-control"
    }).appendTo(pcDescriptionDiv);
    
    
    var serialNumberRow = $("<div/>", {class: "form-group row"})
            .appendTo(form);
    
    var serialNumberDiv = $("<div/>", {class: "col-xs-12"})
            .appendTo(serialNumberRow);
    
    $("<input/>", {
        type: "text",
        id: "pc-serial-number",
        placeholder: "Serial Number",
        class: "form-control"
    }).appendTo(serialNumberDiv);
    
    
    var propertyNumberRow = $("<div/>", {class: "form-group row"})
            .appendTo(form);
    
    var propertyNumberDiv = $("<div/>", {class: "col-xs-12"})
            .appendTo(propertyNumberRow);
    
    $("<input/>", {
        type: "text",
        id: "pc-property-number",
        placeholder: "Property Number",
        class: "form-control"
    }).appendTo(propertyNumberDiv);
    
    
    var unitValueRow = $("<div/>", {class: "form-group row"})
            .appendTo(form);
    
    var unitValueDiv = $("<div/>", {class: "col-xs-12"})
            .appendTo(unitValueRow);
    
    $("<input/>", {
        type: "number",
        id: "pc-unit-value",
        placeholder: "Unit Value",
        class: "form-control"
    }).appendTo(unitValueDiv);
    
    
    var mrRow = $("<div/>", {class: "form-group row"})
            .appendTo(form);
    
    var mrDiv = $("<div/>", {class: "col-xs-12"})
            .appendTo(mrRow);
    
    $("<input/>", {
        type: "text",
        id: "pc-mr",
        placeholder: "PAR Issued To",
        class: "form-control"
    }).appendTo(mrDiv);
    
    
    var reMrRow = $("<div/>", {class: "form-group row"})
            .appendTo(form);
    
    var reMrDiv = $("<div/>", {class: "col-xs-12"})
            .appendTo(reMrRow);
    
    $("<input/>", {
        type: "text",
        id: "pc-re-mr",
        placeholder: "Memorandum of Receipt Re-Issued To",
        class: "form-control"
    }).appendTo(reMrDiv);
    
    var buttonRow = $("<div/>", {class: "form-group row"})
            .appendTo(form);
    
    var buttonDiv = $("<div/>", {class: "col-xs-12"})
            .appendTo(buttonRow);
    
    $("<button/>", {
        type: "submit",
        value: "Submit",
        class: "btn btn-primary"
    }).text("Submit").appendTo(buttonDiv);
    
    return container;
}

function updatePcBody(pc) {
    var container = $("<div/>", {class: "container"});
    var isNelia = userObj.type === "property-supply";
    var isLiane = userObj.type === "mis-officer";
    
    var form = $("<form/>")
            .on("submit", function(event) {
                event.preventDefault();
                
                if(isNelia || isLiane) {
                    pc.pcNumber = $("#pc-pc-number-edit").val();
                    pc.pcName = $("#pc-pc-name-edit").val();
                    pc.pcDescription = $("#pc-pc-description-edit").val();
                    pc.serialNumber = $("#pc-serial-number-edit").val();
                    pc.propertyNumber = $("#pc-property-number-edit").val();
                    pc.unitValue = $("#pc-unit-value-edit").val();
                    pc.mr = $("#pc-mr-edit").val();
                    pc.reMr = $("#pc-re-mr-edit").val();
                }
                
                pc.status = $("#pc-status-edit").val();
                
                var pcUpdateData = {
                    pc: pc,
                    remarks: $("#pc-update-remarks-edit").val()
                };
                
                $.ajax({
                    type: "PUT",
                    url: "/Narrido-1.0-SNAPSHOT/api/it/pc/",
                    data: JSON.stringify(pcUpdateData),
                    contentType: "application/json",
                    success: function(response) {
                        showModal($("<p/>").text(response), "Update PC info");
                    },
                    error: function (xhr) {
                        showModal($("<p/>").text(xhr.responsText), "Update PC info");
                    }
                });
            })
            .appendTo(container);
    
    var titleRow = $("<div/>", {class: "form-group row"})
            .append($("<h3/>").text("Edit PC"))
            .appendTo(form);
    
    if(isNelia || isLiane) {
        var pcNumberRow = $("<div/>", {class: "form-group row"})
                .appendTo(form);

        var pcNumberDiv = $("<div/>", {class: "col-xs-12"})
                .appendTo(pcNumberRow);

        $("<input/>", {
            type: "text",
            id: "pc-pc-number-edit",
            placeholder: "PC Number",
            required: true,
            class: "form-control",
            value: pc.pcNumber
        }).appendTo(pcNumberDiv);


        var pcNameRow = $("<div/>", {class: "form-group row"})
                .appendTo(form);

        var pcNameDiv = $("<div/>", {class: "col-xs-12"})
                .appendTo(pcNameRow);

        $("<input/>", {
            type: "text",
            id: "pc-pc-name-edit",
            placeholder: "PC Name",
            required: true,
            class: "form-control",
            value: pc.pcName
        }).appendTo(pcNameDiv);

        var pcDescriptionRow = $("<div/>", {class: "form-group row"})
                .appendTo(form);

        var pcDescriptionDiv = $("<div/>", {class: "col-xs-12"})
                .appendTo(pcDescriptionRow);

        $("<textarea/>", {
            id: "pc-pc-description-edit",
            placeholder: "PC Description",
            required: true,
            class: "form-control"
        }).val(pc.pcDescription).appendTo(pcDescriptionDiv);

        var serialNumberRow = $("<div/>", {class: "form-group row"})
                .appendTo(form);

        var serialNumberDiv = $("<div/>", {class: "col-xs-12"})
                .appendTo(serialNumberRow);

        $("<input/>", {
            type: "text",
            id: "pc-serial-number-edit",
            placeholder: "Serial Number",
            class: "form-control",
            value: pc.serialNumber
        }).appendTo(serialNumberDiv);


        var propertyNumberRow = $("<div/>", {class: "form-group row"})
                .appendTo(form);

        var propertyNumberDiv = $("<div/>", {class: "col-xs-12"})
                .appendTo(propertyNumberRow);

        $("<input/>", {
            type: "text",
            id: "pc-property-number-edit",
            placeholder: "Property Number",
            class: "form-control",
            value: pc.propertyNumber
        }).appendTo(propertyNumberDiv);


        var unitValueRow = $("<div/>", {class: "form-group row"})
                .appendTo(form);

        var unitValueDiv = $("<div/>", {class: "col-xs-12"})
                .appendTo(unitValueRow);

        $("<input/>", {
            type: "number",
            id: "pc-unit-value-edit",
            placeholder: "Unit Value",
            class: "form-control",
            value: pc.unitValue
        }).appendTo(unitValueDiv);


        var mrRow = $("<div/>", {class: "form-group row"})
                .appendTo(form);

        var mrDiv = $("<div/>", {class: "col-xs-12"})
                .appendTo(mrRow);

        $("<input/>", {
            type: "text",
            id: "pc-mr-edit",
            placeholder: "PAR Issued To",
            class: "form-control",
            value: pc.mr
        }).appendTo(mrDiv);


        var reMrRow = $("<div/>", {class: "form-group row"})
                .appendTo(form);

        var reMrDiv = $("<div/>", {class: "col-xs-12"})
                .appendTo(reMrRow);

        $("<input/>", {
            type: "text",
            id: "pc-re-mr-edit",
            placeholder: "Memorandum of Receipt Re-Issued To",
            class: "form-control",
            value: pc.reMr
        }).appendTo(reMrDiv);
    }
    
    var statusComboRow = $("<div/>", {class: "form-group row"})
                .appendTo(form);

    var statusComboDiv = $("<div/>", {class: "col-xs-12"})
            .appendTo(statusComboRow);

    var statusCombo = $("<select/>", {
        id: "pc-status-edit",
        class: "form-control"
    }).appendTo(statusComboDiv);
    
    var statuses = ["WORKING", "DEFFECTIVE", "UNSERVICEABLE", isNelia ? "CONDEMNED" : undefined];
    
    statuses.forEach(function(status) {
        if(status) {
            $("<option/>", {value: status}).text(status).appendTo(statusCombo);
        }
    });
    
    var updateRemarksRow = $("<div/>", {class: "form-group row"})
                .appendTo(form);

    var updateRemarksDiv = $("<div/>", {class: "col-xs-12"})
            .appendTo(updateRemarksRow);

    $("<textarea/>", {
        id: "pc-update-remarks-edit",
        placeholder: "Update Remarks (eg. changed status to working)",
        required: true,
        class: "form-control"
    }).val(pc.pcDescription).appendTo(updateRemarksDiv);
    
    var buttonRow = $("<div/>", {class: "form-group row"})
            .appendTo(form);
    
    var buttonDiv = $("<div/>", {class: "col-xs-12"})
            .appendTo(buttonRow);
    
    $("<button/>", {
        type: "submit",
        value: "Submit",
        class: "btn btn-primary"
    }).text("Submit").appendTo(buttonDiv);
    
    return container;
}

function showSupport() {
    var supportPane = $("#support");
    
    var container = $("<div/>", {class: "container"}).appendTo(supportPane);
    var form = $("<form/>").appendTo(container);
    var row = $("<div/>", {class: "form-group row"}).appendTo(form);

    var comboDiv = $("<div/>", {class: "col-xs-2"}).appendTo(row);
    var statusCombo = $("<select/>", {
        name: "supportStatus",
        id: "support-status",
        class: "form-control"
    }).on("change", function () {
        viewSupport(statusCombo.val());
    }).appendTo(comboDiv);
    
    var statuses = [
        {
            description: "All",
            value: "all"
        },
        {
            description: "Pending Resolution",
            value: "pending"
        },
        {
            description: "Resolved",
            value: "resolved"
        }
    ];
    
    statuses.forEach(function(stat) {
        $("<option/>", {value: stat.value})
                .html(stat.description).appendTo(statusCombo);
    });
    
    var pwede = userObj.type === "it-staff" || userObj.type === "property-supply" || userObj.type === "mis-officer";
    
    if(pwede) {
        var buttonDiv2 = $("<div/>", {class: "col-xs-3 offset-xs-2"}).appendTo(row);
        var button2 = $("<button/>", {
            type: "button",
            class: "form-control btn btn-primary"
        }).html("Generate Report")
                .on("click", function() {
                    showModal($("<p/>").text("Generating Report..."), "Generate Report");
                    $.ajax({
                        type: "GET",
                        url: "/Narrido-1.0-SNAPSHOT/api/it/support/report",
                        success: function (response) {
                            showModal($("<p/>").text(response), "Generate Report");
                        },
                        error: function (xhr) {
                            showModal($("<p/>").text(xhr.responseText), "Generate Report");
                        }
                    });
                })
                .appendTo(buttonDiv2);
    }
    
    var buttonDiv = $("<div/>", {class: "col-xs-3"}).appendTo(row);
    var button = $("<button/>", {
        type: "button",
        class: "form-control btn btn-primary"
    }).html("Report a Problem")
            .on("click", function() {addSupportIssue();})
            .appendTo(buttonDiv);
    
    $("<div/>", {class: "container", id: "support-content"}).appendTo(supportPane);
    viewSupport(statusCombo.val());
}

function viewSupport(status) {
    var content = $("#support-content").empty();
    
    $.ajax({
        type: "GET",
        url: "/Narrido-1.0-SNAPSHOT/api/it/support",
        data: {
            status: status
        },
        dataType: "json",
        success: function(tickets) {
            content.append(tablifySupport(tickets));
        },
        error: function(xhr) {
            showModal($("<p/>").html("Error processing request: " + xhr.responseText), "Error");
        }
    });
}

function tablifySupport(tickets) {
    var frag = document.createDocumentFragment();
    
    var table = $("<table/>", {class: "table"}).appendTo(frag);
    var tHead = $("<thead/>").appendTo(table);
    var headRow = $("<tr/>").appendTo(tHead);
    
    var isTech = userObj.type === "it-staff";
    
    var headers = [
        "Report/s",
        "Date Reported",
        "Reported By",
        "In Charge",
        "Issue Status",
        "PC/Equipment nr.",
        "Date Resolved",
        "Diagnostics",
        "Equipment Status",
        "Application"
    ];
    
    headers.forEach(function(header) {
        $("<th/>").text(header).appendTo(headRow);
    });
    
    var tBody = $("<tbody/>").appendTo(table);
    
    tickets.forEach(function(ticket) {
        var bodyRow = $("<tr/>", {class: "narrido-it-row"})
                .on("click", function() {
                    if(userObj.type === "it-staff") {
                        showModal(updateIssueBody(ticket), "Update Issue");
                    }
                })
                .appendTo(tBody);
        
        $("<th/>", {scope: "row"}).html("#" + ticket.jobId + ": " +ticket.report).appendTo(bodyRow);
        var dateReported = new Date(ticket.dateReported).toDateString();
        $("<td/>").html(dateReported).appendTo(bodyRow);
        $("<td/>").append(nameHeader(ticket.reportedBy, "sm")).appendTo(bodyRow);
        var handler = ticket.handledBy ? nameHeader(ticket.handledBy, "sm") : $("<p/>").text("N/A");
        $("<td/>").append(handler).appendTo(bodyRow);
        $("<td/>").html(ticket.status).appendTo(bodyRow);
        $("<td/>").html(ticket.pc.pcNumber).appendTo(bodyRow);
        var dateResolved = ticket.dateResolved ? new Date(ticket.dateResolved).toDateString() : "N/A";
        $("<td/>").html(dateResolved).appendTo(bodyRow);
        $("<td/>").html(ticket.findings).appendTo(bodyRow);
        $("<td/>").html(ticket.pc.status).appendTo(bodyRow);
        $("<td/>").html(ticket.action).appendTo(bodyRow);
        
    });
    
    return frag;
}

function addSupportIssue() {
    showModal(addSupportIssueBody(), "Report a Problem");
}

function addSupportIssueBody() {
    var frag = document.createDocumentFragment();
    
    var container = $("<div/>", {class: "container"}).appendTo(frag);
    var form = $("<form/>").on("submit", function(event) {
        event.preventDefault();
        
        $("#support-submit").prop("disabled", true).text("Submitting...");
        var job = {
            dateReported: new Date(),
            status: "pending",
            pc: JSON.parse($("#support-pcs").val()),
            report: $("#support-report").val()
        };

        $.ajax({
            type: "POST",
            url: "/Narrido-1.0-SNAPSHOT/api/it/support",
            data: JSON.stringify(job),
            contentType: "application/json",
            success: function() {
                showModal($("<p/>").text("Issue submitted successfully!"), "Report a Problem");
            },
            error: function(xhr) {
                showModal($("<p/>").text("Error submitting issue: " + xhr.responseText), "Report a Problem");
            }
        });
    }).appendTo(container);
    
    var row = $("<div/>", {class: "form-group row"}).appendTo(form);

    var comboDiv = $("<div/>", {class: "col-xs-4"}).appendTo(row);
    var labCombo = $("<select/>", {
        name: "labId",
        id: "support-lab-id",
        class: "form-control"
    }).on("change", function() {
        $("#support-pcs").empty();
        getPcOptions(labCombo.val());
    }).appendTo(comboDiv);
    
    for(var i = 0; i < 9; i++) {
        $("<option/>", {value: i + 1})
                .html("Computer Laboratory " + (i + 1)).appendTo(labCombo);
    }
    
    var comboDiv2 = $("<div/>", {class: "col-xs-4"}).appendTo(row);
    var pcCombo = $("<select/>", {
        name: "pc",
        id: "support-pcs",
        class: "form-control"
    }).appendTo(comboDiv2);

    var reportRow = $("<div/>", {class: "form-group row"})
            .appendTo(form);
    
    var reportDiv = $("<div/>", {class: "col-xs-12"})
            .appendTo(reportRow);
    
    $("<textarea/>", {
        id: "support-report",
        placeholder: "Problem (eg. PC 12 slow running)",
        class: "form-control"
    }).appendTo(reportDiv);
    
    var buttonRow = $("<div/>", {class: "form-group row"})
            .appendTo(form);
    
    var buttonDiv = $("<div/>", {class: "col-xs-12"})
            .appendTo(buttonRow);
    
    $("<button/>", {
        type: "submit",
        id: "support-submit",
        value: "Submit",
        class: "btn btn-primary"
    }).text("Submit").appendTo(buttonDiv);
    
    getPcOptions(labCombo.val());
    return frag;
}

function getPcOptions(labId) {
    $.ajax({
        type: "GET",
        url: "/Narrido-1.0-SNAPSHOT/api/it/pc/" + labId,
        dataType: "json",
        success: function (pcs) {
            pcs.forEach(function(pc) {
                $("<option/>", {value: JSON.stringify(pc)}).text(pc.pcNumber).appendTo($("#support-pcs"));
            });
        },
        error: function (xhr) {
            showModal($("<p/>").text("Error in getting pcs: " + xhr.responseText), "Error");
        }
    });
}

function updateIssueBody(ticket) {
    var frag = document.createDocumentFragment();
    
    var container = $("<div/>", {class: "container"}).appendTo(frag);
    $("<h3/>").text("Issue # " + ticket.jobId + ": " + ticket.report).appendTo(container);
    var form = $("<form/>").on("submit", function(event) {
        event.preventDefault();
        
        ticket.status = $("#support-status-update").val();
        ticket.findings = $("#support-diagnostics-update").val();
        ticket.action = $("#support-application-update").val();
        
        
        $("#support-submit-update").prop("disabled", true).text("Submitting...");

        $.ajax({
            type: "PUT",
            url: "/Narrido-1.0-SNAPSHOT/api/it/support",
            data: JSON.stringify(ticket),
            contentType: "application/json",
            success: function() {
                showModal($("<p/>").text("Issue updated!"), "Report a Problem");
            },
            error: function(xhr) {
                showModal($("<p/>").text("Error updating issue: " + xhr.responseText), "Report a Problem");
            }
        });
    }).appendTo(container);
    
    var row = $("<div/>", {class: "form-group row"}).appendTo(form);

    var comboDiv = $("<div/>", {class: "col-xs-4"}).appendTo(row);
    var statusCombo = $("<select/>", {
        name: "status",
        id: "support-status-update",
        class: "form-control"
    }).appendTo(comboDiv);
    
    ["pending", "resolved"].forEach(function(status) {
        $("<option/>", {value: status})
                .html(status).appendTo(statusCombo);
    });
    
    var diagnosticsRow = $("<div/>", {class: "form-group row"})
            .appendTo(form);
    
    var diagnosticsDiv = $("<div/>", {class: "col-xs-12"})
            .appendTo(diagnosticsRow);
    
    $("<textarea/>", {
        id: "support-diagnostics-update",
        placeholder: "Diagnostics (eg. HDD bad sector, AVR busted fuse)",
        class: "form-control",
        required: true
    }).appendTo(diagnosticsDiv);
    
    var applicationRow = $("<div/>", {class: "form-group row"})
            .appendTo(form);
    
    var applicationDiv = $("<div/>", {class: "col-xs-12"})
            .appendTo(applicationRow);
    
    $("<textarea/>", {
        id: "support-application-update",
        placeholder: "Application (eg. replaced HDD, replaced fuse, for further inspection)",
        class: "form-control"
    }).appendTo(applicationDiv);
    
    var buttonRow = $("<div/>", {class: "form-group row"})
            .appendTo(form);
    
    var buttonDiv = $("<div/>", {class: "col-xs-12"})
            .appendTo(buttonRow);
    
    $("<button/>", {
        type: "submit",
        id: "support-submit-update",
        value: "Submit",
        class: "btn btn-primary"
    }).text("Submit").appendTo(buttonDiv);
    
    return frag;
}

function showPcPage() {
    var pageContainer = $("#pc-page");
    $("#narrido-content-title-pc-page").text("Something Seomthing PC Identifier goes here");
    var pageContent = $("<div/>", {class: "narrido-pc-page-content"}).appendTo(pageContainer);
}

function openPcPage(pc, event) {
    $("#narrido-content-title-pc-page").text(pc.pcNumber + " (" + pc.laboratory.labDescription + ")");
    
    showPcTabs(pc);
    showPane(event, "pc-page", "narrido-tab-pane", "narrido-main-link");
}

function pcDescriptionTable(pc) {
    var pcPageContent = $(".narrido-pc-page-content");
    
    var table = $("<table/>", {class: "table table-sm", id: "pc-page-table"}).hide().appendTo(pcPageContent);
    
    var body = $("<tbody/>").appendTo(table);
    
    var idRow = $("<tr/>").appendTo(body);
    $("<th/>", {scope: "row"}).text("ID: ").appendTo(idRow);
    $("<td/>").text(pc.id).appendTo(idRow);
    
    var numberRow = $("<tr/>").appendTo(body);
    $("<th/>", {scope: "row"}).text("PC Nr.: ").appendTo(numberRow);
    $("<td/>").text(pc.pcNumber).appendTo(numberRow);
    
    var descriptionRow = $("<tr/>").appendTo(body);
    $("<th/>", {scope: "row"}).text("Description: ").appendTo(descriptionRow);
    $("<td/>").text(pc.pcDescription).appendTo(descriptionRow);
    
    var dateAcquiredRow = $("<tr/>").appendTo(body);
    $("<th/>", {scope: "row"}).text("Date Acquired: ").appendTo(dateAcquiredRow);
    $("<td/>").text(pc.dateAcquired).appendTo(dateAcquiredRow);
    
    var serialNumberRow = $("<tr/>").appendTo(body);
    $("<th/>", {scope: "row"}).text("Serial Number: ").appendTo(serialNumberRow);
    $("<td/>").text(pc.serialNumber).appendTo(serialNumberRow);
    
    var propertyNumberRow = $("<tr/>").appendTo(body);
    $("<th/>", {scope: "row"}).text("Property Number: ").appendTo(propertyNumberRow);
    $("<td/>").text(pc.propertyNumber).appendTo(propertyNumberRow);
    
    var unitValueRow = $("<tr/>").appendTo(body);
    $("<th/>", {scope: "row"}).text("Unit Value: ").appendTo(unitValueRow);
    $("<td/>").text(pc.unitValue).appendTo(unitValueRow);
    
    var statusRow = $("<tr/>").appendTo(body);
    $("<th/>", {scope: "row"}).text("Operational Status: ").appendTo(statusRow);
    $("<td/>").text(pc.status).appendTo(statusRow);
    
    var mrRow = $("<tr/>").appendTo(body);
    $("<th/>", {scope: "row"}).text("PAR Assigned To: ").appendTo(mrRow);
    $("<td/>").text(pc.mr).appendTo(mrRow);
    
    var reMrRow = $("<tr/>").appendTo(body);
    $("<th/>", {scope: "row"}).text("Re-MR To: ").appendTo(reMrRow);
    $("<td/>").text(pc.reMr).appendTo(reMrRow);
}

function pcPageNav(pc) {
    var pageNav = document.createElement("ul");
    pageNav.classList.add("narrido-group-nav", "narrido-group-page-title");
    
    var linkz = [
        {
            text: "Installed Programs",
            theLink: "pc-page-installed-"  + pc.id,
            clazz: "narrido-pc-page-link"
        },
        {
            text: "Running Programs",
            theLink: "pc-page-running-" + pc.id,
            clazz: "narrido-pc-page-link"
        },
        {
            text: "Usage Log",
            theLink: "pc-page-usage-log-" + pc.id,
            clazz: "narrido-pc-page-link"
        },
        {
            text: "Issues",
            theLink: "pc-page-issues-" + pc.id,
            clazz: "narrido-pc-page-link"
        }
    ];
    
    linkz.forEach(function(link) {
        var listItem = document.createElement("li");
        var linkElem = document.createElement("a");
        linkElem.textContent = link.text;
        linkElem.setAttribute("href", "#");
        linkElem.classList.add(link.clazz);
        linkElem.addEventListener("click", function(event) {
            event.preventDefault();
            showPane(event, link.theLink, "narrido-pc-page-pane", link.clazz);
        });

        listItem.appendChild(linkElem);
        pageNav.appendChild(listItem);

    });
    
    return pageNav;
}

function showPcTabs(pc) {
    var pageContent = $(".narrido-pc-page-content").empty();

    $("<button/>", {
        type: "button",
        class: "btn btn-primary"
    }).html("View details").on("click", function() {
        $("#pc-page-table").toggle();
    }).appendTo(pageContent);
    
    $("<button/>", {
        type: "button",
        class: "btn btn-primary"
    }).html("Update info").on("click", function() {
        showModal(updatePcBody(pc), "Update PC");
    }).appendTo(pageContent);

    pcDescriptionTable(pc);
    pageContent.append(pcPageNav(pc));

    var panes = [
        {
            title: "pc-page-installed-"
        },
        {
            title: "pc-page-running-"
        },
        {
            title: "pc-page-usage-log-"
        },
        {
            title: "pc-page-issues-"
        }
    ];
    panes.forEach(function(pane) {
        var groupPane = document.createElement("div");
        
        groupPane.className = "narrido-pc-page-pane";
        groupPane.style.display = "none";
        groupPane.id = pane.title + pc.id;
        
        if(pane.content) groupPane.appendChild(pane.content);
        
        pageContent.append(groupPane);
        
    });
    
    showTestData(pc);
    showIssues(pc);
}

function showTestData(pc) {
    var programsPane = $("#pc-page-installed-" + pc.id);
    var runningPane = $("#pc-page-running-" + pc.id);
    var usagePane = $("#pc-page-usage-log-" + pc.id);
    
    $.ajax({
        type: "GET",
        url: "/Narrido-1.0-SNAPSHOT/api/it/pc/software",
        success: function(data) {
            var ul = $("<ul/>").appendTo(programsPane);
            data.forEach(function(datum) {
                $("<li/>").text(datum).appendTo(ul);
            });
        }
    });
    
    $.ajax({
        type: "GET",
        url: "/Narrido-1.0-SNAPSHOT/api/it/pc/running",
        success: function(data) {
            var ul = $("<ul/>").appendTo(runningPane);
            data.forEach(function(datum) {
                $("<li/>").text(datum).appendTo(ul);
            });
        }
    });
    
    $.ajax({
        type: "GET",
        url: "/Narrido-1.0-SNAPSHOT/api/it/pc/logs",
        success: function(logs) {
            var table = $("<table/>", {class: "table"}).appendTo(usagePane);
            var body = $("<tbody/>").appendTo(table);
            
            logs.forEach(function(log) {
               var row = $("<tr/>").appendTo(body);
               
               var date = new Date(log.date).toDateString() + " " + new Date(log.date).toLocaleTimeString();
               $("<td/>").text(date).appendTo(row);
               $("<td/>").text(log.log).appendTo(row);
            });
        }
    });
}

function showIssues(pc) {
    var issuesPane = $("#pc-page-issues-" + pc.id);
    
    $.ajax({
        type: "GET",
        url: "/Narrido-1.0-SNAPSHOT/api/it/support/pc/" + pc.id,
        data: "json",
        success: function(tickets) {
            issuesPane.append(tablifySupport(tickets));
        }
    });
}

function reMrBody() {
    var frag = document.createDocumentFragment();
    
    var container = $("<div/>", {class: "container"}).appendTo(frag);
    var form = $("<form/>").on("submit", function(event) {
        event.preventDefault();
        
        $("#remr-submit").prop("disabled", true).text("Submitting...");
        var remrData = {
            reMr : $("#remr-remr").val(),
            date : $("#remr-date").val(),
            description : $("#remr-description").val(),
            note : $("#remr-note").val(),
            receivedFrom : $("#remr-received-from").val(),
            receivedFromPosition : $("#remr-received-from-position").val(), 
            receiverPosition : $("#remr-receiver-position").val()
        };

        $.ajax({
            type: "POST",
            url: "/Narrido-1.0-SNAPSHOT/api/it/remr",
            data: JSON.stringify(remrData),
            contentType: "application/json",
            success: function() {
                showModal($("<p/>").text("Report Generated!"), "Re-MR");
            },
            error: function(xhr) {
                showModal($("<p/>").text("Error Generating Report: " + xhr.responseText), "Re-MR");
            }
        });
    }).appendTo(container);
    

    var remrRow = $("<div/>", {class: "form-group row"})
            .appendTo(form);
    
    var remrDiv = $("<div/>", {class: "col-xs-12"})
            .appendTo(remrRow);
    
    $("<input/>", {
        type: "text",
        required: "true",
        id: "remr-remr",
        placeholder: "Name of Re-MR'd",
        class: "form-control"
    }).appendTo(remrDiv);
    
    var dateRow = $("<div/>", {class: "form-group row"})
            .appendTo(form);
    
    var dateDiv = $("<div/>", {class: "col-xs-12"})
            .appendTo(dateRow);
    
    $("<input/>", {
        type: "date",
        id: "remr-date",
        required: "true",
        class: "form-control"
    }).appendTo(dateDiv);
    
    var descriptionRow = $("<div/>", {class: "form-group row"})
            .appendTo(form);
    
    var descriptionDiv = $("<div/>", {class: "col-xs-12"})
            .appendTo(descriptionRow);
    
    $("<textarea/>", {
        id: "remr-description",
        required: "true",
        placeholder: "Description (eg. pc specs)",
        class: "form-control"
    }).appendTo(descriptionDiv);
    
    var noteRow = $("<div/>", {class: "form-group row"})
            .appendTo(form);
    
    var noteDiv = $("<div/>", {class: "col-xs-12"})
            .appendTo(noteRow);
    
    $("<textarea/>", {
        id: "remr-note",
        required: "true",
        placeholder: "Note (eg. Transferred from Juan Dela Cruz to John Doe)",
        class: "form-control"
    }).appendTo(noteDiv);
    
    var receivedFromRow = $("<div/>", {class: "form-group row"})
            .appendTo(form);
    
    var receivedFromDiv = $("<div/>", {class: "col-xs-12"})
            .appendTo(receivedFromRow);
    
    $("<input/>", {
        type: "text",
        id: "remr-received-from",
        required: "true",
        placeholder: "Received From",
        class: "form-control"
    }).appendTo(receivedFromDiv);
    
    var receivedFromPositionRow = $("<div/>", {class: "form-group row"})
            .appendTo(form);
    
    var receivedFromPositionDiv = $("<div/>", {class: "col-xs-12"})
            .appendTo(receivedFromPositionRow);
    
    $("<input/>", {
        type: "text",
        id: "remr-received-from-position",
        required: "true",
        placeholder: "Received From Position",
        class: "form-control"
    }).appendTo(receivedFromPositionDiv);
    
    var receiverPositionRow = $("<div/>", {class: "form-group row"})
            .appendTo(form);
    
    var receiverPositionDiv = $("<div/>", {class: "col-xs-12"})
            .appendTo(receiverPositionRow);
    
    $("<input/>", {
        type: "text",
        id: "remr-receiver-position",
        required: "true",
        placeholder: "Receiver Position",
        class: "form-control"
    }).appendTo(receiverPositionDiv);
    
    var buttonRow = $("<div/>", {class: "form-group row"})
            .appendTo(form);
    
    var buttonDiv = $("<div/>", {class: "col-xs-12"})
            .appendTo(buttonRow);
    
    $("<button/>", {
        type: "submit",
        id: "remr-submit",
        value: "Submit",
        class: "btn btn-primary"
    }).text("Create Re-MR").appendTo(buttonDiv);
    
    return frag;
}