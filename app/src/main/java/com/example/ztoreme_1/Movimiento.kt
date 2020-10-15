package com.example.ztoreme_1

class Movimiento {

    var fechaRegistro : String = ""
    var idProducto : Int = 0
    var cantidadMov : Int = 0
    var entrada : Int = 0 // 0 para false 1 para true

    constructor(fechaRegistro : String, idProducto: Int, cantidadMov : Int, entrada : Int){
        this.fechaRegistro = fechaRegistro
        this.idProducto = idProducto
        this.cantidadMov = cantidadMov
        this.entrada = entrada
    }

    constructor(){}

}