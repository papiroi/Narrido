/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 * This group is invoked by the group module callback.
 * @returns {undefined}
 */
function showGroups() {
    var contentArea = document.getElementById("groups");
    var isOwning = userObj.type === "faculty" || userObj.type === "dept-head" || userObj.type === "mis-officer";
    //contentArea.appendChild(showGroupSearch());
    
    var groupListMainContainer = document.createElement("div");
    groupListMainContainer.classList.add("narrido-group-main-container");
    
    var ownedGroups;
    var joinedGroups;

    var joinAjax = $.ajax({
        type: "GET",
        url: "/Narrido-1.0-SNAPSHOT/api/groups/user/joined"
    });

    var ownAjax = $.ajax({
        type: "GET",
        url: "/Narrido-1.0-SNAPSHOT/api/groups/user/owned"
    });
 
    
    $.when(joinAjax, ownAjax).done(function(ja, ow) {
        
        if(ja[0]) joinedGroups = ja[0];
        if(ow[0]) ownedGroups = ow[0];
        
        groupListMainContainer.appendChild(showGroupItems(joinedGroups, false));
        if(isOwning) groupListMainContainer.appendChild(showGroupItems(ownedGroups, true));

        contentArea.appendChild(groupListMainContainer);
        if(isOwning) showGroupNav();
    });
}

function showGroupPage(gp) {
    alert(gp.groupName);
}

function showGroupNav(){
    var titleContainer = document.getElementById("narrido-header-groups");
    
    var titleNav = document.createElement("ul");
    titleNav.className = "narrido-group-nav";
    
    var links = [
        {
            text: "Groups I Join",
            linkName: "narrido-group-join-container",
            clazz: "groups-link"
        },
        {
            text: "Groups I Manage",
            linkName: "narrido-group-manage-container",
            clazz: "groups-link"
        }
    ];
    links.forEach(function(link) {
        var titleNavItem = document.createElement("li");
        var titleNavItemLink = document.createElement("a");
        titleNavItemLink.setAttribute("href", "#");
        titleNavItemLink.innerHTML = link.text;
        titleNavItemLink.addEventListener("click", function(event) {
            showPane(event, link.linkName, "narrido-group-item-pane", link.clazz);
        });
        titleNavItemLink.classList.add(link.clazz);
        titleNavItem.appendChild(titleNavItemLink);
        titleNav.appendChild(titleNavItem);
    });
    titleContainer.appendChild(titleNav);
}

function showGroupItems(groups, isOwning){
    var groupListContainer = document.createElement("div");
    groupListContainer.setAttribute(
        "id",
        isOwning ? "narrido-group-manage-container" : "narrido-group-join-container"
    );
    groupListContainer.classList.add("narrido-group-item-pane");
    
    //isOwning means the owned groups
    groupListContainer.style.display = isOwning ? "none" : "block";
    
    var groupFrag = document.createDocumentFragment();
    
    if(groups) {
        groups.forEach(function(gp) {
            var owner = gp.owner;

            var groupItem = document.createElement("div");
            groupItem.className = "narrido-group-item";
            groupItem.addEventListener("click", function(event) {openGroupPage(gp, event);});

            var groupIcon = document.createElement("div");
            groupIcon.className = "narrido-group-icon narrido-" + gp.type;
            groupIcon.innerHTML = "<p>"+gp.groupName.substring(0, 1)+"</p>";
            groupItem.appendChild(groupIcon);

            var groupDetails = document.createElement("div");
            groupDetails.className = "narrido-group-item-details";

            var groupTitle = document.createElement("div");
            groupTitle.className = "narrido-group-item-title";

            var groupH4 = document.createElement("h4");
            groupH4.innerHTML = gp.groupName;

            var groupType = document.createElement("span");
            groupType.className = "narrido-group-type narrido-" + gp.type;
            groupType.innerHTML = gp.type;

            groupTitle.appendChild(groupH4);
            groupTitle.innerHTML += "&nbsp";
            groupTitle.appendChild(groupType);
            groupDetails.appendChild(groupTitle);

            var groupBody = document.createElement("div");
            groupBody.className = "narrido-group-item-body";
            groupBody.innerHTML = "<p>Owner: " + owner.firstName + " " + owner.middleName + " " + owner.lastName +"</p>";
            groupDetails.appendChild(groupBody);
            groupItem.appendChild(groupDetails);

            groupFrag.appendChild(groupItem);
        });
    }
    
    groupFrag.appendChild(showGroupJoinButton(isOwning));
    
    groupListContainer.appendChild(groupFrag);
    return groupListContainer;
}

