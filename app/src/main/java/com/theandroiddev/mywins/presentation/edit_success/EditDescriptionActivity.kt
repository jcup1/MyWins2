package com.theandroiddev.mywins.presentation.edit_success

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.support.v4.content.ContextCompat
import android.support.v4.content.res.ResourcesCompat
import android.support.v7.app.AppCompatActivity
import com.theandroiddev.mywins.R
import com.theandroiddev.mywins.utils.Constants.Companion.EXTRA_DESCRIPTION
import kotlinx.android.synthetic.main.activity_edit_description.*
import kotlinx.android.synthetic.main.content_edit_description.*

class EditDescriptionActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_description)

        initFab()
        initValues()
    }

    private fun initFab() {

        edit_description_fab.setOnClickListener { saveChanges() }

        val id = R.drawable.ic_done
        val color = R.color.white
        val myDrawable = ResourcesCompat.getDrawable(resources, id, null)

        edit_description_fab.setImageDrawable(myDrawable)
        edit_description_fab.setColorFilter(ContextCompat.getColor(this, color))
    }

    private fun saveChanges() {

        val desc = edit_description_et.text.toString()
        val returnIntent = Intent()
        returnIntent.putExtra(EXTRA_DESCRIPTION, desc)
        setResult(Activity.RESULT_OK, returnIntent)
        finish()

    }

    private fun initValues() {

        val description = intent.extras?.getString("description", "")
        edit_description_et.setText(description)

    }
}
