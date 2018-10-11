package com.theandroiddev.mywins.presentation.image

import android.app.Activity
import android.content.Context
import android.graphics.Bitmap
import android.os.Parcelable
import android.support.constraint.ConstraintLayout
import android.support.v4.view.PagerAdapter
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.Toast

import com.nostra13.universalimageloader.core.DisplayImageOptions
import com.nostra13.universalimageloader.core.ImageLoader
import com.nostra13.universalimageloader.core.assist.FailReason
import com.nostra13.universalimageloader.core.assist.ImageScaleType
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer
import com.nostra13.universalimageloader.core.download.ImageDownloader
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener
import com.theandroiddev.mywins.R
import com.theandroiddev.mywins.utils.TouchImageView

import java.util.ArrayList

import javax.inject.Inject

import butterknife.ButterKnife

/**
 * Created by jakub on 27.10.17.
 */

class ImageSwipeAdapter(context: Context, private val imagePaths: ArrayList<String>) : PagerAdapter() {

    private val inflater: LayoutInflater = LayoutInflater.from(context)

    private val options: DisplayImageOptions = DisplayImageOptions.Builder()
            .showImageForEmptyUri(R.drawable.ic_empty)
            .showImageOnFail(R.drawable.ic_error)
            .resetViewBeforeLoading(true)
            .cacheOnDisk(true)
            .imageScaleType(ImageScaleType.EXACTLY)
            .bitmapConfig(Bitmap.Config.RGB_565)
            .considerExifParams(true)
            .displayer(FadeInBitmapDisplayer(300))
            .build()

    override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
        container.removeView(`object` as View)
    }

    override fun getCount(): Int {
        return imagePaths.size
    }

    override fun instantiateItem(view: ViewGroup, position: Int): Any {
        val imageLayout = inflater.inflate(R.layout.item_pager_image, view, false)

        val touchImageView = imageLayout.findViewById<TouchImageView>(R.id.image)
        val spinner = imageLayout.findViewById<ProgressBar>(R.id.loading)

        ImageLoader.getInstance().displayImage(ImageDownloader.Scheme.FILE.wrap(imagePaths[position]), touchImageView, options, object : SimpleImageLoadingListener() {
            override fun onLoadingStarted(imageUri: String?, view: View?) {
                spinner.visibility = View.VISIBLE
            }

            override fun onLoadingFailed(imageUri: String?, view: View?, failReason: FailReason?) {
                val message =
                when (failReason?.type) {
                    FailReason.FailType.IO_ERROR -> "Input/Output error"
                    FailReason.FailType.DECODING_ERROR -> "Image can't be decoded"
                    FailReason.FailType.NETWORK_DENIED -> "Downloads are denied"
                    FailReason.FailType.OUT_OF_MEMORY -> "Out Of Memory error"
                    FailReason.FailType.UNKNOWN -> "Unknown error"
                    else -> { null }
                }
                Toast.makeText(view!!.context, message, Toast.LENGTH_SHORT).show()

                spinner.visibility = View.GONE

            }

            override fun onLoadingComplete(imageUri: String?, view: View?, loadedImage: Bitmap?) {
                spinner.visibility = View.GONE
                //strange bug (I have to set touchImageView visibility to GONE and change it to VISIBLE
                //after ProgressBar is set to GONE. Otherwise it won't work
                touchImageView.visibility = View.VISIBLE

            }
        })

        view.addView(imageLayout)
        return imageLayout
    }

    override fun isViewFromObject(view: View, `object`: Any): Boolean {
        return view == `object`
    }

    override fun restoreState(state: Parcelable?, loader: ClassLoader?) {

    }

    override fun saveState(): Parcelable? {
        return null
    }

}
