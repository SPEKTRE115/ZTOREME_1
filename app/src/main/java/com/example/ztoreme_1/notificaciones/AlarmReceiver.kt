package com.example.ztoreme_1.notificaciones

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent

class AlarmReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        val service = Intent(context, NotificationService::class.java)
        service.putExtra("reason", intent.getStringExtra("reason"))
        service.putExtra("timestamp", intent.getLongExtra("timestamp", 0))
        service.putExtra("titulo", intent.getStringExtra("titulo"))
        service.putExtra("mensaje", intent.getStringExtra("mensaje"))
        context.startService(service)
    }
}