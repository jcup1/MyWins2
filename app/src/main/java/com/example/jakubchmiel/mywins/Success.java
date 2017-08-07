package com.example.jakubchmiel.mywins;

/**
 * Created by jakub on 07.08.17.
 */

public class Success {

    String title, category, importance, description, date;

    public Success(String title, String category, String importance, String description, String date) {
        this.title = title;
        this.category = category;
        this.importance = importance;
        this.description = description;
        this.date = date;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getImportance() {
        return importance;
    }

    public void setImportance(String importance) {
        this.importance = importance;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}
