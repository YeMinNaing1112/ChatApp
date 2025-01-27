package com.yeminnaing.chatapp.data.workers

import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.work.CoroutineWorker
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import androidx.work.WorkerParameters
import androidx.work.workDataOf
import com.yeminnaing.chatapp.R
import com.yeminnaing.chatapp.data.notificationManager.NOTIFICATION_CHANNEL_ID
import com.yeminnaing.chatapp.data.notificationManager.NOTIFICATION_ID
import com.yeminnaing.chatapp.data.notificationManager.REQUEST_CODE
import com.yeminnaing.chatapp.data.repositories.MessageRepoImpl
import com.yeminnaing.chatapp.domain.responses.MessageResponse
import com.yeminnaing.chatapp.presentation.MainActivity
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject

//class CheckMessageWorker @AssistedInject constructor(
//    @Assisted context: Context,
//    @Assisted workerParameters: WorkerParameters,
//    private val repository: MessageRepoImpl,
//) : CoroutineWorker(context, workerParameters) {
//    override suspend fun doWork(): Result {
//        val chatId = inputData.getString("chatId") ?: return Result.failure()
//
//        return try {
//            val result = repository.getLastMessage(chatId)
//            result.fold(
//                onSuccess = { message ->
//                    showNotification(message)
//                    Result.success()
//                },
//                onFailure = { error ->
//                    Log.e("CheckMessagesWorker", "Error fetching message: ${error.message}")
//                    Result.failure()
//                }
//            )
//        } catch (e: Exception) {
//            Log.e("CheckMessagesWorker", "Unexpected error: ${e.message}")
//            Result.failure()
//        }
//    }
//
//    //create Notification Manager
//    private val notificationManager =
//        context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
//
//    private val myIntent= Intent(context, MainActivity::class.java)
//    private val pendingIntent= PendingIntent.getActivity(
//        context,
//        REQUEST_CODE,
//        myIntent,
//        PendingIntent.FLAG_MUTABLE
//    )
//    private fun showNotification(message: MessageResponse) {
//        val notification = NotificationCompat.Builder(applicationContext, "chat_channel")
//            .setContentTitle("New Message")
//            .setContentText(message.text)
//            .setPriority(NotificationCompat.PRIORITY_HIGH)
//            .setContentIntent(pendingIntent)
//            .setSmallIcon(R.drawable.ic_launcher_foreground)
//            .build()
//        notificationManager.notify(1, notification)
//    }
//}
//
//fun scheduleMessageWorker(context: Context, chatId: String) {
//    val workRequest = OneTimeWorkRequestBuilder<CheckMessageWorker>()
//        .setInputData(
//            workDataOf("chatId" to chatId)
//        )
//        .build()
//
//    WorkManager.getInstance(context).enqueue(workRequest)
//}