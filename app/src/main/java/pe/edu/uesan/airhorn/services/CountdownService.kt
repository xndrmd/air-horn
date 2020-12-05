package pe.edu.uesan.airhorn.services

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Intent
import android.os.Build
import android.os.CountDownTimer
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat
import androidx.lifecycle.LifecycleService
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.map
import androidx.lifecycle.observe
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import pe.edu.uesan.airhorn.models.CountdownEvent
import pe.edu.uesan.airhorn.sharedpreferences.SharedPreferencesRepository
import pe.edu.uesan.airhorn.utilities.*
import javax.inject.Inject

@AndroidEntryPoint
class CountdownService: LifecycleService() {
    @Inject
    lateinit var sharedPreferencesRepository: SharedPreferencesRepository

    @Inject
    lateinit var notificationBuilder: NotificationCompat.Builder

    @Inject
    lateinit var notificationManager: NotificationManager

    private var isServiceStopped = true

    private lateinit var timer: CountDownTimer

    override fun onCreate() {
        super.onCreate()

        init()
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        intent?.let {
            when (it.action) {
                ACTION_START_SERVICE -> startForegroundService()
                ACTION_STOP_SERVICE -> stopService()
            }
        }

        return super.onStartCommand(intent, flags, startId)
    }

    private fun init() {
        countdownEvent.postValue(CountdownEvent.STOP)
        millisUntilFinished.postValue(0L)
    }

    private fun startForegroundService() {
        if (!isServiceStopped) return else isServiceStopped = false

        countdownEvent.postValue(CountdownEvent.START)
        startCountdown()

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            createNotificationChannel()
        }

        startForeground(NOTIFICATION_ID, notificationBuilder.build())

        millisUntilFinished.observe(this) {
            if (!isServiceStopped) {
                val millis = TimerUtil.getFormattedTime(it, false)
                notificationBuilder.setContentText("Modo alerta inicia en $millis")

                notificationManager.notify(NOTIFICATION_ID, notificationBuilder.build())
            }
        }
    }

    private fun stopService() {
        if (isServiceStopped) return

        isServiceStopped = true
        timer.cancel()
        init()
        // cancel notification ?
        stopForeground(true)
        stopSelf()
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun createNotificationChannel() {
        val channel = NotificationChannel(
                NOTIFICATION_CHANNEL_ID,
                NOTIFICATION_CHANNEL_NAME,
                NotificationManager.IMPORTANCE_LOW
        )

        notificationManager.createNotificationChannel(channel)
    }

    private fun startCountdown() {
        val millisInFuture = sharedPreferencesRepository
                .get(SHARED_PREFERENCES_PARAMS_SECONDS_THRESHOLD) * 1000L

        CoroutineScope(Dispatchers.Main).launch {
            timer = object: CountDownTimer(if (millisInFuture <= 0) 10000L else millisInFuture, 1000) {
                override fun onTick(millis: Long) {
                    millisUntilFinished.postValue(millis)
                }

                override fun onFinish() {
                    countdownEvent.postValue(CountdownEvent.COMPLETE)
                    startAlertModeService()
                    stopService()
                }
            }
            timer.start()
        }
    }

    private fun startAlertModeService() {
        Intent(this, AlertModeService::class.java).also {
            it.action = ACTION_START_SERVICE

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                startForegroundService(it)
            } else {
                startService(it)
            }
        }
    }

    companion object {
        val countdownEvent = MutableLiveData<CountdownEvent>()
        val millisUntilFinished = MutableLiveData<Long>()
    }
}