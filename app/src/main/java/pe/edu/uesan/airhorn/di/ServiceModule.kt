package pe.edu.uesan.airhorn.di

import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import androidx.core.app.NotificationCompat
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ServiceComponent
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.android.scopes.ServiceScoped
import pe.edu.uesan.airhorn.MainActivity
import pe.edu.uesan.airhorn.R
import pe.edu.uesan.airhorn.utilities.NOTIFICATION_CHANNEL_ID

@InstallIn(ServiceComponent::class)
@Module
class ServiceModule {
    @ServiceScoped
    @Provides
    fun provideMainActivityPendingIntent(@ApplicationContext context: Context) =
            PendingIntent.getActivity(
                    context,
                    420,
                    Intent(context, MainActivity::class.java).apply {
                        this.flags = Intent.FLAG_ACTIVITY_SINGLE_TOP
                    },
                    PendingIntent.FLAG_UPDATE_CURRENT
            )

    @ServiceScoped
    @Provides
    fun provideNotificationBuilder(
            @ApplicationContext context: Context,
            pendingIntent: PendingIntent
    ) = NotificationCompat.Builder(context, NOTIFICATION_CHANNEL_ID)
            .setAutoCancel(false)
            .setOngoing(true)
            .setSmallIcon(R.mipmap.ic_launcher)
            .setContentTitle("Air Horn!")
            .setContentText("00:00:00")
            .setContentIntent(pendingIntent)

    @ServiceScoped
    @Provides
    fun provideNotificationManager(@ApplicationContext context: Context) =
            context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
}