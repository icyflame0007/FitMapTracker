package com.example.fitmaptracker.repositories

import com.example.fitmaptracker.db.Run
import com.example.fitmaptracker.db.RunDAO
import javax.inject.Inject

class MainRepository @Inject constructor(
    val runDAO: RunDAO
) {

    //Job of repository is collect all the data from data sources

    suspend fun insertRun(run : Run) = runDAO.insertRun(run)
    suspend fun deleteRun(run : Run) = runDAO.deleteRun(run)


    fun getAllRunsSortedByDate() = runDAO.getAllRunsSortedByDate()

    fun getAllRunsSortedByDistance() = runDAO.getAllRunsSortedByDistance()

    fun getAllRunsSortedByCaloriesBurned() = runDAO.getAllRunsSortedByCaloriesBurned()

    fun getAllRunsSortedByAvgSpeed() = runDAO.getAllRunsSortedByAvgSpeed()

    fun getAllRunsSortedByTimeInMillis() = runDAO.getAllRunsSortedByTimeInMillis()



    fun getTotalAvgSpeed() = runDAO.getTotalAvgSpeed()


    fun getTotalCaloriesBurned() = runDAO.getTotalCaloriesBurned()

    fun getTotalDistance() = runDAO.getTotalDistance()

    fun getTotalTimeInMillis() = runDAO.getTotalTimeInMillis()

    fun getRunsWithId(id:Int)=runDAO.getRuns(id)

    fun getSummary()=runDAO.getSummary()
}