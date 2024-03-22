package io.proglib;

import java.util.ArrayList;
import java.util.List;

public class User {
    private String username;
    private List<Post> posts;

    public User() {
        posts = new ArrayList<>();
    }

    public User(String username, List<Post> posts) {
        this.username = username;
        this.posts = posts == null ? new ArrayList<>() : posts;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public List<Post> getPosts() {
        return posts;
    }

    public void setPosts(List<Post> posts) {
        this.posts = posts;
    }

}
