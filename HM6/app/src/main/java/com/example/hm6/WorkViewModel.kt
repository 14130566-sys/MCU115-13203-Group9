package com.example.hm6

import android.app.Application
import android.media.MediaPlayer
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class WorkViewModel(application: Application) : AndroidViewModel(application) {

    private val _progressText = MutableLiveData("Idle")
    val progressText: LiveData<String> get() = _progressText

    private val _progressPercent = MutableLiveData(0)
    val progressPercent: LiveData<Int> get() = _progressPercent

    private val _resultText = MutableLiveData("Idle")
    val resultText: LiveData<String> get() = _resultText

    private val _navigateToPage = MutableLiveData<Int?>(null)
    val navigateToPage: LiveData<Int?> get() = _navigateToPage

    private var workerJob: Job? = null
    private var mediaPlayer: MediaPlayer? = null

    fun onPageNavigated() {
        _navigateToPage.value = null
    }

    fun startTask() {
        if (workerJob?.isActive == true) return

        startMusic()
        _navigateToPage.value = 1

        workerJob = viewModelScope.launch(Dispatchers.Default) {
            _progressText.postValue("Preparing...")
            _resultText.postValue("Preparing...")
            _progressPercent.postValue(0)
            delay(2000)

            _resultText.postValue("Working...")
            for (i in 1..100) {
                _progressPercent.postValue(i)
                _progressText.postValue("Working... $i%")
                delay(100)
            }

            _progressText.postValue("背景工作結束!")
            _resultText.postValue("背景工作結束!")
            stopMusic()
        }
    }

    fun cancelTask() {
        if (workerJob?.isActive == true) {
            workerJob?.cancel()
            _progressText.postValue("Canceled")
            _resultText.postValue("Canceled")
            stopMusic()
        }
    }

    private fun startMusic() {
        stopMusic()
        try {
            mediaPlayer = MediaPlayer.create(getApplication(), R.raw.background_music)
            if (mediaPlayer == null) {
                Log.e("WorkViewModel", "MediaPlayer 創建失敗 (mediaPlayer == null)，請檢查 R.raw.background_music 檔案")
            } else {
                mediaPlayer?.isLooping = true
                mediaPlayer?.start()
                Log.d("WorkViewModel", "背景音樂開始播放")
            }
        } catch (e: Exception) {
            Log.e("WorkViewModel", "播放音樂時發生錯誤", e)
        }
    }

    private fun stopMusic() {
        mediaPlayer?.let {
            try {
                if (it.isPlaying) {
                    it.stop()
                }
                it.release()
                Log.d("WorkViewModel", "背景音樂已停止並釋放資源")
            } catch (e: Exception) {
                Log.e("WorkViewModel", "停止音樂時發生錯誤", e)
            }
        }
        mediaPlayer = null
    }

    override fun onCleared() {
        super.onCleared()
        stopMusic()
    }
}