package com.github.bkhezry.demomapdrawingtools;

public class Employee {

    private String name;
    private String lastName;
    private int img;
    private int id;

    public Employee(String name, String lastName, int id,int img) {
        this.name = name;
        this.lastName = lastName;
        this.id = id;
        this.img=img;
    }

    public int getImg() {
        return img;
    }

    public void setImg(int img) {
        this.img = img;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getlastName() {
        return lastName;
    }

    public void setlastName(String lastName) {
        this.lastName = lastName;
    }
}