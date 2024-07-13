package com.elvin.e_commerce.data

data class CartProduct(
    val product: Product,
    val quantity: Int,
    val selectedSize: String? = null,
    val selectedColor: Int? = null
){
    constructor(): this(Product(), 1, null, null)
}
