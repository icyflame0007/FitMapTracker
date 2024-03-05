package com.example.fitmaptracker.ui.viewmodels

import androidx.lifecycle.ViewModel
import com.example.fitmaptracker.repositories.MainRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class StatisticsViewModel @Inject constructor
    (val mainRepository: MainRepository)  :
    ViewModel(){
    fun getSummary()=mainRepository.getSummary()
    fun getRunsSortedByDate()=mainRepository.getAllRunsSortedByDate()

}