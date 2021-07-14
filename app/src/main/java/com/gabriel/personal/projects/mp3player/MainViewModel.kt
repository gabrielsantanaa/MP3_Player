package com.gabriel.personal.projects.mp3player

import androidx.lifecycle.*

class MainViewModel private constructor(private val _coroutineTimer: CoroutineTimer) : ViewModel() {

    private val _currentButtonType = MutableLiveData(ButtonType.PLAY)
    val currentButtonType: LiveData<ButtonType> = _currentButtonType

    fun startTimer(onTick: (tick: Long) -> Unit)  {
        _coroutineTimer.play(viewModelScope) {
            onTick(it)
        }
        _currentButtonType.value = ButtonType.PAUSE
    }

    fun pauseTimer() {
        _coroutineTimer.pause()
        _currentButtonType.value = ButtonType.PLAY
    }

    fun completeTimer() {
        _coroutineTimer.complete()
        _currentButtonType.value = ButtonType.PLAY
    }

    @Suppress("UNCHECKED_CAST")
    class MainViewModelFactory(private val coroutineTimer: CoroutineTimer): ViewModelProvider.Factory {
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            if(modelClass.isAssignableFrom(MainViewModel::class.java)) {
                return MainViewModel(coroutineTimer) as T
            }
            throw IllegalArgumentException("Invalid viewModel class")
        }
    }

    enum class ButtonType(val iconDrawableRes: Int) {
        PLAY(R.drawable.ic_baseline_play_arrow_24),
        PAUSE(R.drawable.ic_baseline_pause_24)
    }
}