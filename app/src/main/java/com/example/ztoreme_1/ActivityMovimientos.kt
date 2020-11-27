package com.example.ztoreme_1

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.ztoreme_1.basedatos.DataBaseHandler
import com.example.ztoreme_1.productos.DetalleProducto
import com.example.ztoreme_1.productos.Producto
import com.example.ztoreme_1.productos.ProductosAdapter
import kotlinx.android.synthetic.main.activity_mis_productos.*
import kotlinx.android.synthetic.main.activity_movimientos.*


/*
Clase que se encarga de mostrar la vista de la sección de movimientos
y de invocar los métodos necesarios para visualizar los movimientos.
*/

class ActivityMovimientos : AppCompatActivity() {
    /*Método que se encarga de cargar todos los métodos y elementos visuales al iniciar
    la activity. Además de añadir las acciones necesarias a los botones y la lista.*/
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_movimientos)

        val context = this
        val db = DataBaseHandler(context)

        var lista_movi= db.extraerMovimientos()
        coloca_movimientos(lista_movi)
    }

    /*Método que se encarga de colocar los movinetos en la lista
    * que contiene esta activity.*/
    fun coloca_movimientos(lista_mov : MutableList<Movimiento>){
        val listam : MutableList<Movimiento> = ArrayList()

        for (i in lista_mov){
            val movimiento = Movimiento(i.fechaRegistro,i.idProducto,i.cantidadMov,i.entrada)
            listam.add(movimiento)
        }

        val adapter = MovimientosAdapter(this, listam)

        lista_movimientos.adapter = adapter
    }

    /*Método que se encraga de redireccionar a cierto activity al momento de
    presiobnar el botón back de los celulares Android.*/
    override fun onBackPressed() {
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
    }
}