package com.example.components.utils

import android.app.AlertDialog
import android.app.Dialog
import android.content.Context
import android.content.DialogInterface
import android.os.Bundle
import androidx.fragment.app.DialogFragment

class MyAlertDialog(context:Context): DialogFragment() {
    val builder = AlertDialog.Builder(context)
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        // Create an instance of AlertDialog.Builder
        // Set the title
        builder.setTitle("My Alert Dialog")

        // Set the message
        builder.setMessage("This is my alert dialog.")

        // Set the buttons
        builder.setPositiveButton("OK", DialogInterface.OnClickListener { dialog, which ->
            // Do something when the OK button is clicked
        })

        // Set the negative button
        builder.setNegativeButton("Cancel", DialogInterface.OnClickListener { dialog, which ->
            // Do something when the Cancel button is clicked
        })

        // Create the dialog
        return builder.create()
    }

}