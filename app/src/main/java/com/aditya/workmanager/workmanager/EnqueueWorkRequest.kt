package com.aditya.workmanager.workmanager

import android.content.Context
import androidx.work.*

class EnqueueWorkRequest(private val context: Context) {
    fun startTaskWorker() {
        val constraints: Constraints = Constraints.Builder().setRequiredNetworkType(NetworkType.CONNECTED)
                .build()
        val oneTimeWorkRequest: OneTimeWorkRequest = OneTimeWorkRequest.Builder(WorkerTask::class.java)
                .setConstraints(constraints)
                .build()
        WorkManager.getInstance(context).cancelUniqueWork("task-worker")

        WorkManager.getInstance(context)
                .beginUniqueWork("task-worker", ExistingWorkPolicy.APPEND_OR_REPLACE, oneTimeWorkRequest)
                .enqueue()
    }
}