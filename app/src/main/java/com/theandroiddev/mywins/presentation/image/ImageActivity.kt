package com.theandroiddev.mywins.presentation.image

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.theandroiddev.mywins.R
import kotlinx.android.synthetic.main.activity_image.*

class ImageActivity : AppCompatActivity() {

    private var imageSwipeAdapter: ImageSwipeAdapter? = null
    private var pos: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_image)

        val imagePaths = intent.getStringArrayListExtra("imagePaths")
        pos = intent.getIntExtra("position", 0)
        imageSwipeAdapter = ImageSwipeAdapter(this, imagePaths)
        view_pager.adapter = imageSwipeAdapter
        view_pager.currentItem = pos
        view_pager.offscreenPageLimit = 2

    }

}
