package com.theandroiddev.mywins.successes;

import android.content.Context;
import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.MenuItem;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.theandroiddev.mywins.BasePresenter;
import com.theandroiddev.mywins.BaseView;
import com.theandroiddev.mywins.data.models.SearchFilter;
import com.theandroiddev.mywins.data.models.Success;
import com.theandroiddev.mywins.data.prefs.PreferencesHelper;
import com.theandroiddev.mywins.data.repositories.SuccessesRepository;

import java.util.ArrayList;

/**
 * Created by jakub on 12.11.17.
 */

public interface SuccessesContract {
    /**
     * Created by jakub on 28.10.17.
     */

    interface View extends BaseView<Presenter> {

        void displayDefaultSuccesses(ArrayList<Success> successList);

        void displayNoSuccesses();

        void displaySuccesses(ArrayList<Success> successList);

        void updateAdapterList(ArrayList<Success> successList);

        void undoToRemove(int position);

        void successRemoved(int position);

        void displaySuccessChanged();

        void displayCategory(String category);

        void hideSearchBar();

        void displaySearchBar();

        void displayUpdatedSuccesses();

        void displaySlider(ArrayList<Success> successList);

        void displaySliderAnimation(ArrayList<Success> successes, Success success, int position, TextView titleTv, TextView categoryTv, TextView dateStartedTv, TextView dateEndedTv, ImageView categoryIv, ImageView importanceIv, ConstraintLayout constraintLayout, CardView cardView);

        void displaySearch();
    }

    /**
     * Created by jakub on 07.11.17.
     */

    interface Presenter extends BasePresenter<View> {

        void loadSuccesses();

        void setRepository(SuccessesRepository repository);

        void removeSuccessesFromQueue();

        void undoToRemove(final int position);

        void sendToRemoveQueue(final int position);

        void backupSuccess(int position);

        void updateSuccess(int clickedPosition);

        void categoryPicked(String category);

        void setPrefHelper(PreferencesHelper preferencesHelper);

        void handleOptionsItemSelected(MenuItem item);

        void handleMenuSearch();

        void setSearchTerm(String searchTerm);

        void addSuccess(Success s);

        void closeDB();

        void checkPreferences();

        void openDB();

        void startSlider(Success success, int position, TextView titleTv, TextView categoryTv, TextView dateStartedTv, TextView dateEndedTv, ImageView categoryIv, ImageView importanceIv, ConstraintLayout constraintLayout, CardView cardView);

        SearchFilter getSearchFilter();

        void clearSearch();

        void showSearch();

        void onCreateActivity(Context context, SuccessesContract.View view, PreferencesHelper prefs);

        void onDestroyActivity();

        void onResumeActivity(SuccessesContract.View view, int clickedPosition, EditText searchBox);

        void showSoftKeyboard(InputMethodManager imm, EditText searchBox);

        void hideSoftKeyboard(EditText searchBox, InputMethodManager imm);

        void setSuccessListVisible(RecyclerView recyclerView, TextView emptyListTv);

        void setSuccessListInvisible(RecyclerView recyclerView, TextView emptyListTv);

        void selectCategory(int id);

        void showCircularReveal(final android.view.View myView);

        void hideCircularReveal(final android.view.View myView);
    }

}
