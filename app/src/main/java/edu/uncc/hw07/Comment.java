package edu.uncc.hw07;

import com.google.firebase.Timestamp;

public class Comment {
    String user_name;
    String comment_text;
    String user_id;
    String post_id;

    public String getPost_id() {
        return post_id;
    }

    public void setPost_id(String post_id) {
        this.post_id = post_id;
    }

    public String getComment_id() {
        return comment_id;
    }

    public void setComment_id(String comment_id) {
        this.comment_id = comment_id;
    }

    String comment_id;
    com.google.firebase.Timestamp timestamp;

    public String getUser_name() {
        return user_name;
    }

    public void setUser_name(String user_name) {
        this.user_name = user_name;
    }

    public String getComment_text() {
        return comment_text;
    }

    public void setComment_text(String comment_text) {
        this.comment_text = comment_text;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getTimestamp() {

        final String preConverted = timestamp.toString();
        final int _seconds = Integer.parseInt(preConverted.substring(18, 28)); // 1621176915
        final int _nanoseconds = Integer.parseInt(preConverted.substring(42, preConverted.lastIndexOf(')'))); // 276147000
        final com.google.firebase.Timestamp postConverted = new com.google.firebase.Timestamp(_seconds, _nanoseconds);

        String date = new java.text.SimpleDateFormat("MM/dd/yyyy HH:mm:ss").format(new java.util.Date (postConverted.getSeconds()*1000));

        return date;
    }

    public void setTimestamp(Timestamp timestamp) {
        this.timestamp = timestamp;
    }

    @Override
    public String toString() {
        return "Comment{" +
                "user_name='" + user_name + '\'' +
                ", comment_text='" + comment_text + '\'' +
                ", user_id='" + user_id + '\'' +
                ", timestamp=" + timestamp +
                '}';
    }
}
