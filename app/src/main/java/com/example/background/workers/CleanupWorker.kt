package com.example.background.workers

import android.content.Context
import android.util.Log
import androidx.work.Worker
import androidx.work.WorkerParameters
import com.example.background.OUTPUT_PATH
import java.io.File

private const val TAG = "CleanupWorker"

class CleanupWorker(
    private val ctx: Context,
    params: WorkerParameters
) : Worker(ctx, params) {

    override fun doWork(): Result {

        makeStatusNotification("Cleaning up old temporary files", ctx)
        sleep()

        return try {
            val outputDirectory = File(applicationContext.filesDir, OUTPUT_PATH)
            if (outputDirectory.exists()) {
                val entries = outputDirectory.listFiles()
                entries?.let {
                    entries.forEach { tempFile ->
                        if (tempFile.name.isNotEmpty() && tempFile.name.endsWith(".png")) {
                            val isDeleted = tempFile.delete()
                            Log.d(TAG, "Deleted ${tempFile.name}: $isDeleted")
                        }
                    }
                }
            }
            Result.success()
        } catch (exception: java.lang.Exception) {
            exception.printStackTrace()
            Result.failure()
        }
    }
}