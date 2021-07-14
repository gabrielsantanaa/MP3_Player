package com.gabriel.personal.projects.mp3player

import android.media.MediaPlayer
import android.os.Bundle
import android.util.Log
import android.widget.SeekBar
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.gabriel.personal.projects.mp3player.MainViewModel.ButtonType.*
import com.gabriel.personal.projects.mp3player.databinding.ActivityMainBinding

const val TIMER_DELAY: Long = 100L

class MainActivity : AppCompatActivity() {

    private var _binding: ActivityMainBinding? = null
    private val binding get() = _binding!!

    private val mediaPlayer by lazy {
        Log.d("mediaPlayer", "created")
        MediaPlayer.create(applicationContext, R.raw.cant_say)
    }

    private val mainViewModel by lazy {
        ViewModelProvider(
            this,
            MainViewModel.MainViewModelFactory(
                CoroutineTimer(mediaPlayer.duration.toLong(), TIMER_DELAY))
        ).get(MainViewModel::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupSeekbar()
        setupMediaPlayer()
        formatTexts()
        setupButtonListeners()
        setupLiveDataStates()

    }

    private fun setupMediaPlayer() {
        mediaPlayer.setOnCompletionListener {
            mainViewModel.completeTimer()
            mediaPlayer.seekTo(0)
        }

    }

    private fun setupLiveDataStates() {
        mainViewModel.currentButtonType.observe(this, {
            binding.buttonPlay.setImageResource(it.iconDrawableRes)
        })
    }

    private fun setupButtonListeners() {
        binding.apply {
            buttonPlay.setOnClickListener{
                when (mainViewModel.currentButtonType.value) {
                    PLAY -> {
                        mainViewModel.startTimer {
                            seekBar.progress = mediaPlayer.currentPosition
                            textCurrentDuration.text = formatMillisToMinAndSec(mediaPlayer.currentPosition)

                        }
                        mediaPlayer.start()
                    }
                    PAUSE -> {
                        mainViewModel.pauseTimer()
                        mediaPlayer.pause()
                    }
                }

            }
        }
    }

    private fun formatTexts() {
        binding.apply {
            textTotalDuration.text = formatMillisToMinAndSec(mediaPlayer.duration)
            textCurrentDuration.text = formatMillisToMinAndSec(mediaPlayer.currentPosition)
        }
    }

    private fun setupSeekbar() {
        binding.seekBar.apply {
            max = mediaPlayer.duration

            setOnSeekBarChangeListener(object: SeekBar.OnSeekBarChangeListener {
                override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                    if (fromUser) mediaPlayer.seekTo(progress)
                }

                override fun onStartTrackingTouch(seekBar: SeekBar?) {

                }

                override fun onStopTrackingTouch(seekBar: SeekBar?) {

                }
            })
        }
    }

    private fun formatMillisToMinAndSec(time: Int): String {
        val minutes: Int = time / (60 * 1000)
        val seconds: Int = time / 1000 % 60
        return String.format("%d:%02d", minutes, seconds)
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}

