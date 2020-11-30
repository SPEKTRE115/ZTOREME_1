package com.example.ztoreme_1.categorias

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import com.example.ztoreme_1.R
import kotlinx.android.synthetic.main.item_categoria.view.*


/*
* Adaptador para la visualizacion de las categorias registradas en el sistema con el formato
* mencionado.
* */
class CategoriasAdapter(private val mContex: Context, private val listaCategoria: List<Categoria>) : ArrayAdapter<Categoria>(mContex,0, listaCategoria){
    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val layout = LayoutInflater.from(mContex).inflate(R.layout.item_categoria, parent, false)
        val categoria = listaCategoria[position]
        layout.nombreCategoria.text = categoria.nombreCategoria

        return layout
    }
}