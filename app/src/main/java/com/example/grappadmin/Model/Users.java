package com.example.grappadmin.Model;

public class Users {
    String Email,Username;

    public Users() {
    }

    public Users(String email, String username) {
        Email = email;
        Username = username;
    }

    public String getEmail() {
        return Email;
    }

    public void setEmail(String email) {
        Email = email;
    }

    public String getUsername() {
        return Username;
    }

    public void setUsername(String username) {
        Username = username;
    }
}
