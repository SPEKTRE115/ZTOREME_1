package com.example.ztoreme_1

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import android.view.Menu
import android.view.MenuItem
import android.widget.ImageView
import android.widget.TextView
import com.example.ztoreme_1.categorias.ActivityAgregarCategoria
import com.example.ztoreme_1.productos.ActivityAgregar
import com.example.ztoreme_1.productos.MisProductos

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(findViewById(R.id.toolbar))

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

    }
}