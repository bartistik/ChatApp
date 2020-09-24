package com.example.budgram;

public class User {

    private String name;
    private String email;
    private String id;
    private int avatarImageResource;
    private String myAvatarImageResource;
    private String pathIdUser;

    public User() {
    }

    public User(String name, String email, String id, int avatarImageResource, String myAvatarImageResource, String pathIdUser) {
        this.name = name;
        this.email = email;
        this.id = id;
        this.avatarImageResource = avatarImageResource;
        this.myAvatarImageResource = myAvatarImageResource;
        this.pathIdUser = pathIdUser;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public int getAvatarImageResource() {
        return avatarImageResource;
    }

    public void setAvatarImageResource(int avatarImageResource) {
        this.avatarImageResource = avatarImageResource;
    }

    public String getMyAvatarImageResource() {
        return myAvatarImageResource;
    }

    public void setMyAvatarImageResource(String myAvatarImageResource) {
        this.myAvatarImageResource = myAvatarImageResource;
    }

    public String getPathIdUser() {
        return pathIdUser;
    }

    public void setPathIdUser(String pathIdUser) {
        this.pathIdUser = pathIdUser;
    }
}
