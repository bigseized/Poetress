package com.example.poetress.data.types;

import java.sql.Timestamp;

public class Message {
    String senderID, receiverId, messageText;
    com.google.firebase.Timestamp date;

    public Message(String senderID, String receiverId, String messageText, com.google.firebase.Timestamp date) {
        this.senderID = senderID;
        this.receiverId = receiverId;
        this.messageText = messageText;
        this.date = date;
    }

    public Message() {

    }

    public String getSenderID() {
        return senderID;
    }

    public String getReceiverId() {
        return receiverId;
    }

    public String getMessageText() {
        return messageText;
    }

    public com.google.firebase.Timestamp getDate() {
        return date;
    }

    public void setSenderID(String senderID) {
        this.senderID = senderID;
    }

    public void setReceiverId(String receiverId) {
        this.receiverId = receiverId;
    }

    public void setMessageText(String messageText) {
        this.messageText = messageText;
    }

    public void setDate(com.google.firebase.Timestamp date) {
        this.date = date;
    }
}
