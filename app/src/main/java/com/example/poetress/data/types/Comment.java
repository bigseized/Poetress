package com.example.poetress.data.types;

import com.google.firebase.Timestamp;

public class Comment {
    String Text, UserId, uri;
    Timestamp Date;

    public Comment() {
    }

    public Comment(Timestamp date, String text, String userId, String uri) {
        Date = date;
        this.Text = text;
        this.UserId = userId;
        this.uri = uri;
    }

    public Timestamp getDate() {
        return Date;
    }

    public String getText() {
        return Text;
    }

    public String getUserId() {
        return UserId;
    }

    public String getUri() {
        return uri;
    }

    public void setDate(Timestamp date) {
        Date = date;
    }

    public void setText(String text) {
        this.Text = text;
    }

    public void setUserId(String userId) {
        this.UserId = userId;
    }

    public void setUri(String uri) {
        this.uri = uri;
    }
}
