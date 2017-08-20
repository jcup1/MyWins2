package com.theandroiddev.mywins;

import android.animation.Animator;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
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
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.getbase.floatingactionbutton.FloatingActionsMenu;

import java.util.ArrayList;
import java.util.List;

import io.codetail.animation.ViewAnimationUtils;

public class MainActivity extends AppCompatActivity implements View.OnClickListener, SuccessAdapter.OnItemClickListener {

    public static final String EXTRA_SUCCESS_ITEM = "success_item";
    public static final String EXTRA_INSERT_SUCCESS_ITEM = "insert_success_item";
    public static final String EXTRA_SHOW_SUCCESS_ITEM = "show_success_item";
    public static final String EXTRA_EDIT_SUCCESS_ITEM = "edit_success_item";

    public static final String EXTRA_SUCCESS_TITLE = "title";
    public static final String EXTRA_SUCCESS_CATEGORY_IV = "category_iv";
    public static final String EXTRA_SUCCESS_CATEGORY = "category";
    public static final String EXTRA_SUCCESS_DATE_STARTED = "date_started";
    public static final String EXTRA_SUCCESS_DATE_ENDED = "date_ended";
    public static final String EXTRA_SUCCESS_IMPORTANCE_IV = "importance_iv";
    public static final String EXTRA_SUCCESS_CONSTRAINT = "success_constraint";
    public static final String EXTRA_SUCCESS_CARD_VIEW = "success_card_view";
    static final int INSERT_SUCCESS_REQUEST = 1;
    static final int SHOW_SUCCESS_REQUEST = 2;
    private static final int EDIT_SUCCESS_REQUEST = 3;
    private static final String TAG = "MainActivity";
    SharedPreferences prefs = null;
    FloatingActionsMenu floatingActionsMenu;
    private MenuItem mSearchAction;
    private boolean isSearchOpened = false;
    private EditText edtSeach;
    private RecyclerView recyclerView;
    private List<Success> successes;
    private List<Success> successesToRemove;
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

    @Override
    protected void onPause() {
        super.onPause();
        remove();

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
        Toolbar toolbar = (Toolbar) findViewById(R.id.show_toolbar);
        setSupportActionBar(toolbar);
        initFABs();
        initRecycler();
        initCircularReveal();
        initVariables();
        prefs = getSharedPreferences("example.jakubchmiel.mywins", MODE_PRIVATE);

    }

    @Override
    protected void onPostResume() {
        super.onPostResume();

        if (prefs.getBoolean("firstrun", true)) {
            insertDummyData();
            prefs.edit().putBoolean("firstrun", false).apply();
        }
    }

    private void insertDummyData() {
        Success video = new Success("Recorded First Video", "Video", 3, "I always wanted to create youtube video and finally Hot Shitty Challenge is live...", "17-05-20", "17-05-25");
        Success money = new Success("25.000$ Sale", "Money", 4, "It's my first sold company. I grew it in 10 years. I denied first offer which was 5.000$ and it's one of my the best choices in my life. I...", "17-05-22", "17-05-30");
        Success journey = new Success("Visited Wroclaw", "Journey", 2, "I travel now and then. Met girl but she introduced me her friend: 'Zoned'. Guess I've got to try again in 2018.", "16-04-15", "16-04-20");
        Success learn = new Success("Learned Java", "Learn", 3, "This language is easier than I thought and now I'm multilingual! :O", "15-03-12", "15-05-01");
        Success sport = new Success("20 km marathon", "Sport", 4, "I burned a lot of calories. Fridge is empty tonight...", "16-02-21", "16-02-21");
        save(video);
        save(money);
        save(journey);
        save(learn);
        save(sport);
        getSuccesses(null, sortType);
        successAdapter.notifyDataSetChanged();
    }

    private void save(Success success) {

        DBAdapter dbAdapter = new DBAdapter(this);
        dbAdapter.openDB();
        dbAdapter.add(success);
        Log.e(TAG, "save: " + success);
        getSuccesses(null, sortType);
        dbAdapter.closeDB();

    }

