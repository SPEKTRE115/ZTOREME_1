package com.example.ztoreme_1

import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.example.ztoreme_1.basedatos.DataBaseHandler
import com.example.ztoreme_1.productos.DetalleProducto
import com.example.ztoreme_1.productos.Producto
import com.example.ztoreme_1.productos.ProductosAdapter
import kotlinx.android.synthetic.main.activity_reorden.*

class ActivityReorden : AppCompatActivity() {

    var precio = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_reorden)

        val context = this
        val db = DataBaseHandler(context)
        val lista = db.extraerProductos()

        var adaptame = ProductosAdapter(this, coloca_productos())
        lista_prods.adapter = adaptame
        lista_prods.setOnItemClickListener{ parent, view, position, id ->
            precio += (lista[position].precioCompra.toInt()*((lista[position].stockMaximo)-(lista[position].cantidadActual)))
            Toast.makeText(this, "El stock faltante del producto seleccionado es de:" +
                    " "+((lista[position].stockMaximo)-(lista[position].cantidadActual)).toString()+" elementos", Toast.LENGTH_SHORT).show()
        }
        val builder = AlertDialog.Builder(this)
        btnCancelar.setOnClickListener({
            builder.setTitle("Confirmación")
            builder.setMessage("¿Estas seguro deseas salir?, se borrará el progreso hecho.")
            builder.setPositiveButton("Salir", { dialogInterface: DialogInterface, i: Int ->
                val intento1 = Intent(this, MainActivity::class.java)
                startActivity(intento1)
            })
            builder.setNeutralButton("Cancelar", {dialogInterface: DialogInterface, i: Int ->
                dialogInterface.dismiss()
            })
            builder.show()
        })

        btnReorden.setOnClickListener({
            builder.setTitle("Precio reorden")
            builder.setMessage("El precio de reorden por los productos seleccionados es de: "+precio+" pesos")
            builder.setNegativeButton("Aceptar",{ dialogInterface: DialogInterface, i: Int ->
                val intento1 = Intent(this, MainActivity::class.java)
                precio = 0
                startActivity(intento1)
            })
            builder.show()
        })

    }

    private fun coloca_productos() : MutableList<Producto>{
        val context = this
        val db = DataBaseHandler(context)
        val lista2 = db.extraerProductos()
        val lista : MutableList<Producto> = ArrayList()
        for (i in lista2){
            val producto = Producto(i.nombreProducto,i.imagen,
                i.descripcion,i.cantidadActual,i.stockMinimo,i.stockMaximo,i.precioCompra,i.precioVenta)
            lista.add(producto)
        }
        return lista
    }

    override fun onBackPressed() {
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
    }
}