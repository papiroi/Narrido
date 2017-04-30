 /* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

var newsFeedModule = {
        paneId: "feed",
        className: "narrido-main-link",
        linkText: "News Feed",
        linkSymbol: '<i class = "fa fa-file"></i>',
        disp: showNewsFeed
    },
    
    groupsModule = {
        paneId: "groups",
        className: "narrido-main-link",
        linkText: "Groups",
        linkSymbol: '<i class = "fa fa-group"></i>',
        isDefault: true,
        disp: showGroups
    },
            
    groupPageModule = {
        paneId: "group-page",
        className: "narrido-main-link",
        linkText: "Group Page",
        isNotOnMenu: true,
        linkSymbol: '<i class = "fa fa-group"></i>',
        disp: showGroupPage
    },
            
    myFilesModule = {
        paneId: "files",
        className: "narrido-main-link",
        linkText: "My Files",
        linkSymbol: '<i class = "fa fa-file"></i>',
        disp: showFiles
    },
            
    monitoringModule = {
        paneId: "daily-monitoring",
        className: "narrido-main-link",
        linkText: "Daily Monitoring",
        linkSymbol: '<i class = "fa fa-eye"></i>',
        disp: showMonitoring
    },
    
    supportModule = {
        paneId: "support",
        className: "narrido-main-link",
        linkText: "Technical Support",
        linkSymbol: '<i class = "fa fa-wrench"></i>',
        disp: showSupport
    },
    
    pcModule = {
        paneId: "pc",
        className: "narrido-main-link",
        linkText: "PCs",
        linkSymbol: '<i class = "fa fa-desktop"></i>',
        disp: showPcs
    },
            
    qrCodeModule = {
        paneId: "qr-codes",
        className: "narrido-main-link",
        linkText: "QR Codes",
        linkSymbol: '<i class = "fa fa-qrcode"></i>',
        disp: showQR
    },
            
    reportsModule = {
        paneId: "reports",
        className: "narrido-main-link",
        linkText: "Reports",
        linkSymbol: '<i class = "fa fa-file-text"></i>',
        disp: showReports
    },
    
    pcPageModule = {
        paneId: "pc-page",
        className: "narrido-main-link",
        linkText: "PC Page",
        isNotOnMenu: true,
        linkSymbol: '<i class = "fa fa-file-text"></i>',
        disp: showPcPage //TODO define method
    };
    
var genericModules = [
    groupsModule,
    groupPageModule,
    myFilesModule
];

var studentElements = [
];

var facultyElements = [
    pcModule,
    pcPageModule,
    supportModule
];

var itStaffElements = [
    pcModule,
    pcPageModule,
    qrCodeModule,
    reportsModule,
    monitoringModule,
    supportModule
];

var deptHeadElements = [
    pcModule,
    pcPageModule,
    qrCodeModule,
    reportsModule,
    monitoringModule,
    supportModule
];

var propertySupplyElements = [
    pcModule,
    pcPageModule,
    qrCodeModule,
    reportsModule,
    monitoringModule,
    supportModule
];

var misElements = [
    pcModule,
    pcPageModule,
    qrCodeModule,
    reportsModule,
    monitoringModule,
    supportModule
];

var loginButton = document.getElementById("login-button");
var regButton = document.getElementById("reg-button");
var loginRegister = document.querySelector('.login-register');
var contentPane = document.getElementById("narrido-tab-content");

var userObj;

$(document).ajaxSend(function (event, xhr, options) {
    var token = Cookies.get("token");
    if (token) {
        xhr.setRequestHeader("Authorization", "Bearer " + token);
    } else {
        location.replace("/Narrido-1.0-SNAPSHOT/login.html");
    }
});

$(document).ajaxError(function (event, xhr) {
   //show modal 
});

$(document).ready(function () {
    $.ajax({
        type: "GET",
        url: "api/account",
        dataType: "json",
        success: function (user) {
            userObj = user;
            addDetails();
            openChannel();
        },
        error: function () {
            location.replace("/Narrido-1.0-SNAPSHOT/login.html");
        }
    });
});

var modal = $(".narrido-modal");
$(window).on("click", function(event) {
    if($(event.target).is(modal) || $(event.target).is($(".close"))) {
        modal.css("display", "none");
    }
});

function openChannel() {
    $.ajax({
        type: "GET",
        url: "api/push",
        dataType: "json",
        success: function (data) {
            console.log("Object received");
            dataResolver(data);
        },
        complete: function() {
            openChannel();
        },
        timeout: 15000
    });
}

function dataResolver(data) {
    if(data.postId) {
        console.log(data.group.groupId);
        $("#group-page-feed-body-" + data.group.groupId)
                .prepend($(feedItem(data)).hide().fadeIn(1500));
    } else if (data.monitoringId) {
        var bodyrow = $("<tr/>");
        
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
        
        $("#table-lab-" + data.laboratory.labId).append(bodyrow);
    } else if (data.pcNumber) {
        var bodyrow = $("<tr/>");
        $("<th/>", {scope: "row"}).text(data.laboratory.labId).appendTo(bodyrow);
        $("<td/>").text(data.pcNumber).appendTo(bodyrow);
        $("<td/>").text(data.pcDescription).appendTo(bodyrow);
        var dateObj = new Date(data.dateAcquired);
        var date = dateObj.toDateString();
        $("<td/>").text(date).appendTo(bodyrow);
        $("<td/>").text(data.serialNumber).appendTo(bodyrow);
        $("<td/>").text(data.propertyNumber).appendTo(bodyrow);
        $("<td/>").text(data.unitValue).appendTo(bodyrow);
        $("<td/>").text(data.status).appendTo(bodyrow);
        $("<td/>").text(data.mr).appendTo(bodyrow);
        $("<td/>").text(data.reMr).appendTo(bodyrow);
        
        $("#table-pc-lab-" + data.laboratory.labId).append(bodyrow);
    } else if (data.fileId) {
        if(data.fileType === "file") {
            var frag = $("#narrido-my-files");
            updateFiles(frag, data);
        } else if (data.fileType === "qr") {
            var frag = $("#narrido-qr-codes");
            var column = $("<div/>", {class: "col-xs-12 col-sm-4 col-md-3"}).appendTo(frag);
                 $("<div/>", {class: "narrido-file"})
                    .append($("<img/>", {src: data.fileUrl}))
                    .append($("<a/>", {href: data.fileUrl}).text(data.fileName))
                    .append($("<span/>", {class: "close"}).html("&times;"))
                    .appendTo(column);
        } else if (data.fileType === "report") {
            var frag = $("#narrido-reports");
            updateFiles(frag, data);
        }
    }
}

function updateFiles(fileContainer, data) {
    var frag = fileContainer;
    var column = $("<div/>", {class: "col-xs-12 col-sm-4 col-md-3"}).appendTo(frag);
         $("<div/>", {class: "narrido-file"})
            .append($("<img/>", {src: data.fileUrl}))
            .append($("<a/>", {href: data.fileUrl}).text(data.fileName))
            .append($("<span/>", {class: "close"}).html("&times;"))
            .appendTo(column);
}

function showModal(body, headerText, callback) {
    if(body){
        $(".narrido-modal-body").html("").append(body);
    }
    
    if(headerText){
        $(".narrido-modal-header").find("h3").text(headerText);
    }
    modal.css("display", "block");
}

function addDetails() {
    var userNameDoc = document.getElementsByClassName("user-name");
    for (var i = 0; i < userNameDoc.length; i++) {
        userNameDoc[i].innerHTML += userObj.firstName + " " + userObj.middleName + " " + userObj.lastName;
    }

    var userTypeDoc = document.getElementsByClassName("role");

    for (var i = 0; i < userTypeDoc.length; i++) {
        userTypeDoc[i].className += " narrido-" + userObj.type;
        userTypeDoc[i].innerHTML = userObj.type;
    }

    loadMenu(genericModules);
    
    if (userObj.type === "student") {
        loadMenu(studentElements);
    } else if (userObj.type === "faculty") {
        loadMenu(facultyElements);
    } else if (userObj.type === "it-staff") {
        loadMenu(itStaffElements);
    } else if (userObj.type === "dept-head") {
        loadMenu(deptHeadElements);
    } else if (userObj.type === "property-supply") {
        loadMenu(propertySupplyElements);
    } else if (userObj.type === "mis-officer") {
        loadMenu(misElements);
    }
}

function loadMenu(elementArray) {
    var sidebarDoc = document.getElementById("narrido-sidebar");

    elementArray.forEach((ea) => {
        if (!ea.isNotOnMenu) {
            var span = document.createElement("span");
            span.setAttribute("class", "narrido-link-symbol");
            span.innerHTML = ea.linkSymbol;

            var linkElement = document.createElement("a");

            var li = document.createElement("li");

            linkElement.setAttribute("href", "#");
            linkElement.addEventListener("click", (event) => {
                showPane(event, ea.paneId, "narrido-tab-pane", "narrido-main-link");
            });
            linkElement.setAttribute("class", ea.className);
            linkElement.innerHTML = ea.linkText;

            linkElement.insertBefore(span, linkElement.childNodes[0]);

            li.appendChild(linkElement);

            sidebarDoc.appendChild(li);
        }
        loadPaneHeader.call(ea, ea.isDefault);
    });

}

function loadPaneHeader(isFront = false) {
    var paneElement = document.createElement("div");
    var header = document.createElement("h1");
    var headerContainer = document.createElement("div");
    headerContainer.className = "narrido-header";
    headerContainer.setAttribute("id", "narrido-header-" + this.paneId);

    paneElement.setAttribute("class", "narrido-tab-pane");
    paneElement.setAttribute("id", this.paneId);

    header.setAttribute("class", "content-title");
    header.appendChild(document.createTextNode(this.linkText));
    header.setAttribute("id", "narrido-content-title-" + this.paneId);
    headerContainer.appendChild(header);
    paneElement.appendChild(headerContainer);
    paneElement.style.display = isFront ? 'block' : 'none';

    contentPane.appendChild(paneElement);

    this.disp.call();
}

function showNewsFeed() {
    var feedContent = document.createElement("div");
    feedContent.className = "narrido-news-feed-container";
    document.getElementById("feed").appendChild(feedContent);

    //feedContent.appendChild(newsFeed(userObj));
}

function showFiles() {
    var filesContent = $("<div/>", {class: "container"}).appendTo($("#files"));
    
    var row = $("<div/>", {class: "row", id: "narrido-my-files"}).appendTo(filesContent);
    
    var div = $("<div/>", {class: "col-xs-12 col-sm-4 col-md-3"}).appendTo(row);
    
    $("<div/>", {class: "narrido-file"})
            .append($("<p/>").text("+ Add File"))
            .append($("<span/>", {class: "close"}).html("&times;"))
            .on("click", function() {
                showModal(uploadBody(), "Upload File");
            })
            .appendTo(div);
    
    getMyFiles();
}

function uploadBody() {
    var frag = document.createDocumentFragment();
    
    var form = $("<form/>", {class: "form-inline", method: "post"})
            .on("submit", function(event) {
                event.preventDefault();
                var form = $(this)[0];
                var formData = new FormData(form);
                
                showModal($("<p/>").text("Uploading file..."), "Submit Post");
                
                $.ajax({
                    type: "POST",
                    enctype: "multipart/form-data",
                    url: "/Narrido-1.0-SNAPSHOT/api/files/user",
                    data: formData,
                    processData: false,
                    contentType: false,
                    success: function(response) {
                        $(".narrido-modal-body").empty().text("File submitted successfully!");
                        console.log(response);
                    },
                    error: function(xhr) {
                        $(".narrido-modal-body").empty().text("Post submit failed! Cause: " + xhr.responseText);
                        console.log(xhr.responseText);
                    }
                });
            })
            .appendTo(frag);
    
    $("<input/>", {
        type: "file",
        class: "form-control-file",
        name: "file"
    }).appendTo(form);
    
    $("<button/>", {
        type: "submit",
        class: "btn btn-primary"
    }).text("Upload File").appendTo(form);
    
    return frag;
}

function getMyFiles() {
    $.ajax({
        type: "GET",
        url: "/Narrido-1.0-SNAPSHOT/api/files/user",
        dataType: "json",
        success: function(files) {
            frag = $("#narrido-my-files");
            files.forEach(function(file) {
                 var column = $("<div/>", {class: "col-xs-12 col-sm-4 col-md-3"}).appendTo(frag);
                 $("<div/>", {class: "narrido-file"})
                    .append($("<a/>", {href: file.fileUrl}).text(file.fileName))
                    .append($("<span/>", {class: "close"}).html("&times;"))
                    .appendTo(column);
            });
        }
    });
}

function showQR() {
    var filesContent = $("<div/>", {class: "container"}).appendTo($("#qr-codes"));
    
    var row = $("<div/>", {class: "row", id: "narrido-qr-codes"}).appendTo(filesContent);
    
    getMyQR();
}

function getMyQR() {
    $.ajax({
        type: "GET",
        url: "/Narrido-1.0-SNAPSHOT/api/files/qr",
        dataType: "json",
        success: function(files) {
            frag = $("#narrido-qr-codes");
            files.forEach(function(file) {
                 var column = $("<div/>", {class: "col-xs-12 col-sm-4 col-md-3"}).appendTo(frag);
                 $("<div/>", {class: "narrido-file"})
                    .append($("<img/>", {src: file.fileUrl}))
                    .append($("<a/>", {href: file.fileUrl}).text(file.fileName))
                    .append($("<span/>", {class: "close"}).html("&times;"))
                    .appendTo(column);
            });
        }
    });
}

function showReports() {
    var filesContent = $("<div/>", {class: "container"}).appendTo($("#reports"));
    
    var row = $("<div/>", {class: "row", id: "narrido-reports"}).appendTo(filesContent);
    
    getMyReports();
}

function getMyReports() {
    $.ajax({
        type: "GET",
        url: "/Narrido-1.0-SNAPSHOT/api/files/reports",
        dataType: "json",
        success: function(files) {
            frag = $("#narrido-reports");
            files.forEach(function(file) {
                 var column = $("<div/>", {class: "col-xs-12 col-sm-4 col-md-3"}).appendTo(frag);
                 $("<div/>", {class: "narrido-file"})
                    .append($("<a/>", {href: file.fileUrl}).text(file.fileName))
                    .append($("<span/>", {class: "close"}).html("&times;"))
                    .appendTo(column);
            });
        }
    });
}

function showPane(evt, paneName, paneClass, linkClass) {
    var contentPanes, links;
    contentPanes = document.getElementsByClassName(paneClass);
    links = document.getElementsByClassName(linkClass);
    for (var i = 0; i < contentPanes.length; i++) {
        contentPanes[i].style.display = "none";
    }

    for (var i = 0; i < links.length; i++) {
        links[i].classList.remove("active");
    }

    document.getElementById(paneName).style.display = "block";

    evt.currentTarget.classList.add("active");
}

function nameHeader(user, size = "md") {
    var textElementString;

    switch (size) {
        case "lg":
            textElementString = "h3";
            break;
        case "md":
            textElementString = "h5";
            break;
        case "sm":
            textElementString = "h6";
            break;
    }

    var nameDiv = document.createElement("div");
    nameDiv.className = "narrido-name-" + size;

    var profileImg = document.createElement("img");
    profileImg.className = "narrido-user-pic-" + size;
    profileImg.setAttribute("src", "image/user/narrido/profile/narrido.jpg"); /*replace with user url soon*/

    nameDiv.appendChild(profileImg);

    var nameText = document.createElement(textElementString);
    nameText.className = "narrido-content-owner-name";
    nameText.innerHTML = user.firstName + " " + user.lastName;

    nameDiv.appendChild(nameText);

    var userTypeSpan = document.createElement("span");
    userTypeSpan.className = "narrido-user-type narrido-" + user.type;
    userTypeSpan.innerHTML = user.type;

    nameDiv.appendChild(userTypeSpan);

    return nameDiv;
}
