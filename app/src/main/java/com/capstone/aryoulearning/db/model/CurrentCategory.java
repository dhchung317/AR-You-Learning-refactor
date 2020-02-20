package com.capstone.aryoulearning.db.model;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "current_category")
public class CurrentCategory {
    @PrimaryKey
    @NonNull
    String currentCategory;

    public CurrentCategory(@NonNull String currentCategory) {
        this.currentCategory = currentCategory;
    }

    @NonNull
    public String getCurrentCategory() {
        return currentCategory;
    }

}
