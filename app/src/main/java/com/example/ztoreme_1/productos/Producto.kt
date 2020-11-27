package com.example.ztoreme_1.productos

import java.io.Serializable

/*
Clase que crea un objeto de tipo producto para la manipulación
sencilla de la información de un producto sin la necesidad
de hacer consultas a la base de datos.
 */
class Producto : Serializable{

    var idProducto : Int = 0
    var nombreProducto : String = ""
    var imagen : String = ""
    var descripcion : String = ""
    var cantidadActual : Int = 0
    var stockMinimo : Int = 0
    var stockMaximo  : Int = 0
    var precioCompra  : Double = 0.0
    var precioVenta : Double = 0.0
    var fechaRegistro : String = ""

    /* Constrcutor de la clase Producto para guardar o ediatr un producto en la base
    de datos*/
    constructor(nombreProducto : String, imagen : String, descripcion : String, cantidadActual : Int,
                stockMinimo : Int, stockMaximo : Int, precioCompra : Double, precioVenta : Double){
        this.nombreProducto = nombreProducto
        this.imagen = imagen
        this.descripcion = descripcion
        this.cantidadActual = cantidadActual
        this.stockMinimo = stockMinimo
        this.stockMaximo = stockMaximo
        this.precioCompra = precioCompra
        this.precioVenta = precioVenta
    }

    /*Constructor de la clase Producto que solo recibe un nombre.*/
    constructor(nombreProducto: String){
        this.nombreProducto = nombreProducto
    }

    /*Constructor vació de la clase Producto.*/
    constructor(){}

}