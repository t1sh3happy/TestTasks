package ru.emkucherenko.testtasks

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.Service
import android.content.Intent
import android.content.pm.ServiceInfo
import android.os.Build
import android.os.IBinder
import android.util.Log
import kotlin.concurrent.thread

class SecondService : Service() {
    override fun onBind(intent: Intent?): IBinder? = null

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        Log.i(TAG, "SecondService: onStartCommand, поток=${Thread.currentThread().name}")
        startAsForeground()
        thread {
            Log.i(TAG, "SecondService: жду 40 секунд, поток=${Thread.currentThread().name}")
            Thread.sleep(40000)
            Log.i(
                TAG,
                "SecondService: пауза прошла, открываю чужое приложение, поток=${Thread.currentThread().name}"
            )
            val intent = Intent().apply {
                setClassName(
                    "ru.emkucherenko.testtasks",
                    "ru.emkucherenko.testtasks.MainActivity"
                )
                addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            }
            startActivity(intent)
            stopSelf()
        }
        return START_NOT_STICKY
    }

    override fun onDestroy() {
        Log.i(TAG, "SecondService: onDestroy, поток=${Thread.currentThread().name}")
        super.onDestroy()
    }

    private fun startAsForeground() {
        val channelId = "second_service_channel"
        val notificationManager = getSystemService(NOTIFICATION_SERVICE) as NotificationManager

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                channelId,
                "Second Service",
                NotificationManager.IMPORTANCE_LOW
            )
            notificationManager.createNotificationChannel(channel)
        }

        val notification: Notification = Notification.Builder(this, channelId)
            .setContentTitle("SecondService")
            .setContentText("Работает 40 секунд...")
            .setSmallIcon(android.R.drawable.ic_popup_sync)
            .build()

        startForeground(1, notification, ServiceInfo.FOREGROUND_SERVICE_TYPE_DATA_SYNC)
    }
}