package com.example.sleeptracker.sleeptracker

import android.app.Application
import android.util.Log
import androidx.lifecycle.*
import com.example.sleeptracker.database.SleepDatabaseDao
import com.example.sleeptracker.database.SleepNight
import kotlinx.coroutines.*


class SleepTrackerViewModel(
    val database: SleepDatabaseDao,
    application: Application) : AndroidViewModel(application) {

    private var viewModelJob= Job()

    private val uiScope = CoroutineScope(Dispatchers.Main + viewModelJob)

    private var tonight = MutableLiveData<SleepNight?>()

    val nights = database.getAll()

    // adding button states...
    val startEnabled = Transformations.map(tonight){
        it == null
    }

    val endEnabled = Transformations.map(tonight){
        it != null
    }

    val clearEnabled = Transformations.map(nights){
        it.isNotEmpty()
    }

    // adding event for navigation..
    private var _eventNavigate = MutableLiveData<SleepNight>()
    val eventNavigate: LiveData<SleepNight> = _eventNavigate

    fun onNavigate(){
        _eventNavigate.value = null
    }

    // adding event for snackbar..
    private var _showSnackbar = MutableLiveData<Boolean>()
    val showSnackbar: LiveData<Boolean> = _showSnackbar

    fun onShow(){
        _showSnackbar.value = false
    }

    val nightString = Transformations.map(nights){ nights ->
        com.example.sleeptracker.formatNights(nights,application.resources)
    }

    init {
        initializeTonight()
    }

    private fun initializeTonight(){
        uiScope.launch {
            tonight.value = getTonightFromDB()
        }
    }

    private suspend fun getTonightFromDB(): SleepNight?{
        return withContext(Dispatchers.IO){
            var night = database.getTonight()
            if(night?.endTime != night?.startTime){
                night = null
            }
            night
        }
    }

    fun startTracking(){
        uiScope.launch {
            val night = SleepNight()
            insert(night)
            tonight.value = getTonightFromDB()
        }
    }

    private suspend fun insert(night: SleepNight){
        withContext(Dispatchers.IO){
            database.insert(night)
            Log.i("sleeptracker","night inserted")
        }
    }

    fun stopTracking(){
        uiScope.launch {
            val night = tonight.value ?: return@launch
            _eventNavigate.value = night
            night.endTime= System.currentTimeMillis()
            update(night)
        }
    }

    private suspend fun update(night: SleepNight){
        withContext(Dispatchers.IO){
            database.update(night)
            Log.i("sleeptracker","night updated")
        }
    }

    fun onClear(){
        uiScope.launch {
            clear()
            tonight.value = null
        }
        _showSnackbar.value = true
    }

    private suspend fun clear(){
        withContext(Dispatchers.IO){
            database.clear()
        }
    }

    override fun onCleared() {
            super.onCleared()
            viewModelJob.cancel()
    }
}

