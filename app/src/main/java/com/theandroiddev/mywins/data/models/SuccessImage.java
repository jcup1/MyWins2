package com.theandroiddev.mywins.data.models;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by grazyna on 2017-08-23.
 */

public class SuccessImage implements Parcelable {

    public static final Creator<SuccessImage> CREATOR = new Creator<SuccessImage>() {
        @Override
        public SuccessImage createFromParcel(Parcel in) {
            return new SuccessImage(in);
        }

        @Override
        public SuccessImage[] newArray(int size) {
            return new SuccessImage[size];
        }
    };
    int id;
    String successId;
    String imagePath;

    protected SuccessImage(Parcel in) {
        id = in.readInt();
        imagePath = in.readString();
        successId = in.readString();
    }

    public SuccessImage(String successId) {
        this.successId = successId;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getSuccessId() {
        return successId;
    }

    public void setSuccessId(String successId) {
        this.successId = successId;
    }

    public String getImagePath() {
        return imagePath;
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(id);
        parcel.writeString(imagePath);
        parcel.writeString(successId);
    }


}
