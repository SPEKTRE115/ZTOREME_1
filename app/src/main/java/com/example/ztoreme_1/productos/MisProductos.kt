package com.example.ztoreme_1.productos

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Spinner
import android.widget.Toast
import com.example.ztoreme_1.MainActivity
import com.example.ztoreme_1.R
import com.example.ztoreme_1.basedatos.DataBaseHandler
import com.example.ztoreme_1.categorias.Categoria
import kotlinx.android.synthetic.main.activity_mis_productos.*

class MisProductos : AppCompatActivity() {
    @SuppressLint("UseCompatLoadingForDrawables")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_mis_productos)

        val context = this
        val db = DataBaseHandler(context)
        // Sipinner para las categorias
        val spinner_categoria : Spinner  = findViewById(R.id.spinner_categoria)
        // Obtenemos la lista de categorias que hay dentro de la bd
        val ob_categorias = db.extraerCategorias()
        // Arreglo que almacena el nombre de cada categoria
        val options = arrayListOf<String>()

        // Agregamos las categorias al arreglo options
        options.add("Escoge una categoria")
        for (i in ob_categorias){
            options.add(i.nombreCategoria)
        }

        // Añadimos el adapter al spinner para que tenga estilo cool XD
        spinner_categoria.adapter = ArrayAdapter<String>(this, R.layout.item_spinner,options)

        /**
         * Cuando el usuario da click dentro de un elemento del spinner
         */
        spinner_categoria.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?,view: View?,position: Int,id: Long) {
                var valor = options.get(position)
                var lista_productos = db.extraerProductos()

                /**
                 * Si la opcion seleccionada es diferente a Escoge una categoria quiere decir
                 * que el usuario escogio una categoria
                 * por lo tanto debe de mostrar los productos filtrados por su categoria
                 */
                if (!valor.equals("Escoge una categoria")) {

                    println("Ya puedo hacer filtros de categorias")

                    lista_productos.clear()

                    val lista_productos_filtro = db.extraerProductosbyCategoria(options.get(position))

                    coloca_productos(lista_productos_filtro)
                }else{
                    coloca_productos(lista_productos)
                }

            }

            override fun onNothingSelected(parent: AdapterView<*>?) {

            }
        }
    }

    /**
     * Funcion que se manda llamar cuando se necesita colocar los productos
     * dentro del activity
     */
    fun coloca_productos(lista_productos : MutableList<Producto>){
        println("Este es un mensaje desde coloca productos")

        val listaProduct : MutableList<Producto> = ArrayList()

        for (i in lista_productos){
            val producto = Producto(i.nombreProducto,
                R.drawable.camara.toString(), i.descripcion,i.cantidadActual,5,45,4600,i.precioVenta)
            listaProduct.add(producto)
        }

        val adapter = ProductosAdapter(this, listaProduct)

        lista_articulos.adapter = adapter
        lista_articulos.setOnItemClickListener { parent, view, position, id ->
            val intent = Intent(this, DetalleProducto::class.java)
            intent.putExtra("producto",listaProduct[position])
            startActivity(intent)
        }
    }
}