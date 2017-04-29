/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

function comments(commentz){
    
    var commentzFrag = document.createDocumentFragment();
    
    commentz.forEach(function(comment) {
        var owner = getUserById(comment.user);
        
        var commentDiv = document.createElement("div");
        commentDiv.className = "narrido-comment";

        commentDiv.appendChild(nameHeader(owner, "sm"));
        
        var commentText = document.createElement("p");
        commentText.innerHTML = comment.text;
        
        commentDiv.appendChild(commentText);
        
        commentzFrag.appendChild(commentDiv);
    });
    
    var writeComment = document.createElement("div");
    writeComment.className = "narrido-comment-write";
    
    var commentInput = document.createElement("textarea");
    commentInput.className = "narrido-input-text";
    commentInput.setAttribute("placeholder", "Write a comment...");
    
    writeComment.appendChild(commentInput);
    
    var commentButton = document.createElement("button");
    commentButton.setAttribute("type", "button");
    commentButton.setAttribute("value", "Send");
    commentButton.innerHTML = "Send";
    
    writeComment.appendChild(commentButton);
    
    commentzFrag.appendChild(writeComment);
    
    return commentzFrag;
}

function feedItem(post, isNewsFeed = false){
    var feedDiv = document.createElement("div");
    var owner = post.user;
    
    feedDiv.className = "narrido-feed-item";
    
    var feedItemHeader = document.createElement("div");
    feedItemHeader.className = "narrido-feed-item-header";
    var feedHeaderName = nameHeader(owner, "md");
    feedItemHeader.appendChild(feedHeaderName);
    
    var feedItemDate = document.createElement("span");
    var date = new Date(post.date);
    feedItemDate.innerHTML = date.toDateString() + " " + date.toLocaleTimeString();
    feedHeaderName.appendChild(feedItemDate);
    feedDiv.appendChild(feedItemHeader);
    
    var feedContent = document.createElement("div");
    feedContent.className = "narrido-feed-content";
    feedDiv.appendChild(feedContent);
    
    var feedContentText = document.createElement("p");
    feedContentText.className = "narrido-feed-text";
    feedContentText.innerHTML = post.postContent;
    feedContent.appendChild(feedContentText);
    
    if(post.files){
        var feedContentFile = document.createElement("div");
        feedContentFile.className = "narrido-feed-file";
        
        var fileLink = document.createElement("a");
        fileLink.setAttribute("href", post.files.fileUrl);
        fileLink.innerHTML = post.files.fileName; /*TODO add file size*/
        feedContentFile.appendChild(fileLink);
        feedContent.appendChild(feedContentFile);
    }
    
    //TODO: replace this with AJAX call
    //TODO: uncomment until such time reliable comment retrieval is in place
//    var commentz = post.comments;
//    
//    var commentzShow = document.createElement("a");
//    commentzShow.setAttribute("href", "#");
//    commentzShow.innerHTML = "Comments(" + commentz.length + ")";
//    commentzShow.addEventListener("click", function(e) {
//        e.preventDefault();
//        var commentDoc = document.getElementById("comments-post-" + post.postId + (isNewsFeed ? "-news-feed" : ""));
//        
//        if(commentDoc.innerHTML === ""){
//            commentDoc.appendChild(comments(commentz));
//        } else {
//            commentDoc.innerHTML = "";
//        }
//        
//    });
//    
//    feedDiv.appendChild(commentzShow);
//    
//    var commentzDiv = document.createElement("div");
//    commentzDiv.className = "narrido-feed-comments";
//    commentzDiv.id = "comments-post-" + post.postId + (isNewsFeed ? "-news-feed" : "");
//    
//    feedDiv.appendChild(commentzDiv);
    feedDiv.appendChild(document.createElement("hr"));
    return feedDiv;
}

function newsFeed(user){
    //submit 
    
//    var posts = getPostsForUser(user);
//    
//    var fragment = document.createDocumentFragment();
//    
//    posts.forEach((post)=>{
//        fragment.appendChild(feedItem(post, true));
//    })
//    
//    return fragment;
}