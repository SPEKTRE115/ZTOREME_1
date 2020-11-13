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

class ActivityMovimientos : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_movimientos)

        val context = this
        val db = DataBaseHandler(context)

        var lista_movi= db.extraerMovimientos()
        coloca_movimientos(lista_movi)
    }


    fun coloca_movimientos(lista_mov : MutableList<Movimiento>){
        val listam : MutableList<Movimiento> = ArrayList()

        for (i in lista_mov){
            val movimiento = Movimiento(i.fechaRegistro,i.idProducto,i.cantidadMov,i.entrada)
            listam.add(movimiento)
        }

        val adapter = MovimientosAdapter(this, listam)

        lista_movimientos.adapter = adapter
    }

    override fun onBackPressed() {
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
    }
}