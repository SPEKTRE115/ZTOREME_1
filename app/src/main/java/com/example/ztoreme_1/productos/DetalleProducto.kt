package com.example.ztoreme_1.productos

import android.annotation.SuppressLint
import android.content.DialogInterface
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.example.ztoreme_1.R
import com.example.ztoreme_1.basedatos.DataBaseHandler
import kotlinx.android.synthetic.main.activity_detalle_producto.*

class DetalleProducto : AppCompatActivity() {
    @SuppressLint("WrongViewCast")
    override fun onCreate(savedInstanceState: Bundle?) {
        val builder = AlertDialog.Builder(this)

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detalle_producto)
        val context = this

        val producto = intent.getSerializableExtra("producto") as Producto

        nombre_producto.text = producto.nombreProducto
        stock_actual.text = producto.cantidadActual.toString()
        stock_maximo.text = producto.stockMaximo.toString()
        stock_minimo.text = producto.stockMinimo.toString()
        descripcion.text = producto.descripcion
        precio_venta.text = producto.precioVenta.toString()
        precio_compra.text = producto.precioCompra.toString()

        btn_borrar_producto.setOnClickListener({
            builder.setTitle("Confirmacion")
            builder.setMessage("¿Estas seguro de eliminar el producto"+producto.nombreProducto+"?")

            builder.setPositiveButton("Guardar", { dialogInterface: DialogInterface, i: Int ->

                var db = DataBaseHandler(context)
                db.borrarProducto(producto.nombreProducto)
                Toast.makeText(context, "Producto eliminado", Toast.LENGTH_SHORT).show()
                val intent = Intent(this, MisProductos::class.java)
                startActivity(intent)
                finish()
            })

            builder.setNegativeButton("Cancelar",{ dialogInterface: DialogInterface, i: Int -> })
            builder.show()

        })
    }


}