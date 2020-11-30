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
import com.example.ztoreme_1.notificaciones.NotificationUtils
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
import java.text.SimpleDateFormat
import java.util.*

/*
Clase que se encarga de mostrar la vista de la edición de un producto
y de invocar los métodos necesarios para editar un producto.
 */
@Suppress("MoveLambdaOutsideParentheses")
class ActivityActualizar : AppCompatActivity() {
    private val pickImage = 100
    private var categoria = ""
    private val mNotificationTime = Calendar.getInstance().timeInMillis + 60000
    private var cantidadAntes = 0


    /*Método que se encarga de cargar todos los métodos y elementos visuales al iniciar
    la activity. Además de añadir las acciones necesarias a los botones y spinners
    que se encuentran en la activity.*/
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_actualizar)
        val context = this
        var db = DataBaseHandler(context)
        val producto = intent.getSerializableExtra("producto") as Producto
        //Setear antiguos
        editTextProd.setText(producto.nombreProducto)
        editprecioVenta.setText(producto.precioVenta.toString())
        editprecioCompra.setText(producto.precioCompra.toString())
        editNumCantidad.setText(producto.cantidadActual.toString())
        editDescripcion.setText(producto.descripcion)
        editstockMin.setText(producto.stockMinimo.toString())
        editstockMax.setText(producto.stockMaximo.toString())
        editUrl.setText(producto.imagen)

        cantidadAntes=producto.cantidadActual
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

                val intento1 = Intent(this, DetalleProducto::class.java)
                intento1.putExtra("producto", producto)
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



                builder.setTitle("Confirmacion")
                builder.setMessage("¿Estas seguro de actualizar el producto?")

                builder.setPositiveButton(
                    "Guardar",
                    { dialogInterface: DialogInterface, i: Int ->
                        db.actualizarProducto(productoNuevo, producto.nombreProducto)
                        Toast.makeText(context, "Producto actualizado", Toast.LENGTH_SHORT).show()
                        db.actualizarCategoria(editTextProd.text.toString(), categoria)

                        val sdf = SimpleDateFormat("dd/M/yyyy hh:mm:ss a")
                        val fechaActual = sdf.format(Date())
                        var id_prod = db.extraerIdPorNombreProducto(productoNuevo.nombreProducto)
                        if(productoNuevo.cantidadActual>cantidadAntes){
                            var nuevoMovimiento = Movimiento(fechaActual,id_prod,productoNuevo.cantidadActual-cantidadAntes,1)
                            db.insertarMovimiento(nuevoMovimiento)
                        }else if(productoNuevo.cantidadActual<cantidadAntes){
                            var nuevoMovimiento = Movimiento(fechaActual,id_prod,(productoNuevo.cantidadActual-cantidadAntes)*-1,0)
                            db.insertarMovimiento(nuevoMovimiento)
                        }else{

                        }

                        val intento1 = Intent(this, DetalleProducto::class.java)
                        intento1.putExtra("producto", productoNuevo)
                        startActivity(intento1)
                        finish()
                        verificaStockMinimo()
                    })

                builder.setNegativeButton("Cancelar",
                    { dialogInterface: DialogInterface, i: Int -> })
                builder.show()


            } else {
                Toast.makeText(context, "Introduce los datos correctamente", Toast.LENGTH_SHORT)
                    .show()
            }
        })
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
        if ((editprecioVenta.text.toString()).toDouble() <= 0 || (editprecioCompra.text.toString()).toDouble() <= 0) {
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
        val intent = Intent(this, MisProductos::class.java)
        startActivity(intent)
    }

    /*Método que verfica si la cantidad actual de un producto se acerca al
    stock mínimo y si es así mostrar una notificación al usuario.*/
    fun verificaStockMinimo(){
        val context = this
        val db = DataBaseHandler(context)
        val lista : MutableList<Producto> = db.extraerProductos()
        for (producto in lista){
            val stockMinimo = producto.stockMinimo
            val stockActual = producto.cantidadActual
            if((stockMinimo + 3) > stockActual ){ //verifica si el stock actual se está aproximando al stock minimo
                val titulo = "Tus productos se agotan"
                val mensaje = "Hay productos en tu inventario que estan llegando  a los niveles de stock minimo " +
                        "que tu definiste, hechale un vistazo."
                NotificationUtils().setNotification(mNotificationTime, this@ActivityActualizar, titulo, mensaje)
            }
        }
    }

}