package com.theandroiddev.mywins.successes;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by jakub on 12.11.17.
 */

public class SearchFilter implements Parcelable {

    public static final Parcelable.Creator<SearchFilter> CREATOR = new Parcelable.Creator<SearchFilter>() {
        @Override
        public SearchFilter createFromParcel(Parcel in) {
            return new SearchFilter(in);
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
        searchTerm = in.readString();
        sortType = in.readString();
        isSortingAscending = in.readByte() != 0x00;
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
        dest.writeString(searchTerm);
        dest.writeString(sortType);
        dest.writeByte((byte) (isSortingAscending ? 0x01 : 0x00));
    }
}