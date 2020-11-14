package com.example.ztoreme_1

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
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
import com.itextpdf.text.*
import com.itextpdf.text.pdf.PdfPCell
import com.itextpdf.text.pdf.PdfPTable
import com.itextpdf.text.pdf.PdfWriter
import kotlinx.android.synthetic.main.activity_main.*
import java.io.FileOutputStream
import java.lang.Integer.parseInt
import java.text.SimpleDateFormat
import java.util.*
import kotlin.system.exitProcess


class MainActivity : AppCompatActivity() {

    private val mNotificationTime = Calendar.getInstance().timeInMillis + 10000 //Set after 5 seconds from the current time.
    private var mNotified = false
    private var dosvecesAtras = false
    private val STORAGE_CODE : Int = 100;
    /*lateinit var notificationManager : NotificationManager
    lateinit var notificationChannel: NotificationChannel
    lateinit var  builder : Notification.Builder
    private val channelID = "NOTIFICACION_PRODUCTO_ANTIGUO"
    private val description = "Notifica que productos han estado" +
            "mucho tiempo almacenados"*/

    @RequiresApi(Build.VERSION_CODES.M)

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

        if (!mNotified) {
            NotificationUtils().setNotification(mNotificationTime, this@MainActivity)
        }

        if  (checkSelfPermission("READ_EXTERNAL_STORAGE") == PackageManager.PERMISSION_GRANTED){
            Toast.makeText(context, "PERMISOS CONCEDIDOS", Toast.LENGTH_SHORT).show()
        }else{
            requestPermissions(arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE), 100)
        }

        val click_1 = findViewById(R.id.seccion1) as TextView
        click_1.setOnClickListener {
            val intento1 = Intent(this, MisProductos::class.java)
            startActivity(intento1)
        }

        val click_1_1 = findViewById(R.id.imagen_inventario) as ImageView
        click_1_1.setOnClickListener {
            val intento1 = Intent(this, MisProductos::class.java)
            startActivity(intento1)
        }

        val click_2 = findViewById(R.id.seccion2) as TextView
        click_2.setOnClickListener {
            val intento1 = Intent(this, ActivityAgregar::class.java)
            startActivity(intento1)
        }

        val click_2_1 = findViewById(R.id.imagen_agregar) as ImageView
        click_2_1.setOnClickListener {
            val intento1 = Intent(this, ActivityAgregar::class.java)
            startActivity(intento1)
        }

        val click_3 = findViewById(R.id.seccion3) as TextView
        click_3.setOnClickListener {
            val intento1 = Intent(this, ActivityAgregarCategoria::class.java)
            startActivity(intento1)
        }

        val click_3_1 = findViewById(R.id.imagen_categoria) as ImageView
        click_3_1.setOnClickListener {
            val intento1 = Intent(this, ActivityAgregarCategoria::class.java)
            startActivity(intento1)
        }

        val click_4 = findViewById(R.id.seccion4) as TextView
        click_4.setOnClickListener {
            val intento1 = Intent(this, ActivityMovimientos::class.java)
            startActivity(intento1)
        }

        val click_4_1 = findViewById(R.id.imagen_movimientos) as ImageView
        click_4_1.setOnClickListener {
            val intento1 = Intent(this, ActivityMovimientos::class.java)
            startActivity(intento1)
        }


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

    }

    private fun savePdf() {
        val context = this
        val db = DataBaseHandler(context)
        var lista_movimientos = db.extraerMovimientos()

        // Creacion del documento
        val mDoc = Document()

        val mFileName = SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(System.currentTimeMillis())
        val mFilePath = getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS).toString()+"/"+mFileName+".pdf"

        try {
            PdfWriter.getInstance(mDoc, FileOutputStream(mFilePath))
            mDoc.open()

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

            for(i in lista_movimientos){
                var movimiento = Movimiento(i.fechaRegistro, i.idProducto, i.cantidadMov, i.entrada)

                if(!verificaFecha(movimiento.fechaRegistro)){
                    // Saber el tipo de movimiento
                    if(movimiento.entrada == 1){
                        var nombre_producto = db.extraerNombreProductoPorMovimiento(movimiento.idProducto)
                        var cantidad_movi = movimiento.cantidadMov.toString() + " entradas"
                        var fecha_movi = movimiento.fechaRegistro
                        table.addCell(nombre_producto)
                        table.addCell(cantidad_movi)
                        table.addCell(fecha_movi)
                    }else{
                        var nombre_producto = db.extraerNombreProductoPorMovimiento(movimiento.idProducto)
                        var cantidad_movi = movimiento.cantidadMov.toString() + " salidas"
                        var fecha_movi = movimiento.fechaRegistro
                        table.addCell(nombre_producto)
                        table.addCell(cantidad_movi)
                        table.addCell(fecha_movi)
                    }
                }
            }

            mDoc.add(table)
            mDoc.close()
            Toast.makeText(this, "$mFileName.pdf guardado en \n$mFilePath", Toast.LENGTH_LONG).show()

        }catch (e: Exception){
            println("Entro en el error" + e)
            Toast.makeText(this, "No se pudo guardar tu archivo", Toast.LENGTH_LONG).show()
        }
    }

    private fun verificaFecha(fechaProducto: String): Boolean{

        var fecha_hora = fechaProducto.split(" ")
        var fecha = fecha_hora[0].split("/")
        val calendario = Calendar.getInstance()
        var dia = SimpleDateFormat("d").format(calendario.time)
        var mes = SimpleDateFormat("M").format(calendario.time)

        if(parseInt(dia) == parseInt(fecha[0]) && (parseInt(fecha[1])+1) == parseInt(mes)){
            return true
        }
        return false
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        when(requestCode){
            STORAGE_CODE -> {
                if (grantResults.size > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    //savePdf()
                } else {
                    Toast.makeText(this, "Permisos denegados...!", Toast.LENGTH_LONG).show()
                }
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        return when (item.itemId) {
            R.id.action_settings -> true
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onBackPressed() {
        if (dosvecesAtras){
            finish()
            exitProcess(0)
        }
        this.dosvecesAtras = true
        Toast.makeText(this, "Regresa una vez más para salir de la aplicación", Toast.LENGTH_SHORT).show()
        Handler().postDelayed(Runnable { dosvecesAtras = false }, 2000)
    }

}