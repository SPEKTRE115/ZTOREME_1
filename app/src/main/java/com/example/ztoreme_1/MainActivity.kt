package com.example.ztoreme_1

import android.Manifest
import android.app.*
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Color
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.os.Handler
import android.view.Menu
import android.view.MenuItem
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import com.example.ztoreme_1.basedatos.DataBaseHandler
import com.example.ztoreme_1.categorias.ActivityAgregarCategoria
import com.example.ztoreme_1.categorias.Categoria
import com.example.ztoreme_1.notificaciones.NotificationUtils
import com.example.ztoreme_1.productos.ActivityAgregar
import com.example.ztoreme_1.productos.MisProductos
import com.example.ztoreme_1.productos.Producto
import java.lang.Integer.parseInt
import java.text.SimpleDateFormat
import com.itextpdf.text.*
import com.itextpdf.text.pdf.PdfPCell
import com.itextpdf.text.pdf.PdfPTable
import com.itextpdf.text.pdf.PdfWriter
import kotlinx.android.synthetic.main.activity_main.*
import java.io.FileOutputStream
import java.lang.Integer.parseInt
import java.util.*
import kotlin.system.exitProcess

/*
* Clase que carga las operaciones iniciales para la aplicacion, principalmente la vista principal de
* ella.
* Parametros:
* mNotificationTime: Tiempo de notificacion en milisegundos, sirve para hacer notificaciones al minuto
* de haber iniciado la señal.
* dosvecesAtras:
* */
class MainActivity : AppCompatActivity() {

    private val mNotificationTime = Calendar.getInstance().timeInMillis + 60000
    private var dosvecesAtras = false
    private val STORAGE_CODE : Int = 100;

    @RequiresApi(Build.VERSION_CODES.M)

    /*
    * Funcion que se activa al iniciar el MainActivity y carga la vista de la aplicacion, los botones
    * y recursos necesarios (Imagenes) para que el usuario pueda ver el menu principal de la aplicacion.
    * */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(findViewById(R.id.toolbar))

        val context = this
        var db = DataBaseHandler(context)
        val lista  = arrayListOf<String>()
        var datos = db.extraerCategorias()

        for (i in datos){
            lista.add(i.nombreCategoria)
        }
        if (lista.isEmpty()){
            db.insertarCategoria(Categoria("Sin Categoría"))
        }

