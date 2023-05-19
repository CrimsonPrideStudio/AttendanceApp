package com.example.components.utils

import android.app.Activity
import android.app.AlertDialog
import android.view.LayoutInflater
import com.example.components.R

class LoadingDialog(myActivity: Activity) {
    val activity = myActivity
    lateinit var dialog: AlertDialog

    fun startLoadingDialog(){
        val builder:AlertDialog.Builder = AlertDialog.Builder(activity)

        val inflater:LayoutInflater  = activity.layoutInflater

        builder.setView(inflater.inflate(R.layout.custom_dialog,null))

        builder.setCancelable(true)

        dialog = builder.create()
        dialog.show()
    }

    fun dismissDialog(){
        dialog.dismiss()
    }

}