package com.theandroiddev.mywins.successes;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.content.ContextCompat;
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
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.getbase.floatingactionbutton.FloatingActionButton;
import com.getbase.floatingactionbutton.FloatingActionsMenu;
import com.theandroiddev.mywins.InsertSuccessActivity;
import com.theandroiddev.mywins.MyWinsApplication;
import com.theandroiddev.mywins.R;
import com.theandroiddev.mywins.data.models.Success;
import com.theandroiddev.mywins.data.prefs.PreferencesHelper;
import com.theandroiddev.mywins.successslider.SuccessSliderActivity;
import com.theandroiddev.mywins.utils.SuccessesConfig;

import java.util.ArrayList;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.theandroiddev.mywins.utils.Constants.EXTRA_INSERT_SUCCESS_ITEM;
import static com.theandroiddev.mywins.utils.Constants.EXTRA_SUCCESS_CARD_VIEW;
import static com.theandroiddev.mywins.utils.Constants.EXTRA_SUCCESS_CATEGORY;
import static com.theandroiddev.mywins.utils.Constants.EXTRA_SUCCESS_CATEGORY_IV;
import static com.theandroiddev.mywins.utils.Constants.EXTRA_SUCCESS_DATE_ENDED;
import static com.theandroiddev.mywins.utils.Constants.EXTRA_SUCCESS_DATE_STARTED;
import static com.theandroiddev.mywins.utils.Constants.EXTRA_SUCCESS_IMPORTANCE_IV;
import static com.theandroiddev.mywins.utils.Constants.EXTRA_SUCCESS_TITLE;
import static com.theandroiddev.mywins.utils.Constants.NOT_ACTIVE;
import static com.theandroiddev.mywins.utils.Constants.REQUEST_CODE_INSERT;
import static com.theandroiddev.mywins.utils.Constants.REQUEST_CODE_SLIDER;
import static com.theandroiddev.mywins.utils.Constants.SNACK_SUCCESS_NOT_ADDED;
import static com.theandroiddev.mywins.utils.Constants.SNACK_SUCCESS_REMOVED;
import static com.theandroiddev.mywins.utils.Constants.SNACK_UNDO;

public class SuccessesActivity extends AppCompatActivity implements android.view.View.OnClickListener, SuccessAdapter.OnItemClickListener, SuccessesContract.View {
    private static final String TAG = "SuccessesActivity";

    private static final String EXTRA_TRIGGER_SYNC_FLAG =
            "com.theandroiddev.mywins.UI.Activities.SuccessesActivity.EXTRA_TRIGGER_SYNC_FLAG";
    FloatingActionsMenu floatingActionsMenu;
    SuccessAdapter successAdapter;
    @Inject
    SuccessesContract.Presenter presenter;
    @Inject
    SuccessesConfig successesConfig;

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

    ActionBar action;
    Drawable videoDrawable, moneyDrawable, journeyDrawable, sportDrawable, learnDrawable;
    FloatingActionButton actionLearn, actionSport, actionJourney, actionBusiness, actionVideo;
    ItemTouchHelper.SimpleCallback simpleCallback = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {

        @Override
        public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
            return false;
        }

        @Override
        public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {

            toRemove(viewHolder.getAdapterPosition());

        }
    };
    private MenuItem searchAction;
    private EditText searchBox;
    private int clickedPosition = NOT_ACTIVE;

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
        presenter.onDestroyActivity();
        super.onDestroy();

    }

    @Override
    protected void onResume() {
        super.onResume();
        presenter.onResumeActivity(this, clickedPosition, searchBox);

    }


    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        searchAction = menu.findItem(R.id.action_search);
        return super.onPrepareOptionsMenu(menu);
    }


    @Override
    public void onBackPressed() {

        if (floatingActionsMenu.isExpanded()) {
            floatingActionsMenu.collapse();
        } else if (searchBox != null) {

            presenter.handleMenuSearch();
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

        PreferencesHelper preferencesHelper = new PreferencesHelper(this);
        setSupportActionBar(toolbar);//1
        initFABs();
        initCircularReveal();
        initRecycler();
        presenter.onCreateActivity(getApplicationContext(), this, preferencesHelper);


    }

    private void initCircularReveal() {
        shadowView.setVisibility(android.view.View.GONE);

        floatingActionsMenu = findViewById(R.id.multiple_actions);

    }

    private void initFABs() {

        actionLearn = findViewById(R.id.action_learn);
        actionSport = findViewById(R.id.action_sport);
        actionJourney = findViewById(R.id.action_journey);
        actionBusiness = findViewById(R.id.action_money);
        actionVideo = findViewById(R.id.action_video);

        successesConfig.configFABs(getApplicationContext(),
                videoDrawable, moneyDrawable, journeyDrawable, sportDrawable, learnDrawable,
                actionLearn, actionSport, actionJourney, actionBusiness, actionVideo);

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

        presenter.selectCategory(fab.getId());

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

        if (requestCode == REQUEST_CODE_SLIDER) {
            if (resultCode == RESULT_OK) {
                onSliderResultSuccess(data);
            }
        }

    }

    private void onSliderResultSuccess(Intent data) {
        int position = data.getIntExtra("position", 0);

        recyclerView.scrollToPosition(position);

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

        this.clickedPosition = position;
        hideSoftKeyboard();
        presenter.startSlider(success, position, titleTv, categoryTv, dateStartedTv, dateEndedTv, categoryIv, importanceIv, constraintLayout, cardView);

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

    @Override
    public void displayDefaultSuccesses(ArrayList<Success> successList) {
        successAdapter.updateSuccessList(successList);
        presenter.setSuccessListVisible(recyclerView, emptyListTv);

    }

    @Override
    public void displayNoSuccesses() {
        presenter.setSuccessListInvisible(recyclerView, emptyListTv);
    }

    @Override
    public void displaySuccesses(ArrayList<Success> successList) {
        successAdapter.updateSuccessList(successList);
        presenter.setSuccessListVisible(recyclerView, emptyListTv);
    }

    @Override
    public void updateAdapterList(ArrayList<Success> successList) {
        successAdapter.updateSuccessList(successList);
    }

    @Override
    public void undoToRemove(int position) {
        presenter.loadSuccesses();
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
        hideSoftKeyboard();


        if (action != null) {
            action.setDisplayShowCustomEnabled(false);
            action.setDisplayShowTitleEnabled(true);
        }
        searchAction.setIcon(ContextCompat.getDrawable(this, R.drawable.ic_search));
        searchBox = null;
        presenter.clearSearch();
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
                                                    presenter.showSearch();

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
        startActivityForResult(intent, REQUEST_CODE_SLIDER);
    }

    @Override
    public void displaySliderAnimation(ArrayList<Success> successes, Success success, int position, TextView titleTv, TextView categoryTv, TextView dateStartedTv, TextView dateEndedTv, ImageView categoryIv, ImageView importanceIv, ConstraintLayout constraintLayout, CardView cardView) {

        Intent intent = new Intent(SuccessesActivity.this, SuccessSliderActivity.class);

        intent.putExtra("searchfilter", presenter.getSearchFilter());
        intent.putExtra("position", clickedPosition);

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

        startActivity(intent, activityOptionsCompat.toBundle());
    }

    @Override
    public void displaySearch() {

        hideSoftKeyboard();
    }

    private void hideSoftKeyboard() {
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        presenter.hideSoftKeyboard(searchBox, imm);

    }

    private void showSoftKeyboard() {
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        presenter.showSoftKeyboard(imm, searchBox);

    }

}
