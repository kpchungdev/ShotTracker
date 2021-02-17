package com.example.shottracker_ai

import android.app.Application
import android.content.Context
import dagger.hilt.android.HiltAndroidApp
import timber.log.Timber

@HiltAndroidApp
class MainApplication : Application() {

    companion object {
        lateinit var appContext: Context

        fun setApplicationContext(context: Context) {
            this.appContext = context
        }
    }

    override fun onCreate() {
        super.onCreate()

        setApplicationContext(applicationContext)

        if (BuildConfig.DEBUG) Timber.plant(Timber.DebugTree())
    }

}