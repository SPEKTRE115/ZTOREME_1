package com.example.ztoreme_1.basedatos

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.core.text.isDigitsOnly
import com.example.ztoreme_1.categorias.Categoria
import com.example.ztoreme_1.Movimiento
import com.example.ztoreme_1.productos.Producto
import java.text.SimpleDateFormat
import java.time.DateTimeException
import java.time.LocalDateTime
import java.util.*
import kotlin.collections.ArrayList

val DATABASE_NAME = "ZTOREMEDB"
/*
* Constante que crea la tabla tablaProductos en la cual se almacena todos los datos necesarios
* para registrar un producto en el sistema.
* */
private const val tablaProductos = "CREATE TABLE IF NOT EXISTS PRODUCTOS  (" +
        "ID_PRODUCTO INTEGER PRIMARY KEY AUTOINCREMENT," +
        "NOMBRE TEXT," +
        "IMAGEN TEXT," +
        "DESCRIPCION TEXT," +
        "CANTIDAD_ACTUAL INTEGER," +
        "STOCK_MINIMO INTEGER," +
        "STOCK_MAXIMO INTEGER," +
        "PRECIO_COMPRA DECIMAL(8,2)," +
        "PRECIO_VENTA DECIMAL(8,2)," +
        "FECHA_REGISTRO TEXT);"
/*
* Constante que crea la tabla tablaMovimientos en la cual se almacenan todos los movimientos internos
* del intentario.
* */
private const val tablaMovimientos = "CREATE TABLE IF NOT EXISTS MOVIMIENTOS (" +
        "FECHA_REGISTRO TEXT," +
        "ID_PRODUCTO INTEGER," +
        "CANTIDAD_MOV INTEGER," +
        "ENTRADA INTEGER," +
        "FOREIGN KEY(ID_PRODUCTO) REFERENCES PRODUCTOS(ID_PRODUCTO)" +
        "PRIMARY KEY(FECHA_REGISTRO, ID_PRODUCTO))"
/*
* Constante que crea ta tabla relacionProductosCategoria en la cual almacena la relaciones que hay
* entre un producto con una categoria.
* */
private const val relacionProductosCategoria = "CREATE TABLE IF NOT EXISTS CATEGORIAS_PRODUCTOS(" +
        "ID_PRODUCTO INTEGER," +
        "ID_CATEGORIA INTEGER," +
        "CONSTRAINT fk_producto FOREIGN KEY (ID_PRODUCTO) REFERENCES PRODUCTOS(ID_PRODUCTO)," +
        "CONSTRAINT fk_categoria FOREIGN KEY (ID_CATEGORIA) REFERENCES CATEGORIAS(ID_CATEGORIA));"
/*
* Constante que crea ta tabla tablaCategorias en la cual almacena las categorias que sera usadas
* por el usuario.
* */
private const val tablaCategorias = "CREATE TABLE IF NOT EXISTS CATEGORIAS(" +
        "ID_CATEGORIA INTEGER PRIMARY KEY AUTOINCREMENT," +
        "NOMBRE TEXT);"

/*
* Ejecuta todas las constantes anteriormente especificadas para crear las respectivas tablas dentro
* de la base de datos.
* */
class DataBaseHandler(context : Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, 1){
    override fun onCreate(db: SQLiteDatabase?) {
        db?.execSQL(tablaProductos)
        db?.execSQL(tablaMovimientos)
        db?.execSQL(tablaCategorias)
        db?.execSQL(relacionProductosCategoria)
    }

    /*
    * Metodo para actualizar la base de datos.
    * */
    override fun onUpgrade(p0: SQLiteDatabase?, p1: Int, p2: Int) {

    }

    /*
    * Metodo para agregar productos dentro de la base de datos, haciendo uso del modelo Producto
    * */
    fun insertarProducto(producto : Producto){
        val db = this.writableDatabase
        var cv = ContentValues()
        cv.put("NOMBRE", producto.nombreProducto)
        cv.put("IMAGEN", producto.imagen)
        cv.put("DESCRIPCION", producto.descripcion)
        cv.put("CANTIDAD_ACTUAL", producto.cantidadActual)
        cv.put("STOCK_MINIMO", producto.stockMinimo)
        cv.put("STOCK_MAXIMO", producto.stockMaximo)
        cv.put("PRECIO_COMPRA", producto.precioCompra)
        cv.put("PRECIO_VENTA", producto.precioVenta)
        cv.put("FECHA_REGISTRO", getFechaRegistro())
        var result = db.insert("PRODUCTOS",null, cv)
        db.close()
    }

