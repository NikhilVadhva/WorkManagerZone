package com.example.workmanagerdemo

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.work.OneTimeWorkRequest
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import androidx.work.WorkRequest
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {


    var mWorkRequest: WorkRequest =
        OneTimeWorkRequest.Builder(NotificationWorker::class.java)
            .build()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        btStart.setOnClickListener {
            WorkManager.getInstance(this).enqueue(mWorkRequest)
        }
    }
}