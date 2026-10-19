package com.example.homework7

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

sealed class WorkState {
    object Idle : WorkState()
    object Preparing : WorkState()
    data class Running(val progress: Int) : WorkState()
    object Completed : WorkState()
    object Canceled : WorkState()
}

class WorkViewModel : ViewModel() {

    private val _workState = MutableLiveData<WorkState>(WorkState.Idle)
    val workState: LiveData<WorkState> = _workState

    private var job: Job? = null

    fun startWork() {
        job?.cancel()
        job = viewModelScope.launch(Dispatchers.Main) {
            _workState.value = WorkState.Preparing
            delay(1500)

            for (i in 1..100) {
                _workState.value = WorkState.Running(i)
                delay(50)
            }

            _workState.value = WorkState.Completed
        }
    }

    fun cancelWork() {
        if (job?.isActive == true) {
            job?.cancel()
            _workState.value = WorkState.Canceled
        }
    }
}