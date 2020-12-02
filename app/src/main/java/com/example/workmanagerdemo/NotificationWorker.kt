package com.example.workmanagerdemo

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import android.widget.Toast
import androidx.core.app.NotificationCompat
import androidx.work.Worker
import androidx.work.WorkerParameters

class NotificationWorker(context: Context, workerParameters: WorkerParameters) :
    Worker(context, workerParameters) {
    companion object
    {
         const val TASK : String = "NotificationTask"
    }
    override fun doWork(): Result {
        val task   = inputData.getString(TASK) ?:return Result.failure()
        displayNotification("WorkMangerDemo", task)
        return Result.retry()
    }

    private fun displayNotification(title: String, task: String) {
        val notificationManager =
            applicationContext.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                "notificationWorker",
                "notificationWorker",
                NotificationManager.IMPORTANCE_DEFAULT
            )
            notificationManager.createNotificationChannel(channel)
        }
        val notification =
            NotificationCompat.Builder(applicationContext, "notificationWorker")
                .setContentTitle(title)
                .setContentText(task)
                .setSmallIcon(R.mipmap.ic_launcher)
        notificationManager.notify(1, notification.build())
    }

    override fun onStopped() {
        Toast.makeText(applicationContext,"Cancelled Notification Task", Toast.LENGTH_LONG).show()
        super.onStopped()
    }
}
