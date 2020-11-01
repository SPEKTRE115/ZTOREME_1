package com.example.ztoreme_1.categorias

import android.content.DialogInterface
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.example.ztoreme_1.MainActivity
import com.example.ztoreme_1.R
import com.example.ztoreme_1.basedatos.DataBaseHandler
import kotlinx.android.synthetic.main.activity_agregar.btnCancelar
import kotlinx.android.synthetic.main.activity_agregar.btnGuardar
import kotlinx.android.synthetic.main.activity_agregar_categoria.*

class ActivityAgregarCategoria : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_agregar_categoria)
        val context = this

        val builder = AlertDialog.Builder(this)

        btnCancelar.setOnClickListener({

            builder.setTitle("Confirmacion")
            builder.setMessage("¿Estas seguro de salir?, se borraran los datos introducidos")

            builder.setPositiveButton("Salir", { dialogInterface: DialogInterface, i: Int ->

                val intento1 = Intent(this, MainActivity::class.java)
                startActivity(intento1)

            })

            builder.setNegativeButton("Permanecer", { dialogInterface: DialogInterface, i: Int ->
                dialogInterface.dismiss()
            })

            builder.show()

        })




        btnGuardar.setOnClickListener({
            if (!edit_nom_categoria.text.isNullOrEmpty()
            ) {

                /*Modal para confirmar el guardar el producto*/
                var categoriaNueva = Categoria(
                    edit_nom_categoria.text.toString()
                )
                var db = DataBaseHandler(context)


                if (validarCategoria(edit_nom_categoria.text.toString())) {

                    Toast.makeText(
                        context,
                        "Hay una categoría que ya tiene este nombre",
                        Toast.LENGTH_SHORT
                    ).show()

                } else {

                    builder.setTitle("Confirmacion")
                    builder.setMessage("¿Estas seguro de guardar la nueva categoría?")

                    builder.setPositiveButton(
                        "Guardar",
                        { dialogInterface: DialogInterface, i: Int ->

                            db.insertarCategoria(categoriaNueva)
                            Toast.makeText(context, "Categoría agregada", Toast.LENGTH_SHORT).show()
                            finish()
                        })

                    builder.setNegativeButton("Cancelar",
                        { dialogInterface: DialogInterface, i: Int -> })
                    builder.show()
                }


            } else {
                Toast.makeText(context, "Introduce el nombre de la categoría", Toast.LENGTH_SHORT)
                    .show()
            }
        })
    }


    fun validarCategoria(nombre: String): Boolean{
        val context = this
        var bandera = false
        var db = DataBaseHandler(context)
        var datos = db.extraerCategorias()
        for (i in datos){
            if (i.nombreCategoria.equals(nombre, ignoreCase = true)){
                bandera = true
                break
            }
        }
        return bandera
    }


    override fun onBackPressed() {
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
    }
}