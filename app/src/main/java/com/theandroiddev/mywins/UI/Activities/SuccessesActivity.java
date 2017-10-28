package com.theandroiddev.mywins.UI.Activities;

import android.animation.Animator;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.AssetManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v4.util.Pair;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.getbase.floatingactionbutton.FloatingActionsMenu;
import com.theandroiddev.mywins.R;
import com.theandroiddev.mywins.Storage.DBAdapter;
import com.theandroiddev.mywins.UI.Adapters.SuccessAdapter;
import com.theandroiddev.mywins.UI.Helpers.Constants;
import com.theandroiddev.mywins.UI.Models.Success;
import com.theandroiddev.mywins.UI.Models.SuccessImage;
import com.theandroiddev.mywins.UI.Views.SuccessesActivityView;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import io.codetail.animation.ViewAnimationUtils;

import static com.theandroiddev.mywins.UI.Helpers.Constants.ADD_ON_TOP;
import static com.theandroiddev.mywins.UI.Helpers.Constants.CATEGORY_JOURNEY;
import static com.theandroiddev.mywins.UI.Helpers.Constants.CATEGORY_LEARN;
import static com.theandroiddev.mywins.UI.Helpers.Constants.CATEGORY_MONEY;
import static com.theandroiddev.mywins.UI.Helpers.Constants.CATEGORY_SPORT;
import static com.theandroiddev.mywins.UI.Helpers.Constants.CATEGORY_VALUE;
import static com.theandroiddev.mywins.UI.Helpers.Constants.CATEGORY_VIDEO;
import static com.theandroiddev.mywins.UI.Helpers.Constants.DATE_ENDED_VALUE;
import static com.theandroiddev.mywins.UI.Helpers.Constants.DATE_STARTED_VALUE;
import static com.theandroiddev.mywins.UI.Helpers.Constants.DESCRIPTION_VALUE;
import static com.theandroiddev.mywins.UI.Helpers.Constants.EXTRA_INSERT_SUCCESS_ITEM;
import static com.theandroiddev.mywins.UI.Helpers.Constants.EXTRA_SUCCESS_CARD_VIEW;
import static com.theandroiddev.mywins.UI.Helpers.Constants.EXTRA_SUCCESS_CATEGORY;
import static com.theandroiddev.mywins.UI.Helpers.Constants.EXTRA_SUCCESS_CATEGORY_IV;
import static com.theandroiddev.mywins.UI.Helpers.Constants.EXTRA_SUCCESS_DATE_ENDED;
import static com.theandroiddev.mywins.UI.Helpers.Constants.EXTRA_SUCCESS_DATE_STARTED;
import static com.theandroiddev.mywins.UI.Helpers.Constants.EXTRA_SUCCESS_IMPORTANCE_IV;
import static com.theandroiddev.mywins.UI.Helpers.Constants.EXTRA_SUCCESS_ITEM;
import static com.theandroiddev.mywins.UI.Helpers.Constants.EXTRA_SUCCESS_TITLE;
import static com.theandroiddev.mywins.UI.Helpers.Constants.IMPORTANCE_VALUE;
import static com.theandroiddev.mywins.UI.Helpers.Constants.NOT_ACTIVE;
import static com.theandroiddev.mywins.UI.Helpers.Constants.PACKAGE_NAME;
import static com.theandroiddev.mywins.UI.Helpers.Constants.REQUEST_CODE_INSERT;
import static com.theandroiddev.mywins.UI.Helpers.Constants.SNACK_SUCCESS_NOT_ADDED;
import static com.theandroiddev.mywins.UI.Helpers.Constants.SNACK_SUCCESS_REMOVED;
import static com.theandroiddev.mywins.UI.Helpers.Constants.SNACK_UNDO;
import static com.theandroiddev.mywins.UI.Helpers.Constants.SUCCESS_ID_VALUE;
import static com.theandroiddev.mywins.UI.Helpers.Constants.TITLE_VALUE;
import static com.theandroiddev.mywins.UI.Helpers.Constants.dummyCategory;
import static com.theandroiddev.mywins.UI.Helpers.Constants.dummyDescription;
import static com.theandroiddev.mywins.UI.Helpers.Constants.dummyEndDate;
import static com.theandroiddev.mywins.UI.Helpers.Constants.dummyImportance;
import static com.theandroiddev.mywins.UI.Helpers.Constants.dummyStartDate;
import static com.theandroiddev.mywins.UI.Helpers.Constants.dummySuccessesSize;
import static com.theandroiddev.mywins.UI.Helpers.Constants.dummyTitle;

