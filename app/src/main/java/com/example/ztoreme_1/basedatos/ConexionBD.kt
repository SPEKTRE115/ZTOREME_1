package com.example.ztoreme_1.basedatos

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import com.example.ztoreme_1.Categoria
import com.example.ztoreme_1.Movimiento
import com.example.ztoreme_1.productos.Producto

val DATABASE_NAME = "ZTOREMEDB"

private const val tablaProductos = "CREATE TABLE IF NOT EXISTS PRODUCTOS  (" +
        "ID_PRODUCTO INTEGER PRIMARY KEY AUTOINCREMENT," +
        "NOMBRE TEXT," +
        "IMAGEN TEXT," +
        "DESCRIPCION TEXT," +
        "CANTIDAD_ACTUAL INTEGER," +
        "STOCK_MINIMO INTEGER," +
        "STOCK_MAXIMO INTEGER," +
        "PRECIO_COMPRA INTEGER," +
        "PRECIO_VENTA INTEGER);"

private const val tablaMovimientos = "CREATE TABLE IF NOT EXISTS MOVIMIENTOS (" +
        "FECHA_REGISTRO TEXT," +
        "ID_PRODUCTO INTEGER," +
        "CANTIDAD_MOV INTEGER," +
        "ENTRADA INTEGER," +
        "FOREIGN KEY(ID_PRODUCTO) REFERENCES PRODUCTOS(ID_PRODUCTO)" +
        "PRIMARY KEY(FECHA_REGISTRO, ID_PRODUCTO))"

private const val relacionProductosCategoria = "CREATE TABLE IF NOT EXISTS CATEGORIAS_PRODUCTOS(" +
        "ID_PRODUCTO INTEGER," +
        "ID_CATEGORIA INTEGER," +
        "CONSTRAINT fk_producto FOREIGN KEY (ID_PRODUCTO) REFERENCES PRODUCTOS(ID_PRODUCTO)," +
        "CONSTRAINT fk_categoria FOREIGN KEY (ID_CATEGORIA) REFERENCES CATEGORIAS(ID_CATEGORIA));"

private const val tablaCategorias = "CREATE TABLE IF NOT EXISTS CATEGORIAS(" +
        "ID_CATEGORIA INTEGER PRIMARY KEY AUTOINCREMENT," +
        "NOMBRE TEXT);"

class DataBaseHandler(context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, 1){
    override fun onCreate(db: SQLiteDatabase?) {
        db?.execSQL(tablaProductos)
        db?.execSQL(tablaMovimientos)
        db?.execSQL(tablaCategorias)
        db?.execSQL(relacionProductosCategoria)
    }

    override fun onUpgrade(p0: SQLiteDatabase?, p1: Int, p2: Int) {
        TODO("Not yet implemented")
    }

    fun insertarProducto(producto: Producto){
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
        var result = db.insert("PRODUCTOS", null, cv)
    }


    fun insertarMovimiento(movimiento: Movimiento){
        val db = this.writableDatabase
        var cv = ContentValues()
        cv.put("FECHA_REGISTRO", movimiento.fechaRegistro)
        cv.put("ID_PRODUCTO", movimiento.idProducto)
        cv.put("CANTIDAD_MOV", movimiento.cantidadMov)
        cv.put("ENTRADA", movimiento.entrada)
        var result = db.insert("MOVIMIENTOS", null, cv)
    }

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
                producto.cantidadActual = result.getString(result.getColumnIndex("CANTIDAD_ACTUAL")).toInt()
                producto.stockMinimo = result.getString(result.getColumnIndex("STOCK_MINIMO")).toInt()
                producto.stockMaximo = result.getString(result.getColumnIndex("STOCK_MAXIMO")).toInt()
                producto.precioCompra = result.getString(result.getColumnIndex("PRECIO_COMPRA")).toInt()
                producto.precioVenta = result.getString(result.getColumnIndex("PRECIO_VENTA")).toInt()
                lista.add(producto)
            } while (result.moveToNext())
        }
        result.close()
        db.close()
        return lista
    }

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




    fun borrarProducto(nombre: String){
        val db = this.writableDatabase
        //db.execSQL("DELETE FROM PRODUCTOS WHERE NOMBRE="+nombre)
        db.delete("PRODUCTOS", "NOMBRE = '" + nombre + "'", null)
        db.close()
    }


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



    fun insertarCategoria(categoria: Categoria){
        val db = this.writableDatabase
        var cv = ContentValues()
        cv.put("NOMBRE", categoria.nombreCategoria)
        var result = db.insert("CATEGORIAS", null, cv)
    }

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

}
