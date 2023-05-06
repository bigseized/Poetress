package com.example.poetress.data.data_sources;

import java.util.ArrayList;

public class CategoryFeedMain {
    private ArrayList<String> category;
    public CategoryFeedMain(){
        category = new ArrayList<>();
        category.add("Лирика");
        category.add("Любовная лирика");
        category.add("Гражданская лирика");
        category.add("Пейзажная лирика");
        category.add("Военная поэзия");
        category.add("Басня");
        category.add("Сатира");
        category.add("Проза");
        category.add("Хокку");
        category.add("Песня");
        category.add("Рэп");
        category.add("Для детей");
        category.add("Без категории");
    }

    public ArrayList<String> getCategory() {
        return category;
    }
}
