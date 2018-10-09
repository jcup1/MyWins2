package com.theandroiddev.mywins.presentation.insert_success

import android.app.Activity
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.support.v4.content.ContextCompat
import android.support.v4.content.res.ResourcesCompat
import android.util.DisplayMetrics
import android.view.View
import com.theandroiddev.mywins.R
import com.theandroiddev.mywins.core.mvp.MvpDaggerAppCompatActivity
import com.theandroiddev.mywins.presentation.edit_success.EditDescriptionActivity
import com.theandroiddev.mywins.presentation.successes.SuccessModel
import com.theandroiddev.mywins.utils.Constants
import com.theandroiddev.mywins.utils.Constants.Companion.Category
import com.theandroiddev.mywins.utils.Constants.Companion.Importance
import com.theandroiddev.mywins.utils.DateHelper
import com.theandroiddev.mywins.utils.DrawableSelector
import kotlinx.android.synthetic.main.activity_insert_success.*

class InsertSuccessActivity : MvpDaggerAppCompatActivity<InsertSuccessView,
        InsertSuccessBundle, InsertSuccessPresenter>(), InsertSuccessView, View.OnClickListener {


    var drawableSelector = DrawableSelector(this)
    var dateHelper = DateHelper(this)
    private var displayMetrics: DisplayMetrics? = null
    private var accentColor: Int = 0

    private var currentCategory: Category = Category.NONE
    private var currentImportance: Importance = Importance.NONE

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_insert_success)

        initWindow()
        initViews()
        presenter.onAfterCreate()

    }

    override fun onBackPressed() {
        val intentParent = intent
        setResult(Activity.RESULT_CANCELED, intentParent)
        finish()
    }

    private fun initViews() {

        drawableSelector = DrawableSelector(this)
        dateHelper = DateHelper(this)

        accentColor = ResourcesCompat.getColor(resources, R.color.accent, null)

        insert_date_started_iv.setColorFilter(accentColor)
        insert_description_iv.setColorFilter(accentColor)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            insert_add_btn.backgroundTintList = resources.getColorStateList(R.color.accent, null)
        } else {
            insert_add_btn.setBackgroundColor(ContextCompat.getColor(this, R.color.accent))
        }

        insert_date_ended_iv.setColorFilter(accentColor)
        insert_description_iv.setColorFilter(accentColor)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            insert_add_btn.backgroundTintList = resources.getColorStateList(R.color.accent, null)
        } else {
            insert_add_btn.setBackgroundColor(ContextCompat.getColor(this, R.color.accent))
        }

        insert_importance_iv_1.setOnClickListener(this)
        insert_importance_iv_2.setOnClickListener(this)
        insert_importance_iv_3.setOnClickListener(this)
        insert_importance_iv_4.setOnClickListener(this)
        insert_date_started_tv.setOnClickListener(this)
        insert_date_ended_tv.setOnClickListener(this)
        insert_date_started_iv.setOnClickListener(this)
        insert_date_ended_iv.setOnClickListener(this)
        insert_description_iv.setOnClickListener(this)
        insert_add_btn.setOnClickListener(this)

    }

    private fun initWindow() {

        displayMetrics = DisplayMetrics()
        windowManager.defaultDisplay.getMetrics(displayMetrics)
        val width = displayMetrics!!.widthPixels
        val height = displayMetrics!!.heightPixels
        window.setLayout((width * .9).toInt(), (height * .8).toInt())

    }

    override fun displayInitCategory(category: Category) {

        insert_category_tv.text = getString(category.res)
        currentCategory = category
        drawableSelector.selectCategoryImage(insert_category_iv, category, insert_category_tv)
        drawableSelector.setImportance(
                Constants.dummyImportanceDefault,
                insert_importance_tv,
                insert_importance_iv_1,
                insert_importance_iv_2,
                insert_importance_iv_3,
                insert_importance_iv_4)

    }

    override fun onClick(view: View) {
        when (view.id) {
            R.id.insert_importance_iv_1 -> {
                drawableSelector.setSmallImportance(insert_importance_tv, insert_importance_iv_1,
                        insert_importance_iv_2, insert_importance_iv_3, insert_importance_iv_4)
                currentImportance = Importance.values()[1]
            }
            R.id.insert_importance_iv_2 -> {
                drawableSelector.setMediumImportance(insert_importance_tv, insert_importance_iv_1,
                        insert_importance_iv_2, insert_importance_iv_3, insert_importance_iv_4)
                currentImportance = Importance.values()[2]
            }
            R.id.insert_importance_iv_3 -> {
                drawableSelector.setBigImportance(insert_importance_tv, insert_importance_iv_1,
                        insert_importance_iv_2, insert_importance_iv_3, insert_importance_iv_4)
                currentImportance = Importance.values()[3]
            }
            R.id.insert_importance_iv_4 -> {
                drawableSelector.setHugeImportance(insert_importance_tv, insert_importance_iv_1,
                        insert_importance_iv_2, insert_importance_iv_3, insert_importance_iv_4)
                currentImportance = Importance.values()[4]
            }
            R.id.insert_date_started_iv -> {
                dateHelper.setDate(getString(R.string.date_started_empty), insert_date_started_tv, insert_date_ended_tv)
            }
            R.id.insert_date_ended_iv -> {
                dateHelper.setDate(getString(R.string.date_ended_empty), insert_date_started_tv, insert_date_ended_tv)
            }
            R.id.insert_date_started_tv -> {
                dateHelper.setDate(getString(R.string.date_started_empty), insert_date_started_tv, insert_date_ended_tv)
            }
            R.id.insert_date_ended_tv -> {
                dateHelper.setDate(getString(R.string.date_ended_empty), insert_date_started_tv, insert_date_ended_tv)
            }
            R.id.insert_description_iv -> setDesc()
            R.id.insert_add_btn -> insertSuccess()

            else -> {
            }
        }
    }

    private fun insertSuccess() {

        if (dateHelper.validateData(instert_title_et, insert_date_started_tv, insert_date_ended_tv)) {

            val dateStarted = dateHelper.checkBlankDate(insert_date_started_tv.text.toString())
            val dateEnded = dateHelper.checkBlankDate(insert_date_ended_tv.text.toString())

            sendData(dateStarted, dateEnded)
        }
    }

    private fun sendData(dateStarted: String, dateEnded: String) {

        val returnIntent = Intent()

        val s = SuccessModel(
                null,
                instert_title_et.text.toString(),
                currentCategory,
                insert_description_et.text.toString(),
                dateStarted, dateEnded,
                currentImportance)
        //s.setId(UUID.randomUUID().toString());
        //Log.e(TAG, "sendData: uuid" + s.getId() );

        returnIntent.putExtra(Constants.EXTRA_INSERT_SUCCESS_ITEM, s)
        setResult(Activity.RESULT_OK, returnIntent)
        finish()
    }


    private fun setDesc() {

        val intent = Intent(this@InsertSuccessActivity, EditDescriptionActivity::class.java)
        intent.putExtra("description", insert_description_et.text.toString())
        startActivityForResult(intent, Constants.REQUEST_CODE_DESCRIPTION)

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == Constants.REQUEST_CODE_DESCRIPTION) {
            if (resultCode == Activity.RESULT_OK) {
                val description = data.extras.getString(Constants.EXTRA_DESCRIPTION)
                insert_description_et.setText(description)

            }
        }
    }

}
