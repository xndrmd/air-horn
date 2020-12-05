package pe.edu.uesan.airhorn.services

import android.app.*
import android.content.Intent
import android.hardware.SensorManager
import android.os.Build
import android.os.HandlerThread
import android.os.Process
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat
import androidx.lifecycle.LifecycleService
import com.squareup.seismic.ShakeDetector
import dagger.hilt.android.AndroidEntryPoint
import pe.edu.uesan.airhorn.sharedpreferences.SharedPreferencesRepository
import pe.edu.uesan.airhorn.utilities.*
import javax.inject.Inject

@AndroidEntryPoint
class ShakeService: LifecycleService(), ShakeDetector.Listener {
    @Inject
    lateinit var sharedPreferencesRepository: SharedPreferencesRepository

    @Inject
    lateinit var notificationBuilder: NotificationCompat.Builder

    @Inject
    lateinit var notificationManager: NotificationManager

    private var isServiceStopped = true

    private val shakeDetector = ShakeDetector(this);

    private var lastShake = 0L
    private val timeBetweenShakes = 1000L
    private var consecutiveShakes = 0

    override fun onCreate() {
        super.onCreate()
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

    override fun onDestroy() {
        super.onDestroy()

        shakeDetector.stop()
    }

    override fun hearShake() {
        val currentShake = System.currentTimeMillis()

        val distance = currentShake - lastShake

        if (distance <= timeBetweenShakes) {
            consecutiveShakes++
        } else {
            consecutiveShakes = 1
        }

        val shakesInFuture = sharedPreferencesRepository.getInt(SHARED_PREFERENCES_PARAMS_EVENTS_THRESHOLD)

        if (consecutiveShakes >= shakesInFuture) {
            startCountdownService()

            consecutiveShakes = 0
        }

        lastShake = System.currentTimeMillis()
    }

    private fun startForegroundService() {
        if (!isServiceStopped) return else isServiceStopped = false

        startShakeDetector()

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            createNotificationChannel()
        }

        startForeground(NOTIFICATION_ID, notificationBuilder.build())

        notificationBuilder.setContentText("")
        notificationManager.notify(NOTIFICATION_ID, notificationBuilder.build())
    }

    private fun stopService() {
        if (isServiceStopped) return

        isServiceStopped = true
        // cancel notification?
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

    private fun startShakeDetector() {
        HandlerThread("ShakeService", Process.THREAD_PRIORITY_FOREGROUND).apply {
            start()

            val sensorManager: SensorManager = getSystemService(SENSOR_SERVICE)  as SensorManager
            shakeDetector.start(sensorManager)
        }
    }

    private fun startCountdownService() {
        Intent(this, CountdownService::class.java).also {
            it.action = ACTION_START_SERVICE

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                startForegroundService(it)
            } else {
                startService(it)
            }
        }
    }
}