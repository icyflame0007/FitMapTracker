package com.example.fitmaptracker.ui.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.fitmaptracker.db.Run
import com.example.fitmaptracker.other.Constants
import com.example.fitmaptracker.repositories.MainRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor
    (val mainRepository: MainRepository)  :
    ViewModel(){

    private val runsByDate: LiveData<List<Run>> = mainRepository.getAllRunsSortedByDate()
    private val runsByTime: LiveData<List<Run>> = mainRepository.getAllRunsSortedByTimeInMillis()
    private val runsByDistance: LiveData<List<Run>> = mainRepository.getAllRunsSortedByDistance()
    private val runsByAvgSpeed: LiveData<List<Run>> = mainRepository.getAllRunsSortedByAvgSpeed()
    private val runsByCalories: LiveData<List<Run>> = mainRepository.getAllRunsSortedByCaloriesBurned()

    var runLiveData = MediatorLiveData<List<Run>>()

    init {
        // default sort logic is by date
        runLiveData.addSource(runsByDate){runLiveData.value=it}
    }

    fun saveRunInDb(run: Run) = viewModelScope.launch(Dispatchers.IO) {
        mainRepository.insertRun(run)
    }

    fun switchSortingStrategy(sort : Constants.SortingOptions){
        runLiveData.removeSource(getCurrentSource())
        val newSource = when(sort){
            Constants.SortingOptions.DATE -> {runsByDate}
            Constants.SortingOptions.TIME -> {runsByTime}
            Constants.SortingOptions.DISTANCE -> {runsByDistance}
            Constants.SortingOptions.AVG_SPEED -> {runsByAvgSpeed}
            Constants.SortingOptions.CALORIES -> {runsByCalories}

        }
        runLiveData.addSource(newSource){
            runLiveData.value=it
        }
    }


    private fun getCurrentSource() = when(runLiveData.value){
        runsByDate.value -> {runsByDate}
        runsByTime.value -> {runsByTime}
        runsByDistance.value -> {runsByDistance}
        runsByAvgSpeed.value -> {runsByAvgSpeed}
        runsByCalories.value -> {runsByCalories}
        else-> runsByDate
    }

    fun deleteRun(run: Run)=viewModelScope.launch { mainRepository.deleteRun(run)}

}