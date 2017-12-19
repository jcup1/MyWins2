package com.theandroiddev.mywins.successes;

import android.animation.Animator;
import android.content.Context;
import android.graphics.Color;
import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.MenuItem;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.theandroiddev.mywins.MyWinsApplication;
import com.theandroiddev.mywins.R;
import com.theandroiddev.mywins.data.models.SearchFilter;
import com.theandroiddev.mywins.data.models.Success;
import com.theandroiddev.mywins.data.prefs.PreferencesHelper;
import com.theandroiddev.mywins.data.repositories.DatabaseSuccessesRepository;
import com.theandroiddev.mywins.data.repositories.SuccessesRepository;
import com.theandroiddev.mywins.utils.Constants;

import java.util.ArrayList;

import io.codetail.animation.ViewAnimationUtils;

import static com.theandroiddev.mywins.utils.Constants.CATEGORY_JOURNEY;
import static com.theandroiddev.mywins.utils.Constants.CATEGORY_LEARN;
import static com.theandroiddev.mywins.utils.Constants.CATEGORY_MONEY;
import static com.theandroiddev.mywins.utils.Constants.CATEGORY_SPORT;
import static com.theandroiddev.mywins.utils.Constants.CATEGORY_VIDEO;
import static com.theandroiddev.mywins.utils.Constants.NOT_ACTIVE;

/**
 * Created by jakub on 04.11.17.
 */

public class SuccessesPresenter implements SuccessesContract.Presenter {
    private static final String TAG = "SuccessesActivityPresen";
    ArrayList<Success> successList;
    ArrayList<Success> successToRemoveList;
    Success backupSuccess;
    PreferencesHelper preferencesHelper;
    private SuccessesContract.View view;
    private SuccessesRepository successesRepository;
    private String sortType;
    private boolean isSortingAscending;
    private boolean isSearchOpened = false;
    private String searchTerm;

    public SuccessesPresenter(Context context) {
        ((MyWinsApplication) context).getAppComponent().inject(this);
        successList = new ArrayList<>();
        successToRemoveList = new ArrayList<>();
        sortType = Constants.SORT_DATE_ADDED;
        isSortingAscending = true;
    }

    @Override
    public void loadSuccesses() {

        successList = successesRepository.getSuccesses(getSearchFilter());

        if (successList.isEmpty()) {
            view.displayNoSuccesses();
        } else {
            view.displaySuccesses(successList);
        }
    }

    @Override
    public void setRepository(SuccessesRepository repository) {
        this.successesRepository = repository;
    }

    @Override
    public void removeSuccessesFromQueue() {
        successesRepository.removeSuccesses(successToRemoveList);
        successToRemoveList.clear();

    }

    @Override
    public void undoToRemove(final int position) {
        successList.add(position, backupSuccess);
        view.undoToRemove(position);
        successToRemoveList.remove(backupSuccess);

    }

    @Override
    public void sendToRemoveQueue(final int position) {
        successList.remove(position);
        view.successRemoved(position);
        successToRemoveList.add(backupSuccess);
        //TODO handle it
        if (successList.isEmpty()) {
            //loadSuccesses();
            view.displayNoSuccesses();
        }

    }

    @Override
    public void backupSuccess(int position) {
        backupSuccess = successList.get(position);
    }

    @Override
    public void updateSuccess(int clickedPosition) {
        //TODO check it later
        if (preferencesHelper.isFirstSuccessAdded() && successList.size() > clickedPosition) {

            if (clickedPosition != NOT_ACTIVE) {
                String id = successList.get(clickedPosition).getId();
                successList.set(clickedPosition, successesRepository.getSuccess(id));
                view.displaySuccessChanged();
            }
        }

    }

    @Override
    public void categoryPicked(String category) {
        view.displayCategory(category);
    }

    @Override
    public void setPrefHelper(PreferencesHelper preferencesHelper) {
        this.preferencesHelper = preferencesHelper;

    }

