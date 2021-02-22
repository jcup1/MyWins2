package com.theandroiddev.mywins.presentation.about_app

import android.app.DialogFragment
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import com.google.gson.GsonBuilder
import com.theandroiddev.mywins.R
import com.theandroiddev.mywins.data.app_info.model.AppInfoEntity
import okhttp3.*
import java.io.IOException

class AboutAppDialog : DialogFragment() {

    lateinit var appInfo: AppInfoEntity

    lateinit var appInfoTextView: TextView
    lateinit var appLicenceTextView: TextView
    lateinit var appVersionTextView: TextView

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        dialog!!.window!!.setBackgroundDrawableResource(R.drawable.round_corner)
        val dialog = inflater?.inflate(R.layout.dialog_about_app, container, false)

        if (dialog != null) {
            appInfoTextView = dialog.findViewById(R.id.appInfoTextView)
            appLicenceTextView = dialog.findViewById(R.id.appLicenceTextView)
            appVersionTextView = dialog.findViewById(R.id.appVersionTextView)
        }
        fetchAppData()

        return dialog
    }

    private fun fetchAppData() {
        val gson = GsonBuilder().create()
        val client = OkHttpClient()
        val request = Request.Builder()
                .url("https://api.mocki.io/v1/3a4560d0")
                .build()

        client.newCall(request).enqueue(object : Callback {
            override fun onResponse(call: Call, response: Response) {
                val responseBody = response.body()!!.string()
                appInfo = gson.fromJson(responseBody, AppInfoEntity::class.java)
                println(appInfo)

                activity.runOnUiThread {
                    appInfoTextView.text = appInfo.info
                    appLicenceTextView.text = appInfo.licence
                    appVersionTextView.text = "version: " + appInfo.version.toString()
                }
            }

            override fun onFailure(call: Call, e: IOException) {
                Toast.makeText(activity, "Failed to fetch AppInfo", Toast.LENGTH_SHORT).show()
            }
        })
    }
}