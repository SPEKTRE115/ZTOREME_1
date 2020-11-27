package com.example.ztoreme_1.notificaciones

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
/*
Esta clase se encarga de recibir las señales de las notificaciones generadas
con la herramienta en el NotificactionUtils.
 */
class AlarmReceiver : BroadcastReceiver() {
    /*Método que se encarga de recibir el intent, desempaquetarlo e iniciar el servicio
     de notificación.*/
    override fun onReceive(context: Context, intent: Intent) {
        val service = Intent(context, NotificationService::class.java)
        service.putExtra("reason", intent.getStringExtra("reason"))
        service.putExtra("timestamp", intent.getLongExtra("timestamp", 0))
        service.putExtra("titulo", intent.getStringExtra("titulo"))
        service.putExtra("mensaje", intent.getStringExtra("mensaje"))
        context.startService(service)
    }
}