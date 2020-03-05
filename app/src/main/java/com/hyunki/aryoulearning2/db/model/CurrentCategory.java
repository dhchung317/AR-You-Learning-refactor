package com.hyunki.aryoulearning2.db.model;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "current_category")
public class CurrentCategory {

    @PrimaryKey
    @NonNull
    public String CURRENT;

    String currentCategory;

    public CurrentCategory(@NonNull String currentCategory) {
        this.CURRENT = "current";
        this.currentCategory = currentCategory;
    }

    @NonNull
    public String getCurrentCategory() {
        return currentCategory;
    }

    @NonNull
    public String getCURRENT() {
        return CURRENT;
    }

}
