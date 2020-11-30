package com.example.ztoreme_1

/*
Clase que srive para crear objetos de tipo movimiento
y permite manipular la información de los movmientos
sin tener que hacer consultas a la base de datos.
 */
class Movimiento {

    var fechaRegistro : String = ""
    var idProducto : Int = 0
    var cantidadMov : Int = 0
    var entrada : Int = 0 // 0 para false 1 para true

    /* Constructor de la clase Movimiento que recibe todos los
    datso necearios para crear movmientos.*/
    constructor(fechaRegistro : String, idProducto: Int, cantidadMov : Int, entrada : Int){
        this.fechaRegistro = fechaRegistro
        this.idProducto = idProducto
        this.cantidadMov = cantidadMov
        this.entrada = entrada
    }

    /*Constructoir vació de la clase Movimiento*/
    constructor(){}

}