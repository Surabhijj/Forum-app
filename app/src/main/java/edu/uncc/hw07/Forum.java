package edu.uncc.hw07;

import com.google.firebase.Timestamp;

import java.io.Serializable;
import java.lang.reflect.Array;
import java.util.ArrayList;

public class Forum implements Serializable {
    String forum_title;
    String forum_description;
    String user_id;
    String user_name;

    public String getPost_id() {
        return post_id;
    }

    public void setPost_id(String post_id) {
        this.post_id = post_id;
    }

    String post_id;
    ArrayList<String> likes;
    Timestamp timestamp;

    public String getForum_title() {
        return forum_title;
    }

    public void setForum_title(String forum_title) {
        this.forum_title = forum_title;
    }

    public String getForum_description() {
        return forum_description;
    }

    public void setForum_description(String forum_description) {
        this.forum_description = forum_description;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getUser_name() {
        return user_name;
    }

    public void setUser_name(String user_name) {
        this.user_name = user_name;
    }

    public ArrayList<String> getLikes() {
        return likes;
    }

    public void setLikes(ArrayList<String> likes) {
        this.likes = likes;
    }

    public String getTimestamp() {
        final String preConverted = timestamp.toString();
        final int _seconds = Integer.parseInt(preConverted.substring(18, 28)); // 1621176915
        final int _nanoseconds = Integer.parseInt(preConverted.substring(42, preConverted.lastIndexOf(')'))); // 276147000
        final Timestamp postConverted = new Timestamp(_seconds, _nanoseconds);

        String date = new java.text.SimpleDateFormat("MM/dd/yyyy HH:mm:ss").format(new java.util.Date (postConverted.getSeconds()*1000));

        return date;
    }

    public void setTimestamp(Timestamp timestamp) {
        this.timestamp = timestamp;
    }

    @Override
    public String toString() {
        return "Forum{" +
                "forum_title='" + forum_title + '\'' +
                ", forum_description='" + forum_description + '\'' +
                ", user_id='" + user_id + '\'' +
                ", user_name='" + user_name + '\'' +
                ", likes=" + likes +
                ", timestamp=" + timestamp +
                '}';
    }
}
