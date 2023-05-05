package com.example.poetress.data.types;

import java.net.URL;
import java.util.Date;

public class ProfileVerse {

    ProfileVerse () {}

    private String Uri;
    private String Author;
    private Date Date_Verse;
    private String Ganre_Verse;
    private String Name_Verse;
    private String Text_Verse;

    public ProfileVerse(String Uri, String Author, Date Date_Verse, String Ganre_Verse, String Name_Verse, String Text_Verse) {
        this.Uri = Uri;
        this.Name_Verse = Name_Verse;
        this.Date_Verse = Date_Verse;
        this.Text_Verse = Text_Verse;
        this.Ganre_Verse = Ganre_Verse;
        this.Author = Author;
    }

    public String getName_Verse() {
        return Name_Verse;
    }

    public String getAuthor() {
        return Author;
    }

    public String getGanre_Verse() {
        return Ganre_Verse;
    }

    public String getUri() {return Uri;}

    public Date getDate_Verse() {
        return Date_Verse;
    }

    public String getText_Verse() {
        return Text_Verse;
    }

    public void setGanre_Verse(String ganre_Verse) {
        this.Ganre_Verse = ganre_Verse;
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

    public void setUri(String uri) {Uri = uri;}
}
