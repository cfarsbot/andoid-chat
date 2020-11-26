package com.example.christopher.fchat.Models;

public class ChatMessage {

    private String author;
    private String authorUID;
    private String timestamp;
    private String message;
public ChatMessage(){}

public ChatMessage(String author, String authorUID, String timestamp, String message){
    this.author = author;
    this.authorUID = authorUID;
    this.message = message;
    this.timestamp = timestamp;
}

    public String getAuthorUID() {
        return authorUID;
    }

    public String getAuthor() {
        return author;
    }

    public String getMessage() {
        return message;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setAuthorUID(String authorUID) {
        this.authorUID = authorUID;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }
}
