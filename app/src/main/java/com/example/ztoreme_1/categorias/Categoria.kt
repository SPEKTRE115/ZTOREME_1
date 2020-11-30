package com.example.ztoreme_1.categorias

import java.io.Serializable

/*
* Modelo para la creacion de las categorias.
* idCategoria = atributo de tipo Int que es el identificador unico de una categoria.
* nombreCategoria = atributo de tipo String que es el nombre de la categoria.
* */
class Categoria : Serializable {

    var idCategoria : Int = 0
    var nombreCategoria : String = ""

    /*
    * Contructor de la categoria para crear un objecto de tipo Categoria
    * Su parametro es : nombreCategoria de tipo Stirng para establecer el nombre de la categoria a
    * registrar
    * */
    constructor(nombreCategoria : String){
        this.nombreCategoria = nombreCategoria
    }

    /*
    * Contructor vacio en caso de no registrar ninguna categoria.
    * */
    constructor(){ }
}