    private void initCircularReveal() {
        shadowView = findViewById(R.id.shadow_view);
        shadowView.setVisibility(View.GONE);

        floatingActionsMenu = (FloatingActionsMenu) findViewById(R.id.multiple_actions);
        floatingActionsMenu.setOnFloatingActionsMenuUpdateListener(new FloatingActionsMenu.OnFloatingActionsMenuUpdateListener() {
            @Override
            public void onMenuExpanded() {
                showCircularReveal(shadowView);
            }

            @Override
            public void onMenuCollapsed() {
                hideCircularReveal(shadowView);
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

    @Override
    protected void onResume() {
        super.onResume();
        Log.e(TAG, "onResume: " + "RESUME");
        getSuccesses(null, sortType);

    }


    private void initFABs() {

        final com.getbase.floatingactionbutton.FloatingActionButton actionLearn = (com.getbase.floatingactionbutton.FloatingActionButton) findViewById(R.id.action_learn);
        final com.getbase.floatingactionbutton.FloatingActionButton actionSport = (com.getbase.floatingactionbutton.FloatingActionButton) findViewById(R.id.action_sport);
        final com.getbase.floatingactionbutton.FloatingActionButton actionJourney = (com.getbase.floatingactionbutton.FloatingActionButton) findViewById(R.id.action_journey);
        final com.getbase.floatingactionbutton.FloatingActionButton actionBusiness = (com.getbase.floatingactionbutton.FloatingActionButton) findViewById(R.id.action_money);
        final com.getbase.floatingactionbutton.FloatingActionButton actionVideo = (com.getbase.floatingactionbutton.FloatingActionButton) findViewById(R.id.action_video);

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

        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setHasFixedSize(true);

        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(simpleCallback);
        itemTouchHelper.attachToRecyclerView(recyclerView);

    }

    private void initVariables() {

        sortType = Constants.DATE_STARTED_DESC;
        getSuccesses(null, sortType);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement

        if (id == R.id.action_search) {
            handleMenuSearch();
            return true;
        }

        if (id == R.id.action_date_started) {


            if (!sortType.equals(Constants.DATE_STARTED_ASC)) {
                sortType = Constants.DATE_STARTED_ASC;
                getSuccesses(null, sortType);
                return true;
            }

            if (!sortType.equals(Constants.DATE_STARTED_DESC)) {
                sortType = Constants.DATE_STARTED_DESC;
                getSuccesses(null, sortType);
                return true;
            }

        }

        if (id == R.id.action_date_ended) {

            if (!sortType.equals(Constants.DATE_ENDED_ASC)) {
                sortType = Constants.DATE_ENDED_ASC;
                getSuccesses(null, sortType);
                return true;
            }

            if (!sortType.equals(Constants.DATE_ENDED_DESC)) {
                sortType = Constants.DATE_ENDED_DESC;
                getSuccesses(null, sortType);
                return true;
            }

        }

        if (id == R.id.action_title) {

            if (!sortType.equals(Constants.TITLE_ASC)) {
                sortType = Constants.TITLE_ASC;
                getSuccesses(null, sortType);
                return true;
            }

            if (!sortType.equals(Constants.TITLE_DESC)) {
                sortType = Constants.TITLE_DESC;
                getSuccesses(null, sortType);
                return true;
            }
        }

        if (id == R.id.action_importance) {

            if (!sortType.equals(Constants.IMPORTANCE_ASC)) {
                sortType = Constants.IMPORTANCE_ASC;
                getSuccesses(null, sortType);
                return true;
            }

            if (!sortType.equals(Constants.IMPORTANCE_DESC)) {
                sortType = Constants.IMPORTANCE_DESC;
                getSuccesses(null, sortType);
                return true;
            }

        }

        if (id == R.id.action_description) {

            if (!sortType.equals(Constants.DESCRIPTION_ASC)) {
                sortType = Constants.DESCRIPTION_ASC;
                getSuccesses(null, sortType);
                return true;
            }

            if (!sortType.equals(Constants.DESCRIPTION_DESC)) {
                sortType = Constants.DESCRIPTION_DESC;
                getSuccesses(null, sortType);
                return true;
            }

        }


        return super.onOptionsItemSelected(item);
    }

    protected void handleMenuSearch() {
        ActionBar action = getSupportActionBar(); //get the actionbar

        if (isSearchOpened) { //test if the search is open

            action.setDisplayShowCustomEnabled(false); //disable a custom view inside the actionbar
            action.setDisplayShowTitleEnabled(true); //show the title in the action bar

            //hides the keyboard
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(edtSeach.getWindowToken(), 0);

            //add the search icon in the action bar
            mSearchAction.setIcon(ContextCompat.getDrawable(this, R.drawable.ic_search));

            isSearchOpened = false;
            getSuccesses(null, sortType);
        } else { //open the search entry

            action.setDisplayShowCustomEnabled(true); //enable it to display a
            // custom view in the action bar.
            action.setCustomView(R.layout.search_bar);//add the custom view
            action.setDisplayShowTitleEnabled(false); //hide the title

            edtSeach = action.getCustomView().findViewById(R.id.edtSearch); //the text editor

            //this is a listener to do a search when the user clicks on search button
            edtSeach.setOnEditorActionListener(new TextView.OnEditorActionListener() {
                @Override
                public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                    if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                        doSearch(edtSeach);
                        return true;
                    }
                    return false;
                }
            });


            edtSeach.requestFocus();

            //open the keyboard focused in the edtSearch
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.showSoftInput(edtSeach, InputMethodManager.SHOW_IMPLICIT);


            //add the close icon
            mSearchAction.setIcon(ContextCompat.getDrawable(this, R.drawable.ic_close));

            isSearchOpened = true;
        }
    }

    private void doSearch(EditText edtSeach) {
        getSuccesses(edtSeach.getText().toString(), sortType);
    }

    @Override
    public void onClick(View shadowView) {
        com.getbase.floatingactionbutton.FloatingActionButton fab = (com.getbase.floatingactionbutton.FloatingActionButton) shadowView;

        switch (fab.getId()) {

            case R.id.action_learn:
                categoryPicked("Learn");
                break;
            case R.id.action_sport:
                categoryPicked("Sport");
                break;
            case R.id.action_journey:
                categoryPicked("Journey");
                break;
            case R.id.action_money:
                categoryPicked("Money");
                break;
            case R.id.action_video:
                categoryPicked("Video");
                break;

            default:
                break;

        }

    }

    void categoryPicked(String categoryName) {

        Intent intent = new Intent(MainActivity.this, InsertSuccess.class);
        intent.putExtra("categoryName", categoryName);
        startActivityForResult(intent, EDIT_SUCCESS_REQUEST);
        floatingActionsMenu.collapse();

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == EDIT_SUCCESS_REQUEST) {
            if (resultCode == Activity.RESULT_OK) {


                Success s = data.getExtras().getParcelable(MainActivity.EXTRA_INSERT_SUCCESS_ITEM);
                save(s);

                successAdapter.notifyDataSetChanged();

            }
            if (resultCode == Activity.RESULT_CANCELED) {
                //TODO handle
            }
        }

    }


    private void toRemove(int position) {
        showSnackbar(position);

    }

    private void showSnackbar(final int position) {
        final Success success = successes.get(position);
        Snackbar snackbar = Snackbar
                .make(recyclerView, "SUCCESS REMOVED", Snackbar.LENGTH_LONG)
                .setAction("UNDO", new View.OnClickListener() {
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
        DBAdapter dbAdapter = new DBAdapter(this);
        dbAdapter.openDB();
        dbAdapter.remove(successesToRemove);
        dbAdapter.closeDB();
    }

    private void getSuccesses(String searchTerm, String sort) {

        successes = new ArrayList<>();
        successesToRemove = new ArrayList<>();
        successes.clear();

        DBAdapter dbAdapter = new DBAdapter(this);
        dbAdapter.openDB();
        Success success;
        Cursor cursor = dbAdapter.retrieve(searchTerm, sort);

        while (cursor.moveToNext()) {
            int id = cursor.getInt(0);
            String title = cursor.getString(1);
            String category = cursor.getString(2);
            int importance = cursor.getInt(3);
            String description = cursor.getString(4);
            String dateStarted = cursor.getString(5);
            String dateEnded = cursor.getString(6);

            success = new Success(title, category, importance, description, dateStarted, dateEnded);
            success.setId(id);
            successes.add(success);

        }

        dbAdapter.closeDB();

        successAdapter = new SuccessAdapter(successes, R.layout.item_layout, getApplicationContext(), this);
        recyclerView.setAdapter(successAdapter);
        successAdapter.notifyDataSetChanged();
    }

    @Override
    public void onItemClick(Success success, TextView titleTv, TextView categoryTv, TextView dateStartedTv, TextView dateEndedTv, ImageView categoryIv, ImageView importanceIv, ConstraintLayout constraintLayout, CardView cardView) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            showSuccessAnimation(success, titleTv, categoryTv, dateStartedTv, dateEndedTv, categoryIv, importanceIv, constraintLayout, cardView);

        } else {
            Log.e(TAG, "onItemClick: " + success.getId());
            showSuccess(success);
        }

    }

    private void showSuccess(Success success) {

        Intent showSuccessIntent = new Intent(MainActivity.this, ShowSuccess.class);

        showSuccessIntent.putExtra(EXTRA_SUCCESS_ITEM, success);

        startActivity(showSuccessIntent);

    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    private void showSuccessAnimation(Success success, TextView titleTv, TextView categoryTv, TextView dateStartedTv, TextView dateEndedTv, ImageView categoryIv, ImageView importanceIv, ConstraintLayout constraintLayout, CardView cardView) {


        Intent showSuccessIntent = new Intent(MainActivity.this, ShowSuccess.class);


        showSuccessIntent.putExtra(EXTRA_SUCCESS_ITEM, success);

        Pair<View, String> p1, p2, p3, p4, p5, p6, p7;
        p1 = Pair.create((View) titleTv, EXTRA_SUCCESS_TITLE);
        p2 = Pair.create((View) categoryTv, EXTRA_SUCCESS_CATEGORY);
        p3 = Pair.create((View) dateStartedTv, EXTRA_SUCCESS_DATE_STARTED);
        p4 = Pair.create((View) dateEndedTv, EXTRA_SUCCESS_DATE_ENDED);
        p5 = Pair.create((View) categoryIv, EXTRA_SUCCESS_CATEGORY_IV);
        p6 = Pair.create((View) importanceIv, EXTRA_SUCCESS_IMPORTANCE_IV);
        p7 = Pair.create((View) cardView, EXTRA_SUCCESS_CARD_VIEW);

//        getWindow().setEnterTransition(new Fade(Fade.IN));
//        getWindow().setExitTransition(new Fade(Fade.IN));
        ActivityOptionsCompat activityOptionsCompat = ActivityOptionsCompat.makeSceneTransitionAnimation(
                this,
                p1, p2, p3, p4, p5, p6, p7);

        startActivity(showSuccessIntent, activityOptionsCompat.toBundle());

    }





}