    /*
    * Metodo para colocar registros en la tabla categorias_productos.
    * */
    fun asociarCategoria(nombreP : String, nombreC : String){
        val db = this.writableDatabase
        val db2 = this.readableDatabase
        var cv = ContentValues()
        val query1 = "SELECT * FROM PRODUCTOS WHERE NOMBRE = '"+nombreP+"'"
        val query2 = "SELECT * FROM CATEGORIAS WHERE NOMBRE = '"+nombreC+"'"
        val resultado1 = db2.rawQuery(query1, null)
        val resultado2 = db2.rawQuery(query2, null)
        if (resultado1.moveToFirst()){
            cv.put("ID_PRODUCTO", resultado1.getString(resultado1.getColumnIndex("ID_PRODUCTO")).toInt())
        }
        if (resultado2.moveToFirst()){
            cv.put("ID_CATEGORIA", resultado2.getString(resultado2.getColumnIndex("ID_CATEGORIA")).toInt())
        }
        var result = db.insert("CATEGORIAS_PRODUCTOS", null, cv)
        db.close()
        db2.close()
    }

    /*
    * Metodo que ayuda a actualizar alguna categoria especifica.
    * */
    fun actualizarCategoria(nombreP: String, nombreC: String){
        val db = this.writableDatabase
        val db2 = this.readableDatabase
        var ayuda = ""
        var cv = ContentValues()
        val query1 = "SELECT * FROM PRODUCTOS WHERE NOMBRE = '"+nombreP+"'"
        val query2 = "SELECT * FROM CATEGORIAS WHERE NOMBRE = '"+nombreC+"'"
        val resultado1 = db2.rawQuery(query1, null)
        val resultado2 = db2.rawQuery(query2, null)
        if (resultado1.moveToFirst()){
            ayuda = resultado1.getString(resultado1.getColumnIndex("ID_PRODUCTO"))
        }
        if (resultado2.moveToFirst()){
            cv.put("ID_CATEGORIA", resultado2.getString(resultado2.getColumnIndex("ID_CATEGORIA")).toInt())
        }
        db.update("CATEGORIAS_PRODUCTOS", cv, "ID_PRODUCTO = ?", Array(1){ayuda})
        db.close()
    }

    /*
    * Metodo que ayuda a actualizar los datos de un producto que ya existe en la base de datos.
    * */
    fun actualizarProducto(producto: Producto, nombreP: String){

        val db = this.writableDatabase
        val query = "SELECT * FROM PRODUCTOS"
        val result = db.rawQuery(query, null)
        var cv = ContentValues()
        cv.put("NOMBRE", producto.nombreProducto)
        cv.put("IMAGEN", producto.imagen)
        cv.put("DESCRIPCION", producto.descripcion)
        cv.put("CANTIDAD_ACTUAL", producto.cantidadActual)
        cv.put("STOCK_MINIMO", producto.stockMinimo)
        cv.put("STOCK_MAXIMO", producto.stockMaximo)
        cv.put("PRECIO_COMPRA", producto.precioCompra)
        cv.put("PRECIO_VENTA", producto.precioVenta)
        db.update("PRODUCTOS", cv, "NOMBRE =?", Array<String>(1) {nombreP})
        result.close()
        db.close()
    }


    /*
    * Metodo que inserta movimientos en la tabla movimientos
    * */
    fun insertarMovimiento(movimiento : Movimiento){
        val db = this.writableDatabase
        var cv = ContentValues()
        cv.put("FECHA_REGISTRO", movimiento.fechaRegistro)
        cv.put("ID_PRODUCTO", movimiento.idProducto)
        cv.put("CANTIDAD_MOV", movimiento.cantidadMov)
        cv.put("ENTRADA", movimiento.entrada)
        db.insert("MOVIMIENTOS",null, cv)
    }

