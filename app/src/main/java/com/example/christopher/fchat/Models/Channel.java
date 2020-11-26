package com.example.christopher.fchat.Models;

public class Channel {
    private int mImageResource;
    private String Name;
    private String Latest_message;
    private String Room_id;

    public int getmImageResource() {
        return mImageResource;
    }

    public void setmImageResource(int mImageResource) {
        this.mImageResource = mImageResource;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getLatest_message() {
        return Latest_message;
    }

    public void setLatest_message(String latest_message) {
        Latest_message = latest_message;
    }

    public String getRoom_id() {
        return Room_id;
    }

    public void setRoom_id(String room_id) {
        Room_id = room_id;
    }

    public Channel(int imageResource, String Name, String Latest_message, String Room_id) {
        mImageResource = imageResource;
        this.Name = Name;
        this.Latest_message = Latest_message;
        this.Room_id = Room_id;
    }

}