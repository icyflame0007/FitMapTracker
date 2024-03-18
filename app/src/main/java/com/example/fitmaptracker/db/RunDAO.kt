package com.example.fitmaptracker.db
import androidx.lifecycle.LiveData
import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface RunDAO {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertRun(run: Run)

    @Delete
    suspend fun deleteRun(run: Run)

    @Query("SELECT * FROM running_table ORDER BY timestamp DESC")
    fun getAllRunsSortedByDate(): LiveData<List<Run>>

    @Query("SELECT * FROM running_table ORDER BY avgSpeedInKMH DESC")
    fun getAllRunsSortedByAvgSpeed(): LiveData<List<Run>>

    @Query("SELECT * FROM running_table ORDER BY caloriesBurned DESC")
    fun getAllRunsSortedByCaloriesBurned(): LiveData<List<Run>>


    @Query("SELECT * FROM running_table ORDER BY distanceInMeters DESC")
    fun getAllRunsSortedByDistance(): LiveData<List<Run>>


    @Query("SELECT * FROM running_table ORDER BY timeInMillis DESC")
    fun getAllRunsSortedByTimeInMillis(): LiveData<List<Run>>


    @Query("SELECT SUM(timeInMillis) FROM running_table")
    fun getTotalTimeInMillis(): Flow<Long>

    @Query("SELECT SUM(caloriesBurned) FROM running_table")
    fun getTotalCaloriesBurned(): LiveData<Int>

    @Query("SELECT SUM(distanceInMeters) FROM running_table")
    fun getTotalDistance(): Flow<Int>

    @Query("SELECT AVG(avgSpeedInKMH) FROM running_table")
    fun getTotalAvgSpeed(): Flow<Float>

    @Query("select * from running_table where id = :id")
    fun getRuns(id:Int): List<Run>

    @SuppressWarnings(RoomWarnings.CURSOR_MISMATCH
    )
    @Query("select AVG(avgSpeedInKMH) as avgSpeedInKMH,SUM(distanceInMeters) as distanceInMeters,SUM(timeInMillis) as timeInMillis,SUM(caloriesBurned) as caloriesBurned, 0 as timestamp from running_table")
    fun getSummary():Flow<Run>

}