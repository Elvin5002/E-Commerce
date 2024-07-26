package com.elvin.e_commerce.data

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class CartProduct(
    val product: Product,
    val quantity: Int,
    val selectedSize: String? = null,
    val selectedColor: Int? = null
): Parcelable {
    constructor(): this(Product(), 1, null, null)
}
