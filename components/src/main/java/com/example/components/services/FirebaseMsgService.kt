package com.example.components.services

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.PendingIntent.FLAG_ONE_SHOT
import android.content.Intent
import android.os.Build
import android.util.Log
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat
import com.example.components.R
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import kotlin.random.Random


const val CHANNEL_ID = "MY Channel"
class FirebaseMsgService : FirebaseMessagingService() {
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onMessageReceived(message: RemoteMessage) {
        super.onMessageReceived(message)

        pushNotification(message.data["title"].toString(), message.data["message"].toString())

        message.notification?.let {
            // Get the notification message body
            val notificationBody = it.body
            val notificationTitle = it.title
            // Display the notification message
            pushNotification(notificationTitle.toString(), notificationBody.toString())
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun pushNotification(title:String,msg:String) {

        val notificationManager: NotificationManager =
            getSystemService(NOTIFICATION_SERVICE) as NotificationManager


        val notification: Notification =
            NotificationCompat.Builder(this,CHANNEL_ID).setSmallIcon(R.drawable.about_icon).setContentTitle(title).setContentText(msg).setAutoCancel(true).build()

        notificationManager.createNotificationChannel(
            NotificationChannel(
                CHANNEL_ID,
                "New Channel",
                NotificationManager.IMPORTANCE_HIGH
            )
        )
        notificationManager.notify(Random.nextInt(), notification)
    }

    override fun onNewToken(token: String) {
        super.onNewToken(token)
        Log.d("Refreshed Token", token)
    }
}