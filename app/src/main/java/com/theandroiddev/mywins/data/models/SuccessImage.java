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
 * Created by grazyna on 2017-08-23.
 */

//@Entity/*(tableName = "successImage",
//        foreignKeys = {
//                @ForeignKey(
//                        entity = Success.class,
//                        parentColumns = "id",
//                        childColumns = "successId",
//                        onDelete = ForeignKey.CASCADE
//                )},
//        indices = { @Index(value = "id")}
//)*/
//
@Entity
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

    @PrimaryKey
    @NonNull
    @ColumnInfo(name = "id")
    private
    String id;
    @ColumnInfo(name = "successId")
    private
    String successId;
    @ColumnInfo(name = "imagePath")
    private
    String imagePath;

    @Ignore
    public SuccessImage(Parcel in) {
        id = in.readString();
        imagePath = in.readString();
        successId = in.readString();
    }

    @Ignore
    public SuccessImage() {
        this.id = UUID.randomUUID().toString();

    }

    public SuccessImage(@NonNull String id, @NonNull String successId, @NonNull String imagePath) {
        this.id = id;
        this.successId = successId;
        this.imagePath = imagePath;
    }

    @Ignore
    public SuccessImage(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
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
        parcel.writeString(id);
        parcel.writeString(imagePath);
        parcel.writeString(successId);
    }


}
