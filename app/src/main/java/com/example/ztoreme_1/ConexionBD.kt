package com.example.ztoreme_1

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.widget.Toast

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
        "PRIMARY_KEY(FECHA_REGISTRO, ID_PRODUCTO)"

class DataBaseHandler(context : Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, 1){
    override fun onCreate(db: SQLiteDatabase?) {
        db?.execSQL(tablaProductos)
        db?.execSQL(tablaMovimientos)
    }

    override fun onUpgrade(p0: SQLiteDatabase?, p1: Int, p2: Int) {
        TODO("Not yet implemented")
    }

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
        var result = db.insert("PRODUCTOS",null, cv)
    }

    fun insertarMovimiento(movimiento : Movimiento){
        val db = this.writableDatabase
        var cv = ContentValues()
        cv.put("FECHA_REGISTRO", movimiento.fechaRegistro)
        cv.put("ID_PRODUCTO", movimiento.idProducto)
        cv.put("CANTIDAD_MOV", movimiento.cantidadMov)
        cv.put("ENTRADA", movimiento.entrada)
        var result = db.insert("MOVIMIENTOS",null, cv)
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
                producto.imagen = result.getString(result.getColumnIndex("IMAGEN")).toString()
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


}
