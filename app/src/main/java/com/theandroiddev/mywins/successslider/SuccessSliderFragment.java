package com.theandroiddev.mywins.successslider;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.widget.ImageView;
import android.widget.TextView;

import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;
import com.theandroiddev.mywins.R;
import com.theandroiddev.mywins.UI.activities.ImageActivity;
import com.theandroiddev.mywins.UI.activities.SuccessImageAdapter;
import com.theandroiddev.mywins.data.models.Success;
import com.theandroiddev.mywins.data.models.SuccessImage;
import com.theandroiddev.mywins.data.repositories.DatabaseSuccessesRepository;
import com.theandroiddev.mywins.utils.DrawableSelector;

import java.util.ArrayList;

/**
 * Created by jakub on 27.10.17.
 */

public class SuccessSliderFragment extends Fragment implements SuccessImageAdapter.OnSuccessImageClickListener {

    TextView titleTv, dateStartedTv, dateEndedTv, categoryTv, descriptionTv;
    ImageView categoryIv, importanceIv;
    RecyclerView recyclerView;
    DrawableSelector drawableSelector;
    SuccessImageAdapter successImageAdapter;
    Success s;
    ArrayList<SuccessImage> successImageList;

    SuccessSliderContract.SuccessImageLoader successImageLoader;

    public static void initImageLoader(Context context) {
        ImageLoaderConfiguration.Builder config = new ImageLoaderConfiguration.Builder(context);
        config.threadPriority(Thread.NORM_PRIORITY - 2);
        config.denyCacheImageMultipleSizesInMemory();
        config.diskCacheFileNameGenerator(new Md5FileNameGenerator());
        config.diskCacheSize(50 * 1024 * 1024);
        config.tasksProcessingOrder(QueueProcessingType.LIFO);
        ImageLoader.getInstance().init(config.build());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.content_show_success, container, false);

        drawableSelector = new DrawableSelector(getContext());
        titleTv = view.findViewById(R.id.item_title);
        dateStartedTv = view.findViewById(R.id.item_date_started);
        dateEndedTv = view.findViewById(R.id.item_date_ended);
        categoryTv = view.findViewById(R.id.item_category);
        descriptionTv = view.findViewById(R.id.show_description);
        categoryIv = view.findViewById(R.id.item_category_iv);
        importanceIv = view.findViewById(R.id.item_importance_iv);
        recyclerView = view.findViewById(R.id.show_image_recycler_view);

        successImageLoader = new SuccessImageLoader();
        successImageLoader.setRepository(new DatabaseSuccessesRepository(getContext()));


        initImageLoader(getContext());
        initRecycler();
        initViews();
        initAnimation();

        return view;
    }

    private void initAnimation() {

        AlphaAnimation fadeIn = new AlphaAnimation(0.0f, 1.0f);
        descriptionTv.startAnimation(fadeIn);
        fadeIn.setDuration(375);
        fadeIn.setFillAfter(true);
    }

    @Override
    public void onResume() {
        super.onResume();

        initViews();

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    private void initViews() {

        Bundle bundle = this.getArguments();
        String id = bundle.getString("id");

        s = successImageLoader.getSuccess(id);

        successImageList = successImageLoader.getSuccessImages(id);

        titleTv.setTag(s.getId());
        titleTv.setText(s.getTitle());
        categoryTv.setText(s.getCategory());
        descriptionTv.setText(s.getDescription());
        dateStartedTv.setText(s.getDateStarted());
        dateEndedTv.setText(s.getDateEnded());
        importanceIv.setTag(s.getImportance());

        drawableSelector.selectCategoryImage(categoryIv, s.getCategory(), categoryTv);
        drawableSelector.selectImportanceImage(importanceIv, s.getImportance());

        successImageAdapter = new SuccessImageAdapter(successImageList, this, R.layout.success_image_layout, getContext());
        recyclerView.setAdapter(successImageAdapter);
        successImageAdapter.notifyDataSetChanged();
    }

    private void initRecycler() {

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setHasFixedSize(true);

    }

    @Override
    public void onSuccessImageClick(SuccessImage successImage, ImageView successImageIv, int position, ConstraintLayout constraintLayout, CardView cardView) {

        Intent intent = new Intent(getActivity(), ImageActivity.class);
        ArrayList<String> imagePaths = new ArrayList<>();

        for (int i = 0; i < successImageList.size(); i++) {
            imagePaths.add(successImageList.get(i).getImagePath());
        }

        intent.putStringArrayListExtra("imagePaths", imagePaths);
        intent.putExtra("position", position);
        startActivity(intent);
    }

    @Override
    public void onSuccessImageLongClick(SuccessImage successImage, ImageView successImageIv, int position, ConstraintLayout constraintLayout, CardView cardView) {

    }

}
