package com.example.ztoreme_1

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import kotlinx.android.synthetic.main.item_producto.view.*

class ProductosAdapter(private val mContex: Context, private val listaProductos: List<Producto>) : ArrayAdapter<Producto>(mContex,0, listaProductos) {
    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val layout = LayoutInflater.from(mContex).inflate(R.layout.item_producto, parent, false)

        val producto = listaProductos[position]

        layout.nameProduct.text = producto.nombre
        layout.precio.text  = "$${producto.precio}"
        layout.stock.text = producto.stock.toString()

        return layout
    }
}