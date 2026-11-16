package com.example.hw7

import android.Manifest
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.content.pm.PackageManager
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import java.util.Locale

class MainActivity : AppCompatActivity() {

    private lateinit var tvStatus: TextView
    private lateinit var tvTimer: TextView
    private lateinit var btnStart: Button
    private lateinit var btnStop: Button
    private var isReceiverRegistered = false

    private val timerUpdateReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
            when (intent.action) {
                TimerService.ACTION_TIMER_UPDATE -> {
                    val seconds = intent.getIntExtra(TimerService.EXTRA_REMAINING_SECONDS, 0)
                    updateTimerUI(seconds)
                }
                TimerService.ACTION_TIMER_FINISHED -> {
                    val finishIntent = Intent(this@MainActivity, FinishActivity::class.java)
                    startActivity(finishIntent)
                }
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        requestNotificationPermission()

        tvStatus = findViewById(R.id.tvStatus)
        tvTimer = findViewById(R.id.tvTimer)
        btnStart = findViewById(R.id.btnStart)
        btnStop = findViewById(R.id.btnStop)

        btnStart.setOnClickListener {
            registerTimerReceiver()
            val serviceIntent = Intent(this, TimerService::class.java).apply {
                // 【測試小技巧】先設為 10 秒方便快速測試完成畫面，測試成功後再改回 25 * 60
                putExtra("DURATION_SECONDS", 10)
            }
            startService(serviceIntent)

            tvStatus.text = "Focusing..."
            tvStatus.setTextColor(Color.parseColor("#E53935"))
        }

        btnStop.setOnClickListener {
            stopService(Intent(this, TimerService::class.java))
            tvStatus.text = "Ready to Focus"
            tvStatus.setTextColor(Color.parseColor("#2E7D32"))
            tvTimer.text = "25:00"
        }
    }

    private fun requestNotificationPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (checkSelfPermission(Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
                requestPermissions(arrayOf(Manifest.permission.POST_NOTIFICATIONS), 101)
            }
        }
    }

    private fun registerTimerReceiver() {
        if (!isReceiverRegistered) {
            val filter = IntentFilter().apply {
                addAction(TimerService.ACTION_TIMER_UPDATE)
                addAction(TimerService.ACTION_TIMER_FINISHED)
            }
            ContextCompat.registerReceiver(
                this,
                timerUpdateReceiver,
                filter,
                ContextCompat.RECEIVER_NOT_EXPORTED
            )
            isReceiverRegistered = true
        }
    }

    private fun updateTimerUI(totalSeconds: Int) {
        val minutes = totalSeconds / 60
        val seconds = totalSeconds % 60
        tvTimer.text = String.format(Locale.getDefault(), "%02d:%02d", minutes, seconds)
    }

    override fun onDestroy() {
        if (isReceiverRegistered) {
            unregisterReceiver(timerUpdateReceiver)
            isReceiverRegistered = false
        }
        super.onDestroy()
    }
}
