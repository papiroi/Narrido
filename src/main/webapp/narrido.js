/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

//TODO: Major to do: add view pc

var loginButton = document.getElementById("login-button");
var regButton = document.getElementById("reg-button");
var loginRegister = document.querySelector('.login-register');
var registerSubmit = document.getElementById("register-form");


$(document).ready(function(event) {
    if(Cookies.get("token")) location.replace("/Narrido-1.0-SNAPSHOT");
});

var modal = $(".narrido-modal");
$(window).on("click", function(event) {
    $("#request-status-register").text("");
    if($(event.target).is(modal) || $(event.target).is($(".close"))) {
        modal.css("display", "none");
    }
});

$("#login-form").on("submit", function(event) {
    var loginData = {
        username: $("#user").val(),
        password: $("#pass").val()
    };
    
    $.ajax({
        url: "api/account/login",
        type: "POST",
        data: JSON.stringify(loginData),
        contentType: "application/json",
        success: function() {
            location.replace("/Narrido-1.0-SNAPSHOT");
        },
        error: function(xhr) {
            showModal($("<p/>").text(xhr.responseText), "Login failed")
        }
    });
    event.preventDefault();
});

if(registerSubmit){
    registerSubmit.addEventListener("submit", function(event) {
        $.ajax({
            type: "GET",
            url: "api/account/register",
            data: {code: $("#code").val()},
            dataType: "json",
            success: function(accessCode) {
                showModal(registerBody(accessCode), "Register (" + accessCode.type + ")");
            },
            error: function() {
                $("#request-status-register").text("Access Code Invalid");
            }
        });
        event.preventDefault();
    });
}

function getPcs() {
    $.ajax(
        {
            type: "GET",
            url: "api/pc/allpc",
            dataType: "json",
            success: function(result) {tabulate(result);},
            error: function() {
                alert("Invalid request!");
            }
        }
    );
}

function tabulate(result) {
    var table = $("<table></table>");
    $("#loljs").append(table);
    
    var header = $("<thead></thead>");
    var tr = $("<tr></tr>");
    
    $("<th>PC Nr.</th>").appendTo(tr);
    $("<th>Item/Description.</th>").appendTo(tr);
    $("<th>Date Acquired</th>").appendTo(tr);
    $("<th>Serial Nr.</th>").appendTo(tr);
    $("<th>Property Nr.</th>").appendTo(tr);
    $("<th>Unit Value</th>").appendTo(tr);
    $("<th>Status</th>").appendTo(tr);
    $("<th>Location</th>").appendTo(tr);
    tr.appendTo(header);
    header.appendTo(table);
    
    var tbody = $("<tbody></tbody>");
    tbody.appendTo(table);
    
    for(var i = 0; i < result.length; i++){
        var row = $("<tr></tr>");
        row.appendTo(tbody);
        
        $("<td>" + result[i].pcNumber + "</td>").appendTo(row);
        $("<td>" + result[i].pcDescription + "</td>").appendTo(row);
        $("<td>" + result[i].dateAcquired + "</td>").appendTo(row);
        $("<td>" + result[i].serialNumber + "</td>").appendTo(row);
        $("<td>" + result[i].propertyNumber + "</td>").appendTo(row);
        $("<td>" + result[i].unitValue + "</td>").appendTo(row);
        $("<td>" + result[i].status + "</td>").appendTo(row);
        $("<td>CL-01</td>").appendTo(row);
    }
}

loginButton.addEventListener("click", function() {
    if(!loginRegister.classList.contains('appear')){
        loginRegister.classList.add('appear');
    }
});

regButton.addEventListener("click", function() {
    if(loginRegister.classList.contains('appear')){
        loginRegister.classList.remove('appear');
    }
});

function showModal(body, headerText, callback) {
    if(body){
        $(".narrido-modal-body").html("").append(body);
    }
    
    if(headerText){
        $(".narrido-modal-header").find("h3").text(headerText);
    }
    modal.css("display", "block");
}

