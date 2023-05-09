package com.example.background

import android.app.Application
import androidx.viewbinding.BuildConfig
import androidx.work.Configuration
import timber.log.Timber
import timber.log.Timber.DebugTree

class BlurApplication() : Application(), Configuration.Provider {
    override fun onCreate() {
        super.onCreate()
        if (BuildConfig.DEBUG) {
            Timber.plant(DebugTree())
        }
    }

    override fun getWorkManagerConfiguration(): Configuration =
        if(BuildConfig.DEBUG){
            Configuration.Builder()
                .setMinimumLoggingLevel(android.util.Log.DEBUG)
                .build()
        }else{
            Configuration.Builder()
                .setMinimumLoggingLevel(android.util.Log.ERROR)
                .build()
        }

}