    @Override
    public void handleOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_search) {
            handleMenuSearch();
        }//TODO it's so dumb
        if (id == R.id.action_date_started) {

            if (!sortType.equals(Constants.SORT_DATE_STARTED)) {
                sortType = Constants.SORT_DATE_STARTED;
            } else {
                isSortingAscending = !isSortingAscending;
            }
            loadSuccesses();

        }
        if (id == R.id.action_date_ended) {

            if (!sortType.equals(Constants.SORT_DATE_ENDED)) {
                sortType = Constants.SORT_DATE_ENDED;
            } else {
                isSortingAscending = !isSortingAscending;
            }
            loadSuccesses();

        }
        if (id == R.id.action_title) {

            if (!sortType.equals(Constants.SORT_TITLE)) {
                sortType = Constants.SORT_TITLE;
            } else {
                isSortingAscending = !isSortingAscending;
            }
            loadSuccesses();

        }
        if (id == R.id.action_date_added) {

            if (!sortType.equals(Constants.SORT_DATE_ADDED)) {
                sortType = Constants.SORT_DATE_ADDED;
            } else {
                isSortingAscending = !isSortingAscending;
            }
            loadSuccesses();

        }
        if (id == R.id.action_importance) {

            if (!sortType.equals(Constants.SORT_IMPORTANCE)) {
                sortType = Constants.SORT_IMPORTANCE;
            } else {
                isSortingAscending = !isSortingAscending;
            }
            loadSuccesses();

        }
        if (id == R.id.action_description) {

            if (!sortType.equals(Constants.SORT_DESCRIPTION)) {
                sortType = Constants.SORT_DESCRIPTION;
            } else {
                isSortingAscending = !isSortingAscending;
            }
            loadSuccesses();

        }
    }

    @Override
    public void handleMenuSearch() {
        if (isSearchOpened) {
            view.hideSearchBar();
            isSearchOpened = false;
            successList = successesRepository.getSuccesses(getSearchFilter());
            view.displaySuccesses(successList);
        } else {
            view.displaySearchBar();
            isSearchOpened = true;
        }
    }

    @Override
    public void showSearch() {
        view.displaySearch();
    }

    @Override
    public void onCreateActivity(Context context, SuccessesContract.View view, PreferencesHelper prefs) {

        setRepository(new DatabaseSuccessesRepository(context));
        setPrefHelper(prefs);
        setSearchTerm("");
        setView(view);
        openDB();
        checkPreferences();
        loadSuccesses();
    }

    @Override
    public void onDestroyActivity() {
        closeDB();
        dropView();
    }

    @Override
    public void onResumeActivity(SuccessesContract.View view, int clickedPosition, EditText searchBox) {

        setView(view);
        updateSuccess(clickedPosition);

    }

    @Override
    public void showSoftKeyboard(InputMethodManager imm, EditText searchBox) {

        if (searchBox != null) {
            if (imm != null) {
                imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, InputMethodManager.SHOW_IMPLICIT);
            }
        }
    }

    @Override
    public void hideSoftKeyboard(EditText searchBox, InputMethodManager imm) {

        if (imm != null && searchBox != null) {
            imm.hideSoftInputFromWindow(searchBox.getWindowToken(), 0);
        }

    }

    @Override
    public void setSuccessListVisible(RecyclerView recyclerView, TextView emptyListTv) {
        recyclerView.setVisibility(android.view.View.VISIBLE);
        emptyListTv.setVisibility(android.view.View.INVISIBLE);
    }

    @Override
    public void setSuccessListInvisible(RecyclerView recyclerView, TextView emptyListTv) {
        recyclerView.setVisibility(android.view.View.INVISIBLE);
        emptyListTv.setVisibility(android.view.View.VISIBLE);
    }

    @Override
    public void selectCategory(int id) {

        switch (id) {

            case R.id.action_learn:
                categoryPicked(CATEGORY_LEARN);
                break;
            case R.id.action_sport:
                categoryPicked(CATEGORY_SPORT);
                break;
            case R.id.action_journey:
                categoryPicked(CATEGORY_JOURNEY);
                break;
            case R.id.action_money:
                categoryPicked(CATEGORY_MONEY);
                break;
            case R.id.action_video:
                categoryPicked(CATEGORY_VIDEO);
                break;

            default:
                break;

        }
    }

    @Override
    public void showCircularReveal(final android.view.View myView) {
        myView.setBackgroundColor(Color.argb(0, 0, 0, 0));
        myView.setVisibility(android.view.View.VISIBLE);
        myView.post(new Runnable() {
            @Override
            public void run() {
                myView.setBackgroundColor(Color.argb(127, 0, 0, 0));
                int cx = (myView.getLeft() + myView.getRight());
                int cy = (myView.getTop() + myView.getBottom());
                int dx = Math.max(cx, myView.getWidth() - cx);
                int dy = Math.max(cy, myView.getHeight() - cy);
                float finalRadius = (float) Math.hypot(dx, dy);
                Animator animator =
                        ViewAnimationUtils.createCircularReveal(myView, cx, cy, 0, finalRadius);
                animator.setInterpolator(new AccelerateDecelerateInterpolator());
                animator.setDuration(375);

                animator.start();
            }
        });

    }

    @Override
    public void hideCircularReveal(final android.view.View myView) {
        myView.setVisibility(android.view.View.VISIBLE);
        myView.post(new Runnable() {
            @Override
            public void run() {
                int cx = (myView.getLeft() + myView.getRight());
                int cy = (myView.getTop() + myView.getBottom());
                int dx = Math.max(cx, myView.getWidth() - cx);
                int dy = Math.max(cy, myView.getHeight() - cy);
                float finalRadius = (float) Math.hypot(dx, dy);
                final Animator animator =
                        ViewAnimationUtils.createCircularReveal(myView, cx, cy, finalRadius, 0);
                animator.setInterpolator(new AccelerateDecelerateInterpolator());
                animator.setDuration(375).addListener(new Animator.AnimatorListener() {
                    @Override
                    public void onAnimationStart(Animator animator) {

                    }

                    @Override
                    public void onAnimationEnd(Animator animator) {
                        myView.setVisibility(android.view.View.GONE);
                    }

                    @Override
                    public void onAnimationCancel(Animator animator) {

                    }

                    @Override
                    public void onAnimationRepeat(Animator animator) {

                    }
                });

                animator.start();
            }
        });

    }

    @Override
    public void setSearchTerm(String searchTerm) {
        this.searchTerm = searchTerm;
    }

    @Override
    public void addSuccess(Success s) {

        if (!preferencesHelper.isFirstSuccessAdded()) {
            successesRepository.clearDatabase();
            preferencesHelper.setFirstSuccessAdded();
        }
        successesRepository.addSuccess(s);
        loadSuccesses();
        view.displayUpdatedSuccesses();

    }

    @Override
    public void checkPreferences() {
        if (preferencesHelper.isFirstRun()) {
            //TODO add to database
            successesRepository.saveSuccesses(successesRepository.getDefaultSuccesses());

            preferencesHelper.setNotFirstRun();
        }
    }


    @Override
    public void openDB() {
        successesRepository.openDB();
    }

    @Override
    public void startSlider(Success success, int position, TextView titleTv, TextView categoryTv, TextView dateStartedTv, TextView dateEndedTv, ImageView categoryIv, ImageView importanceIv, ConstraintLayout constraintLayout, CardView cardView) {
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
            view.displaySliderAnimation(successesRepository.getSuccesses(getSearchFilter()), success, position, titleTv, categoryTv, dateStartedTv, dateEndedTv, categoryIv, importanceIv, constraintLayout, cardView);

        } else {
            view.displaySlider(successesRepository.getSuccesses(getSearchFilter()));

        }
    }

    @Override
    public SearchFilter getSearchFilter() {
        return new SearchFilter(searchTerm, sortType, isSortingAscending);
    }

    @Override
    public void clearSearch() {
        searchTerm = "";
        //loadSuccesses();

    }


    @Override
    public void closeDB() {
        successesRepository.closeDB();
    }

    private void clearSuccesses() {
        successList.clear();
    }

    @Override
    public void setView(SuccessesContract.View view) {
        this.view = view;
    }

    @Override
    public void dropView() {
        view = null;
    }
}
