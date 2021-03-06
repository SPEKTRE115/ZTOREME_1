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
import com.example.ztoreme_1.Movimiento
import com.example.ztoreme_1.R
import com.example.ztoreme_1.basedatos.DataBaseHandler
import kotlinx.android.synthetic.main.activity_agregar.*
import java.text.SimpleDateFormat
import java.util.*

/*
Clase que se encarga de mostrar la vista de la agregación de un producto
y de invocar los métodos necesarios para agregar un producto.
 */
@Suppress("MoveLambdaOutsideParentheses")
class ActivityAgregar : AppCompatActivity() {
    private val pickImage = 100
    private var categoria = ""

    /*Método que se encarga de cargar todos los métodos y elementos visuales al iniciar
    la activity. Además de añadir las acciones necesarias a los botones y spinners
    que se encuentran en la activity.*/
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
        if (lista.isEmpty()){
            db.insertarCategoria(Categoria("Sin Categoría"))
        }

        spinner_categorias.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?,view: View?,position: Int,id: Long) {
                categoria = lista.get(position)

            }

            //Cambiar para cuando se tenga la base predeterminada
            override fun onNothingSelected(parent: AdapterView<*>?) {
                categoria = "Sin Categoría"
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
                validarCantidad() &&
                validarStocks() &&
                validarPrecios()) {

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
                            val sdf = SimpleDateFormat("dd/M/yyyy hh:mm:ss a")
                            val fechaActual = sdf.format(Date())
                            db.insertarProducto(productoNuevo)
                            var id_prod = db.extraerIdPorNombreProducto(productoNuevo.nombreProducto)
                            var nuevoMovimiento = Movimiento(fechaActual,id_prod,productoNuevo.cantidadActual,1)
                            db.insertarMovimiento(nuevoMovimiento)
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

    /*Método que valida si el nombre del producto ha agregar
    ya existe en la base de datos o no.*/
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

    /*Método que se encarga de validar si los datos de cantidad actual
   son correctos. */
    fun validarCantidad(): Boolean{
        if (editNumCantidad.text.toString().isNullOrEmpty()) {
            Toast.makeText(this, "La cantidad no puede estar vacía", Toast.LENGTH_SHORT).show()
            return false
        }
        if (!validarStocks()){
            return false
        }
        if ((editNumCantidad.text.toString()).toInt() > (editstockMax.text.toString()).toInt()) {
            Toast.makeText(this, "La cantidad no puede ser mayor al stock máximo", Toast.LENGTH_SHORT).show()
            return false
        }
        if ((editNumCantidad.text.toString()).toInt() <= 0) {
            Toast.makeText(this, "La cantidad no puede ser menor a 1", Toast.LENGTH_SHORT).show()
            return false
        }
        return true
    }

    /*Método que se encarga de validar que los datos a insertar de
    los campos de stock mínimo y máximo sean correctos.*/
    fun validarStocks(): Boolean{
        if (editstockMax.text.toString().isNullOrEmpty() || editstockMin.text.toString().isNullOrEmpty()) {
            Toast.makeText(this, "Los stocks no pueden estar vacíos", Toast.LENGTH_SHORT).show()
            return false
        }
        if ((editstockMax.text.toString()).toInt() < (editstockMin.text.toString()).toInt()) {
            Toast.makeText(this, "El stock máximo no puede ser menor al stock mínimo", Toast.LENGTH_SHORT).show()
            return false
        }
        if ((editstockMax.text.toString()).toInt() <= 0 || (editstockMin.text.toString()).toInt() <= 0) {
            Toast.makeText(this, "Los stocks no pueden ser menores a 1", Toast.LENGTH_SHORT).show()
            return false
        }
        return true
    }

    /*Método que valida si los datos de los campos de precio compra y precio venta
   sean correctos antes de insertarlos.*/
    fun validarPrecios(): Boolean{
        if (editprecioCompra.text.toString().isNullOrEmpty() || editprecioVenta.text.toString().isNullOrEmpty()) {
            Toast.makeText(this, "Los precios no pueden estar vacíos", Toast.LENGTH_SHORT).show()
            return false
        }
        if ((editprecioCompra.text.toString()).toDouble() > (editprecioVenta.text.toString()).toDouble()) {
            Toast.makeText(this, "El precio de venta no puede ser menor al de compra", Toast.LENGTH_SHORT).show()
            return false
        }
        if ((editprecioVenta.text.toString()).toDouble() <= 0.0 || (editprecioCompra.text.toString()).toDouble() <= 0) {
            Toast.makeText(this, "Los precios no pueden ser menores a 1", Toast.LENGTH_SHORT).show()
            return false
        }
        return true
    }

    /*Método que se encraga de establecer el uri de la imagen en el campo
    de imagen.*/
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {       super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == RESULT_OK && requestCode == pickImage) {
            editUrl.setText(data?.dataString)
        }
    }

    /*Método que se encraga de redireccionar a cierto activity al momento de
    presiobnar el botón back de los celulares Android.*/
    override fun onBackPressed() {
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
    }

}