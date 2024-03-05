package com.example.fitmaptracker.di

import android.Manifest
import android.content.Context
import android.content.SharedPreferences
import android.os.Build
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.fitmaptracker.db.RunDAO
import com.example.fitmaptracker.db.RunningDatabase
import com.example.fitmaptracker.other.Constants
import com.example.fitmaptracker.other.Constants.RUNNING_DATABASE_NAME
import com.example.fitmaptracker.other.Constants.SHARED_PREFRENCES_NAME

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityComponent
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Singleton
    @Provides
    fun provideRunningDatabase(@ApplicationContext context: Context ) :RunningDatabase{

       return Room.databaseBuilder(context,RunningDatabase::class.java,RUNNING_DATABASE_NAME)
            .build()
    }


    @Singleton
    @Provides
    fun provideRunDao(db:RunningDatabase) :RunDAO
    {
        return db.getRunDao()
    }

    @Provides
    @Singleton
    fun provideSharedPreferences(@ApplicationContext context:Context) : SharedPreferences {
        return context.getSharedPreferences(SHARED_PREFRENCES_NAME, Context.MODE_PRIVATE)
    }

    @Provides
    @Singleton
    fun provideIsFirstRun(appPref:SharedPreferences) = appPref.getBoolean(Constants.KEY_FIRST_TIME_TOGGLE,true)

    @Provides
    @Singleton
    fun provideLocationPermissionsArray() = mutableListOf(
        Manifest.permission.ACCESS_COARSE_LOCATION,
        Manifest.permission.ACCESS_FINE_LOCATION)
        .apply {if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) add(Manifest.permission.ACCESS_BACKGROUND_LOCATION)}.toTypedArray()

}