package com.manuni.postacceptdeny.models;

public class PostModel {
    private String postedBy, date,  time, postImage, postId, postDescription;

    public PostModel() {

    }

    public PostModel(String postedBy, String date, String time, String postImage, String postId, String postDescription) {
        this.postedBy = postedBy;
        this.date = date;
        this.time = time;
        this.postImage = postImage;
        this.postId = postId;
        this.postDescription = postDescription;
    }

    public String getPostedBy() {
        return postedBy;
    }

    public void setPostedBy(String postedBy) {
        this.postedBy = postedBy;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getPostImage() {
        return postImage;
    }

    public void setPostImage(String postImage) {
        this.postImage = postImage;
    }

    public String getPostId() {
        return postId;
    }

    public void setPostId(String postId) {
        this.postId = postId;
    }
    public String getPostDescription(){
        return postDescription;
    }
    public void setPostDescription(String postDescription){
        this.postDescription = postDescription;
    }
}
