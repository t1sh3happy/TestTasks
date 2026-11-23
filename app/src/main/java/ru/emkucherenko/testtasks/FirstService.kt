package ru.emkucherenko.testtasks

import android.app.Service
import android.content.Intent
import android.os.IBinder
import android.util.Log

class FirstService : Service() {
    override fun onBind(intent: Intent?): IBinder? = null

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {

        Log.i(TAG, "FirstService: onStartCommand, поток=${Thread.currentThread().name}")
        stopSelf()
        Thread {
            Log.i(TAG, "FirstService: жду 5 секунд, поток=${Thread.currentThread().name}")
            Thread.sleep(5000)
            Log.i(
                TAG,
                "FirstService: пауза прошла, запускаю SecondService, поток=${Thread.currentThread().name}"
            )
        }
            .start()
        startService(Intent(this, SecondService::class.java))
        stopSelf()
        return START_NOT_STICKY
    }

    override fun onDestroy() {
        Log.i(TAG, "FirstService: onDestroy, поток=${Thread.currentThread().name}")
        super.onDestroy()
    }
}