package com.example.ztoreme_1

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ListView
import kotlinx.android.synthetic.main.activity_mis_productos.*

class MisProductos : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_mis_productos)

        val producto = Producto("camara",1580.00, "Camara ultimo modelo", R.drawable.camara, 28)
        val producto2 = Producto("PC",32500.00,"16 GB RAM",R.drawable.computadora,9)

        val listaProduct = listOf(producto,producto2)

        val adapter = ProductosAdapter(this, listaProduct)

        lista_articulos.adapter = adapter
    }
}