    /*
    * Metodo que permite extrar productos de la base de datos haciendo el uso del modelo Producto.
    * */
    fun extraerProductos() : MutableList<Producto>{
        var lista : MutableList<Producto> = ArrayList()
        val db = this.readableDatabase
        val query = "SELECT * FROM PRODUCTOS"
        val result = db.rawQuery(query, null)
        if(result.moveToFirst()){
            do {
                var producto = Producto()
                producto.idProducto = result.getString(result.getColumnIndex("ID_PRODUCTO")).toInt()
                producto.nombreProducto = result.getString(result.getColumnIndex("NOMBRE")).toString()
                producto.descripcion = result.getString(result.getColumnIndex("DESCRIPCION")).toString()
                producto.imagen = result.getString(result.getColumnIndex("IMAGEN")).toString()
                producto.cantidadActual = result.getString(result.getColumnIndex("CANTIDAD_ACTUAL")).toInt()
                producto.stockMinimo = result.getString(result.getColumnIndex("STOCK_MINIMO")).toInt()
                producto.stockMaximo = result.getString(result.getColumnIndex("STOCK_MAXIMO")).toInt()
                producto.fechaRegistro = result.getString(result.getColumnIndex("FECHA_REGISTRO")).toString()
                producto.precioCompra = result.getString(result.getColumnIndex("PRECIO_COMPRA")).toDouble()
                producto.precioVenta = result.getString(result.getColumnIndex("PRECIO_VENTA")).toDouble()
                lista.add(producto)
            } while (result.moveToNext())
        }
        result.close()
        db.close()
        return lista
    }

    /*
    * Metodo para extrar productos que pertenezcan a la categoria mencionada.
    * */
    fun extraerProductosbyCategoria(categoria:String) : MutableList<Producto>{
        var lista : MutableList<Producto> = ArrayList()

        val db = this.readableDatabase
        val query = "SELECT P.NOMBRE, P.DESCRIPCION, P.CANTIDAD_ACTUAL, P.STOCK_MINIMO, P.STOCK_MAXIMO," +
                " P.PRECIO_COMPRA, P.PRECIO_VENTA, P.IMAGEN FROM PRODUCTOS P JOIN CATEGORIAS_PRODUCTOS CP ON P.ID_PRODUCTO = CP.ID_PRODUCTO" +
                " JOIN CATEGORIAS C ON CP.ID_CATEGORIA = C.ID_CATEGORIA WHERE C.NOMBRE LIKE '"+categoria+"'"
        val result = db.rawQuery(query, null)

        println(result.toString())

        if(result.moveToFirst()){
            do {
                var producto = Producto()
                producto.imagen = result.getString(result.getColumnIndex("IMAGEN")).toString()
                producto.nombreProducto = result.getString(result.getColumnIndex("NOMBRE")).toString()
                producto.descripcion = result.getString(result.getColumnIndex("DESCRIPCION")).toString()
                producto.cantidadActual = result.getString(result.getColumnIndex("CANTIDAD_ACTUAL")).toInt()
                producto.stockMinimo = result.getString(result.getColumnIndex("STOCK_MINIMO")).toInt()
                producto.stockMaximo = result.getString(result.getColumnIndex("STOCK_MAXIMO")).toInt()
                producto.precioCompra = result.getString(result.getColumnIndex("PRECIO_COMPRA")).toDouble()
                producto.precioVenta = result.getString(result.getColumnIndex("PRECIO_VENTA")).toDouble()
                lista.add(producto)
            } while (result.moveToNext())
        }
        result.close()
        db.close()
        return lista
    }

