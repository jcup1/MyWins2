package com.theandroiddev.mywins.success_slider

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
import com.theandroiddev.mywins.data.models.SearchFilter
import com.theandroiddev.mywins.data.models.Success
import com.theandroiddev.mywins.edit_success.EditSuccessActivity
import com.theandroiddev.mywins.mvp.MvpDaggerAppCompatActivity
import com.theandroiddev.mywins.utils.Constants.Companion.REQUEST_CODE_INSERT
import kotlinx.android.synthetic.main.activity_slider.*

/**
 * Created by jakub on 27.10.17.
 */

class SuccessSliderActivity : MvpDaggerAppCompatActivity<SuccessSliderView, SuccessSliderPresenter>(), SuccessSliderView, ActionHandler {

    var adapter: ScreenSlidePagerAdapter? = null

    override fun onDestroy() {
        presenter.onDestroy()
        super.onDestroy()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_slider)

        slider_fab.setOnClickListener { sliderFabClicked() }

        initFab(slider_fab)

        presenter.onCreate()

        adapter = ScreenSlidePagerAdapter(supportFragmentManager)
        slider_pager.adapter = adapter

        val extras = intent.extras
        val searchFilter: SearchFilter?
        if (extras != null) {
            searchFilter = extras.getParcelable("searchfilter")
            val position = extras.getInt("position")
            presenter.onExtrasLoaded(searchFilter, position)
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

        presenter.sliderFabClicked(slider_pager.currentItem, adapter?.successes)

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

    override fun displaySuccesses(successes: ArrayList<Success>, position: Int) {

        adapter?.successes = successes
        slider_pager.currentItem = position
        adapter?.notifyDataSetChanged()

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

        var successes = ArrayList<Success>()

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
