package com.example.poetress.data.data_sources;

import java.util.ArrayList;

public class CategoryFeedMain {
    private ArrayList category;
    public CategoryFeedMain(){
        category = new ArrayList<>();
        category.add("Лирика");
        category.add("Гражданская лирика");
        category.add("Военная поэзия");
        category.add("Проза");
        category.add("Хоку");
    }

    public ArrayList getCategory() {
        return category;
    }
}
