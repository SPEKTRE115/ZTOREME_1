package com.example.ztoreme_1

import android.Manifest
import android.app.*
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Color
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.view.Menu
import android.view.MenuItem
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import com.example.ztoreme_1.basedatos.DataBaseHandler
import com.example.ztoreme_1.categorias.ActivityAgregarCategoria
import com.example.ztoreme_1.categorias.Categoria
import com.example.ztoreme_1.notificaciones.NotificationUtils
import com.example.ztoreme_1.productos.ActivityAgregar
import com.example.ztoreme_1.productos.MisProductos
import java.util.*
import kotlin.system.exitProcess


class MainActivity : AppCompatActivity() {

    private val mNotificationTime = Calendar.getInstance().timeInMillis + 10000 //Set after 5 seconds from the current time.
    private var mNotified = false
    private var dosvecesAtras = false
    /*lateinit var notificationManager : NotificationManager
    lateinit var notificationChannel: NotificationChannel
    lateinit var  builder : Notification.Builder
    private val channelID = "NOTIFICACION_PRODUCTO_ANTIGUO"
    private val description = "Notifica que productos han estado" +
            "mucho tiempo almacenados"*/

    @RequiresApi(Build.VERSION_CODES.M)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(findViewById(R.id.toolbar))

        val context = this
        var db = DataBaseHandler(context)
        val lista  = arrayListOf<String>()
        var datos = db.extraerCategorias()

        for (i in datos){
            lista.add(i.nombreCategoria)
        }
        if (lista.isEmpty()){
            db.insertarCategoria(Categoria("Sin Categoría"))
        }

        if (!mNotified) {
            NotificationUtils().setNotification(mNotificationTime, this@MainActivity)
        }

        if  (checkSelfPermission("READ_EXTERNAL_STORAGE") == PackageManager.PERMISSION_GRANTED){
            Toast.makeText(context, "PERMISOS CONCEDIDOS", Toast.LENGTH_SHORT).show()
        }else{
            requestPermissions(arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE), 100)
        }

        val click_1 = findViewById(R.id.seccion1) as TextView
        click_1.setOnClickListener {
            val intento1 = Intent(this, MisProductos::class.java)
            startActivity(intento1)
        }

        val click_1_1 = findViewById(R.id.imagen_inventario) as ImageView
        click_1_1.setOnClickListener {
            val intento1 = Intent(this, MisProductos::class.java)
            startActivity(intento1)
        }

        val click_2 = findViewById(R.id.seccion2) as TextView
        click_2.setOnClickListener {
            val intento1 = Intent(this, ActivityAgregar::class.java)
            startActivity(intento1)
        }

        val click_2_1 = findViewById(R.id.imagen_agregar) as ImageView
        click_2_1.setOnClickListener {
            val intento1 = Intent(this, ActivityAgregar::class.java)
            startActivity(intento1)
        }

        val click_3 = findViewById(R.id.seccion3) as TextView
        click_3.setOnClickListener {
            val intento1 = Intent(this, ActivityAgregarCategoria::class.java)
            startActivity(intento1)
        }

        val click_3_1 = findViewById(R.id.imagen_categoria) as ImageView
        click_3_1.setOnClickListener {
            val intento1 = Intent(this, ActivityAgregarCategoria::class.java)
            startActivity(intento1)
        }

    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        return when (item.itemId) {
            R.id.action_settings -> true
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onBackPressed() {
        if (dosvecesAtras){
            finish()
            exitProcess(0)
        }
        this.dosvecesAtras = true
        Toast.makeText(this, "Regresa una vez más para salir de la aplicación", Toast.LENGTH_SHORT).show()
        Handler().postDelayed(Runnable { dosvecesAtras = false }, 2000)
    }

}