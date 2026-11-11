package ru.emkucherenko.testtasks

import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.os.Bundle
import android.os.Handler
import android.os.IBinder
import android.os.Looper
import android.os.Message
import android.os.Messenger
import android.os.Process
import android.os.RemoteException
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

private const val TAG = "Square"

class MainActivity : AppCompatActivity() {

    private lateinit var numberInput: EditText
    private lateinit var resultText: TextView

    private var serviceMessenger: Messenger? = null
    private var bound = false

    // Сюда сервис присылает ответ
    private val replyMessenger = Messenger(Handler(Looper.getMainLooper()) { msg ->
        if (msg.what == SquareService.MSG_RESULT) {
            resultText.text = msg.data.getLong(SquareService.KEY_RESULT).toString()
        }
        true
    })

    private val connection = object : ServiceConnection {
        override fun onServiceConnected(name: ComponentName?, service: IBinder?) {
            serviceMessenger = Messenger(service)
        }

        override fun onServiceDisconnected(name: ComponentName?) {
            serviceMessenger = null
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        Log.i(TAG, "activity pid=${Process.myPid()}")

        numberInput = findViewById(R.id.numberInput)
        resultText = findViewById(R.id.resultText)
        findViewById<Button>(R.id.squareButton).setOnClickListener { requestSquare() }
    }

    override fun onStart() {
        super.onStart()
        bound = bindService(
            Intent(this, SquareService::class.java),
            connection,
            Context.BIND_AUTO_CREATE
        )
    }

    override fun onStop() {
        super.onStop()
        if (bound) {
            unbindService(connection)
            bound = false
        }
        serviceMessenger = null
    }

    private fun requestSquare() {
        val number = numberInput.text.toString().toIntOrNull()
        if (number == null) {
            resultText.text = "Введите целое число"
            return
        }
        val target = serviceMessenger
        if (target == null) {
            resultText.text = "Сервис ещё не подключён"
            return
        }
        val message = Message.obtain(null, SquareService.MSG_SQUARE, number, 0)
        message.replyTo = replyMessenger
        try {
            target.send(message)
        } catch (e: RemoteException) {
            resultText.text = "Сервис недоступен"
        }
    }
}
