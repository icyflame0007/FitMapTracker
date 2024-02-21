package com.example.fitmaptracker.di

import android.content.Context
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.fitmaptracker.db.RunDAO
import com.example.fitmaptracker.db.RunningDatabase
import com.example.fitmaptracker.other.Constants.RUNNING_DATABASE_NAME

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

}