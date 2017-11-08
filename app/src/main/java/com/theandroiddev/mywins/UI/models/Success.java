package com.theandroiddev.mywins.UI.models;

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
    String title, category, description, date_started, date_ended;
    int id, importance;

    public Success(String title, String category, int importance, String description, String date_started, String date_ended) {
        this.title = title;
        this.category = category;
        this.importance = importance;
        this.description = description;
        this.date_started = date_started;
        this.date_ended = date_ended;
    }

    protected Success(Parcel in) {
        title = in.readString();
        category = in.readString();
        importance = in.readInt();
        description = in.readString();
        date_started = in.readString();
        date_ended = in.readString();
        id = in.readInt();
    }

    public Success() {

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

    public int getImportance() {
        return importance;
    }

    public void setImportance(int importance) {
        this.importance = importance;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDateStarted() {
        return date_started;
    }

    public void setDateStarted(String date) {
        this.date_started = date;
    }

    public String getDateEnded() {
        return date_ended;
    }

    public void setDateEnded(String date) {
        this.date_ended = date;
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
        parcel.writeInt(importance);
        parcel.writeString(description);
        parcel.writeString(date_started);
        parcel.writeString(date_ended);
        parcel.writeInt(id);
    }
}