function showGroupJoinButton(isOwning){
    /*add group option*/
    var groupAddItem = document.createElement("div");
    groupAddItem.className = "narrido-group-item";
    
    var groupAddIcon = document.createElement("div");
    groupAddIcon.className = "narrido-group-icon";
    var groupPlusSign = document.createElement("p");
    groupPlusSign.innerHTML = "+";
    groupAddIcon.appendChild(groupPlusSign);
    groupAddItem.appendChild(groupAddIcon);
    
    var groupAddDetails = document.createElement("div");
    groupAddDetails.className = "narrido-group-item-details";
    
    var groupAddTitle = document.createElement("div");
    groupAddTitle.className = "narrido-group-item-title";
    groupAddTitle.innerHTML = isOwning ? "<h3>Create a Group</h3>" : "<h3>Join a Group</h3>";
    
    groupAddDetails.appendChild(groupAddTitle);
    groupAddItem.appendChild(groupAddDetails);
    
    //jQuery crap starts here
    $(groupAddItem).on("click", function() {
        showModal(isOwning ? createGroupBody() : joinGroupBody(), isOwning ? "Create a Group" : "Join a Group");
    });
    
    return groupAddItem;
}

function showGroupSearch(){
    var searchBar = document.createElement("div");
    searchBar.className = "narrido-group-search";
    
    var inputText = document.createElement("input");
    inputText.setAttribute("type", "text");
    inputText.setAttribute("placeholder", "Search group...");
    
    var searchSpan =  document.createElement("span");
    var searchButton = document.createElement("button");
    searchButton.setAttribute("type", "button");
    searchButton.setAttribute("value", "Search");
    searchButton.innerHTML = "Search";
    searchSpan.appendChild(searchButton);
    searchBar.appendChild(inputText);
    searchBar.innerHTML += "&nbsp";
    searchBar.appendChild(searchSpan);
    
    return searchBar;
}

function groupPageNav(group){
    
    var isOwner = userObj.userid === group.owner.userid;
    
    var pageNav = document.createElement("ul");
    pageNav.classList.add("narrido-group-nav", "narrido-group-page-title");
    
    var linkz = [
        {
            text: "Discussion",
            theLink: "group-page-feed-" + group.groupId,
            clazz: "narrido-group-page-link"
        },
        {
            text: "Members",
            theLink: "group-page-members-"  + group.groupId,
            clazz: "narrido-group-page-link"
        },
        {
            text: "Files",
            theLink: "group-page-files-" + group.groupId,
            clazz: "narrido-group-page-link"
        },
        {
            text: "Access Code",
            theLink: "group-page-access-codes-" + group.groupId,
            clazz: "narrido-group-page-link",
            ownersOnly: "ownersOnly"
        }
    ];
    
    linkz.forEach(function(link) {
        var listItem = document.createElement("li");
        var linkElem = document.createElement("a");
        linkElem.textContent = link.text;
        linkElem.setAttribute("href", "#");
        linkElem.classList.add(link.clazz);
        linkElem.addEventListener("click", function(event) {
            showPane(event, link.theLink, "narrido-group-page-pane", link.clazz);
        });
        
        if(!link.ownersOnly) {
            listItem.appendChild(linkElem);
            pageNav.appendChild(listItem);
        }
        
        if(link.ownersOnly && isOwner) {
            listItem.appendChild(linkElem);
            pageNav.appendChild(listItem);
        }
    });
    
    return pageNav;
}

function showGroupPage(){
    var groupPageContainer = document.getElementById("group-page");
    
    var header = document.getElementById("narrido-header-group-page");
    var span = document.createElement("span");
    span.className = "narrido-group-type";
    span.id = "narrido-group-type-page";
    span.textContent = "something";
    header.appendChild(span);

    var title = document.getElementById("narrido-content-title-group-page");
    title.textContent = "Something Something";

    var pageContent = document.createElement("div");
    pageContent.className = "narrido-group-page-content";
    groupPageContainer.appendChild(pageContent);
}

