package com.example.suyxhear.util

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.example.suyxhear.R

class NotificationHelper(private val context: Context) {

    private val CHANNEL_ID = "noise_alert_channel"
    private val NOTIFICATION_ID = 101

    init {
        createNotificationChannel()
    }

    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name = "Alertas de Ruído"
            val descriptionText = "Canal para notificações de excesso de ruído."
            val importance = NotificationManager.IMPORTANCE_HIGH
            val channel = NotificationChannel(CHANNEL_ID, name, importance).apply {
                description = descriptionText
            }
            val notificationManager: NotificationManager =
                context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
    }

    fun sendNotification(decibels: Int, limit: Int) {
        val builder = NotificationCompat.Builder(context, CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_hearing) // Ícone da notificação
            .setContentTitle("Alerta de Ruído!")
            .setContentText("O nível de ruído ($decibels dB) ultrapassou o limite de $limit dB.")
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setAutoCancel(true)

        try {
            NotificationManagerCompat.from(context).notify(NOTIFICATION_ID, builder.build())
        } catch (e: SecurityException) {
            // Lidar com a falta da permissão POST_NOTIFICATIONS em Android 13+
            // O ideal é ter uma UI para explicar e pedir a permissão novamente.
        }
    }
}
