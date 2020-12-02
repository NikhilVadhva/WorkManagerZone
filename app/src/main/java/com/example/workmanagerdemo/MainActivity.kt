package com.example.workmanagerdemo

import android.os.Bundle
import android.widget.Toast
import androidx.annotation.Nullable
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.work.*
import kotlinx.android.synthetic.main.activity_main.*
import java.util.concurrent.TimeUnit


class MainActivity : AppCompatActivity() {

    // passing input data
    private val data = Data.Builder()
        .putString(NotificationWorker.TASK, "Assigning Input data for NotificationTask")

    // creating a constraints for charging required
    private val constraints = Constraints.Builder()
        .setRequiresCharging(true)
        .build()

    var mWorkRequest: WorkRequest =
        OneTimeWorkRequest.Builder(NotificationWorker::class.java)
            .setInputData(data.build())
            .addTag("Notification")
            .setConstraints(constraints)
            .setBackoffCriteria(
                BackoffPolicy.LINEAR,
                OneTimeWorkRequest.MIN_BACKOFF_MILLIS,
                TimeUnit.MILLISECONDS
            )
            .build()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // enqueue the workRequest
        btStart.setOnClickListener {
            WorkManager.getInstance(this).enqueue(mWorkRequest)
        }

        // cancel the running work
        btCancel.setOnClickListener {
            WorkManager.getInstance(this).cancelWorkById(mWorkRequest.id)
        }

        // observing the workInfo states
        WorkManager.getInstance(this).getWorkInfoByIdLiveData(mWorkRequest.id)
            .observe(this, Observer { workInfo ->
                when (workInfo?.state) {
                    WorkInfo.State.ENQUEUED -> {
                        // here you can uncomment the below line and check the work state for cancelled task
                        //WorkManager.getInstance(this).cancelWorkById(mWorkRequest.id)
                        Toast.makeText(this, "Enqueued Notification Task", Toast.LENGTH_LONG).show()
                    }
                    WorkInfo.State.SUCCEEDED -> {
                        Toast.makeText(this, "Success Notification Task", Toast.LENGTH_LONG).show()
                    }
                    WorkInfo.State.FAILED -> {
                        Toast.makeText(this, "Failed Notification Task", Toast.LENGTH_LONG).show()
                    }
                    WorkInfo.State.CANCELLED -> {
                        Toast.makeText(this, "Cancelled Notification Task", Toast.LENGTH_LONG)
                            .show()
                    }
                    WorkInfo.State.RUNNING -> {
                        Toast.makeText(this, "Running Notification Task", Toast.LENGTH_LONG).show()

                    }
                    else -> {
                        // do nothing
                    }
                }

                if (workInfo.state.isFinished)
                    Toast.makeText(this, "Notification Task Finished", Toast.LENGTH_LONG).show()

                // receiving the input data
//                    Toast.makeText(this,"Received Data: "+workInfo.outputData.getString(NotificationWorker.TASK)
//                        ,Toast.LENGTH_LONG).show()
            })


    }


}