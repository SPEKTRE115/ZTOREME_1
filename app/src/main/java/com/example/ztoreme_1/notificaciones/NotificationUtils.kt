package com.example.ztoreme_1.notificaciones

import android.app.Activity
import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Intent
import java.util.*
/*
Clase que crea la señal que se va ha enviar al AlarmReceiver.
 */
class NotificationUtils {
    /*Método que sirve para crear la señal.*/
    fun setNotification(timeInMilliSeconds: Long, activity: Activity, titulo : String, mensaje : String) {

        if (timeInMilliSeconds > 0) {
            val alarmManager = activity.getSystemService(Activity.ALARM_SERVICE) as AlarmManager
            val alarmIntent = Intent(activity.applicationContext, AlarmReceiver::class.java)
            alarmIntent.putExtra("reason", "notification")
            alarmIntent.putExtra("timestamp", timeInMilliSeconds)
            alarmIntent.putExtra("titulo", titulo)
            alarmIntent.putExtra("mensaje", mensaje)
            val calendar = Calendar.getInstance()
            calendar.timeInMillis = timeInMilliSeconds
            val pendingIntent = PendingIntent.getBroadcast(activity, 0, alarmIntent, PendingIntent.FLAG_CANCEL_CURRENT)
            alarmManager.set(AlarmManager.RTC_WAKEUP, calendar.timeInMillis, pendingIntent)
        }
    }
}