package com.example.ztoreme_1

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ListView
import kotlinx.android.synthetic.main.activity_mis_productos.*

class MisProductos : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_mis_productos)

        val context = this
        var db = DataBaseHandler(context)
        var datos = db.extraerProductos()
        val listaProduct : MutableList<Producto> = ArrayList()

        for (i in datos){
            val producto = Producto(i.nombreProducto,R.drawable.camara.toString(), i.descripcion,i.cantidadActual,5,45,4600,i.precioVenta)
            listaProduct.add(producto)
        }
        val adapter = ProductosAdapter(this, listaProduct)

        lista_articulos.adapter = adapter
    }
}