package com.theandroiddev.mywins.successes;

import android.animation.Animator;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
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
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.getbase.floatingactionbutton.FloatingActionButton;
import com.getbase.floatingactionbutton.FloatingActionsMenu;
import com.theandroiddev.mywins.MyWinsApplication;
import com.theandroiddev.mywins.R;
import com.theandroiddev.mywins.UI.activities.InsertSuccessActivity;
import com.theandroiddev.mywins.UI.activities.ShowSuccessActivity;
import com.theandroiddev.mywins.data.db.DBAdapter;
import com.theandroiddev.mywins.data.models.Success;
import com.theandroiddev.mywins.data.prefs.PreferencesHelper;
import com.theandroiddev.mywins.data.repositories.DatabaseSuccessesRepository;
import com.theandroiddev.mywins.successslider.SuccessSliderActivity;

import java.util.ArrayList;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.codetail.animation.ViewAnimationUtils;

import static com.theandroiddev.mywins.utils.Constants.CATEGORY_JOURNEY;
import static com.theandroiddev.mywins.utils.Constants.CATEGORY_LEARN;
import static com.theandroiddev.mywins.utils.Constants.CATEGORY_MONEY;
import static com.theandroiddev.mywins.utils.Constants.CATEGORY_SPORT;
import static com.theandroiddev.mywins.utils.Constants.CATEGORY_VIDEO;
import static com.theandroiddev.mywins.utils.Constants.EXTRA_INSERT_SUCCESS_ITEM;
import static com.theandroiddev.mywins.utils.Constants.EXTRA_SUCCESS_CARD_VIEW;
import static com.theandroiddev.mywins.utils.Constants.EXTRA_SUCCESS_CATEGORY;
import static com.theandroiddev.mywins.utils.Constants.EXTRA_SUCCESS_CATEGORY_IV;
import static com.theandroiddev.mywins.utils.Constants.EXTRA_SUCCESS_DATE_ENDED;
import static com.theandroiddev.mywins.utils.Constants.EXTRA_SUCCESS_DATE_STARTED;
import static com.theandroiddev.mywins.utils.Constants.EXTRA_SUCCESS_IMPORTANCE_IV;
import static com.theandroiddev.mywins.utils.Constants.EXTRA_SUCCESS_ITEM;
import static com.theandroiddev.mywins.utils.Constants.EXTRA_SUCCESS_TITLE;
import static com.theandroiddev.mywins.utils.Constants.NOT_ACTIVE;
import static com.theandroiddev.mywins.utils.Constants.REQUEST_CODE_INSERT;
import static com.theandroiddev.mywins.utils.Constants.SNACK_SUCCESS_NOT_ADDED;
import static com.theandroiddev.mywins.utils.Constants.SNACK_SUCCESS_REMOVED;
import static com.theandroiddev.mywins.utils.Constants.SNACK_UNDO;

public class SuccessesActivity extends AppCompatActivity implements android.view.View.OnClickListener, SuccessAdapter.OnItemClickListener, SuccessesContract.View {
    private static final String TAG = "SuccessesActivity";

    private static final String EXTRA_TRIGGER_SYNC_FLAG =
            "com.theandroiddev.mywins.UI.Activities.SuccessesActivity.EXTRA_TRIGGER_SYNC_FLAG";
    FloatingActionsMenu floatingActionsMenu;
    DBAdapter dbAdapter;
    SuccessAdapter successAdapter;
    @Inject
    SuccessesContract.Presenter presenter;
    @BindView(R.id.recycler_view)
    RecyclerView recyclerView;
    @BindView(R.id.main_constraint)
    ConstraintLayout mainConstraint;
    @BindView(R.id.empty_list_text)
    TextView emptyListTv;
    @BindView(R.id.show_toolbar)
    Toolbar toolbar;
    @BindView(R.id.shadow_view)
    android.view.View shadowView;

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
    ActionBar action;
    private MenuItem searchAction;
    private EditText searchBox;
    private int clickedPosition = NOT_ACTIVE;
    private PreferencesHelper preferencesHelper;

