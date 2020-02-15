package com.capstone.aryoulearning.model;

import java.util.ArrayList;

public final class ModelResponse {
    private int error = 1;
    private String category;
    private String background;
    private ArrayList<Model> list;

    public String getBackground() {
        return background;
    }

    public String getCategory() {
        return category;
    }

    public ModelResponse(ArrayList<Model> list) {
        this.list = list;
    }

    public ArrayList<Model> getList() {
        return list;
    }

    public void setError(int error) {
        this.error = error;
    }

    public int getError() {
        return error;
    }
}
