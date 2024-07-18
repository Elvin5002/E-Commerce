package com.elvin.e_commerce.helper

fun Float?.getProductPrice(price: Float): Float {
    if (this == null) return price
    val remainingPercentage = 1f - this
    val priceAfterOffer = price * remainingPercentage

    return priceAfterOffer
}