package com.example.myapplication

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.media.AudioAttributes
import android.media.RingtoneManager
import android.os.Build
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.app.TaskStackBuilder
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity() {

    // TODO: 28-12-2021 Understand Broadcast Receiver & add action buttons such as SEE or DISMISS

    companion object {
        const val CHANNEL_ID = "NOTIFICATION_CHANNEL_ID_1"
        private const val CHANNEL_NAME = "General notifications"

        //for each notification
        private const val PENDING_INTENT_REQUEST_CODE = 1
        private const val NOTIFICATION_ID = 1
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        createNotificationChannel()

        // Create an explicit intent for an Activity in your app
        val intent = Intent(this, SecondActivity::class.java)

        // to allow the NotificationManager execute your Intent using your application's permission.
        val pendingIntent = TaskStackBuilder.create(this).run {
            addNextIntentWithParentStack(intent)
            getPendingIntent(PENDING_INTENT_REQUEST_CODE, PendingIntent.FLAG_UPDATE_CURRENT)
        }

        // builder defines your actual Notification
        val builder = NotificationCompat.Builder(this, CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_notifications_active_black_24dp)
            .setContentTitle("Click to see me")
            .setStyle(
                NotificationCompat.BigTextStyle()
                    .bigText("Hii\nHello\nWhat's doing bro\nNothing Much\nSee Me Bro")
            )
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setContentIntent(pendingIntent)
            .setAutoCancel(true)

        show_notification_button.setOnClickListener {
            with(NotificationManagerCompat.from(this)) {
                // notificationId is a unique int for each notification that you must define
                // if it is the same then it just update the notification with new one
                notify(NOTIFICATION_ID, builder.build())
            }
        }
    }

    private fun createNotificationChannel() {
        // Create the NotificationChannel, but only on API 26+ because
        // the NotificationChannel class is new and not in the support library
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            // Create separate channel for each type of notification the app issue
            val channel = NotificationChannel(
                CHANNEL_ID,
                CHANNEL_NAME,
                NotificationManager.IMPORTANCE_HIGH
            ).also {
                //      LIGHT
                it.enableLights(true)

                //      SOUND
                val ringtoneUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)
                val audioAttribute = AudioAttributes.Builder()
                    .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
                    .build()
                it.setSound(ringtoneUri, audioAttribute)

                //      VIBRATION
                it.enableVibration(true)
                // Vibration Pattern if vibration is enabled
                val vibrationDuration = 1000L
                val waitingDuration = 2000L
                it.vibrationPattern = longArrayOf(
                    waitingDuration,
                    vibrationDuration,
                    waitingDuration,
                    vibrationDuration
                )
            }

            // Register the channel with the system
            val notificationManager =
                getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
    }
}