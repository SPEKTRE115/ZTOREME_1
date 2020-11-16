package com.example.ztoreme_1.productos

import java.io.Serializable

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

    constructor(nombreProducto: String){
        this.nombreProducto = nombreProducto
    }

    constructor(){}

}