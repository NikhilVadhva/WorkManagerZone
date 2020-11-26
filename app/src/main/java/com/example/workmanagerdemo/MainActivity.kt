package com.example.workmanagerdemo

import android.os.Bundle
import android.widget.Toast
import androidx.annotation.Nullable
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.work.OneTimeWorkRequest
import androidx.work.WorkInfo
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

        WorkManager.getInstance(this).getWorkInfoByIdLiveData(mWorkRequest.id)
            .observe(this, Observer{ workInfo ->
                when (workInfo?.state) {
                    WorkInfo.State.SUCCEEDED -> {
                        Toast.makeText(this,"Success Notification Task",Toast.LENGTH_LONG).show()
                    }
                    WorkInfo.State.FAILED -> {
                        Toast.makeText(this,"Failed Notification Task",Toast.LENGTH_LONG).show()
                    }
                    WorkInfo.State.CANCELLED -> {
                        Toast.makeText(this,"Cancelled Notification Task",Toast.LENGTH_LONG).show()
                    }
                    else ->
                        Toast.makeText(this,"Running Notification Task",Toast.LENGTH_LONG).show()
                }

                if(workInfo.state.isFinished)
                    Toast.makeText(this,"Notification Task Finished",Toast.LENGTH_LONG).show()
            })



    }
}