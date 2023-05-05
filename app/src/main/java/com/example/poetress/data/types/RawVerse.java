package com.example.poetress.data.types;

import java.net.URL;
import java.util.Date;

public class RawVerse {

    RawVerse () {}

    private String Name_Verse;
    private String Text_Verse;

    public RawVerse(String Name_Verse, String Text_Verse) {
        this.Name_Verse = Name_Verse;
        this.Text_Verse = Text_Verse;
    }

    public String getName_Verse() {
        return Name_Verse;
    }

    public String getText_Verse() {
        return Text_Verse;
    }

    public void setName_Verse(String name_Verse) {
        Name_Verse = name_Verse;
    }

    public void setText_Verse(String text_Verse) {
        this.Text_Verse = text_Verse;
    }

}
