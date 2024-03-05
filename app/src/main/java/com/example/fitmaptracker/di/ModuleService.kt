package com.example.fitmaptracker.di

import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import androidx.core.app.NotificationCompat
import com.example.fitmaptracker.R
import com.example.fitmaptracker.other.Constants
import com.example.fitmaptracker.other.Constants.NOTIFICATION_TITLE
import com.example.fitmaptracker.ui.MainActivity
import com.google.android.gms.location.LocationServices
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ServiceComponent
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.android.scopes.ServiceScoped

@Module
@InstallIn(ServiceComponent::class)
object ModuleService {

    @Provides
    @ServiceScoped
    fun provideFusedLocationClient(
        @ApplicationContext context: Context
    ) = LocationServices.getFusedLocationProviderClient(context)

    @Provides
    fun getMainActivityPendingIntent(
        @ApplicationContext context: Context,
        intent: Intent
    ): PendingIntent = PendingIntent.getActivity(context,0,intent, PendingIntent.FLAG_MUTABLE)

    @Provides
    fun getMainActivityIntent(@ApplicationContext context: Context,)=
        Intent(context, MainActivity::class.java).also { it.action =
        Constants.ACTION_TRACKING_FRAGMENT
    }

    @Provides
    @ServiceScoped
    fun baseNotificationBuilder(
        @ApplicationContext context: Context,
        pendingIntent: PendingIntent
    )= NotificationCompat.Builder(context, Constants.NOTIFICATION_CHANNEL_ID)
        .setAutoCancel(false)
        .setOngoing(true) // can't be swiped away
        .setSmallIcon(R.drawable.run_icon)
        .setContentTitle(NOTIFICATION_TITLE)
        .setContentText("00:00:00")
        .setContentIntent(pendingIntent)

}