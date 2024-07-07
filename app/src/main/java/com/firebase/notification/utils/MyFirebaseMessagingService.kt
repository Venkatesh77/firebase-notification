package com.firebase.notification.utils

import com.firebase.notification.R
import android.annotation.SuppressLint
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import android.util.Log
import android.widget.RemoteViews
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat
import com.firebase.notification.MainActivity
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage


class MyFirebaseMessagingService : FirebaseMessagingService() {

    private val channel_notification_id = 1

    @SuppressLint("NewApi")
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        super.onMessageReceived(remoteMessage)
        try {
            val callerName = remoteMessage.data[Constants.caller_name]
            val acceptIntent = Intent(this, MainActivity::class.java)
            acceptIntent.putExtra(Constants.notification_id, channel_notification_id)
            val mainPagePendingIntent = PendingIntent.getActivity(this, 0, acceptIntent, PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE)

            val customNotificationLayout = RemoteViews(packageName, R.layout.notification_layout).apply {
                setTextViewText(R.id.notification_title, callerName ?: getText(R.string.caller_name))
                setTextViewText(R.id.notification_text, getText(R.string.incoming_call))
                setOnClickPendingIntent(R.id.tv_answer, mainPagePendingIntent)
                setOnClickPendingIntent(R.id.tv_decline, mainPagePendingIntent)
            }
            val notification = NotificationCompat.Builder(this, Constants.channel_id)
                .setSmallIcon(R.drawable.viber)
                .setContentIntent(mainPagePendingIntent)
                .setCustomContentView(customNotificationLayout)
                .setCustomBigContentView(customNotificationLayout)
                .setStyle(NotificationCompat.DecoratedCustomViewStyle())
                .setPriority(NotificationCompat.PRIORITY_MAX)
                .build()

            val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            createNotificationChannel(notificationManager)
            notificationManager.notify(channel_notification_id, notification)
        }catch (ex: Exception){
            Log.i("Exception", ex.message.toString())
        }
    }

    private fun createNotificationChannel(notificationManager: NotificationManager) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name = Constants.channel_name
            val descriptionText = Constants.channel_description
            val importance = NotificationManager.IMPORTANCE_HIGH
            val channel = NotificationChannel(Constants.channel_id, name, importance).apply {
                description = descriptionText
            }
            notificationManager.createNotificationChannel(channel)
        }
    }
}
