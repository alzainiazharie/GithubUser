package com.azharie.alzaini.githubuser3.alarmnotif

import android.app.AlarmManager
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.media.RingtoneManager
import android.os.Build
import android.widget.Toast
import androidx.core.app.NotificationCompat
import com.azharie.alzaini.githubuser3.R
import com.azharie.alzaini.githubuser3.activity.MainActivity
import java.util.*

class AlarmReceiver : BroadcastReceiver() {
    companion object {
        const val CHANNEL_ID = "Channel_1"
        const val CHANNEL_NAME = "AlarmManager channel"

        const val TYPE_REPEATING = "RepeatingAlarm"
        const val EXTRA_MESSAGE = "message"




        private const val ID_REPEATING = 101

    }

    override fun onReceive(context: Context, intent: Intent) {

        val message = intent.getStringExtra(EXTRA_MESSAGE)

        showAlarmNotif(context, message.toString())

    }

    private fun showAlarmNotif(context: Context , message: String){
        val notificationManagerCompat = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        val alarmSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)
        val intent = Intent(context, MainActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        val pendingIntentActivity = PendingIntent.getActivity(context, ID_REPEATING, intent, PendingIntent.FLAG_UPDATE_CURRENT)

        val builder = NotificationCompat.Builder(context, CHANNEL_ID)
            .setContentTitle(TYPE_REPEATING)
            .setContentText(message)
            .setSmallIcon(R.drawable.ic_baseline_notifications_24)
            .setVibrate(longArrayOf(1000,1000,1000,1000,1000))
            .setSound(alarmSound)
            .setContentIntent(pendingIntentActivity)
            .setAutoCancel(true)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            val channel = NotificationChannel(CHANNEL_ID, CHANNEL_NAME, NotificationManager.IMPORTANCE_DEFAULT)
            channel.enableVibration(true)
            channel.description = CHANNEL_NAME
            channel.vibrationPattern = longArrayOf(1000,1000,1000,1000,1000)
            builder.setChannelId(CHANNEL_ID)
            notificationManagerCompat.createNotificationChannel(channel)
        }
        val notification = builder.build()
        notificationManagerCompat.notify(ID_REPEATING, notification)


    }
    fun repeatingAlarm(context: Context, message: String){
        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val intent = Intent(context, AlarmReceiver::class.java)
        intent.putExtra(EXTRA_MESSAGE, message)


        val calender = Calendar.getInstance()
        calender.set(Calendar.HOUR_OF_DAY, 9)
        calender.set(Calendar.MINUTE, 0)
        calender.set(Calendar.SECOND, 0)

        //pending intent
        /*
        *         val pendingIntent = PendingIntent.getBroadcast(context, ID_REPEATING, intent, PendingIntent.FLAG_ONE_SHOT)
        alarmManager.setInexactRepeating(AlarmManager.RTC_WAKEUP, calender.timeInMillis, AlarmManager.INTERVAL_DAY, pendingIntent)
        Toast.makeText(context, "Daily Reminder Set Up !", Toast.LENGTH_SHORT).show()
        * */
        val pendingIntent = PendingIntent.getBroadcast(context, ID_REPEATING, intent, PendingIntent.FLAG_ONE_SHOT)
        alarmManager.setInexactRepeating(AlarmManager.RTC_WAKEUP, calender.timeInMillis, AlarmManager.INTERVAL_DAY, pendingIntent)
        Toast.makeText(context, R.string.toast_set_alarm,Toast.LENGTH_SHORT).show()

    }

    fun cancelAlarm(context: Context){
        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val intent = Intent(context, AlarmReceiver::class.java)
        val requestCode = ID_REPEATING
        val pendingIntent = PendingIntent.getBroadcast(context, requestCode, intent, 0)
        PendingIntent.FLAG_CANCEL_CURRENT
        alarmManager.cancel(pendingIntent)
        Toast.makeText(context, R.string.toast_cancel_alarm,Toast.LENGTH_SHORT).show()
    }
}