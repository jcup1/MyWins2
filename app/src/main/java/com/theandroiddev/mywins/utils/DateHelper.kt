package com.theandroiddev.mywins.utils

import android.app.DatePickerDialog
import android.content.Context
import android.text.TextUtils
import android.widget.EditText
import android.widget.TextView

import com.theandroiddev.mywins.R

import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

/**
 * Created by jakub on 03.09.17.
 */

class DateHelper(private val context: Context) {

    private lateinit var myCalendar: Calendar

    fun setDate(dateText: String, dateStarted: TextView, dateEnded: TextView) {

        myCalendar = Calendar.getInstance()

        val date = DatePickerDialog.OnDateSetListener { view, year, monthOfYear, dayOfMonth ->
            myCalendar.set(Calendar.YEAR, year)
            myCalendar.set(Calendar.MONTH, monthOfYear)
            myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth)
            updateLabel(dateText, dateStarted, dateEnded)
        }

        DatePickerDialog(context, date, myCalendar.get(Calendar.YEAR),
                myCalendar.get(Calendar.MONTH),
                myCalendar.get(Calendar.DAY_OF_MONTH)).show()

    }

    private fun updateLabel(dateText: String, dateStarted: TextView, dateEnded: TextView) {

        val sdf = SimpleDateFormat(Constants.DATE_FORMAT, Locale.US)

        if (dateText == context.getString(R.string.date_started_empty)) {
            dateStarted.text = sdf.format(myCalendar.time)
            dateStarted.error = null
        }
        if (dateText == context.getString(R.string.date_ended_empty)) {
            dateEnded.text = sdf.format(myCalendar.time)
            dateEnded.error = null

        }

    }

    fun checkBlankDate(s: String): String {
        return if (s.contains(Constants.DATE)) {
            ""
        } else
            s
    }

    fun validateData(titleEt: EditText, dateStartedTv: TextView, dateEndedTv: TextView): Boolean {

        if (TextUtils.isEmpty(titleEt.text.toString())) {
            titleEt.error = context.getString(R.string.error_title)
            return false

        }

        if (!dateStartedTv.text.toString().contains("Date")) {
            if (!TextUtils.isEmpty(dateStartedTv.text.toString()) && !TextUtils.isEmpty(dateEndedTv.text.toString())) {
                val sdf = SimpleDateFormat(Constants.DATE_FORMAT, Locale.getDefault())
                try {
                    val date1 = sdf.parse(dateStartedTv.text.toString())
                    val date2 = sdf.parse(dateEndedTv.text.toString())
                    if (date1.after(date2)) {
                        dateEndedTv.error = context.getString(R.string.error_date_ended)
                        return false
                    }
                } catch (e: ParseException) {
                    e.printStackTrace()
                }

            }

        }

        return true

    }

}
