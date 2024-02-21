package com.example.fitmaptracker

import android.app.Application
import dagger.hilt.android.HiltAndroidApp
import timber.log.Timber

@HiltAndroidApp
class BaseApplication : Application()
{
// This tells that we want to inject dependency using daggerhilt

    override fun onCreate() {
        super.onCreate()
        Timber.plant(Timber.DebugTree())
    }
}