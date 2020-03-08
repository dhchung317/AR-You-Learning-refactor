package com.hyunki.aryoulearning2.db.model;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "model_info")
public class ModelInfo {

    @PrimaryKey
    @NonNull
    private String name;

    private String category;

    private String image;

    public ModelInfo(String category, String name, String image) {
        this.category = category;
        this.name = name;
        this.image = image;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

}
