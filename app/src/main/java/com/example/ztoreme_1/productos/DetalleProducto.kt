package com.example.ztoreme_1.productos

import android.annotation.SuppressLint
import android.content.DialogInterface
import android.content.Intent
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.net.toUri
import com.bumptech.glide.Glide
import com.example.ztoreme_1.MainActivity
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

        var uri = producto.imagen.toUri()

        img_producto.setImageURI(uri)

        btn_borrar_producto.setOnClickListener({
            builder.setTitle("Confirmacion")
            builder.setMessage("¿Estas seguro de eliminar el producto" + producto.nombreProducto + "?")

            builder.setPositiveButton("Eliminar", { dialogInterface: DialogInterface, i: Int ->

                var db = DataBaseHandler(context)
                db.borrarProducto(producto.nombreProducto)
                Toast.makeText(context, "Producto eliminado", Toast.LENGTH_SHORT).show()
                val intent = Intent(this, MisProductos::class.java)
                startActivity(intent)
            })

            builder.setNegativeButton("Cancelar", { dialogInterface: DialogInterface, i: Int -> })
            builder.show()

        })

        btn_editar.setOnClickListener({
            val intent = Intent(this, ActivityActualizar::class.java)
            intent.putExtra("producto", producto)
            startActivity(intent)
            finish()
        })

        btn_borrar.setOnClickListener {
            val txtcantidad = findViewById(R.id.cantidad) as TextView
            if(!txtcantidad.text.isNullOrEmpty()) {
                builder.setTitle("Quitar articulos del inventario")
                builder.setMessage("¿Estas seguro de quitar " + (txtcantidad.text.toString()).toInt() + " artículos del producto: " + producto.nombreProducto + "?")
                val lp = LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.MATCH_PARENT
                )

                builder.setPositiveButton(
                    "Guardar cambios",
                    { dialogInterface: DialogInterface, i: Int ->
                        val txtcantidad = findViewById(R.id.cantidad) as TextView
                        if (!txtcantidad.text.isNullOrEmpty()) {
                            var cantidad = (txtcantidad.text.toString()).toInt()
                            val txtcantidadactual = findViewById(R.id.stock_actual) as TextView
                            var cantidadactual = (txtcantidadactual.text.toString()).toInt()
                            //val txtcantidadmax = findViewById(R.id.stock_actual) as TextView
                            //var cantidadmax = (txtcantidadactual.text.toString()).toInt()
                            if ((cantidadactual - cantidad) < 0) {
                                Toast.makeText(
                                    context,
                                    "No puedes quitar más productos de los que tienes",
                                    Toast.LENGTH_SHORT
                                ).show()
                            } else {
                                var db = DataBaseHandler(context)
                                db.disminuirStockProducto(producto.nombreProducto, cantidad)
                                Toast.makeText(context, "Producto modificado", Toast.LENGTH_SHORT)
                                    .show()
                                val intent = Intent(this, MisProductos::class.java)
                                startActivity(intent)
                                finish()
                            }
                        } else {
                            Toast.makeText(context, "Ingresa una cantidad", Toast.LENGTH_SHORT)
                                .show()
                            finish()
                        }
                    })

                builder.setNegativeButton(
                    "Cancelar",
                    { dialogInterface: DialogInterface, i: Int -> })
                builder.show()
            }else{
                Toast.makeText(context, "Ingresa una cantidad", Toast.LENGTH_SHORT).show()
            }


        }


        btn_agregar.setOnClickListener {
            val txtcantidad = findViewById(R.id.cantidad) as TextView
            builder.setTitle("Agregar articulos al inventario")
            if(!txtcantidad.text.isNullOrEmpty()) {
                builder.setMessage("¿Estas seguro de agregar " + (txtcantidad.text.toString()).toInt() + " artículos del producto: " + producto.nombreProducto + "?")

                val lp = LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.MATCH_PARENT
                )

                builder.setPositiveButton(
                    "Guardar cambios"
                ) { dialogInterface: DialogInterface, i: Int ->
                    val txtcantidad = findViewById(R.id.cantidad) as TextView
                    var cantidad = (txtcantidad.text.toString()).toInt()
                    val txtcantidadactual = findViewById(R.id.stock_actual) as TextView
                    var cantidadactual = (txtcantidadactual.text.toString()).toInt()
                    val txtcantidadmax = findViewById(R.id.stock_maximo) as TextView
                    var cantidadmax = (txtcantidadmax.text.toString()).toInt()

                    if (cantidadactual + cantidad > cantidadmax) {
                        Toast.makeText(
                            context,
                            "No puedes pasar tu stock máximo",
                            Toast.LENGTH_SHORT
                        ).show()
                    } else {
                        var db = DataBaseHandler(context)
                        db.aumentarStockProducto(producto.nombreProducto, cantidad)
                        Toast.makeText(context, "Producto modificado", Toast.LENGTH_SHORT).show()
                        val intent = Intent(this, MisProductos::class.java)
                        startActivity(intent)
                        finish()
                    }

                }

                builder.setNegativeButton("Cancelar", { dialogInterface: DialogInterface, i: Int -> })
                builder.show()

            }else{
                Toast.makeText(context, "Ingresa una cantidad", Toast.LENGTH_SHORT).show()
            }

        }

    }

    override fun onBackPressed() {
        val intent = Intent(this, MisProductos::class.java)
        startActivity(intent)
    }


}
