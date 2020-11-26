package com.example.christopher.fchat.Models;

public class UserInformation {
    private String Email;
    private String Name;
    private String UID;

    public UserInformation(){

    }
    public UserInformation(String Email, String displayName, String CurrentUID){
        this.Email = Email;
        this.Name = displayName;
        this.UID = CurrentUID;

    }

    public String getUID() {
        return UID;
    }

    public String getEmail() {
        return Email;
    }

    public String getName() {
        return Name;
    }

    public void setEmail(String email) {
        Email = email;
    }

    public void setName(String name) {
        Name = name;
    }

    public void setUID(String currentUID) {
        UID = currentUID;
    }
}

