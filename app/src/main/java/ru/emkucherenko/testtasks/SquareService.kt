package ru.emkucherenko.testtasks

import android.app.Service
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.IBinder
import android.os.Looper
import android.os.Message
import android.os.Messenger
import android.os.Process
import android.os.RemoteException
import android.util.Log

private const val TAG = "Square"

class SquareService : Service() {

    private val messenger = Messenger(Handler(Looper.getMainLooper()) { msg ->
        if (msg.what == MSG_SQUARE) {
            val number = msg.arg1.toLong()
            Log.i(TAG, "service pid=${Process.myPid()}: $number squared")

            val reply = Message.obtain(null, MSG_RESULT)
            reply.data = Bundle().apply { putLong(KEY_RESULT, number * number) }
            try {
                msg.replyTo?.send(reply)
            } catch (e: RemoteException) {
                Log.e(TAG, "cannot send reply", e)
            }
        }
        true
    })

    override fun onBind(intent: Intent?): IBinder = messenger.binder

    companion object {
        const val MSG_SQUARE = 1
        const val MSG_RESULT = 2
        const val KEY_RESULT = "result"
    }
}
