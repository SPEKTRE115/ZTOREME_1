package com.example.ztoreme_1.productos

import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.provider.MediaStore
import android.view.View
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.example.ztoreme_1.categorias.Categoria
import com.example.ztoreme_1.MainActivity
import com.example.ztoreme_1.R
import com.example.ztoreme_1.basedatos.DataBaseHandler
import kotlinx.android.synthetic.main.activity_agregar.*

@Suppress("MoveLambdaOutsideParentheses")
class ActivityAgregar : AppCompatActivity() {
    private val pickImage = 100
    private var categoria = ""


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_agregar)
        val context = this
        var db = DataBaseHandler(context)

        val builder = AlertDialog.Builder(this)

        //Categorias
        val spinner_categorias : Spinner = findViewById<Spinner>(R.id.spinner)
        val lista  = arrayListOf<String>()
        var datos = db.extraerCategorias()

        for (i in datos){
            lista.add(i.nombreCategoria)
        }
        if (spinner != null){
            val adapter = ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, lista)
            spinner_categorias.adapter = adapter
        }

        spinner_categorias.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?,view: View?,position: Int,id: Long) {
                categoria = lista.get(position)

            }

            //Cambiar para cuando se tenga la base predeterminada
            override fun onNothingSelected(parent: AdapterView<*>?) {
                categoria = "Holi"
            }
        }

        //

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


        //Parte Imagen
        editUrl.setEnabled(false)
        BtnImagen.setOnClickListener({
            val galeria = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI)
            startActivityForResult(galeria, pickImage)
        })

        btnGuardar.setOnClickListener({
            if (!editTextProd.text.isNullOrEmpty() && editTextProd.text.toString().length < 50 &&
                !editNumCantidad.text.isNullOrEmpty() &&
                !editstockMax.text.isNullOrEmpty() &&
                !editstockMin.text.isNullOrEmpty() &&
                !editprecioCompra.text.isNullOrEmpty() &&
                !editprecioVenta.text.isNullOrEmpty()
            ) {

                /*Modal para confirmar el guardar el producto*/
                var productoNuevo = Producto(
                    editTextProd.text.toString(),
                    editUrl.text.toString(),
                    editDescripcion.text.toString(),
                    (editNumCantidad.text.toString()).toInt(),
                    (editstockMin.text.toString()).toInt(),
                    (editstockMax.text.toString()).toInt(),
                    (editprecioCompra.text.toString()).toDouble(),
                    (editprecioVenta.text.toString()).toDouble()
                )



                if (validarNombre(editTextProd.text.toString())) {

                    Toast.makeText(
                        context,
                        "Hay un producto que ya tiene este nombre",
                        Toast.LENGTH_SHORT
                    ).show()

                } else {

                    builder.setTitle("Confirmacion")
                    builder.setMessage("¿Estas seguro de guardar el nuevo producto?")

                    builder.setPositiveButton(
                        "Guardar",
                        { dialogInterface: DialogInterface, i: Int ->

                            db.insertarProducto(productoNuevo)
                            Toast.makeText(context, "Producto agregado", Toast.LENGTH_SHORT).show()
                            db.asociarCategoria(editTextProd.text.toString(), categoria)
                            finish()
                        })

                    builder.setNegativeButton("Cancelar",
                        { dialogInterface: DialogInterface, i: Int -> })
                    builder.show()
                }

            } else {
                Toast.makeText(context, "Introduce los datos correctamente", Toast.LENGTH_SHORT)
                    .show()
            }
        })
    }

    fun validarNombre(nombre: String): Boolean{
        val context = this
        var bandera = false
        var db = DataBaseHandler(context)
        var datos = db.extraeNom()
        for (i in datos){
            if (i.nombreProducto.equals(nombre, ignoreCase = true)){
                bandera = true
                break
            }
        }
        return bandera
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {       super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == RESULT_OK && requestCode == pickImage) {
            editUrl.setText(data?.dataString)
        }
    }

    override fun onBackPressed() {
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
    }

}