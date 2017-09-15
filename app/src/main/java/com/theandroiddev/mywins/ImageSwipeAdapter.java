package com.theandroiddev.mywins;

import android.content.Context;
import android.graphics.drawable.BitmapDrawable;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import java.io.File;
import java.util.ArrayList;

/**
 * Created by jakub on 30.08.17.
 */

class ImageSwipeAdapter extends PagerAdapter {
    private static final String TAG = "ImageSwipeAdapter";

    private Context context;
    private ArrayList<String> imagePaths;


    ImageSwipeAdapter(Context context, ArrayList<String> imagePaths) {
        this.context = context;
        this.imagePaths = imagePaths;
    }


    @Override
    public int getCount() {
        return imagePaths.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View item_view = layoutInflater.inflate(R.layout.image_big, container, false);
        ImageView imageView = item_view.findViewById(R.id.image_big);

        String path = imagePaths.get(position);
        Picasso.with(context).load(new File(path)).into(imageView);

        container.addView(item_view);
        return item_view;

    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {

        View view = (View) object;
        ImageView imageView = view.findViewById(R.id.image_big);
        BitmapDrawable bitmapDrawable = (BitmapDrawable) imageView.getDrawable();
        if (bitmapDrawable != null && bitmapDrawable.getBitmap() != null) {
            bitmapDrawable.getBitmap().recycle();
        }
        container.removeView(view);

    }


}
