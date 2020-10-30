package com.example.ztoreme_1.productos

import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.provider.MediaStore
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.example.ztoreme_1.MainActivity
import com.example.ztoreme_1.R
import com.example.ztoreme_1.basedatos.DataBaseHandler
import kotlinx.android.synthetic.main.activity_agregar.*

@Suppress("MoveLambdaOutsideParentheses")
class ActivityAgregar : AppCompatActivity() {
    private val pickImage = 100


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_agregar)
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

        val click_atras = findViewById(R.id.atras_agregar) as ImageView
        click_atras.setOnClickListener {
            val intento1 = Intent(this, MainActivity::class.java)
            startActivity(intento1)
        }

        //Parte Imagen
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
                    "nop",
                    editDescripcion.text.toString(),
                    (editNumCantidad.text.toString()).toInt(),
                    (editstockMax.text.toString()).toInt(),
                    (editstockMin.text.toString()).toInt(),
                    (editprecioCompra.text.toString()).toInt(),
                    (editprecioVenta.text.toString()).toInt()
                )
                var db = DataBaseHandler(context)


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