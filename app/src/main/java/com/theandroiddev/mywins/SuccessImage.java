package com.theandroiddev.mywins;

import android.graphics.Bitmap;
import android.os.Parcel;
import android.os.Parcelable;

import java.io.ByteArrayOutputStream;

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
    int successId;
    String fileName;
    byte[] imageData;
    private Bitmap imageDataBitmap;

    public SuccessImage(String fileName, byte[] imageData, int successId) {
        this.fileName = fileName;
        this.imageData = imageData;
        this.successId = successId;
    }

    protected SuccessImage(Parcel in) {
        id = in.readInt();
        fileName = in.readString();
        imageData = in.createByteArray();
        successId = in.readInt();
    }

    public SuccessImage() {

        this.fileName = "default";
        this.imageData = null;
        this.imageDataBitmap = null;

    }

    public SuccessImage(String fileName, Bitmap imageDataBitmap) {
        this.fileName = fileName;
        this.imageDataBitmap = imageDataBitmap;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public byte[] getImageData() {
        return imageData;
    }

    public void setImageData(byte[] imageData) {
        this.imageData = imageData;
    }

    public int getSuccessId() {
        return successId;
    }

    public void setSuccessId(int successId) {
        this.successId = successId;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(id);
        parcel.writeString(fileName);
        parcel.writeByteArray(imageData);
        parcel.writeInt(successId);
    }

    public Bitmap getImageDataBitmap() {


        //Bitmap bitmap = BitmapFactory.decodeByteArray(getImageData(), 0, getImageData().length);

        return imageDataBitmap;
    }

    public void setImageDataBitmap(Bitmap imageDataBitmap) {
        this.imageDataBitmap = imageDataBitmap;
        if (getImageData() == null) {
            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            imageDataBitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
            setImageData(stream.toByteArray());
        }

    }
}
