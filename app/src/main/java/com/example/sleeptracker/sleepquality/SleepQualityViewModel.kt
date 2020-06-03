package com.example.sleeptracker.sleepquality

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.sleeptracker.database.SleepDatabaseDao
import kotlinx.coroutines.*

class SleepQualityViewModel(
    private val nightKey: Long = 0L,
    val database: SleepDatabaseDao): ViewModel(){
    private val viewModelJob = Job()
    private val uiScope =  CoroutineScope(Dispatchers.Main + viewModelJob)

    private var _eventNavigate = MutableLiveData<Boolean>()
    val eventNavigate: LiveData<Boolean> = _eventNavigate

    fun onNavigate(){
        _eventNavigate.value = false
    }

    override fun onCleared() {
        super.onCleared()
        viewModelJob.cancel()
    }

    fun setSleepQuality(rating: Int){
        uiScope.launch {
            withContext(Dispatchers.IO){
                val night = database.get(nightKey) ?: return@withContext
                night.rating = rating
                database.update(night)
            }
            _eventNavigate.value = true
        }
    }
}