    /*
    * Metodo para extrar los movimientos que fueron relizados dentro del inventario.
    * */
    fun extraerMovimientos() : MutableList<Movimiento>{
        var lista : MutableList<Movimiento> = ArrayList()
        val db = this.readableDatabase
        val query = "SELECT * FROM MOVIMIENTOS"
        val result = db.rawQuery(query, null)
        if(result.moveToLast()){
            do {
                var movimiento = Movimiento()
                movimiento.idProducto = result.getString(result.getColumnIndex("ID_PRODUCTO")).toInt()
                movimiento.cantidadMov = result.getString(result.getColumnIndex("CANTIDAD_MOV")).toInt()
                movimiento.entrada = result.getString(result.getColumnIndex("ENTRADA")).toInt()
                movimiento.fechaRegistro = result.getString(result.getColumnIndex("FECHA_REGISTRO")).toString()
                lista.add(movimiento)
            } while (result.moveToPrevious())
        }
        result.close()
        db.close()
        return lista
    }

    /*
    * Metodo que nos permite extrar el nombre del producto cuando este
    * */
    fun extraerNombreProductoPorMovimiento(id:Int) : String{
        var nombre_producto= id.toString()
        println(id)
        val db = this.readableDatabase
        val query = "SELECT * FROM PRODUCTOS"
        val result = db.rawQuery(query, null)
        if(result.moveToFirst()){
            do {
                var producto = Producto()
                producto.idProducto = result.getString(result.getColumnIndex("ID_PRODUCTO")).toInt()
                producto.nombreProducto = result.getString(result.getColumnIndex("NOMBRE")).toString()
                if(id == producto.idProducto){
                    nombre_producto = producto.nombreProducto
                }
            } while (result.moveToNext())
        }
        result.close()
        db.close()
        return nombre_producto
    }

    /*
    * Metodo para extrar el id del producto dado el nombre del producto
    * */
    fun extraerIdPorNombreProducto(nombre: String) : Int{
        var id= 0
        val db = this.readableDatabase
        val query = "SELECT * FROM PRODUCTOS"
        val result = db.rawQuery(query, null)
        if(result.moveToFirst()){
            do {
                var producto = Producto()
                producto.idProducto = result.getString(result.getColumnIndex("ID_PRODUCTO")).toInt()
                producto.nombreProducto = result.getString(result.getColumnIndex("NOMBRE")).toString()
                if(nombre == producto.nombreProducto){
                    id = producto.idProducto
                }
            } while (result.moveToNext())
        }
        result.close()
        db.close()
        return id
    }

    fun extraerProductoId(id:Int) : Producto{
        val db = this.readableDatabase
        val query = "SELECT * FROM PRODUCTOS WHERE ID_PRODUCTO ="+id
        val result = db.rawQuery(query, null)
        var producto = Producto()

        if(result.moveToFirst()){
            do {
                producto.imagen = result.getString(result.getColumnIndex("IMAGEN")).toString()
                producto.nombreProducto = result.getString(result.getColumnIndex("NOMBRE")).toString()
                producto.descripcion = result.getString(result.getColumnIndex("DESCRIPCION")).toString()
                producto.cantidadActual = result.getString(result.getColumnIndex("CANTIDAD_ACTUAL")).toInt()
                producto.stockMinimo = result.getString(result.getColumnIndex("STOCK_MINIMO")).toInt()
                producto.stockMaximo = result.getString(result.getColumnIndex("STOCK_MAXIMO")).toInt()
                producto.precioCompra = result.getString(result.getColumnIndex("PRECIO_COMPRA")).toDouble()
                producto.precioVenta = result.getString(result.getColumnIndex("PRECIO_VENTA")).toDouble()
            } while (result.moveToNext())
        }
        result.close()
        db.close()

        return producto
    }

    /*
    * Metodo para extrar nombre de los productos.
    * */
    fun extraeNom() : MutableList<Producto>{
        var lista : MutableList<Producto> = ArrayList()
        val db = this.readableDatabase
        val query = "SELECT * FROM PRODUCTOS"
        val result = db.rawQuery(query, null)
        if(result.moveToFirst()){
            do {
                var producto = Producto()
                producto.nombreProducto = result.getString(result.getColumnIndex("NOMBRE")).toString()
                lista.add(producto)
            } while (result.moveToNext())
        }
        result.close()
        db.close()
        return lista
    }

    /*
    * Metodo que nos permite borrar los productos del inventario.
    * */
    fun borrarProducto(nombre : String){
        val db = this.writableDatabase
        db.delete("PRODUCTOS","NOMBRE = '" +nombre + "'",null)
        db.close()
    }

