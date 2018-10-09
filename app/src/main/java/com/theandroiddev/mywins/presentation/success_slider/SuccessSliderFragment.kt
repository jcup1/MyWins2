package com.theandroiddev.mywins.presentation.success_slider

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.constraint.ConstraintLayout
import android.support.v7.widget.CardView
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.View.INVISIBLE
import android.view.View.VISIBLE
import android.view.ViewGroup
import android.view.animation.AlphaAnimation
import android.widget.ImageView
import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator
import com.nostra13.universalimageloader.core.ImageLoader
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration
import com.nostra13.universalimageloader.core.assist.QueueProcessingType
import com.theandroiddev.mywins.R
import com.theandroiddev.mywins.core.mvp.MvpDaggerFragment
import com.theandroiddev.mywins.presentation.image.ImageActivity
import com.theandroiddev.mywins.presentation.image.SuccessImageAdapter
import com.theandroiddev.mywins.presentation.successes.SuccessImageModel
import com.theandroiddev.mywins.presentation.successes.SuccessModel
import com.theandroiddev.mywins.utils.DrawableSelector
import kotlinx.android.synthetic.main.content_show_success.*
import kotlinx.android.synthetic.main.success_layout.*

class SuccessSliderFragment : MvpDaggerFragment<SuccessSliderFragmentView,
        SuccessSliderBundle, SuccessSliderFragmentPresenter>(),
        SuccessImageAdapter.OnSuccessImageClickListener, SuccessSliderFragmentView {

    private var successImageAdapter: SuccessImageAdapter? = null
    private var actionHandler: ActionHandler? = null
    private var drawableSelector: DrawableSelector? = null

    private var id: Long? = null

    override fun onAttach(context: Context?) {
        super.onAttach(context)

        actionHandler = context as ActionHandler
        drawableSelector = DrawableSelector(context)

    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        super.onCreateView(inflater, container, savedInstanceState)

        return inflater.inflate(R.layout.content_show_success, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        no_images_tv.setOnClickListener {
            //TODO fix
            actionHandler?.onAddImage()
        }

        initImageLoader(context)
        initRecycler()
        initBundle()
        initAnimation()

    }

    private fun initBundle() {
        val bundle = this.arguments
        id = bundle?.getLong("id")
    }

    private fun initRecycler() {

        val linearLayoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        show_image_recycler_view.layoutManager = linearLayoutManager
        show_image_recycler_view.setHasFixedSize(true)

        successImageAdapter = SuccessImageAdapter(this, R.layout.success_image_layout, context)
        show_image_recycler_view.adapter = successImageAdapter

    }

    override fun displaySuccessData(success: SuccessModel, successImages: List<SuccessImageModel>) {

        item_title.text = success.title
        item_category.text = getString(success.category.res)
        show_description.text = success.description
        item_date_started.text = success.dateStarted
        item_date_ended.text = success.dateEnded

        drawableSelector?.selectCategoryImage(item_category_iv, success.category, item_category)
        drawableSelector?.selectImportanceImage(item_importance_iv, success.importance)

        if (successImages.isEmpty()) {
            no_images_tv.visibility = VISIBLE
        } else {
            no_images_tv.visibility = INVISIBLE
        }

        successImageAdapter?.successImages = successImages.toMutableList()
        successImageAdapter?.notifyDataSetChanged()

    }

    override fun startImageActivity(position: Int, imagePaths: ArrayList<String>) {

        val intent = Intent(activity, ImageActivity::class.java)
        intent.putStringArrayListExtra("imagePaths", imagePaths)
        intent.putExtra("position", position)
        startActivity(intent)

    }

    private fun initAnimation() {

        val fadeIn = AlphaAnimation(0.0f, 1.0f)
        show_description.startAnimation(fadeIn)
        fadeIn.duration = 375
        fadeIn.fillAfter = true
    }

    override fun onResume() {
        super.onResume()

        presenter.onResume(id)

    }

    override fun onSuccessImageClick(successImage: SuccessImageModel, successImageIv: ImageView, position: Int, constraintLayout: ConstraintLayout, cardView: CardView) {

        presenter.onSuccessImageClick(position, successImageAdapter?.successImages)

    }

    override fun onSuccessImageLongClick(successImage: SuccessImageModel, successImageIv: ImageView, position: Int, constraintLayout: ConstraintLayout, cardView: CardView) {
        //TODO add functionality
    }

    private fun initImageLoader(context: Context?) {
        val config = ImageLoaderConfiguration.Builder(context!!)
        config.threadPriority(Thread.NORM_PRIORITY - 2)
        config.denyCacheImageMultipleSizesInMemory()
        config.diskCacheFileNameGenerator(Md5FileNameGenerator())
        config.diskCacheSize(50 * 1024 * 1024)
        config.tasksProcessingOrder(QueueProcessingType.LIFO)
        ImageLoader.getInstance().init(config.build())
    }

}
