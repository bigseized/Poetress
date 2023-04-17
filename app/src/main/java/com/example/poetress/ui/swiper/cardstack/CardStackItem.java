package com.example.poetress.ui.swiper.cardstack;

public class CardStackItem {
    private String title, text;
    CardStackItem(){};

    public CardStackItem(String title, String text){
        this.title = title;
        this.text = text;
    }

    public String getTitle() {
        return title;
    }

    public String getText() {
        return text;
    }
}
