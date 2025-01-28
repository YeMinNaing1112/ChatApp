package com.yeminnaing.chatapp.presentation

import android.app.Application
import android.app.NotificationChannel
import android.app.NotificationManager
import android.os.Build
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class App : Application() {
    override fun onCreate() {
        super.onCreate()
        createNotificationChannel()


//        val notificationManager=
//            applicationContext.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
//
//        val notificationChannel =NotificationChannel(
//            NOTIFICATION_CHANNEL_ID,
//            NOTIFICATION_CHANNEL_NAME,
//            NotificationManager.IMPORTANCE_HIGH
//        )
//        notificationManager.createNotificationChannel(notificationChannel)
//
//        // Fetch the FCM token
//        FirebaseMessaging.getInstance().token
//            .addOnCompleteListener { task ->
//                if (task.isSuccessful) {
//                    val token = task.result
//                    Log.d("FCM", "Token: $token")
//                    // Send the token to your backend server here
//                } else {
//                    Log.e("FCM", "Fetching FCM token failed", task.exception)
//                }
//            }

    }

    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                "chat_channel",
                "Chat Notifications",
                NotificationManager.IMPORTANCE_HIGH
            ).apply {
                description = "Channel for chat notifications"
            }
            val manager = getSystemService(NotificationManager::class.java)
            manager?.createNotificationChannel(channel)
        }
    }

}