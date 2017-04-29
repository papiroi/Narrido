/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */


var jobs = [
    {
        pc:'CL2 PC08',
        technician: 'Bryan Balaga',
        report: 'Cannot Boot Windows',
        dateReported: 'August 4, 2016',
        diagnostics: 'corrupted os',
        status: 'Not working',
        application: 'For further inspection'
    },
    
    {
        pc:'CL2 PC05',
        technician: 'Arren Jakob Miranda',
        report: 'Auto safe mode and keyboard missing letters',
        dateReported: 'August 8, 2016',
        diagnostics: 'n/a',
        status: 'Not working',
        application: 'For further inspection'
    },
    
    {
        pc:'CL1 PC09',
        technician: 'Bryan Balagtas',
        report: 'avr problem',
        dateReported: 'August 15, 2016',
        diagnostics: 'Busted AVR fuse; PC09 unusable due to lack of avr',
        status: 'Working',
        application: 'Replaced AVR'
    },
    
    {
        pc:'CL4 PC09',
        technician: 'Arren Jakob Miranda',
        report: 'slow pc',
        dateReported: 'August 16, 2016',
        diagnostics: 'HDD has bad sectors',
        status: 'Working',
        application: 'Replaced HDD'
    }
    
];

/* mock JWT */
var users = [
    {
        ID: 1,
        userName: "milleth",
        passWord: "bautista",
        firstName: "Milleth",
        middleName: "M",
        lastName: "Bautista",
        page: "faculty.html",
        type: "faculty",
        groupList: [4],
        ownedGroups: [1, 2, 3]
    },
    {
        ID: 2,
        userName: "princess",
        passWord: "narrido",
        firstName: "Princess Meliza",
        middleName: "Garcia",
        lastName: "Narrido",
        page: "student.html",
        type: "student",
        groupList: [1, 2],
        ownedGroups: []
    },
    {
        ID: 3,
        userName: "bryan",
        passWord: "balaga",
        firstName: "Bryan",
        middleName: "Tiamson",
        lastName: "Balaga",
        page: "technician.html",
        type: "technician",
        groupList: [],
        ownedGroups: []
    },
    {
        ID: 4,
        userName: "jorrella",
        passWord: "apple",
        firstName: "Leodilyn",
        middleName: "Lacson",
        lastName: "Cezar",
        page: "student.html",
        type: "student",
        groupList: [1],
        ownedGroups: []
    },
    {
        ID: 5,
        userName: "aicola",
        passWord: "gardose",
        firstName: "Aicola Princess Ann",
        middleName: "Apilado",
        lastName: "Gardose",
        page: "student.html",
        type: "student",
        groupList: [1, 2, 3],
        ownedGroups: []
    }
];

var groups = [
    {
        ID: 1,
        groupName: "BSCS 3A MIS",
        owner: 1,
        description: "",
        accessCode: "BWXYZ5",
        type: "student"
    },
    {
        ID: 2,
        groupName: "BSCS 3A Thesis 1",
        owner: 1,
        description: "",
        accessCode: "BWZYZ5",
        type: "student"
    },
    {
        ID: 3,
        groupName: "COSC 200A Thesis 1",
        owner: 1,
        description: "",
        accessCode: "BWZYZ5",
        type: "student"
    },
    {
        ID: 4,
        groupName: "Department of IT",
        owner: 1,
        description: "",
        accessCode: "BWZYZ5",
        type: "faculty"
    }
];

var posts = [
    {
        id: 1,
        user: 1,
        group: 1,
        type: 'post',
        date: 'March 3, 2017 13:01',
        content: 'May exam tayo sa wednesday. Review chapters 3-4',
        attachedFiles: []
    },
    {
        id: 2,
        user: 4,
        group: 3,
        type: 'post',
        date: 'March 3, 2017 14:32',
        content: 'Mam may schedule na po next week?',
        attachedFiles: []
    },
    
    {
        id: 3,
        user: 1,
        group: 3,
        type: 'post',
        date: 'March 3, 2017 15:32',
        content: 'Please be informed na hindi ako makakapasok next meeting.',
        attachedFiles: []
    }
    
];

var postComments = [
    {
        id: 0,
        user: 4,
        post: 1,
        text: 'Wow huh?'
    },
    
    {
        id: 1,
        user: 3,
        post: 1,
        text: 'Takteng yan?! xD'
    }
];

function fullName(){
    return this.firstName + " " + this.middleName + " " + this.lastName;
}

function sortByDate(a, b){
    return new Date(b.date) - new Date(a.date);
}

function getPostsForUser(user = users[4]){
    var allGroups = user.groupList.concat(user.ownedGroups);
    var allPost = [];
    
    allGroups.forEach((groupID)=>{
    	var postArr = getPostsByGroup(groupID);
       	allPost = allPost.concat(postArr);
    });
    
    allPost.sort(sortByDate);
    return allPost;
}

function getPostsByGroup(groupID, isSort = false){
    var postsArr = [];
    
    posts.forEach((post)=>{
        if(post.group === groupID){
            postsArr.push(post);
        }
    });
    
    if(isSort) postsArr.sort(sortByDate);
    
    return postsArr;
}

function getCommentsByPost(postid){
    var commentz = [];
    
    postComments.forEach((cmt)=>{
        if(cmt.post === postid){ commentz.push(cmt);}
    });
    
    return commentz;
}

function getGroups(user, isOwner){
    var groups = [];
    
    var groupArray = isOwner ? user.ownedGroups : user.groupList;
    
    groupArray.forEach(function (id){
        groups.push(getGroup(id));
    });
    return groups;
}

function getUser(username){
    var user;
    
    users.forEach(function(u){
        if(username === u.userName){
             user = u;
             return;
        }
    });
    
    return user;
}

function getUserById(id){
    var user;
    
    users.forEach(function(u){
        if(id === u.ID){
             user = u;
             return;
        }
    });
    
    return user;
}

function getGroup(groupId){
    var group;
    
    groups.forEach(function(g){
        if(groupId === g.ID){
             group = g;
             return;
        }
    });
    
    return group;
}

function authenticate(username, password){
    var user = getUser(username);
    if(username === user.userName && password === user.passWord){
        return user;
    }
}