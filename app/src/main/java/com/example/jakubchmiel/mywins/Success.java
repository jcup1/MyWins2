package com.example.jakubchmiel.mywins;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by jakub on 07.08.17.
 */

public class Success implements Parcelable {

    public static final Creator<Success> CREATOR = new Creator<Success>() {
        @Override
        public Success createFromParcel(Parcel in) {
            return new Success(in);
        }

        @Override
        public Success[] newArray(int size) {
            return new Success[size];
        }
    };
    String title, category, importance, description, date;
    int id;

    public Success(String title, String category, String importance, String description, String date) {
        this.title = title;
        this.category = category;
        this.importance = importance;
        this.description = description;
        this.date = date;
    }

    protected Success(Parcel in) {
        title = in.readString();
        category = in.readString();
        importance = in.readString();
        description = in.readString();
        date = in.readString();
        id = in.readInt();
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

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(title);
        parcel.writeString(category);
        parcel.writeString(importance);
        parcel.writeString(description);
        parcel.writeString(date);
        parcel.writeInt(id);
    }
}
