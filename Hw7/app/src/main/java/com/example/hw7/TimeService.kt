package com.example.hw7

import android.R
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.Service
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Build
import android.os.IBinder
import android.widget.Toast
import androidx.core.app.NotificationCompat
import kotlin.math.ceil

class TimerService : Service() {

    companion object {
        const val ACTION_TIMER_UPDATE = "com.example.hw7.TIMER_UPDATE"
        const val ACTION_TIMER_FINISHED = "com.example.hw7.TIMER_FINISHED"
        const val EXTRA_REMAINING_SECONDS = "remaining_seconds"
        private const val CHANNEL_ID = "timer_channel"
        private const val NOTIFICATION_ID = 1001
    }

    private var remainingSeconds = 25 * 60
    private var isRunning = false
    private var timerThread: Thread? = null

    // 系統廣播接收器：監聽螢幕亮起 (ACTION_SCREEN_ON)
    private val screenOnReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
            if (Intent.ACTION_SCREEN_ON == intent.action && isRunning) {
                val minsLeft = ceil(remainingSeconds / 60.0).toInt()
                Toast.makeText(context, "Focus Reminder: $minsLeft mins remaining!", Toast.LENGTH_LONG).show()
            }
        }
    }

    override fun onCreate() {
        super.onCreate()
        val filter = IntentFilter(Intent.ACTION_SCREEN_ON)
        registerReceiver(screenOnReceiver, filter)
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        if (intent != null && intent.hasExtra("DURATION_SECONDS")) {
            remainingSeconds = intent.getIntExtra("DURATION_SECONDS", 25 * 60)
        }

        if (!isRunning) {
            isRunning = true
            startCountdown()
        }
        return START_NOT_STICKY
    }

    private fun startCountdown() {
        timerThread = Thread {
            while (isRunning && remainingSeconds > 0) {
                try {
                    Thread.sleep(1000)
                    remainingSeconds--

                    val updateIntent = Intent(ACTION_TIMER_UPDATE).apply {
                        setPackage(packageName)
                        putExtra(EXTRA_REMAINING_SECONDS, remainingSeconds)
                    }
                    sendBroadcast(updateIntent)
                } catch (e: InterruptedException) {
                    Thread.currentThread().interrupt()
                    break
                }
            }

            if (isRunning && remainingSeconds <= 0) {
                isRunning = false

                // 1. 發送完成廣播給 MainActivity (若 App 在前景，立即跳轉)
                val finishedIntent = Intent(ACTION_TIMER_FINISHED).apply {
                    setPackage(packageName)
                }
                sendBroadcast(finishedIntent)

                // 2. 準備跳轉 Intent
                val finishIntent = Intent(this@TimerService, FinishActivity::class.java).apply {
                    addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP)
                }

                // 3. 發送 Notification (解決 Android 10+ 背景啟動 Activity 限制)
                showFinishNotification(finishIntent)

                // 4. 嘗試直接啟動 Activity (若系統允許或已有懸浮窗權限)
                try {
                    startActivity(finishIntent)
                } catch (e: Exception) {
                    e.printStackTrace()
                }

                stopSelf()
            }
        }
        timerThread?.start()
    }

    private fun showFinishNotification(targetIntent: Intent) {
        val notificationManager = getSystemService(NOTIFICATION_SERVICE) as NotificationManager

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                CHANNEL_ID,
                "Timer Notifications",
                NotificationManager.IMPORTANCE_HIGH
            ).apply {
                description = "Notifications for completed timer sessions"
            }
            notificationManager.createNotificationChannel(channel)
        }

        val pendingIntent = PendingIntent.getActivity(
            this,
            0,
            targetIntent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        val notification = NotificationCompat.Builder(this, CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_dialog_info)
            .setContentTitle("Focus Session Complete!")
            .setContentText("Great job staying focused! Tap to view summary.")
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setAutoCancel(true)
            .setContentIntent(pendingIntent)
            .build()

        notificationManager.notify(NOTIFICATION_ID, notification)
    }

    override fun onDestroy() {
        isRunning = false
        timerThread?.interrupt()
        unregisterReceiver(screenOnReceiver)
        super.onDestroy()
    }

    override fun onBind(intent: Intent?): IBinder? = null
}
