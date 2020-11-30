package com.example.ztoreme_1

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import com.example.ztoreme_1.basedatos.DataBaseHandler
import com.example.ztoreme_1.productos.Producto
import kotlinx.android.synthetic.main.item_movimiento.view.*
import kotlinx.android.synthetic.main.item_movimiento.view.nameProduct2
import kotlinx.android.synthetic.main.item_movimiento_2.view.*
import kotlinx.android.synthetic.main.item_producto.view.*

/*
* Adaptador para la visualizacion de los movimientos registradas en el sistema con el formato
* mencionado.
* */
class MovimientosAdapter(private val mContex: Context, private val listaMovimientos: List<Movimiento>)  : ArrayAdapter<Movimiento>(mContex,0, listaMovimientos){
    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {

        val movimiento = listaMovimientos[position]
        val db = DataBaseHandler(context)

        if(movimiento.entrada == 1){
            val layout = LayoutInflater.from(mContex).inflate(R.layout.item_movimiento, parent, false)
            layout.nameProduct2.text = db.extraerNombreProductoPorMovimiento(movimiento.idProducto)
            layout.cant_mov.text  = movimiento.cantidadMov.toString() + " entradas"
            layout.fecha_text.text = movimiento.fechaRegistro
            return layout

        }else{
            val layout2 = LayoutInflater.from(mContex).inflate(R.layout.item_movimiento_2, parent, false)
            layout2.nameProduct2.text = db.extraerNombreProductoPorMovimiento(movimiento.idProducto)
            layout2.cant_mov_2.text  = movimiento.cantidadMov.toString() + " salidas"
            layout2.fecha_text_2.text = movimiento.fechaRegistro

            return layout2
        }

    }
}