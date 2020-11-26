package com.example.christopher.fchat.Models;

public class UserProperties {

    private String ChatRoom;
    private String Admin;
    private String Member;
    private String ChatRoom_Name;
    private String Admin_Name;
    private String Member_Name;

    public UserProperties(){}

    public UserProperties(String ChatRoom, String Admin, String Member, String ChatRoom_Name, String Admin_Name, String Member_Name){
        this.ChatRoom = ChatRoom;
        this.Admin = Admin;
        this.Member = Member;
        this.ChatRoom_Name = ChatRoom_Name;
        this.Admin_Name = Admin_Name;
        this.Member_Name = Member_Name;

    }

    public String getAdmin_Name() {

        return Admin_Name;
    }

    public String getChatRoom_Name() {
        return ChatRoom_Name;
    }

    public String getMember_Name() {
        return Member_Name;
    }

    public String getChatRoom() {
        return ChatRoom;
    }

    public String getAdmin() {
        return Admin;
    }

    public String getMember() {
        return Member;
    }

    public void setChatRoom(String chatRoom) {
        ChatRoom = chatRoom;
    }

    public void setAdmin(String admin) {
        Admin = admin;
    }

    public void setMember(String member) {
        Member = member;
    }

    public void setAdmin_Name(String admin_Name) {
        Admin_Name = admin_Name;
    }

    public void setChatRoom_Name(String chatRoom_Name) {
        ChatRoom_Name = chatRoom_Name;
    }

    public void setMember_Name(String member_Name) {
        Member_Name = member_Name;
    }
}