function showGroupTabs(group){
    var isOwner = userObj.userid === group.owner.userid;
    
    var pageContent = document.querySelector(".narrido-group-page-content");
    
    pageContent.innerHTML = "";

    pageContent.appendChild(groupPageNav(group));

    var panes = [
        {
            title: "group-page-feed-",
            content: groupFeedBody(group)
        },
        {
            title: "group-page-members-"
        },
        {
            title: "group-page-files-"
        },
        {
            title: "group-page-access-codes-",
            content: groupAccessCodesBody(group),
            ownersOnly: "ownersOnly"
        }
    ];
    panes.forEach(function(pane) {
        var groupPane = document.createElement("div");
        
        groupPane.className = "narrido-group-page-pane";
        groupPane.style.display = "none";
        groupPane.id = pane.title + group.groupId;
        
        if(pane.content) groupPane.appendChild(pane.content);
        
        if(!pane.ownersOnly) pageContent.appendChild(groupPane);
        
        if(pane.ownersOnly && isOwner) pageContent.appendChild(groupPane); 
    });
    
    showGroupFeed(group);
    showMembers(group);
    showGroupFiles(group);
    if(isOwner) showAccessCodes(group);
}

function openGroupPage(group, event){

    var title = document.getElementById("narrido-content-title-group-page");
    title.textContent = group.groupName;

    var span = document.getElementById("narrido-group-type-page");
    span.className = "narrido-group-type narrido-" + group.type;
    span.textContent = group.type;

    showGroupTabs(group);
    
    showPane(event, "group-page", "narrido-tab-pane", "narrido-main-link");
    
}

//CAVEAT PROGRAMMER
//jQuery crap begins here
function joinGroupBody() {
    var frag = document.createDocumentFragment();
    
    var form = $("<form/>", {id: "form-group-join"}).attr("method", "post")
            .appendTo(frag)
            .on("submit", function(event) {
                event.preventDefault();
                $.ajax({
                    type: "POST",
                    url: "/Narrido-1.0-SNAPSHOT/api/groups/join",
                    data: $(this).serialize(),
                    success: function(data) {
                        $(".narrido-modal-body").empty().html(data);
                    },
                    error: function(xhr) {
                        $(".narrido-modal-body").empty().html(xhr.responseText);
                    }
                });
            });
    
    $("<input/>", {
        type: "text",
        class: "form-control",
        placeholder: "Access Code",
        required: "true",
        id: "access-code",
        name: "accessCode"
    }).appendTo(form);
    
    $("<button/>", {
        type: "submit",
        value: "Join",
        class: "btn btn-primary",
        id: "join-button"
    }).html("Submit Code").appendTo(form);
    
    return frag;
}

function createGroupBody() {
    var frag = document.createDocumentFragment();
    
    var form = $("<form/>").attr("method", "post")
            .appendTo(frag)
            .on("submit", function(event) {
                event.preventDefault();
                $.ajax({
                    type: "POST",
                    url: "/Narrido-1.0-SNAPSHOT/api/groups/",
                    data: $(this).serialize(),
                    success: function(data) {
                        $(".narrido-modal-body").empty().html(data);
                    },
                    error: function(xhr) {
                        $(".narrido-modal-body").empty().html(xhr.responseText);
                    }
                });
            });
    
    $("<input/>", {
        type: "text",
        class: "form-control",
        placeholder: "Group Title",
        required: "true",
        id: "group-title",
        name: "groupTitle"
    }).appendTo(form);
    
    var typeCombo = $("<select/>", {
        name: "groupType",
        class: "form-control"
    }).appendTo(form);

    var studentCondition = userObj.type === "dept-head" || userObj.type === "faculty";
    
    if(studentCondition) {
        $("<option/>", {
            value: "student"
        }).html("Student").appendTo(typeCombo);
    }
    
    if(userObj.type === "dept-head") {
        $("<option/>", {
            value: "faculty"
        }).html("Faculty").appendTo(typeCombo);
    }
    
    if(userObj.type === "mis-officer") {
        $("<option/>", {
            value: "it-staff"
        }).html("IT Staff").appendTo(typeCombo);

        $("<option/>", {
            value: "dept-head"
        }).html("Dept. Head").appendTo(typeCombo);
    }

    $("<button/>", {
        type: "submit",
        value: "createGroup",
        class: "btn btn-primary form-control",
        id: "create-button"
    }).html("Create Group").appendTo(form);
    
    return frag;
}

function showMembers(group) {
    $.ajax({
        type: "GET",
        url: "/Narrido-1.0-SNAPSHOT/api/groups/members/" + group.groupId,
        dataType: "json",
        success: function (membership) {
            $("#group-page-members-" + group.groupId)
                    .empty()
                    .append(elementifyMembers(membership, group));
        }
    });
}

