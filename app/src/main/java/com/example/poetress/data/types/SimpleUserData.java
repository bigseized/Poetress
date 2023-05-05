package com.example.poetress.data.types;

import java.io.Serializable;

public class SimpleUserData implements Serializable {
    private String Name, Surname, Image_Profile, ID;

    public SimpleUserData(String name, String surname, String image_Profile, String ID) {
        Name = name;
        Surname = surname;
        Image_Profile = image_Profile;
        this.ID = ID;
    }

    public SimpleUserData() {

    }

    public String getName() {
        return Name;
    }

    public String getSurname() {
        return Surname;
    }

    public String getImage_Profile() {
        return Image_Profile;
    }

    public String getID() {
        return ID;
    }

    public void setName(String name) {
        Name = name;
    }

    public void setSurname(String surname) {
        Surname = surname;
    }

    public void setImage_Profile(String image_Profile) {
        Image_Profile = image_Profile;
    }

    public void setID(String ID) {
        this.ID = ID;
    }
}
