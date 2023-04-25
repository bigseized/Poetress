package com.example.poetress.data.data_sources;

import android.net.Uri;

import java.util.HashMap;
import java.util.Map;

public class UserMainData {
        public UserMainData(String Image_Profile, String name, String surname, String interests) {
                this.Image_Profile = Image_Profile;
                this.Name = name;
                this.Surname = surname;
                this.Interests = interests;
        }
        public UserMainData(Uri Image_Profile, String name, String surname, String interests) {
                this.Image_Profile = Image_Profile.toString();
                this.Name = name;
                this.Surname = surname;
                this.Interests = interests;
        }
        public UserMainData(){};

        private String Image_Profile;
        private String Interests;
        private String Name, Surname;



        public String getImage_Profile() {
                return Image_Profile;
        }



        public Map<String, Object> getHashMap(String downloadURL){
                Map<String, Object> data = new HashMap<>();
                data.put("Image_Profile", downloadURL);
                data.put("Name", Name);
                data.put("Surname", Surname);
                data.put("Interests", Interests);
                return data;
        }
        public String getName() {
                return Name;
        }

        public String getSurname() {
                return Surname;
        }

        public String getInterests() {
                return Interests;
        }

        public void setImage_Profile(String image_Profile) {
                this.Image_Profile = image_Profile;
        }

        public void setName(String name) {
                this.Name = name;
        }

        public void setSurname(String surname) {
                this.Surname = surname;
        }

        public void setInterests(String interests) {
                this.Interests = interests;
        }
}
