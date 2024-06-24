package com.hagitc.myfirstapplication;

public class User {
    private int  wins;
    private int losts;
    private String name;
    private String email;
    public User(int wins, int losts, String name, String email)
    {
        this.wins = wins;
        this.losts = losts;
        this.name = name;
        this.email = email;
    }

    public User() {
        this.wins = 0;
        this.losts = 0;
        this.name="";
        this.email="";
    }

    public int getWins() {
        return wins;
    }

    public void setWins(int wins) {
        this.wins = wins;
    }

    public int getLosts() {
        return losts;
    }

    public void setLosts(int losts) {
        this.losts = losts;
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
}
