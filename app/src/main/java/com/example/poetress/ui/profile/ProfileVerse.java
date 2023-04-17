package com.example.poetress.ui.profile;

import java.util.Date;

public class ProfileVerse {

    ProfileVerse () {}

    private String Author;
    private Date Date_Verse;
    private String Genre_Verse;
    private String Name_Verse;
    private String Text_Verse;

    public ProfileVerse(String Author, Date Date_Verse, String Genre_Verse, String Name_Verse, String Text_Verse) {
        this.Name_Verse = Name_Verse;
        this.Date_Verse = Date_Verse;
        this.Text_Verse = Text_Verse;
        this.Genre_Verse = Genre_Verse;
        this.Author = Author;
    }

    public String getName_Verse() {
        return Name_Verse;
    }

    public String getAuthor() {
        return Author;
    }

    public String getGenre_Verse() {
        return Genre_Verse;
    }
    

    public Date getDate_Verse() {
        return Date_Verse;
    }

    public String getText_Verse() {
        return Text_Verse;
    }

    public void setGenre_Verse(String genre_Verse) {
        this.Genre_Verse = genre_Verse;
    }

    public void setAuthor(String author) {
        this.Author = author;
    }

    public void setName_Verse(String name_Verse) {
        Name_Verse = name_Verse;
    }

    public void setDate_Verse(Date date_Verse) {
        this.Date_Verse = date_Verse;
    }

    public void setText_Verse(String text_Verse) {
        this.Text_Verse = text_Verse;
    }
}
