package com.example.chattest;

public class MessageList {
    private String  name , phone , profilepic , lastMessage , chatKey;
    private int unSeenMessage;

    public MessageList(String name, String phone, String profilepic, String lastMessage, int unSeenMessage , String chatKey) {
        this.name = name;
        this.phone = phone;
        this.profilepic = profilepic;
        this.lastMessage = lastMessage;
        this.unSeenMessage = unSeenMessage;
        this.chatKey=chatKey;
    }

    public MessageList() {
    }

    public String getChatKey() {
        return chatKey;
    }

    public String getProfilepic() {
        return profilepic;
    }

    public String getName() {
        return name;
    }

    public String getPhone() {
        return phone;
    }

    public String getLastMessage() {
        return lastMessage;
    }

    public int getUnSeenMessage() {
        return unSeenMessage;
    }
}