function registerBody(accessCode) {
    var fragment = document.createDocumentFragment();
    var message = $("</p>");
    message.text("Please enter your credentials.")
            .addClass("text-info")
            .appendTo(fragment);
    
    var container = $("<div/>").addClass("container").appendTo(fragment);
    
    var form = $("<form/>")
            .attr("method","post")
            .appendTo(container)
            .on("submit", function(event) {
                $("#reg-submit-status").html("Loading... <i class = 'fa fa-spinner fa-pulse'></i>");
                if($("#pass-reg").val() !== $("#pass-rep-reg").val()){
                    $("#reg-submit-status").text("Passwords do not match.");
                }else {
                    var user = {
                        idNumber: $("#stuemp-reg").val(),
                        username: $("#user-reg").val(),
                        password: $("#pass-reg").val(),
                        type: accessCode.type,
                        firstName: $("#first-reg").val(),
                        middleName: $("#middle-reg").val(),
                        lastName: $("#last-reg").val(),
                        address: "debug"
                    };
                    accessCode.user = user;
                    register(accessCode);
                }
                
                
                event.preventDefault();
            });
    
    var row = $("<div/>").addClass("form-group row").appendTo(form);
    var col4A = $("<div/>").addClass("col-md-4").appendTo(row);
    $("<input/>", {
        type: "text",
        class: "form-control",
        name: "idnumber",
        placeholder: "Student/Employee Nr.",
        id: "stuemp-reg",
        required: "true"
    }).appendTo(col4A);
    
    $("<hr/>").appendTo(form);

    var row2 = $("<div/>").addClass("form-group row").appendTo(form);
    var col4B = $("<div/>").addClass("col-md-4").appendTo(row2);
    $("<input/>", {
        type: "text",
        class: "form-control",
        name: "username",
        placeholder: "Username",
        id: "user-reg",
        required: "true"
    }).appendTo(col4B);
    
    var col4c = $("<div/>").addClass("col-md-4").appendTo(row2);
    $("<input/>", {
        type: "password",
        class: "form-control",
        name: "password",
        placeholder: "Password",
        id: "pass-reg",
        required: "true"
    }).appendTo(col4c);
    
    var col4d = $("<div/>").addClass("col-md-4").appendTo(row2);
    $("<input/>", {
        type: "password",
        class: "form-control",
        name: "password-repeat",
        placeholder: "Repeat Password",
        id: "pass-rep-reg",
        required: "true"
    }).appendTo(col4d);
    
    $("<hr/>").appendTo(form);
    
    var row3 = $("<div/>").addClass("form-group row").appendTo(form);
    var col4e = $("<div/>").addClass("col-md-4").appendTo(row3);
    $("<input/>", {
        type: "text",
        class: "form-control",
        name: "first-name",
        placeholder: "First Name",
        id: "first-reg",
        required: "true"
    }).appendTo(col4e);
    
    var col4f = $("<div/>").addClass("col-md-4").appendTo(row3);
    $("<input/>", {
        type: "text",
        class: "form-control",
        name: "middle-name",
        placeholder: "Middle Name",
        id: "middle-reg",
        required: "true"
    }).appendTo(col4f);
    
    var col4g = $("<div/>").addClass("col-md-4").appendTo(row3);
    $("<input/>", {
        type: "text",
        class: "form-control",
        name: "last-name",
        placeholder: "Last Name",
        id: "last-reg",
        required: "true"
    }).appendTo(col4g);
    
    var buttonDiv = $("<div/>").addClass("text-xs-right").appendTo(form);
    
    $("<span/>").prop("id", "reg-submit-status").appendTo(buttonDiv);
    
    $("<button/>", {
       type: "submit",
       value: "Submit",
       class: "btn btn-primary"
    }).text("Submit").appendTo(buttonDiv); //CONVOLUTED MESS
    
    $(".narrido-modal-body").children("input").on("click", function() {
        $("#reg-submit-status").removeClass("text-danger").html("");
    });
    
    return fragment;
}

function register(codeObject){
    $.ajax({
        url: "api/account/register",
        type: "POST",
        contentType: "application/json",
        dataType: "text",
        data: JSON.stringify(codeObject),
        success: function() {
            var body = $(".narrido-modal-body").html("");
            $("<h3/>").text("Registration successful!").appendTo(body);
            $("<p/>").text("Your registration has been successful. Please contact your administrator for confirmation of your registration.")
                    .appendTo(body);
            $("<button/>",{type: "button"}).text("Ok")
                    .addClass("btn btn-primary") //add some class
                    .on("click", function() {
                        $(".narrido-modal").css("display", "none");
                    }) //add event listener to button
                    .appendTo(
                        $("<div/>").addClass("text-xs-right").appendTo(body)
                    ); //UNPARALLELED CONVOLUTED MESS; creates a close button to modal following success msg
        },
        error: function(xhr) {
            $("#reg-submit-status").addClass("text-danger")
                    .text("Error processing your request: " + xhr.responseText);
        }
    });
}