public class SuccessesActivity extends AppCompatActivity implements View.OnClickListener, SuccessAdapter.OnItemClickListener, SuccessesActivityView {


    private static final String TAG = "SuccessesActivityActivi";
    public static boolean dbUpdate;
    SharedPreferences prefs = null;
    FloatingActionsMenu floatingActionsMenu;
    boolean isSortingAscending;
    DBAdapter dbAdapter;
    private MenuItem mSearchAction;
    private boolean isSearchOpened = false;
    private EditText searchBox;
    private ConstraintLayout mainConstraint;
    private RecyclerView recyclerView;
    private ArrayList<Success> successes;
    private List<Success> successesToRemove;
    private List<SuccessImage> dummySuccessImages;
    private SuccessAdapter successAdapter;
    ItemTouchHelper.SimpleCallback simpleCallback = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {

        @Override
        public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
            return false;
        }

        @Override
        public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {

            int pos = viewHolder.getAdapterPosition();
            toRemove(pos);

        }
    };

    private View shadowView;
    private String sortType;
    private int clickedPosition = NOT_ACTIVE;
    private SuccessesActivityPresenter presenter;

    @Override
    protected void onPause() {
        super.onPause();
        remove();

    }

    @Override
    protected void onResume() {
        super.onResume();
        updateSuccess(clickedPosition);
    }

    private void updateSuccess(int clickedPosition) {

        if (clickedPosition != NOT_ACTIVE) {
            int id = successes.get(clickedPosition).getId();
            dbAdapter.openDB();
            successes.set(clickedPosition, dbAdapter.getSuccess(id));
            dbAdapter.closeDB();
            successAdapter.notifyItemChanged(clickedPosition);
        }

    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        mSearchAction = menu.findItem(R.id.action_search);
        return super.onPrepareOptionsMenu(menu);
    }


    @Override
    public void onBackPressed() {

        if (!floatingActionsMenu.isExpanded()) {
            super.onBackPressed();
        } else {
            floatingActionsMenu.collapse();

        }

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.show_toolbar);
        setSupportActionBar(toolbar);
        initFABs();
        initRecycler();
        initCircularReveal();
        initVariables();
        prefs = getSharedPreferences(PACKAGE_NAME, MODE_PRIVATE);

        presenter = new SuccessesActivityPresenter(this, null);

    }

    @Override
    protected void onPostResume() {
        super.onPostResume();

        if (prefs.getBoolean("firstrun", true)) {
            insertDummyData();
            prefs.edit().putBoolean("firstrun", false).apply();
            Log.e(TAG, "onPostResume: " + 123);
        }
        if (dbUpdate) {
            insertDummyData();
            dbUpdate = false;
        }
    }

    public void insertDummyData() {

        successes.clear();
        new Constants();

        for (int i = 0; i < dummySuccessesSize; i++) {
            save(new Success(dummyTitle.get(i), dummyCategory.get(i), dummyImportance.get(i), dummyDescription.get(i),
                    dummyStartDate.get(i), dummyEndDate.get(i)));
            Log.e(TAG, "insertDummyData: " + i);
        }

        dummySuccessImages = new ArrayList<>();

        getSuccesses();
        successAdapter.notifyDataSetChanged();
    }

    private Bitmap getBitmapFromAsset(String strName) {
        AssetManager assetManager = getAssets();
        InputStream istr = null;
        try {
            istr = assetManager.open(strName);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return BitmapFactory.decodeStream(istr);
    }

    private void save(Success success) {

        DBAdapter dbAdapter = new DBAdapter(this);
        dbAdapter.openDB();
        dbAdapter.addSuccess(success);
        getSuccesses();
        dbAdapter.closeDB();

    }

    private void initCircularReveal() {
        shadowView = findViewById(R.id.shadow_view);
        shadowView.setVisibility(View.GONE);

        floatingActionsMenu = findViewById(R.id.multiple_actions);
        floatingActionsMenu.setOnFloatingActionsMenuUpdateListener(new FloatingActionsMenu.OnFloatingActionsMenuUpdateListener() {
            @Override
            public void onMenuExpanded() {


                if (searchBox != null) {
                    handleMenuSearch();
                }
                showCircularReveal(shadowView);
            }

            @Override
            public void onMenuCollapsed() {
                hideCircularReveal(shadowView);
            }
        });

        shadowView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (floatingActionsMenu.isExpanded()) {
                    floatingActionsMenu.collapse();
                }
            }
        });

    }

    private void showCircularReveal(final View myView) {
        myView.setBackgroundColor(Color.argb(0, 0, 0, 0));
        myView.setVisibility(View.VISIBLE);
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

    private void hideCircularReveal(final View myView) {
        myView.setVisibility(View.VISIBLE);
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
                        myView.setVisibility(View.GONE);
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


    private void initFABs() {

        final com.getbase.floatingactionbutton.FloatingActionButton actionLearn = findViewById(R.id.action_learn);
        final com.getbase.floatingactionbutton.FloatingActionButton actionSport = findViewById(R.id.action_sport);
        final com.getbase.floatingactionbutton.FloatingActionButton actionJourney = findViewById(R.id.action_journey);
        final com.getbase.floatingactionbutton.FloatingActionButton actionBusiness = findViewById(R.id.action_money);
        final com.getbase.floatingactionbutton.FloatingActionButton actionVideo = findViewById(R.id.action_video);

        actionLearn.setSize(com.getbase.floatingactionbutton.FloatingActionButton.SIZE_MINI);
        actionSport.setSize(com.getbase.floatingactionbutton.FloatingActionButton.SIZE_MINI);
        actionJourney.setSize(com.getbase.floatingactionbutton.FloatingActionButton.SIZE_MINI);
        actionBusiness.setSize(com.getbase.floatingactionbutton.FloatingActionButton.SIZE_MINI);
        actionVideo.setSize(com.getbase.floatingactionbutton.FloatingActionButton.SIZE_MINI);

        int color_video = ResourcesCompat.getColor(getResources(), R.color.video, null);
        int color_money = ResourcesCompat.getColor(getResources(), R.color.money, null);
        int color_journey = ResourcesCompat.getColor(getResources(), R.color.journey, null);
        int color_sport = ResourcesCompat.getColor(getResources(), R.color.sport, null);
        int color_learn = ResourcesCompat.getColor(getResources(), R.color.learn, null);
        int color_white = ResourcesCompat.getColor(getResources(), R.color.white, null);

        Drawable videoDrawable = ResourcesCompat.getDrawable(getResources(), R.drawable.ic_video, null);
        Drawable moneyDrawable = ResourcesCompat.getDrawable(getResources(), R.drawable.ic_money, null);
        Drawable journeyDrawable = ResourcesCompat.getDrawable(getResources(), R.drawable.ic_journey, null);
        Drawable sportDrawable = ResourcesCompat.getDrawable(getResources(), R.drawable.ic_sport, null);
        Drawable learnDrawable = ResourcesCompat.getDrawable(getResources(), R.drawable.ic_learn, null);


        if (videoDrawable != null) {
            videoDrawable.setColorFilter(new PorterDuffColorFilter(color_white, PorterDuff.Mode.SRC_IN));
            actionVideo.setIconDrawable(videoDrawable);
        }
        if (moneyDrawable != null) {
            moneyDrawable.setColorFilter(new PorterDuffColorFilter(color_white, PorterDuff.Mode.SRC_IN));
            actionBusiness.setIconDrawable(moneyDrawable);
        }
        if (journeyDrawable != null) {
            journeyDrawable.setColorFilter(new PorterDuffColorFilter(color_white, PorterDuff.Mode.SRC_IN));
            actionJourney.setIconDrawable(journeyDrawable);
        }
        if (sportDrawable != null) {
            sportDrawable.setColorFilter(new PorterDuffColorFilter(color_white, PorterDuff.Mode.SRC_IN));
            actionSport.setIconDrawable(sportDrawable);
        }
        if (learnDrawable != null) {
            learnDrawable.setColorFilter(new PorterDuffColorFilter(color_white, PorterDuff.Mode.SRC_IN));
            actionLearn.setIconDrawable(learnDrawable);
        }

        actionVideo.setColorNormal(color_video);
        actionBusiness.setColorNormal(color_money);
        actionJourney.setColorNormal(color_journey);
        actionSport.setColorNormal(color_sport);
        actionLearn.setColorNormal(color_learn);

        actionLearn.setOnClickListener(this);
        actionSport.setOnClickListener(this);
        actionJourney.setOnClickListener(this);
        actionBusiness.setOnClickListener(this);
        actionVideo.setOnClickListener(this);

    }

    private void initRecycler() {

        mainConstraint = findViewById(R.id.main_constraint);
        recyclerView = findViewById(R.id.recycler_view);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setHasFixedSize(true);

        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(simpleCallback);
        itemTouchHelper.attachToRecyclerView(recyclerView);

    }

    private void initVariables() {

        dbAdapter = new DBAdapter(this);
        sortType = Constants.SORT_DATE_ADDED;
        isSortingAscending = true;
        getSuccesses();
    }

    private String getSearchText() {
        if (searchBox != null) {
            return searchBox.getText().toString();
        }
        return "";
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        if (id == R.id.action_search) {
            handleMenuSearch();
            return true;
        }

        if (id == R.id.action_date_started) {

            if (!sortType.equals(Constants.SORT_DATE_STARTED)) {
                sortType = Constants.SORT_DATE_STARTED;
            } else {
                isSortingAscending = !isSortingAscending;
            }
            getSuccesses();
            return true;

        }

        if (id == R.id.action_date_ended) {

            if (!sortType.equals(Constants.SORT_DATE_ENDED)) {
                sortType = Constants.SORT_DATE_ENDED;
            } else {
                isSortingAscending = !isSortingAscending;
            }
            getSuccesses();
            return true;

        }

        if (id == R.id.action_title) {

            if (!sortType.equals(Constants.SORT_TITLE)) {
                sortType = Constants.SORT_TITLE;
            } else {
                isSortingAscending = !isSortingAscending;
            }
            getSuccesses();
            return true;

        }

        if (id == R.id.action_date_added) {

            if (!sortType.equals(Constants.SORT_DATE_ADDED)) {
                sortType = Constants.SORT_DATE_ADDED;
            } else {
                isSortingAscending = !isSortingAscending;
            }
            getSuccesses();
            return true;

        }

        if (id == R.id.action_importance) {

            if (!sortType.equals(Constants.SORT_IMPORTANCE)) {
                sortType = Constants.SORT_IMPORTANCE;
            } else {
                isSortingAscending = !isSortingAscending;
            }
            getSuccesses();
            return true;

        }

        if (id == R.id.action_description) {

            if (!sortType.equals(Constants.SORT_DESCRIPTION)) {
                sortType = Constants.SORT_DESCRIPTION;
            } else {
                isSortingAscending = !isSortingAscending;
            }
            getSuccesses();
            return true;

        }

        return super.onOptionsItemSelected(item);
    }


    protected void handleMenuSearch() {
        ActionBar action = getSupportActionBar();

        if (isSearchOpened) {

            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(searchBox.getWindowToken(), 0);

            if (action != null) {
                action.setDisplayShowCustomEnabled(false);
                action.setDisplayShowTitleEnabled(true);

            }

            mSearchAction.setIcon(ContextCompat.getDrawable(this, R.drawable.ic_search));

            isSearchOpened = false;
            searchBox = null;
            getSuccesses();

        } else {

            if (action != null) {
                action.setDisplayShowCustomEnabled(true);
            }

            final ViewGroup nullParent = null;
            View view = getLayoutInflater().inflate(R.layout.search_bar, nullParent);
            ActionBar.LayoutParams layoutParams = new ActionBar.LayoutParams(ActionBar.LayoutParams.MATCH_PARENT,
                    ActionBar.LayoutParams.MATCH_PARENT);
            if (action != null) {
                action.setCustomView(view, layoutParams);
                action.setDisplayShowTitleEnabled(false);
                searchBox = action.getCustomView().findViewById(R.id.edtSearch);

            }

            searchBox.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                }

                @Override
                public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                    getSuccesses();
                }

                @Override
                public void afterTextChanged(Editable editable) {

                }
            });
            searchBox.setOnEditorActionListener(new TextView.OnEditorActionListener() {
                                                    @Override
                                                    public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                                                        getSuccesses();
                                                        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                                                        imm.hideSoftInputFromWindow(searchBox.getWindowToken(), 0);

                                                        return true;
                                                    }

                                                }
            );

            searchBox.requestFocus();

            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.showSoftInput(searchBox, InputMethodManager.SHOW_IMPLICIT);

            mSearchAction.setIcon(ContextCompat.getDrawable(this, R.drawable.ic_close));

            isSearchOpened = true;
        }
    }

    @Override
    public void onClick(View shadowView) {
        com.getbase.floatingactionbutton.FloatingActionButton fab = (com.getbase.floatingactionbutton.FloatingActionButton) shadowView;

        switch (fab.getId()) {

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

    void categoryPicked(String categoryName) {

        Intent intent = new Intent(SuccessesActivity.this, InsertSuccessActivity.class);
        intent.putExtra("categoryName", categoryName);
        startActivityForResult(intent, REQUEST_CODE_INSERT);
        floatingActionsMenu.collapse();

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_CODE_INSERT) {
            if (resultCode == Activity.RESULT_OK) {


                Success s = data.getExtras().getParcelable(EXTRA_INSERT_SUCCESS_ITEM);
                save(s);

                successAdapter.notifyDataSetChanged();

            }
            if (resultCode == Activity.RESULT_CANCELED) {
                Snackbar.make(mainConstraint, SNACK_SUCCESS_NOT_ADDED, Snackbar.LENGTH_SHORT).show();

            }
        }

    }

    private void toRemove(int position) {
        showUndoSnackbar(position);

    }

    private void showUndoSnackbar(final int position) {
        final Success success = successes.get(position);
        Snackbar snackbar = Snackbar
                .make(recyclerView, SNACK_SUCCESS_REMOVED, Snackbar.LENGTH_LONG)
                .setAction(SNACK_UNDO, new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        undoToRemove(success, position);
                    }
                });
        snackbar.show();

        sendToRemoveQueue(success, position);
    }

    private void sendToRemoveQueue(Success success, int position) {
        successes.remove(position);
        successAdapter.notifyItemRemoved(position);
        successesToRemove.add(success);
    }

    private void undoToRemove(Success success, int position) {
        successes.add(position, success);
        successAdapter.notifyItemInserted(position);
        recyclerView.scrollToPosition(position);
        successesToRemove.remove(success);
    }


    private void remove() {
        dbAdapter = new DBAdapter(this);
        dbAdapter.openDB();
        dbAdapter.removeSuccess(successesToRemove);
        dbAdapter.closeDB();
    }

    private void getSuccesses() {

        String searchTerm = getSearchText();

        successes = new ArrayList<>();
        successesToRemove = new ArrayList<>();
        successes.clear();

        DBAdapter dbAdapter = new DBAdapter(this);
        dbAdapter.openDB();
        Success success;
        Cursor cursor = dbAdapter.retrieveSuccess(searchTerm, sortType);

        while (cursor.moveToNext()) {
            int id = cursor.getInt(SUCCESS_ID_VALUE);
            String title = cursor.getString(TITLE_VALUE);
            String category = cursor.getString(CATEGORY_VALUE);
            int importance = cursor.getInt(IMPORTANCE_VALUE);
            String description = cursor.getString(DESCRIPTION_VALUE);
            String dateStarted = cursor.getString(DATE_STARTED_VALUE);
            String dateEnded = cursor.getString(DATE_ENDED_VALUE);

            success = new Success(title, category, importance, description, dateStarted, dateEnded);
            success.setId(id);
            if (isSortingAscending) {
                successes.add(ADD_ON_TOP, success);
            } else {
                successes.add(success); //default = add on bottom
            }

        }

        cursor.close();
        dbAdapter.closeDB();

        successAdapter = new SuccessAdapter(successes, R.layout.success_layout, getApplicationContext(), this);
        recyclerView.setAdapter(successAdapter);
        successAdapter.notifyDataSetChanged();
    }

    @Override
    public void onItemClick(Success success, int position, TextView titleTv, TextView categoryTv, TextView dateStartedTv, TextView dateEndedTv, ImageView categoryIv, ImageView importanceIv, ConstraintLayout constraintLayout, CardView cardView) {
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
//            showSuccessAnimation(success, titleTv, categoryTv, dateStartedTv, dateEndedTv, categoryIv, importanceIv, constraintLayout, cardView);
//
//        } else {
//            showSuccess(success);
//        }
        Intent intent = new Intent(SuccessesActivity.this, ScreenSlidePagerActivity.class);
        intent.putParcelableArrayListExtra("SUCCESSES", successes);
        startActivity(intent);

        //TODO change fragment

        this.clickedPosition = position;

    }

    @Override
    public void onLongItemClick(final int position, CardView cardview) {
        PopupMenu popupMenu;
        popupMenu = new PopupMenu(SuccessesActivity.this, cardview);

        popupMenu.getMenuInflater().inflate(R.menu.menu_item, popupMenu.getMenu());
        popupMenu.show();

        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                if (item.getItemId() == R.id.remove_item_menu) {

                    toRemove(position);

                }

                return true;
            }
        });
    }


    private void showSuccess(Success success) {

        Intent showSuccessIntent = new Intent(SuccessesActivity.this, ShowSuccessActivity.class);

        showSuccessIntent.putExtra(EXTRA_SUCCESS_ITEM, success);

        startActivity(showSuccessIntent);

    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    private void showSuccessAnimation(Success success, TextView titleTv, TextView categoryTv, TextView dateStartedTv, TextView dateEndedTv, ImageView categoryIv, ImageView importanceIv, ConstraintLayout constraintLayout, CardView cardView) {


        Intent showSuccessIntent = new Intent(SuccessesActivity.this, ShowSuccessActivity.class);


        showSuccessIntent.putExtra(EXTRA_SUCCESS_ITEM, success);

        Pair<View, String> p1, p2, p3, p4, p5, p6, p7;
        p1 = Pair.create((View) titleTv, EXTRA_SUCCESS_TITLE);
        p2 = Pair.create((View) categoryTv, EXTRA_SUCCESS_CATEGORY);
        p3 = Pair.create((View) dateStartedTv, EXTRA_SUCCESS_DATE_STARTED);
        p4 = Pair.create((View) dateEndedTv, EXTRA_SUCCESS_DATE_ENDED);
        p5 = Pair.create((View) categoryIv, EXTRA_SUCCESS_CATEGORY_IV);
        p6 = Pair.create((View) importanceIv, EXTRA_SUCCESS_IMPORTANCE_IV);
        p7 = Pair.create((View) cardView, EXTRA_SUCCESS_CARD_VIEW);

        ActivityOptionsCompat activityOptionsCompat = ActivityOptionsCompat.makeSceneTransitionAnimation(
                this,
                p1, p2, p3, p4, p5, p6, p7);

        startActivity(showSuccessIntent, activityOptionsCompat.toBundle());

    }

    @Override
    public void displaySuccesses(List<Success> successList) {

    }

    @Override
    public void displayNoSuccesses() {

    }
}