    /**
     * Return an Intent to start this Activity.
     * triggerDataSyncOnCreate allows disabling the background sync service onCreate. Should
     * only be set to false during testing.
     */
    public static Intent getStartIntent(Context context, boolean triggerDataSyncOnCreate) {
        Intent intent = new Intent(context, SuccessesActivity.class);
        intent.putExtra(EXTRA_TRIGGER_SYNC_FLAG, triggerDataSyncOnCreate);
        return intent;
    }

    @Override
    protected void onPause() {
        super.onPause();
        presenter.removeSuccessesFromQueue();

    }

    @Override
    protected void onDestroy() {
        presenter.closeDB();
        presenter.dropView();
        super.onDestroy();

    }

    @Override
    protected void onResume() {
        super.onResume();
        presenter.setView(this);
        presenter.openDB();
        presenter.updateSuccess(clickedPosition);
    }


    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        searchAction = menu.findItem(R.id.action_search);
        return super.onPrepareOptionsMenu(menu);
    }


    @Override
    public void onBackPressed() {

        if (searchBox != null) {
            hideSearchBar();
        } else if (floatingActionsMenu.isExpanded()) {
            floatingActionsMenu.collapse();
        } else {

            super.onBackPressed();

        }

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ((MyWinsApplication) getApplication()).getAppComponent().inject(this);

        ButterKnife.bind(this);

        dbAdapter = new DBAdapter(this);
        preferencesHelper = new PreferencesHelper(this);
        setSupportActionBar(toolbar);//1
        initFABs();
        initCircularReveal();
        initRecycler();

        presenter.setRepository(new DatabaseSuccessesRepository(getApplication()));
        presenter.setPrefHelper(preferencesHelper);
        presenter.setSearchTerm("");

        presenter.setView(this);
        presenter.loadSuccesses();
        //preferencesHelper.clear();
        presenter.handleFirstSuccessPreference();


    }

    private void initCircularReveal() {
        shadowView.setVisibility(android.view.View.GONE);

        floatingActionsMenu = findViewById(R.id.multiple_actions);
        floatingActionsMenu.setOnFloatingActionsMenuUpdateListener(new FloatingActionsMenu.OnFloatingActionsMenuUpdateListener() {
            @Override
            public void onMenuExpanded() {


                if (searchBox != null) {
                    presenter.handleMenuSearch();
                }
                showCircularReveal(shadowView);
            }

            @Override
            public void onMenuCollapsed() {
                hideCircularReveal(shadowView);
            }
        });

        shadowView.setOnClickListener(new android.view.View.OnClickListener() {
            @Override
            public void onClick(android.view.View view) {
                if (floatingActionsMenu.isExpanded()) {
                    floatingActionsMenu.collapse();
                }
            }
        });

    }

    private void showCircularReveal(final android.view.View myView) {
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

    private void hideCircularReveal(final android.view.View myView) {
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

    private void initFABs() {

        final FloatingActionButton actionLearn = findViewById(R.id.action_learn);
        final FloatingActionButton actionSport = findViewById(R.id.action_sport);
        final FloatingActionButton actionJourney = findViewById(R.id.action_journey);
        final FloatingActionButton actionBusiness = findViewById(R.id.action_money);
        final FloatingActionButton actionVideo = findViewById(R.id.action_video);

        actionLearn.setSize(FloatingActionButton.SIZE_MINI);
        actionSport.setSize(FloatingActionButton.SIZE_MINI);
        actionJourney.setSize(FloatingActionButton.SIZE_MINI);
        actionBusiness.setSize(FloatingActionButton.SIZE_MINI);
        actionVideo.setSize(FloatingActionButton.SIZE_MINI);

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

        successAdapter = new SuccessAdapter(R.layout.success_layout, getApplicationContext(), this);
        recyclerView.setAdapter(successAdapter);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setHasFixedSize(true);
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(simpleCallback);
        itemTouchHelper.attachToRecyclerView(recyclerView);

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
    protected void onStop() {
        super.onStop();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        presenter.handleOptionsItemSelected(item);

        return super.onOptionsItemSelected(item);
    }



    @Override
    public void onClick(android.view.View shadowView) {
        FloatingActionButton fab = (FloatingActionButton) shadowView;

        switch (fab.getId()) {

            case R.id.action_learn:
                presenter.categoryPicked(CATEGORY_LEARN);
                break;
            case R.id.action_sport:
                presenter.categoryPicked(CATEGORY_SPORT);
                break;
            case R.id.action_journey:
                presenter.categoryPicked(CATEGORY_JOURNEY);
                break;
            case R.id.action_money:
                presenter.categoryPicked(CATEGORY_MONEY);
                break;
            case R.id.action_video:
                presenter.categoryPicked(CATEGORY_VIDEO);
                break;

            default:
                break;

        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_CODE_INSERT) {
            if (resultCode == Activity.RESULT_OK) {
                onSuccessAdded(data);

            }
            if (resultCode == Activity.RESULT_CANCELED) {
                onSuccessNotAdded();
            }
        }

    }

    public void onSuccessNotAdded() {
        Snackbar.make(mainConstraint, SNACK_SUCCESS_NOT_ADDED, Snackbar.LENGTH_SHORT).show();

    }

    public void onSuccessAdded(Intent data) {
        Success s = data.getExtras().getParcelable(EXTRA_INSERT_SUCCESS_ITEM);
        presenter.addSuccess(s);
    }

    private void toRemove(int position) {
        showUndoSnackbar(position);

    }

    private void showUndoSnackbar(final int position) {
        presenter.backupSuccess(position);
        Snackbar snackbar = Snackbar
                .make(recyclerView, SNACK_SUCCESS_REMOVED, Snackbar.LENGTH_LONG)
                .setAction(SNACK_UNDO, new android.view.View.OnClickListener() {
                    @Override
                    public void onClick(android.view.View view) {

                        presenter.undoToRemove(position);
                    }
                });
        snackbar.show();

        presenter.sendToRemoveQueue(position);
    }

    @Override
    public void onItemClick(Success success, int position, TextView titleTv, TextView categoryTv, TextView dateStartedTv, TextView dateEndedTv, ImageView categoryIv, ImageView importanceIv, ConstraintLayout constraintLayout, CardView cardView) {
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
//            showSuccessAnimation(success, titleTv, categoryTv, dateStartedTv, dateEndedTv, categoryIv, importanceIv, constraintLayout, cardView);
//
//        } else {
//            showSuccess(success);
//        }
        this.clickedPosition = position;

        presenter.startSlider();

        //TODO maybe put to presenter

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

        Pair<android.view.View, String> p1, p2, p3, p4, p5, p6, p7;
        p1 = Pair.create((android.view.View) titleTv, EXTRA_SUCCESS_TITLE);
        p2 = Pair.create((android.view.View) categoryTv, EXTRA_SUCCESS_CATEGORY);
        p3 = Pair.create((android.view.View) dateStartedTv, EXTRA_SUCCESS_DATE_STARTED);
        p4 = Pair.create((android.view.View) dateEndedTv, EXTRA_SUCCESS_DATE_ENDED);
        p5 = Pair.create((android.view.View) categoryIv, EXTRA_SUCCESS_CATEGORY_IV);
        p6 = Pair.create((android.view.View) importanceIv, EXTRA_SUCCESS_IMPORTANCE_IV);
        p7 = Pair.create((android.view.View) cardView, EXTRA_SUCCESS_CARD_VIEW);

        ActivityOptionsCompat activityOptionsCompat = ActivityOptionsCompat.makeSceneTransitionAnimation(
                this,
                p1, p2, p3, p4, p5, p6, p7);

        startActivity(showSuccessIntent, activityOptionsCompat.toBundle());

    }

    @Override
    public void displayDefaultSuccesses(ArrayList<Success> successList) {
        successAdapter.updateSuccessList(successList);
        recyclerView.setVisibility(android.view.View.VISIBLE);
        emptyListTv.setVisibility(android.view.View.INVISIBLE);
    }

    @Override
    public void displayNoSuccesses() {
        recyclerView.setVisibility(android.view.View.INVISIBLE);
        emptyListTv.setVisibility(android.view.View.VISIBLE);

    }

    @Override
    public void displaySuccesses(ArrayList<Success> successList) {
        successAdapter.updateSuccessList(successList);
        recyclerView.setVisibility(android.view.View.VISIBLE);
        emptyListTv.setVisibility(android.view.View.INVISIBLE);
    }

    @Override
    public void updateAdapterList(ArrayList<Success> successList) {
        successAdapter.updateSuccessList(successList);
    }

    @Override
    public void undoToRemove(int position) {
        successAdapter.notifyItemInserted(position);
        recyclerView.scrollToPosition(position);
    }

    @Override
    public void successRemoved(int position) {
        successAdapter.notifyItemRemoved(position);
    }

    @Override
    public void displaySuccessChanged() {
        successAdapter.notifyItemChanged(clickedPosition);
    }

    @Override
    public void hideSearchBar() {
        action = getSupportActionBar();
        handleSoftKeyboard();
        if (action != null) {
            action.setDisplayShowCustomEnabled(false);
            action.setDisplayShowTitleEnabled(true);
        }
        searchAction.setIcon(ContextCompat.getDrawable(this, R.drawable.ic_search));
        searchBox = null;

    }

    @Override
    public void displaySearchBar() {
        action = getSupportActionBar();

        if (action != null) {
            action.setDisplayShowCustomEnabled(true);
        }
        final ViewGroup nullParent = null;
        android.view.View view = getLayoutInflater().inflate(R.layout.search_bar, nullParent);
        ActionBar.LayoutParams layoutParams = new ActionBar.LayoutParams(ActionBar.LayoutParams.MATCH_PARENT,
                ActionBar.LayoutParams.MATCH_PARENT);
        if (action != null) {
            action.setCustomView(view, layoutParams);
            action.setDisplayShowTitleEnabled(false);
            searchBox = action.getCustomView().findViewById(R.id.edtSearch);
            showSoftKeyboard();

        }

        searchBox.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                presenter.setSearchTerm(getSearchText());
                presenter.loadSuccesses();
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        searchBox.setOnEditorActionListener(new TextView.OnEditorActionListener() {
                                                @Override
                                                public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                                                    presenter.setSearchTerm(getSearchText());
                                                    presenter.loadSuccesses();
                                                    hideSearchBar();

                                                    return true;
                                                }
                                            }
        );

        searchBox.requestFocus();
        searchAction.setIcon(ContextCompat.getDrawable(this, R.drawable.ic_close));

    }

    @Override
    public void displayUpdatedSuccesses() {
        successAdapter.notifyDataSetChanged();
    }

    @Override
    public void displayCategory(String category) {

        Intent intent = new Intent(SuccessesActivity.this, InsertSuccessActivity.class);
        intent.putExtra("categoryName", category);
        startActivityForResult(intent, REQUEST_CODE_INSERT);
        floatingActionsMenu.collapse();

    }

    @Override
    public void displaySlider(ArrayList<Success> successList) {
        Intent intent = new Intent(SuccessesActivity.this, SuccessSliderActivity.class);
        intent.putExtra("searchfilter", presenter.getSearchFilter());
        intent.putExtra("position", clickedPosition);
        startActivity(intent);
    }

    private void handleSoftKeyboard() {
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        if (imm != null && searchBox != null) {
            imm.hideSoftInputFromWindow(searchBox.getWindowToken(), 0);
        }
    }

    private void showSoftKeyboard() {
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);

        if (imm != null) {
            imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, InputMethodManager.SHOW_IMPLICIT);
        }
    }



}
