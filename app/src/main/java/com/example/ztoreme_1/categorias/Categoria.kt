package com.example.ztoreme_1.categorias

import java.io.Serializable

class Categoria : Serializable {

    var idCategoria : Int = 0
    var nombreCategoria : String = ""

    constructor(nombreCategoria : String){
        this.nombreCategoria = nombreCategoria
    }

    constructor(){ }
}