function elementifyMembers(membership, group) {
    var frag = document.createDocumentFragment();
    $("<h3/>").text("Owner").appendTo(frag);

    $("<div/>", {class: "member-box"}).append(
        $(nameHeader(group.owner, "md")).appendTo(frag)
    ).appendTo(frag);

    $("<h3/>").text("Members").appendTo(frag);
    var membersBox = $("<div/>", {class: "member-box"}).appendTo(frag);

    membership.forEach(function(mem) {
        if(mem.confirmed) {
            $(nameHeader(mem.user, "md")).appendTo(membersBox);
        }
    });

    $("<h3/>").text("Pending Membership").appendTo(frag);
    var pendingBox = $("<div/>", {class: "member-box"}).appendTo(frag);
    membership.forEach(function(mem) {
        if(!mem.confirmed) {
            var userItem = $(nameHeader(mem.user, "md"), {id: "user-item-" + mem.user.userid})
                    .appendTo(pendingBox);
            
            var isOwner = userObj.userid === group.owner.userid;
            
            if(isOwner) {
                $("<button/>", {
                    type: "button",
                    class: "btn btn-success"
                }).text("Confirm")
                        .on("click", function() {
                            $.ajax({
                                type: "PUT",
                                url: "/Narrido-1.0-SNAPSHOT/api/groups/members/accept",
                                data: JSON.stringify({membershipId: mem.membershipId}),
                                contentType: "application/json",
                                success: function(response) {
                                    var body = document.createDocumentFragment();
                                    $("<h3/>").text("Operation success").appendTo(body);
                                    $("<p/>").text(response).appendTo(body);
                                    showModal(body, "Accept User");
                                    showMembers(group);
                                },
                                error: function(xhr) {
                                    showModal($("<p/>").text(xhr.responseText), "Accept User");
                                }
                            });
                        })
                        .appendTo(userItem);

                $("<button/>", {
                    type: "button",
                    class: "btn btn-danger"
                }).text("Reject").on("click", function() {
                    $.ajax({
                        type: "PUT",
                        url: "/Narrido-1.0-SNAPSHOT/api/groups/members/reject",
                        data: JSON.stringify({membershipId: mem.membershipId}),
                        contentType: "application/json",
                        success: function(response) {
                            var body = document.createDocumentFragment();
                            $("<h3/>").text("Operation success").appendTo(body);
                            $("<p/>").text(response).appendTo(body);
                            showModal(body, "Reject User");
                            showMembers(group);
                        },
                        error: function(xhr) {
                            showModal($("<p/>").text(xhr.responseText), "Reject User");
                        }
                    });
                }).appendTo(userItem);
            }
        }
    });
    
    return frag;
}

function groupFeedBody(group) {
    var frag = document.createDocumentFragment();
    var form = $("<form/>", {method: "post", enctype: "multipart/form-data"})
            .on("submit", function(event) {
                event.preventDefault();
                var form = $(this)[0];
                var data = new FormData(form);
                $("#submit-post").prop("disabled", true);
                showModal($("<p/>").text("Submitting post..."), "Submit Post");
                
                $.ajax({
                    type: "POST",
                    enctype: "multipart/form-data",
                    url: "/Narrido-1.0-SNAPSHOT/api/groups/posts/" + group.groupId,
                    data: data,
                    processData: false,
                    contentType: false,
                    success: function(response) {
                        $(".narrido-modal-body").empty().text("Post submitted successfully!");
                        console.log(response);
                        $("#submit-post").prop("disabled", false);
                    },
                    error: function(xhr) {
                        $(".narrido-modal-body").empty().text("Post submit failed! Cause: " + xhr.responseText);
                        console.log(xhr.responseText);
                        $("#submit-post").prop("disabled", false);
                    }
                });
            })
            .appendTo(frag);
    
    $("<textarea/>", {
        name: "postContent",
        class: "form-control",
        placeholder: "Write a post...",
        required: "true"
    }).appendTo(form);
    
    $("<input/>", {
        type: "file",
        class: "form-control",
        name: "file"
    }).appendTo(form);
    
    $("<button/>", {
        id: "submit-post",
        type: "submit",
        class: "btn btn-primary form-control"
    }).text("Post").appendTo(form);
    
    return frag;
}

