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
import kotlinx.android.synthetic.main.activity_actualizar.*
import kotlinx.android.synthetic.main.activity_agregar.*
import kotlinx.android.synthetic.main.activity_agregar.BtnImagen
import kotlinx.android.synthetic.main.activity_agregar.btnCancelar
import kotlinx.android.synthetic.main.activity_agregar.btnGuardar
import kotlinx.android.synthetic.main.activity_agregar.editDescripcion
import kotlinx.android.synthetic.main.activity_agregar.editNumCantidad
import kotlinx.android.synthetic.main.activity_agregar.editTextProd
import kotlinx.android.synthetic.main.activity_agregar.editUrl
import kotlinx.android.synthetic.main.activity_agregar.editprecioCompra
import kotlinx.android.synthetic.main.activity_agregar.editprecioVenta
import kotlinx.android.synthetic.main.activity_agregar.editstockMax
import kotlinx.android.synthetic.main.activity_agregar.editstockMin
import kotlinx.android.synthetic.main.activity_agregar.spinner

@Suppress("MoveLambdaOutsideParentheses")
class ActivityActualizar : AppCompatActivity() {
    private val pickImage = 100
    private var categoria = ""


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_actualizar)
        val context = this
        var db = DataBaseHandler(context)

        //Setear antiguos
        var cadena:String = intent.getStringExtra("NOMBRE")

        editTextProd.setText(cadena)

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

                val intento1 = Intent(this, MisProductos::class.java)
                startActivity(intento1)

            })

            builder.setNegativeButton("Permanecer", { dialogInterface: DialogInterface, i: Int ->
                dialogInterface.dismiss()
            })

            builder.show()

        })

        val click_atras = findViewById(R.id.atras_agregar) as ImageView
        click_atras.setOnClickListener {
            val intento1 = Intent(this, MisProductos::class.java)
            startActivity(intento1)
        }

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
                    (editstockMax.text.toString()).toInt(),
                    (editstockMin.text.toString()).toInt(),
                    (editprecioCompra.text.toString()).toInt(),
                    (editprecioVenta.text.toString()).toInt()
                )



                if (validarNombre(editTextProd.text.toString())) {

                    Toast.makeText(
                        context,
                        "Hay un producto que ya tiene este nombre",
                        Toast.LENGTH_SHORT
                    ).show()

                } else {

                    builder.setTitle("Confirmacion")
                    builder.setMessage("¿Estas seguro de actualizar el producto?")

                    builder.setPositiveButton(
                        "Guardar",
                        { dialogInterface: DialogInterface, i: Int ->

                            db.actualizarProducto(productoNuevo, cadena)
                            Toast.makeText(context, "Producto actualizado", Toast.LENGTH_SHORT).show()
                            db.actualizarCategoria(editTextProd.text.toString(), categoria)
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
        val intent = Intent(this, MisProductos::class.java)
        startActivity(intent)
    }

}