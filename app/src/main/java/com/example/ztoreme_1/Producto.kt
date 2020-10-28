package com.example.ztoreme_1

class Producto {

    var idProducto : Int = 0
    var nombreProducto : String = ""
    var imagen : String = ""
    var descripcion : String = ""
    var cantidadActual : Int = 0
    var stockMinimo : Int = 0
    var stockMaximo  : Int = 0
    var precioCompra  : Int = 0
    var precioVenta : Int = 0

    constructor(nombreProducto : String, imagen : String, descripcion : String, cantidadActual : Int,
                stockMinimo : Int, stockMaximo : Int, precioCompra : Int, precioVenta : Int){
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