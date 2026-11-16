package com.example.hw7

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log
import android.widget.Toast

class BootReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        if (Intent.ACTION_BOOT_COMPLETED == intent.action) {
            Log.d("BootReceiver", "裝置已重啟，準備開始每日學習課程！")
            Toast.makeText(context, "裝置已重啟，記得開啟番茄鐘專注！", Toast.LENGTH_LONG).show()
        }
    }
}
