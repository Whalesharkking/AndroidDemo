package com.example.meinedemo.service

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.Service
import android.content.Intent
import android.graphics.BitmapFactory
import android.os.Binder
import android.os.Build
import android.os.IBinder
import androidx.core.app.NotificationCompat
import com.example.meinedemo.R


class MusicPlayerService : Service() {
    private val NOTIFICATION_CHANNEL_ID = "ch.hslu.mobpro.demo.channel"
    private val NOTIFICATION_CHANNEL_NAME = "Music notifications"
    private val NOTIFICATION_ID = 23

    override fun onCreate() {
        super.onCreate()
        createNotificationChannelIfNeeded()
    }
//    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
//        val NOTIFICATION_ID = 23
//
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
//            val channel = NotificationChannel(
//                "ch.hslu.mobpro.demo.channel",
//                "Music notifications",
//                NotificationManager.IMPORTANCE_DEFAULT
//            )
//            val notificationManager = getSystemService(NotificationManager::class.java)
//            notificationManager.createNotificationChannel(channel)
//        }
//        val musicTitles = resources.getStringArray(R.array.music_titles) // Lade das String-Array
//        val firstTitle = musicTitles[0]
//        val notification = createNotification(musicTitle = firstTitle)
//
//        // Startet den Service im Vordergrund direkt in onStartCommand()
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
//            startForeground(
//                NOTIFICATION_ID,
//                notification,
//                ServiceInfo.FOREGROUND_SERVICE_TYPE_MEDIA_PLAYBACK
//            )
//        } else {
//            startForeground(NOTIFICATION_ID, notification)
//        }
//
//        return START_STICKY
//    }


    private fun createNotificationChannelIfNeeded() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                NOTIFICATION_CHANNEL_ID,
                NOTIFICATION_CHANNEL_NAME,
                NotificationManager.IMPORTANCE_DEFAULT
            ).apply {
                description = "Benachrichtigungen für Musiksteuerung"
            }

            val notificationManager = getSystemService(NotificationManager::class.java)
            notificationManager.createNotificationChannel(channel)
        }
    }

    private fun createNotification(musicTitle: String): Notification {
        return NotificationCompat.Builder(this, NOTIFICATION_CHANNEL_ID)
            .setContentTitle("HSLU Music Player")
            .setContentText(musicTitle)
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setOngoing(true)
            .setSmallIcon(android.R.drawable.ic_media_play)
            .setLargeIcon(BitmapFactory.decodeResource(resources, android.R.drawable.ic_media_play))
            .setWhen(System.currentTimeMillis())
            .setCategory(Notification.CATEGORY_SERVICE)
            .build()
    }

    fun playNextSong(): String {
        val titles = resources.getStringArray(R.array.music_titles)
        val next = titles.random()
        val notification = createNotification(musicTitle = next)

        val notificationManager = getSystemService(NotificationManager::class.java)
        notificationManager.notify(NOTIFICATION_ID, notification)

        return next
    }

    override fun onDestroy() {
        stopForeground(STOP_FOREGROUND_REMOVE)
        super.onDestroy()
    }

    private val musicPlayerAPI = MusicPlayerApiImpl()
    override fun onBind(intent: Intent?): IBinder = musicPlayerAPI

    inner class MusicPlayerApiImpl : MusicPlayerApi, Binder() {
        override fun playNextSong(): String {
            return this@MusicPlayerService.playNextSong()
        }
    }
}

