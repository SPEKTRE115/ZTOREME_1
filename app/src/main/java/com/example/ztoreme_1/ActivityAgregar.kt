package com.example.ztoreme_1

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.Window
import android.widget.Button
import android.widget.ImageView
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_agregar.*

@Suppress("MoveLambdaOutsideParentheses")
class ActivityAgregar : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_agregar)

        val context = this

        val click_cancelar = findViewById(R.id.BtnCancelar) as Button
        click_cancelar.setOnClickListener {
            val intento1 = Intent(this, MainActivity::class.java)
            startActivity(intento1)
        }

        val click_atras = findViewById(R.id.atras_agregar) as ImageView
        click_atras.setOnClickListener {
            val intento1 = Intent(this, MainActivity::class.java)
            startActivity(intento1)
        }

        BtnGuardar.setOnClickListener({
            if (!editTextProd.text.isNullOrEmpty() && editTextProd.text.toString().length < 50 &&
                !editNumCantidad.text.isNullOrEmpty() &&
                !editstockMax.text.isNullOrEmpty() &&
                !editstockMin.text.isNullOrEmpty() &&
                !editprecioCompra.text.isNullOrEmpty() &&
                !editprecioVenta.text.isNullOrEmpty()) {
                var productoNuevo = Producto(editTextProd.text.toString(),"nop",editDescripcion.text.toString(), (editNumCantidad.text.toString()).toInt(), (editstockMax.text.toString()).toInt(), (editstockMin.text.toString()).toInt(), (editprecioCompra.text.toString()).toInt(), (editprecioVenta.text.toString()).toInt())
                var db = DataBaseHandler(context)
                db.insertarProducto(productoNuevo)
                Toast.makeText(context, "Producto agregado",Toast.LENGTH_SHORT).show()
                finish()
            } else {
                Toast.makeText(context, "Introduce los datos correctamente", Toast.LENGTH_SHORT).show()
            }
        })
    }
}