function groupAccessCodesBody(group) {
    var frag = document.createDocumentFragment();
    
    if(group.type === "student") return frag;
    
    var form = $("<form/>", {method: "post", class: "form-inline"})
            .on("submit", function(event) {
                event.preventDefault();
                $.ajax({
                    type: "POST",
                    url: "/Narrido-1.0-SNAPSHOT/api/groups/codes/" + group.groupId,
                    data: $(this).serialize(),
                    success: function(data) {
                        showModal($("<p/>").text(data), "Create Access Code");
                    },
                    error: function(xhr) {
                        showModal($("<p/>").text(xhr.responseText), "Create Access Code");
                    }
                });
            })
            .appendTo(frag);
    
    var typeCombo = $("<select/>", {
        name: "type",
        class: "mb-2"
    }).appendTo(form);
    
    if(group.type === "faculty") {
        $("<option/>",{value: "faculty"}).html("faculty").appendTo(typeCombo);
    }
    
    if(group.type === "it-staff") {
        $("<option/>",{value: "it-staff"}).html("IT Staff").appendTo(typeCombo);
        $("<option/>",{value: "property-supply"}).html("Property & Supply").appendTo(typeCombo);
    }
    
    if(group.type === "dept-head") {
        $("<option/>",{value: "dept-head"}).html("Department Head").appendTo(typeCombo);
    }
    
    $("<button/>", {
        type: "submit",
        class: "mb-2",
        class: "btn btn-primary"
    }).text("Create Code").appendTo(form);
    
    return frag;
}

function showGroupFeed(group) {
    $.ajax({
        type: "GET",
        url: "/Narrido-1.0-SNAPSHOT/api/groups/posts/" + group.groupId,
        dataType: "json",
        success: function(posts) {
            $("#group-page-feed-" + group.groupId)
                    .append(elementifyPosts(posts, group.groupId));
        }
    });
}

function elementifyPosts(posts, groupId) {
    var frag = $("<div/>", {id: "group-page-feed-body-" + groupId});
    posts.forEach(function(post) {
       $(feedItem(post)).appendTo(frag); 
    });
    return frag;
}

function showAccessCodes(group) {
    $.ajax({
        type: "GET",
        url: "/Narrido-1.0-SNAPSHOT/api/groups/codes/" + group.groupId,
        dataType: "json",
        success: function (codes) {
            $("#group-page-access-codes-" + group.groupId)
                    .append(elementifyCodes(codes));
        }
    });
}

function elementifyCodes(codes) {
    var frag = document.createDocumentFragment();
    
    var table = $("<table/>", {class: "table"}).appendTo(frag);
    var thead = $("<thead/>").appendTo(table);
    var theadrow = $("<tr/>").appendTo(thead);
    
    $("<th/>").text("Access Code").appendTo(theadrow);
    $("<th/>").text("Type").appendTo(theadrow);
    $("<th/>").text("User").appendTo(theadrow);
    
    var tbody = $("<tbody/>").appendTo(table);
    codes.forEach(function(code) {
        var bodyrow = $("<tr/>").appendTo(tbody);
        $("<th/>", {scope: "row"}).text(code.accessCode).appendTo(bodyrow);
        $("<td/>").append(
                $("<span/>", {class: "narrido-code-type narrido-" + code.type}).text(code.type)
            ).appendTo(bodyrow);
        $("<td/>").text(code.user ? (code.user.firstName + " " + code.user.lastName) : "None").appendTo(bodyrow);
        
//        var actionCell = $("<td/>", {class: "narrido-action"}).appendTo(bodyrow);
//        if(code.type !== "student") {
//            $("<button/>", {
//                type: "button",
//                class: "btn btn-success"
//            }).text("Confirm").appendTo(actionCell);
//
//            $("<button/>", {
//                type: "button",
//                class: "btn btn-danger"
//            }).text("Reject").appendTo(actionCell);
//        }
    });
    
    return frag;
}

function showGroupFiles(group) {
    $.ajax({
        type: "GET",
        url: "/Narrido-1.0-SNAPSHOT/api/groups/files/" + group.groupId,
        dataType: "json",
        success: function (files) {
            $("#group-page-files-" + group.groupId)
                    .append(elementifyFiles(files, group));
        }
    });
}

function elementifyFiles(files, group) {
    var container = $("<div/>", {class: "container", id: "group-page-files-container-" + group.groupId});
    
    files.forEach(function(file) {
                 var column = $("<div/>", {class: "col-xs-12 col-sm-4 col-md-3"}).appendTo(container);
                 $("<div/>", {class: "narrido-file"})
                    .append($("<a/>", {href: file.fileUrl}).text(file.fileName))
                    .append($("<span/>", {class: "close"}).html("&times;"))
                    .appendTo(column);
            });
    
    return container;
}