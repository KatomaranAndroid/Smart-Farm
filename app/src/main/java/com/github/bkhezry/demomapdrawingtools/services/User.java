package com.github.bkhezry.demomapdrawingtools.services;

/**
 * Created by Lincoln on 15/01/16.
 */
public class User {
    private String title;
    public int img;

    public User() {
    }

    public User(String title, int img) {
        this.title = title;
        this.img = img;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String name) {
        this.title = name;
    }

    public int getImg() {
        return img;
    }

    public void setImg(int img) {
        this.img = img;
    }
}