    /*
    * Metodo que nos permite borrar los movimientos registrados en la base de datos.
    * */
    fun borrarMovimientos(id : Int){
        val db = this.writableDatabase
        db.delete("MOVIMIENTOS","ID_PRODUCTO = '" +id + "'",null)
        db.close()
    }

    /*
    * Metodo que nos permite insertar una nueva categoria a la base de datos.
    * */
    fun insertarCategoria(categoria : Categoria){
        val db = this.writableDatabase
        var cv = ContentValues()
        cv.put("NOMBRE", categoria.nombreCategoria)
        var result = db.insert("CATEGORIAS",null,cv)
        db.close()
    }

    /*
    * Metodo que nos permite actualizar las categorias existentes en la base de datos.
    * */
    fun actualizarCategorias(categoria: Categoria, nombreCN: String){
        val db = this.writableDatabase
        var cv = ContentValues()
        cv.put("NOMBRE", categoria.nombreCategoria)
        db.update("CATEGORIAS", cv, "NOMBRE =?", Array(1){nombreCN})
        db.close()
    }

    /*
    * Metodo que nos permite eliminar la categoria registrada en la base de datos.
    * */
    fun eliminarCategoria(nombreC: String){
        val db = this.writableDatabase
        db.delete("CATEGORIAS", "NOMBRE = '"+nombreC+"'",null)
        db.close()
    }

    /*
    * Metodo que nos permite obtener las categorias registradas en la base de datos.
    * */
    fun extraerCategorias() : MutableList<Categoria>{
        var lista : MutableList<Categoria> = ArrayList()

        val db = this.readableDatabase
        val query = "SELECT * FROM CATEGORIAS"
        val result = db.rawQuery(query, null)
        if(result.moveToFirst()){
            do {
                var categoria = Categoria()
                categoria.idCategoria = result.getString(result.getColumnIndex("ID_CATEGORIA")).toInt()
                categoria.nombreCategoria = result.getString(result.getColumnIndex("NOMBRE")).toString()
                lista.add(categoria)
            } while(result.moveToNext())
        }
        result.close()
        db.close()
        return lista
    }

    /*
    * Metodo para aumentar la cantidad de stock actual de un producto registrado en la base de datos.
    * */
    fun aumentarStockProducto(nombre: String, cantidad: Int){
        val db = this.writableDatabase
        val query = "SELECT CANTIDAD_ACTUAL FROM PRODUCTOS WHERE NOMBRE='"+nombre+"'"
        val result = db.rawQuery(query, null)
        result.moveToFirst()
        var cantidad_actual = result.getString(result.getColumnIndex("CANTIDAD_ACTUAL")).toInt()
        var cant_final=(cantidad_actual+cantidad)
        val valores = ContentValues()
        valores.put("CANTIDAD_ACTUAL",cant_final)
        db.update("PRODUCTOS", valores, "NOMBRE = '" + nombre + "'", null)
        db.close()
    }

    /*
    * Metodo para disminuir la cantidad de stock actual de un producto registrado en la base de datos.
    * */
    fun disminuirStockProducto(nombre: String, cantidad: Int){
        val db = this.writableDatabase
        val query = "SELECT CANTIDAD_ACTUAL FROM PRODUCTOS WHERE NOMBRE='"+nombre+"'"
        val result = db.rawQuery(query, null)
        result.moveToFirst()
        var cantidad_actual = result.getString(result.getColumnIndex("CANTIDAD_ACTUAL")).toInt()
        var cant_final=(cantidad_actual-cantidad)
        val valores = ContentValues()
        valores.put("CANTIDAD_ACTUAL",cant_final)
        db.update("PRODUCTOS", valores, "NOMBRE = '" + nombre + "'", null)
        db.close()
    }

    /*
    * Metodo que nos ayuda a obtener la fecha de registro para los productos.
    * */
    fun getFechaRegistro() : String{
        val fechaRegistro = Calendar.getInstance()
        return  "${SimpleDateFormat("dd/M/yyyy hh:mm:ss").format(fechaRegistro.time)}"
    }
}
