package com.example.mad_practical_4

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent

class AlarmBroadcastReceiver : BroadcastReceiver() {

    companion object {
        const val ACTION_SERVICE = "Service1"
    }

    override fun onReceive(context: Context, intent: Intent) {
        val str1 = intent.getStringExtra(ACTION_SERVICE)
        if (str1 != null) {
            val intentService = Intent(context, AlarmService::class.java)
            when (str1) {
                "Start" -> context.startService(intentService)
                "Stop" -> context.stopService(intentService)
            }
        }
    }
}