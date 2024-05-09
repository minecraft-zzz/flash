package com.google.mlkit.vision.demo.entity;


public class User {

    private int uid;
    private String name;
    private String username;
    private String password;
    private int age;
    private String phone;


    public User() {
    }

    public User(int uid, String name, String username, String password, int age, String phone) {
        this.uid = uid;
        this.name = name;
        this.username = username;
        this.password = password;
        this.age = age;
        this.phone = phone;
    }

    public int getId() {
        return uid;
    }

    public void setId(int id) {
        this.uid = uid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }
}