package com.hyunki.aryoulearning2.model;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import java.util.ArrayList;

@Entity(tableName = "models")
public class Model {

    @PrimaryKey
    @NonNull
    private String name;

    private String image;

    private String category;

    public Model(String category, String name, String image) {
        this.category = category;
        this.name = name;
        this.image = image;
    }

    @NonNull
    public String getCategory() {
        return category;
    }

    public String getName() {
        return name;
    }

    public String getImage() {
        return image;
    }
}
