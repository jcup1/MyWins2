package com.theandroiddev.mywins.data.models;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by jakub on 12.11.17.
 */

public class SearchFilter implements Parcelable {

    public static final Parcelable.Creator<SearchFilter> CREATOR = new Parcelable.Creator<SearchFilter>() {
        @Override
        public SearchFilter createFromParcel(Parcel source) {
            return new SearchFilter(source);
        }

        @Override
        public SearchFilter[] newArray(int size) {
            return new SearchFilter[size];
        }
    };
    private String searchTerm;
    private String sortType;
    private boolean isSortingAscending;

    public SearchFilter(String searchTerm, String sortType, boolean isSortingAscending) {
        this.searchTerm = searchTerm;
        this.sortType = sortType;
        this.isSortingAscending = isSortingAscending;
    }

    protected SearchFilter(Parcel in) {
        this.searchTerm = in.readString();
        this.sortType = in.readString();
        this.isSortingAscending = in.readByte() != 0;
    }

    public String getSearchTerm() {
        return searchTerm;
    }

    public void setSearchTerm(String searchTerm) {
        this.searchTerm = searchTerm;
    }

    public String getSortType() {
        return sortType;
    }

    public void setSortType(String sortType) {
        this.sortType = sortType;
    }

    public boolean isSortingAscending() {
        return isSortingAscending;
    }

    public void setSortingAscending(boolean sortingAscending) {
        isSortingAscending = sortingAscending;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.searchTerm);
        dest.writeString(this.sortType);
        dest.writeByte(this.isSortingAscending ? (byte) 1 : (byte) 0);
    }
}