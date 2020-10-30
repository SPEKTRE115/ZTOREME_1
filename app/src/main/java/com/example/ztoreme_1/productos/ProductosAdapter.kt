package com.example.ztoreme_1.productos

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import com.example.ztoreme_1.R
import kotlinx.android.synthetic.main.item_producto.view.*

class ProductosAdapter(private val mContex: Context, private val listaProductos: List<Producto>) : ArrayAdapter<Producto>(mContex,0, listaProductos) {
    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val layout = LayoutInflater.from(mContex).inflate(R.layout.item_producto, parent, false)

        val producto = listaProductos[position]

        layout.nameProduct.text = producto.nombreProducto
        layout.precio.text  = "$${producto.precioVenta}"
        layout.stock.text = producto.cantidadActual.toString()

        return layout
    }
}