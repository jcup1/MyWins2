package com.theandroiddev.mywins;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.drawable.BitmapDrawable;
import android.media.ExifInterface;
import android.support.v4.view.PagerAdapter;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;

/**
 * Created by jakub on 30.08.17.
 */

public class ImageSwipeAdapter extends PagerAdapter {
    private static final String TAG = "ImageSwipeAdapter";

    private Context context;
    private LayoutInflater layoutInflater;
    private ArrayList<String> imagePaths;


    public ImageSwipeAdapter(Context context, ArrayList<String> imagePaths) {
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
        layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View item_view = layoutInflater.inflate(R.layout.image_big, container, false);
        ImageView imageView = item_view.findViewById(R.id.image_big);

        Log.e(TAG, "instantiateItem: " + imagePaths);

        String path = imagePaths.get(position);
        ExifInterface exif;
        ByteArrayOutputStream byteArrayOutputStream;
        byteArrayOutputStream = new ByteArrayOutputStream();

        Bitmap bitmap = BitmapFactory.decodeFile(imagePaths.get(position));
        bitmap.compress(Bitmap.CompressFormat.JPEG, 40, byteArrayOutputStream);
        byte[] BYTE = byteArrayOutputStream.toByteArray();
        Bitmap bitmap2 = BitmapFactory.decodeByteArray(BYTE, 0, BYTE.length);

// Write 'bitmap' to file using JPEG and 80% quality hint for JPEG:


        try {
            exif = new ExifInterface(path);
            int orientation = exif.getAttributeInt(
                    ExifInterface.TAG_ORIENTATION, 0);
            Matrix matrix = new Matrix();
            if (orientation == 6) {
                matrix.postRotate(90);
            } else if (orientation == 3) {
                matrix.postRotate(180);
            } else if (orientation == 8) {
                matrix.postRotate(270);
            }
            bitmap2 = Bitmap.createBitmap(bitmap2, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
        } catch (IOException | OutOfMemoryError e) {
            e.printStackTrace();
        }
        //Bitmap orientedBitmap = ExifUtil.rotateBitmap(imagePaths.get(position), bitmap);

        imageView.setImageBitmap(bitmap2);
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
        view = null;

    }


}
