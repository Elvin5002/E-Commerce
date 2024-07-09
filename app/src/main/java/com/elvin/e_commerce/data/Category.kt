package com.elvin.e_commerce.data

sealed class Category(val category: String) {
    object Cupboard : Category("Cupboard")
    object Table : Category("Table")
    object Chair : Category("Chair")
    object Furniture : Category("Furniture")
    object Accessories : Category("Accessories")

}