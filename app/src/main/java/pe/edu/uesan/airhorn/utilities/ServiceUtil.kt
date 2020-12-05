package pe.edu.uesan.airhorn.utilities

import android.content.Context
import android.content.Intent
import android.os.Build

class ServiceUtil {
    companion object {
        fun sendCommand(context: Context, service: Class<*>, action: String) {
            Intent(context, service).also {
                it.action = action

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    context.startForegroundService(it)
                } else {
                    context.startService(it)
                }
            }
        }
    }
}