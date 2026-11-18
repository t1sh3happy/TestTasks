package ru.emkucherenko.testtasks

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log

class MyReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {
        if (intent.action != Intent.ACTION_AIRPLANE_MODE_CHANGED) return

        val isEnabled = intent.getBooleanExtra("state", false)
        if (isEnabled) {
            Log.i(TAG, "Авиарежим включён")
        } else {
            Log.i(TAG, "Авиарежим выключен")
        }
    }

    private companion object {
        const val TAG = "AirplaneMode"
    }
}