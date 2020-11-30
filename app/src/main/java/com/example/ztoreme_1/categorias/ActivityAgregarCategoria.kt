package com.example.ztoreme_1.categorias

import android.content.DialogInterface
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.view.get
import com.example.ztoreme_1.MainActivity
import com.example.ztoreme_1.R
import com.example.ztoreme_1.basedatos.DataBaseHandler
import kotlinx.android.synthetic.main.activity_agregar.btnCancelar
import kotlinx.android.synthetic.main.activity_agregar.btnGuardar
import kotlinx.android.synthetic.main.activity_agregar_categoria.*
import kotlinx.android.synthetic.main.item_categoria.view.*

/*
* Boleano que se encarga para determinar si es una nueva categoria o si se esta haciendo edicion
* de la misma.
* */
var bandera = true
/*
* Nombre de la categoria.
* */
var nombresito = ""

/*
* Clase que nos ayuda insertar o modificar una categoria dentro de la base de datos haciendo builder para confirmar
* la operacion a realizar validando el nombre de la categoria.
* */
class ActivityAgregarCategoria : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_agregar_categoria)

        val builder = AlertDialog.Builder(this)
        val context = this
        val db = DataBaseHandler(context)

        var adaptame = CategoriasAdapter(this, cargaCategorias())

        lista_categorias.adapter = adaptame

        /*
        * Metodo que se encarga de manejar las opciones al momento de seleccionar un elemento en la lista
        * de categorias.
        * Hace uso de un builder para confirmar la operacion.
        * */
        lista_categorias.setOnItemClickListener{ parent, view, position, id ->
            builder.setTitle("Opciones de: "+lista_categorias[position].nombreCategoria.text.toString())
            builder.setMessage("¿Qué deseas hacer con esta categoría?")
            builder.setNegativeButton("Eliminar", { dialogInterface: DialogInterface, i: Int ->
                db.eliminarCategoria(lista_categorias[position].nombreCategoria.text.toString())
                adaptame = CategoriasAdapter(this, cargaCategorias())
                adaptame.notifyDataSetChanged()
                lista_categorias.adapter = adaptame
            })
            builder.setPositiveButton("Editar", { dialogInterface: DialogInterface, i: Int ->
                bandera = false
                edit_nom_categoria.setText(lista_categorias[position].nombreCategoria.text.toString())
                nombresito = lista_categorias[position].nombreCategoria.text.toString()
            })
            builder.setNeutralButton("Cancelar", { dialogInterface: DialogInterface, i: Int ->
                dialogInterface.dismiss()
            })
            builder.show()
        }

        /*
        * Boton que se encarga de cancelar la insercion de la categoria y regresa al MainActivity.
        * Hace uso de un builder para confirmar la operacion.
        * */
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

        /*
        * Boton que se encarga de crear un objeto de tipo categoria para su introduccion en la base
        * de datos.
        * Hace uso de un builder para confirmar la operacion.
        * */
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

                    builder.setNeutralButton(null, null)

                    builder.setPositiveButton(
                        "Guardar",
                        { dialogInterface: DialogInterface, i: Int ->

                            if (bandera){
                                db.insertarCategoria(categoriaNueva)
                            }else{
                                db.actualizarCategorias(categoriaNueva, nombresito)
                                bandera = true
                            }
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


    /*
    * Metodo que comprueba si la categoria a introducir se encuentra en la base de datos.
    * */
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

    /*
    * Metodo que se encarga de aareglar la lista de categorias en la base de datos para ser insertado
    * dentro del spinner.
    * */
    fun cargaCategorias(): MutableList<Categoria>{
        val context = this
        val db = DataBaseHandler(context)

        val ob_categorias = db.extraerCategorias()

        val listaCategoria : MutableList<Categoria> = ArrayList()

        for (i in ob_categorias){
            val categoria = Categoria(i.nombreCategoria)
            listaCategoria.add(categoria)
        }
        return listaCategoria
    }

    /*
    * Metodo para regresar al activity anterior.
    * */
    override fun onBackPressed() {
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
    }
}