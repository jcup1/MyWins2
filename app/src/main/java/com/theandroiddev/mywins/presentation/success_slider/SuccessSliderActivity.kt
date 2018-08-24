package com.theandroiddev.mywins.presentation.success_slider

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.support.design.widget.FloatingActionButton
import android.support.design.widget.Snackbar
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentStatePagerAdapter
import android.support.v4.content.ContextCompat
import android.support.v4.content.res.ResourcesCompat
import android.widget.Toast
import com.theandroiddev.mywins.R
import com.theandroiddev.mywins.data.entity.SuccessEntity
import com.theandroiddev.mywins.mvp.MvpDaggerAppCompatActivity
import com.theandroiddev.mywins.presentation.edit_success.EditSuccessActivity
import com.theandroiddev.mywins.presentation.successes.SuccessesBundle
import com.theandroiddev.mywins.utils.Constants.Companion.REQUEST_CODE_INSERT
import kotlinx.android.synthetic.main.activity_slider.*

/**
 * Created by jakub on 27.10.17.
 */

class SuccessSliderActivity : MvpDaggerAppCompatActivity<SuccessSliderView, SuccessSliderPresenter>(), SuccessSliderView, ActionHandler {

    lateinit var adapter: ScreenSlidePagerAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_slider)

        slider_fab.setOnClickListener { sliderFabClicked() }
        adapter = ScreenSlidePagerAdapter(supportFragmentManager)

        initFab(slider_fab)
        slider_pager.adapter = adapter

        val extras = intent.extras
        if (extras != null) {
            val bundle = extras.getSerializable("bundle")

            if (bundle != null && bundle is SuccessesBundle) {
                presenter.onExtrasLoaded(bundle.successes, bundle.position)
            }
        }

    }

    private fun initFab(fab: FloatingActionButton) {

        val id = R.drawable.ic_create_black_24dp
        val color = R.color.white
        val myDrawable = ResourcesCompat.getDrawable(resources, id, null)

        fab.setImageDrawable(myDrawable)
        fab.setColorFilter(ContextCompat.getColor(this, color))

    }

    private fun sliderFabClicked() {

        presenter.sliderFabClicked(slider_pager.currentItem, adapter.successes)

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == REQUEST_CODE_INSERT && data != null) {
            if (resultCode == Activity.RESULT_OK) {

                presenter.onRequestCodeInsert()
                Snackbar.make(slider_constraint, "Saved", Toast.LENGTH_SHORT).show()
            }
        }

        if (resultCode == RESULT_CANCELED) {

        }

    }

    override fun onBackPressed() {

        val returnIntent = Intent()
        returnIntent.putExtra("position", slider_pager.currentItem)
        setResult(Activity.RESULT_OK, returnIntent)
        finish()

    }

    override fun displaySuccesses(successes: MutableList<SuccessEntity>, position: Int) {

        adapter.successes = successes
        slider_pager.currentItem = position
        adapter.notifyDataSetChanged()

    }

    override fun displayEditSuccessActivity(id: Long) {
        val editSuccessIntent = Intent(this@SuccessSliderActivity, EditSuccessActivity::class.java)

        editSuccessIntent.putExtra("id", id)

        startActivityForResult(editSuccessIntent, REQUEST_CODE_INSERT)

    }

    override fun onAddImage() {
        sliderFabClicked()
    }

    class ScreenSlidePagerAdapter(fm: FragmentManager) : FragmentStatePagerAdapter(fm) {

        var successes = mutableListOf<SuccessEntity>()

        override fun getItem(position: Int): Fragment {
            val bundle = Bundle()
            bundle.putLong("id", successes[position].id ?: 0)
            val frag = SuccessSliderFragment()
            frag.arguments = bundle
            return frag
        }

        override fun getCount(): Int {
            return successes.size
        }
    }
}