        if  (checkSelfPermission("READ_EXTERNAL_STORAGE") == PackageManager.PERMISSION_GRANTED){
            Toast.makeText(context, "PERMISOS CONCEDIDOS", Toast.LENGTH_SHORT).show()
        }else{
            requestPermissions(arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE), 100)
        }

        /*
        * Texto para inciar el activity de mis productos.
        * */
        val click_1 = findViewById(R.id.seccion1) as TextView
        click_1.setOnClickListener {
            val intento1 = Intent(this, MisProductos::class.java)
            startActivity(intento1)
        }

        /*
        * imagen para iniciar el activity de mis productos
        * */
        val click_1_1 = findViewById(R.id.imagen_inventario) as ImageView
        click_1_1.setOnClickListener {
            val intento1 = Intent(this, MisProductos::class.java)
            startActivity(intento1)
        }

        /*
        * texto para iniciar el activity Agregar producto
        * */
        val click_2 = findViewById(R.id.seccion2) as TextView
        click_2.setOnClickListener {
            val intento1 = Intent(this, ActivityAgregar::class.java)
            startActivity(intento1)
        }

        /*
        * Imagen para iniciar el activity Agregar producto
        * */
        val click_2_1 = findViewById(R.id.imagen_agregar) as ImageView
        click_2_1.setOnClickListener {
            val intento1 = Intent(this, ActivityAgregar::class.java)
            startActivity(intento1)
        }

        /*
        * Texto para iniciar el activity mis categorias
        * */
        val click_3 = findViewById(R.id.seccion3) as TextView
        click_3.setOnClickListener {
            val intento1 = Intent(this, ActivityAgregarCategoria::class.java)
            startActivity(intento1)
        }

        /*
        * Imagen para iniciar el activity mis categorias
        * */
        val click_3_1 = findViewById(R.id.imagen_categoria) as ImageView
        click_3_1.setOnClickListener {
            val intento1 = Intent(this, ActivityAgregarCategoria::class.java)
            startActivity(intento1)
        }

        /*
        * Texto para iniciar el activity mis Movimientos
        * */
        val click_4 = findViewById(R.id.seccion4) as TextView
        click_4.setOnClickListener {
            val intento1 = Intent(this, ActivityMovimientos::class.java)
            startActivity(intento1)
        }

        /*
        * Imagen para iniciar el activity mis Movimientos
        * */
        val click_4_1 = findViewById(R.id.imagen_movimientos) as ImageView
        click_4_1.setOnClickListener {
            val intento1 = Intent(this, ActivityMovimientos::class.java)
            startActivity(intento1)
        }

        /*
        * Boton generar pdf el cual manda llamar al metodo savePdf para crear el pdf de los movimientos
        * que fueron echos en el inventario.
        * */
        btn_generarPDF.setOnClickListener{
            if(Build.VERSION.SDK_INT > Build.VERSION_CODES.M){
                if(checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_DENIED){
                    val permissions = arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    requestPermissions(permissions, STORAGE_CODE)
                }else{
                    savePdf()
                }
            }else{
                savePdf()
            }
        }

        btn_reporte_ganancias.setOnClickListener{
            if(Build.VERSION.SDK_INT > Build.VERSION_CODES.M){
                if(checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_DENIED){
                    val permissions = arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    requestPermissions(permissions, STORAGE_CODE)
                }else{
                    reporteGananciasSave()
                }
            }else{
                reporteGananciasSave()
            }
        }

    }


    /*
    * Metodo para guardar el pdf en la carpeta de descargas con la informacion de los movimientos que
    * fueron realizados en el inventario.
    * */
    private fun savePdf() {
        val context = this
        val db = DataBaseHandler(context)
        var lista_movimientos = db.extraerMovimientos()

        // Creacion del documento
        val mDoc = Document()
        // Nombre del documento
        val mFileName = SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(System.currentTimeMillis())
        // Ruta en la que se guarda el pdf
        val mFilePath = "/storage/emulated/0/Download/"+mFileName+".pdf"

        try {
            // Se guarda el pdf
            PdfWriter.getInstance(mDoc, FileOutputStream(mFilePath))
            // Se abre para hacer la edicion del doc
            mDoc.open()

            // Se crea la tabla con de la libreria itextpdf
            val table = PdfPTable(3)

            //Titulo del documento
            val cell = PdfPCell(Phrase("ZTORE ME | Reporte mensual de inventario"))
            cell.setBorder(Rectangle.NO_BORDER)
            cell.setColspan(3)
            cell.setBackgroundColor(BaseColor(154, 211, 188))
            cell.setHorizontalAlignment(Element.ALIGN_CENTER)
            table.addCell(cell)

            // Columnas
            val cell_produc = PdfPCell(Phrase("Producto"))
            cell_produc.setBackgroundColor(BaseColor.LIGHT_GRAY)
            cell_produc.setHorizontalAlignment(Element.ALIGN_CENTER)
            table.addCell(cell_produc)

            val cell_movi = PdfPCell(Phrase("Movimiento"))
            cell_movi.setBackgroundColor(BaseColor.LIGHT_GRAY)
            cell_movi.setHorizontalAlignment(Element.ALIGN_CENTER)
            table.addCell(cell_movi)

            val cell_fecha = PdfPCell(Phrase("Fecha"))
            cell_fecha.setBackgroundColor(BaseColor.LIGHT_GRAY)
            cell_fecha.setHorizontalAlignment(Element.ALIGN_CENTER)
            table.addCell(cell_fecha)

            // Se obtienen los movimientos realizados en el inventario
            for(i in lista_movimientos){
                // Creacion del objeto movimientos
                var movimiento = Movimiento(i.fechaRegistro, i.idProducto, i.cantidadMov, i.entrada)

                //Validacion para obtener los movimientos solo realizados en el mes
                if(!verificaFecha(movimiento.fechaRegistro)){
                    // Saber el tipo de movimiento
                    if(movimiento.entrada == 1){
                        var nombre_producto = db.extraerNombreProductoPorMovimiento(movimiento.idProducto)
                        var cantidad_movi = movimiento.cantidadMov.toString() + " entradas"
                        var fecha_movi = movimiento.fechaRegistro
                        // Añadir los valores de los movimientos en las tablas
                        table.addCell(nombre_producto)
                        table.addCell(cantidad_movi)
                        table.addCell(fecha_movi)
                    }else{
                        var nombre_producto = db.extraerNombreProductoPorMovimiento(movimiento.idProducto)
                        var cantidad_movi = movimiento.cantidadMov.toString() + " salidas"
                        var fecha_movi = movimiento.fechaRegistro
                        // Añadir los valores de los movimientos en las tablas
                        table.addCell(nombre_producto)
                        table.addCell(cantidad_movi)
                        table.addCell(fecha_movi)
                    }
                }
            }

            // Agregar la tabla al documento
            mDoc.add(table)
            // Cerrar el documento
            mDoc.close()
            Toast.makeText(this, "$mFileName.pdf guardado en \n$mFilePath", Toast.LENGTH_LONG).show()

        }catch (e: Exception){
            println("Entro en el error" + e)
            Toast.makeText(this, "No se pudo guardar tu archivo", Toast.LENGTH_LONG).show()
        }
    }

    private fun reporteGananciasSave(){
        val context = this
        val db = DataBaseHandler(context)
        var lista_movimientos = db.extraerMovimientos()

        // Creacion del documento
        val mDoc = Document()
        // Nombre del documento
        val mFileName = "Ganancias: "+SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(System.currentTimeMillis())
        // Ruta en la que se guarda el pdf
        val mFilePath = "/storage/emulated/0/Download/"+mFileName+".pdf"

        try {
            // Se guarda el pdf
            PdfWriter.getInstance(mDoc, FileOutputStream(mFilePath))
            // Se abre para hacer la edicion del doc
            mDoc.open()

            // Se crea la tabla con de la libreria itextpdf
            val table = PdfPTable(3)

            //Titulo del documento
            val cell = PdfPCell(Phrase("ZTORE ME | Reporte general de ganancias"))
            cell.setBorder(Rectangle.NO_BORDER)
            cell.setColspan(3)
            cell.setBackgroundColor(BaseColor(154, 211, 188))
            cell.setHorizontalAlignment(Element.ALIGN_CENTER)
            table.addCell(cell)

            // Columnas
            val cell_produc = PdfPCell(Phrase("Producto"))
            cell_produc.setBackgroundColor(BaseColor.LIGHT_GRAY)
            cell_produc.setHorizontalAlignment(Element.ALIGN_CENTER)
            table.addCell(cell_produc)

            val cell_sal = PdfPCell(Phrase("Cantidad"))
            cell_sal.setBackgroundColor(BaseColor.LIGHT_GRAY)
            cell_sal.setHorizontalAlignment(Element.ALIGN_CENTER)
            table.addCell(cell_sal)

            val cell_movi = PdfPCell(Phrase("Ganancias"))
            cell_movi.setBackgroundColor(BaseColor.LIGHT_GRAY)
            cell_movi.setHorizontalAlignment(Element.ALIGN_CENTER)
            table.addCell(cell_movi)

            // Se obtienen los movimientos realizados en el inventario
            for(i in lista_movimientos){
                // Creacion del objeto movimientos
                var movimiento = Movimiento(i.fechaRegistro, i.idProducto, i.cantidadMov, i.entrada)

                var producto = db.extraerProductoId(i.idProducto)

                var ganancias = (producto.precioVenta * i.cantidadMov).toString()

                if(movimiento.entrada == 1){
                    var nombre_producto = db.extraerNombreProductoPorMovimiento(movimiento.idProducto)
                    var cantidad_movi = movimiento.cantidadMov.toString() + " salidas"
                    // Añadir los valores de los movimientos en las tablas
                    table.addCell(nombre_producto)
                    table.addCell(cantidad_movi)
                    table.addCell("$"+ganancias)
                }
            }

            // Agregar la tabla al documento
            mDoc.add(table)
            // Cerrar el documento
            mDoc.close()
            Toast.makeText(this, "$mFileName.pdf guardado en \n$mFilePath", Toast.LENGTH_LONG).show()

        }catch (e: Exception){
            println("Entro en el error" + e)
            Toast.makeText(this, "No se pudo guardar tu archivo", Toast.LENGTH_LONG).show()
        }
    }

    /*
    * Metodo para saber si la app tiene permisos para acceder a los archivos.
    * */
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        when(requestCode){
            //En caso de no tener premisos los pide al usuario
            STORAGE_CODE -> {
                if (grantResults.size > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                } else {
                    // En caso de decir que no, aparecera un toast con mensaje y no podra guardar el pdf
                    Toast.makeText(this, "Permisos denegados...!", Toast.LENGTH_LONG).show()
                }
            }
        }
        verificaAntiguedad()
    }

    /*
    * Crea las opciones en el menu del navbar
    * */
    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    /*
    * Para obtener la opcion seleccionada por el usuario del menu
    * */
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        return when (item.itemId) {
            R.id.action_settings -> true
            else -> super.onOptionsItemSelected(item)
        }
    }

    /*
    * Metodo que nos permite salir de la aplicacion presionando dos veces hacia atras.
    * */
    override fun onBackPressed() {
        if (dosvecesAtras){
            finish()
            exitProcess(0)
        }
        this.dosvecesAtras = true
        Toast.makeText(this, "Regresa una vez más para salir de la aplicación", Toast.LENGTH_SHORT).show()
        Handler().postDelayed(Runnable { dosvecesAtras = false }, 2000)
    }

    /*
    * Metodo que recorre todos los productos registrados en la base de datos y checa la fecha de
    * cada uno y si alguno de los productos lleva un mes dentro del inventario generara una
    * notificacion y la mostrara al usuario.
    * */
    fun verificaAntiguedad(){
        val context = this
        val db = DataBaseHandler(context)
        val lista : MutableList<Producto> = db.extraerProductos()
        for (producto in lista){
            var fechaProducto = producto.fechaRegistro
            if(verificaFecha(fechaProducto)){
                val titulo = "Tienes productos antiguos"
                val mensaje = "Hay productos en tu inventario que llevan mucho tiempo almacenados, hecha un vistazo."
                NotificationUtils().setNotification(mNotificationTime, this@MainActivity, titulo, mensaje)
            }
        }
    }

    /*
    * Metodo de ayuda que permite hacer una comparacion de una fecha pasada con la fecha actual para
    * determinar que el tiempo del producto dentro del inventario es de un mes.
    * */
    fun verificaFecha(fechaProducto : String) : Boolean{
        var splitFechaHora = fechaProducto.split(" ")
        var splitFecha = splitFechaHora[0].split("/")
        val calendario = Calendar.getInstance()
        val dia = SimpleDateFormat("d").format(calendario.time)
        val mes = SimpleDateFormat("M").format(calendario.time)
        if (parseInt(dia) == parseInt(splitFecha[0]) && (parseInt(splitFecha[1])+1) == parseInt(mes)) {
            return true
        }
        return false
    }
}