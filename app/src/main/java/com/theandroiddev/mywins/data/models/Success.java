package com.theandroiddev.mywins.data.models;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.PrimaryKey;
import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.NonNull;

import java.util.UUID;

/**
 * Created by jakub on 07.08.17.
 */
@Entity
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
    @PrimaryKey
    @NonNull
    @ColumnInfo(name = "id")
    private String id;
    @ColumnInfo(name = "title")
    private
    String title;
    @ColumnInfo(name = "category")
    private
    String category;
    @ColumnInfo(name = "description")
    private
    String description;
    @ColumnInfo(name = "dateStarted")
    private
    String dateStarted;
    @ColumnInfo(name = "dateEnded")
    private
    String dateEnded;
    @ColumnInfo(name = "importance")
    private
    int importance;

    @Ignore
    public Success(String title, String category, int importance, String description, String dateStarted, String dateEnded) {
        this.id = UUID.randomUUID().toString();
        this.title = title;
        this.category = category;
        this.importance = importance;
        this.description = description;
        this.dateStarted = dateStarted;
        this.dateEnded = dateEnded;
    }

    public Success(@NonNull String id, @NonNull String title, @NonNull String category, String description,
                   String dateStarted, String dateEnded, int importance) {
        this.id = id;
        this.title = title;
        this.category = category;
        this.description = description;
        this.dateStarted = dateStarted;
        this.dateEnded = dateEnded;
        this.importance = importance;
    }

    @Ignore
    protected Success(Parcel in) {
        title = in.readString();
        category = in.readString();
        importance = in.readInt();
        description = in.readString();
        dateStarted = in.readString();
        dateEnded = in.readString();
        id = in.readString();
    }

    @Ignore
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
        return dateStarted;
    }

    public void setDateStarted(String date) {
        this.dateStarted = date;
    }

    public String getDateEnded() {
        return dateEnded;
    }

    public void setDateEnded(String date) {
        this.dateEnded = date;
    }


    public String getId() {
        return id;
    }

    public void setId(String id) {
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
        parcel.writeString(dateStarted);
        parcel.writeString(dateEnded);
        parcel.writeString(id);
    }
}
