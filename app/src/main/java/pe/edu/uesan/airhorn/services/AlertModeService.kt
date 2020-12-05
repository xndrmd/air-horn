package pe.edu.uesan.airhorn.services

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Intent
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat
import androidx.lifecycle.LifecycleService
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.observe
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import pe.edu.uesan.airhorn.models.AlertModeEvent
import pe.edu.uesan.airhorn.models.CountdownEvent
import pe.edu.uesan.airhorn.utilities.*
import javax.inject.Inject

@AndroidEntryPoint
class AlertModeService: LifecycleService() {
    @Inject
    lateinit var notificationBuilder: NotificationCompat.Builder

    @Inject
    lateinit var notificationManager: NotificationManager

    private var isServiceStopped = true

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
        alertModeEvent.postValue(AlertModeEvent.STOP)
        elapsedTimeMillis.postValue(0L)
    }

    private fun startForegroundService() {
        if (!isServiceStopped) return else isServiceStopped = false

        alertModeEvent.postValue(AlertModeEvent.START)
        startTimer()

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            createNotificationChannel()
        }

        startForeground(NOTIFICATION_ID, notificationBuilder.build())

        elapsedTimeMillis.observe(this) {
            if (!isServiceStopped) {
                val millis = TimerUtil.getFormattedTime(it, false)
                notificationBuilder.setContentText("En modo alerta por $millis")

                notificationManager.notify(NOTIFICATION_ID, notificationBuilder.build())
            }
        }
    }

    private fun stopService() {
        if (isServiceStopped) return

        isServiceStopped = true
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

    private fun startTimer() {
        val startedTimeMillis = System.currentTimeMillis()

        CoroutineScope(Dispatchers.Main).launch {
            while (alertModeEvent.value!! == AlertModeEvent.START && !isServiceStopped) {
                elapsedTimeMillis.postValue(System.currentTimeMillis() - startedTimeMillis)
                delay(50L)
            }
        }
    }

    companion object {
        val alertModeEvent = MutableLiveData<AlertModeEvent>()
        val elapsedTimeMillis = MutableLiveData<Long>()
    }
}