package com.theandroiddev.mywins.presentation.importance_popup

import android.app.Activity
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.util.DisplayMetrics
import android.view.View
import androidx.core.content.ContextCompat
import com.theandroiddev.mywins.R
import com.theandroiddev.mywins.core.mvp.MvpDaggerAppCompatActivity
import com.theandroiddev.mywins.utils.Constants.Companion.Importance
import com.theandroiddev.mywins.utils.DrawableSelector
import kotlinx.android.synthetic.main.activity_popup_importance.*

class ImportancePopupActivity : MvpDaggerAppCompatActivity<ImportancePopupView,
        ImportancePopupBundle, ImportancePopupPresenter>(), ImportancePopupView, View.OnClickListener {


    private val drawableSelector: DrawableSelector = DrawableSelector(this)
    private var currentImportanceValue = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_popup_importance)

        initWindow()
        initViews()
        presenter.onAfterCreate()

    }

    private fun initViews() {
        popup_importance_iv_1.setOnClickListener(this)
        popup_importance_iv_2.setOnClickListener(this)
        popup_importance_iv_3.setOnClickListener(this)
        popup_importance_iv_4.setOnClickListener(this)
        popup_add_btn.setOnClickListener(this)

    }

    override fun displayInitialImportance(importance: Importance) {

        popup_importance_tv.text = getString(importance.res)
        currentImportanceValue = importance.value
        drawableSelector.setImportance(importance.value, popup_importance_tv, popup_importance_iv_1, popup_importance_iv_2, popup_importance_iv_3, popup_importance_iv_4)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            popup_add_btn.backgroundTintList = resources.getColorStateList(R.color.accent, null)
        } else {
            popup_add_btn.setBackgroundColor(ContextCompat.getColor(this, R.color.accent))
        }

    }

    private fun saveImportance() {

        val returnIntent = Intent()
        returnIntent.putExtra("bundle", ImportancePopupBundle(Importance.values()[currentImportanceValue]))
        setResult(Activity.RESULT_OK, returnIntent)
        finish()
    }

    private fun initWindow() {

        val displayMetrics = DisplayMetrics()
        windowManager.defaultDisplay.getMetrics(displayMetrics)
        val width = displayMetrics.widthPixels
        val height = displayMetrics.heightPixels
        window.setLayout((width * .8).toInt(), (height * .6).toInt())

    }

    override fun onClick(view: View) {

        when (view.id) {
            R.id.popup_importance_iv_1 -> {
                drawableSelector.setSmallImportance(
                        popup_importance_tv,
                        popup_importance_iv_1,
                        popup_importance_iv_2,
                        popup_importance_iv_3,
                        popup_importance_iv_4)
                currentImportanceValue = 1
            }
            R.id.popup_importance_iv_2 -> {
                drawableSelector.setMediumImportance(
                        popup_importance_tv,
                        popup_importance_iv_1,
                        popup_importance_iv_2,
                        popup_importance_iv_3,
                        popup_importance_iv_4)
                currentImportanceValue = 2
            }
            R.id.popup_importance_iv_3 -> {
                drawableSelector.setBigImportance(
                        popup_importance_tv,
                        popup_importance_iv_1,
                        popup_importance_iv_2,
                        popup_importance_iv_3,
                        popup_importance_iv_4)
                currentImportanceValue = 3
            }
            R.id.popup_importance_iv_4 -> {
                drawableSelector.setHugeImportance(
                        popup_importance_tv,
                        popup_importance_iv_1,
                        popup_importance_iv_2,
                        popup_importance_iv_3,
                        popup_importance_iv_4)
                currentImportanceValue = 4
            }
            R.id.popup_add_btn -> {
                saveImportance()
            }

            else -> {
            }
        }

    }
}
