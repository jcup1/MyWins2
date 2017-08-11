package com.example.jakubchmiel.mywins;

import android.animation.Animator;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.Toast;

import com.getbase.floatingactionbutton.FloatingActionsMenu;

import java.util.ArrayList;
import java.util.List;

import io.codetail.animation.ViewAnimationUtils;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private static final String TAG = "MainActivity";
    FloatingActionsMenu floatingActionsMenu;
    private RecyclerView recyclerView;
    private List<Success> successes;
    private SuccessAdapter successAdapter;
    ItemTouchHelper.SimpleCallback simpleCallback = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {

        @Override
        public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
            return false;
        }

        @Override
        public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {

            int pos = viewHolder.getAdapterPosition();
            successes.remove(pos);
            successAdapter.notifyItemRemoved(pos);

        }
    };
    private View shadowView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        initFABs();
        initRecycler();
        initCircularReveal();
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
    protected void onStart() {
        super.onStart();

    }

    private void initFABs() {

        final com.getbase.floatingactionbutton.FloatingActionButton actionKnowledge = (com.getbase.floatingactionbutton.FloatingActionButton) findViewById(R.id.action_knowledge);
        final com.getbase.floatingactionbutton.FloatingActionButton actionSport = (com.getbase.floatingactionbutton.FloatingActionButton) findViewById(R.id.action_sport);
        final com.getbase.floatingactionbutton.FloatingActionButton actionJourney = (com.getbase.floatingactionbutton.FloatingActionButton) findViewById(R.id.action_journey);
        final com.getbase.floatingactionbutton.FloatingActionButton actionBusiness = (com.getbase.floatingactionbutton.FloatingActionButton) findViewById(R.id.action_business);
        final com.getbase.floatingactionbutton.FloatingActionButton actionVideo = (com.getbase.floatingactionbutton.FloatingActionButton) findViewById(R.id.action_video);

        actionKnowledge.setSize(com.getbase.floatingactionbutton.FloatingActionButton.SIZE_MINI);
        actionSport.setSize(com.getbase.floatingactionbutton.FloatingActionButton.SIZE_MINI);
        actionJourney.setSize(com.getbase.floatingactionbutton.FloatingActionButton.SIZE_MINI);
        actionBusiness.setSize(com.getbase.floatingactionbutton.FloatingActionButton.SIZE_MINI);
        actionVideo.setSize(com.getbase.floatingactionbutton.FloatingActionButton.SIZE_MINI);

        int color_video = ResourcesCompat.getColor(getResources(), R.color.video, null);
        int color_business = ResourcesCompat.getColor(getResources(), R.color.business, null);
        int color_journey = ResourcesCompat.getColor(getResources(), R.color.journey, null);
        int color_sport = ResourcesCompat.getColor(getResources(), R.color.sport, null);
        int color_knowledge = ResourcesCompat.getColor(getResources(), R.color.knowledge, null);
        int color_white = ResourcesCompat.getColor(getResources(), R.color.white, null);

        Drawable videoDrawable = ResourcesCompat.getDrawable(getResources(), R.drawable.ic_video, null);
        Drawable businessDrawable = ResourcesCompat.getDrawable(getResources(), R.drawable.ic_business, null);
        Drawable journeyDrawable = ResourcesCompat.getDrawable(getResources(), R.drawable.ic_journey, null);
        Drawable sportDrawable = ResourcesCompat.getDrawable(getResources(), R.drawable.ic_sport, null);
        Drawable knowledgeDrawable = ResourcesCompat.getDrawable(getResources(), R.drawable.ic_knowledge, null);


        if (videoDrawable != null) {
            videoDrawable.setColorFilter(new PorterDuffColorFilter(color_white, PorterDuff.Mode.SRC_IN));
            actionVideo.setIconDrawable(videoDrawable);
        }
        if (businessDrawable != null) {
            businessDrawable.setColorFilter(new PorterDuffColorFilter(color_white, PorterDuff.Mode.SRC_IN));
            actionBusiness.setIconDrawable(businessDrawable);
        }
        if (journeyDrawable != null) {
            journeyDrawable.setColorFilter(new PorterDuffColorFilter(color_white, PorterDuff.Mode.SRC_IN));
            actionJourney.setIconDrawable(journeyDrawable);
        }
        if (sportDrawable != null) {
            sportDrawable.setColorFilter(new PorterDuffColorFilter(color_white, PorterDuff.Mode.SRC_IN));
            actionSport.setIconDrawable(sportDrawable);
        }
        if (knowledgeDrawable != null) {
            knowledgeDrawable.setColorFilter(new PorterDuffColorFilter(color_white, PorterDuff.Mode.SRC_IN));
            actionKnowledge.setIconDrawable(knowledgeDrawable);
        }

        actionVideo.setColorNormal(color_video);
        actionBusiness.setColorNormal(color_business);
        actionJourney.setColorNormal(color_journey);
        actionSport.setColorNormal(color_sport);
        actionKnowledge.setColorNormal(color_knowledge);

        actionKnowledge.setOnClickListener(this);
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

        initVariables();
    }

    private void initVariables() {
        successes = new ArrayList<>();
        successAdapter = new SuccessAdapter(successes, R.layout.item_layout, getApplicationContext());
        recyclerView.setAdapter(successAdapter);

        successes.add(new Success("First yt video", "Video", "medium", "", "07.08.17"));
        successes.add(new Success("Big sale", "Money", "huge", "", "06.08.17"));
        successes.add(new Success("10km run", "Sport", "big", "", "05.08.17"));
        successes.add(new Success("Graduated", "Knowledge", "small", "", "04.08.17"));
        successAdapter.notifyDataSetChanged();
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
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View shadowView) {
        com.getbase.floatingactionbutton.FloatingActionButton fab = (com.getbase.floatingactionbutton.FloatingActionButton) shadowView;

        switch (fab.getId()) {

            case R.id.action_knowledge:
                categoryPicked("knowledge");
                break;
            case R.id.action_sport:
                categoryPicked("sport");
                break;
            case R.id.action_journey:
                categoryPicked("journey");
                break;
            case R.id.action_business:
                categoryPicked("business");
                break;
            case R.id.action_video:
                categoryPicked("video");
                break;

            default:
                break;

        }

    }

    void categoryPicked(String name) {
        Toast.makeText(this, "Category picked " + name, Toast.LENGTH_SHORT).show();

    }

}
