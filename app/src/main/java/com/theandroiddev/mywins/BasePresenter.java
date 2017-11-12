package com.theandroiddev.mywins;

/**
 * Created by jakub on 12.11.17.
 */

public interface BasePresenter<T> {

    /**
     * Binds presenter with a view when resumed. The Presenter will perform initialization here.
     *
     * @param view the view associated with this presenter
     */
    void setView(T view);

    /**
     * Drops the reference to the view when destroyed
     */
    void dropView();

}
