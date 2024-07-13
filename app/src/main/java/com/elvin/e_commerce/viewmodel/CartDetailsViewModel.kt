package com.elvin.e_commerce.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.elvin.e_commerce.data.CartProduct
import com.elvin.e_commerce.firebase.FirebaseCommon
import com.elvin.e_commerce.utils.Resource
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CartDetailsViewModel @Inject constructor(
    private val firestore: FirebaseFirestore,
    val auth: FirebaseAuth,
    private val firebaseCommon: FirebaseCommon
) : ViewModel() {

    private val _cartProducts = MutableStateFlow<Resource<CartProduct>>(Resource.Unspecified())
    val cartProduct = _cartProducts.asStateFlow()

    fun addUpdateCartProduct(cartProduct: CartProduct) {
        viewModelScope.launch {
            _cartProducts.emit(Resource.Loading())
        }
        firestore.collection("user").document(auth.uid!!).collection("cart")
            .whereEqualTo("product.id", cartProduct.product.id).get()
            .addOnSuccessListener {
                it.documents.let {
                    if (it.isEmpty()) {
                        addNewProduct(cartProduct)
                    } else{
                        val product = it.first().toObject(CartProduct::class.java)
                        if (product == cartProduct){
                            val documentId = it.first().id
                            increaseQuantity(documentId, cartProduct)
                        }else{
                            addNewProduct(cartProduct)
                        }
                    }

                }
            }
            .addOnFailureListener{
                viewModelScope.launch {
                    _cartProducts.emit(Resource.Error(it.message.toString()))
                }
            }
    }

    private fun addNewProduct(cartProduct: CartProduct){
        firebaseCommon.addProductToCart(cartProduct){ addedProduct, e ->
            viewModelScope.launch {
                if (e == null)
                    _cartProducts.emit(Resource.Success(addedProduct!!))
                else
                    _cartProducts.emit(Resource.Error(e.message.toString()))
            }

        }
    }

    private fun increaseQuantity(documentId: String, cartProduct: CartProduct){
        firebaseCommon.increaseQuantity(documentId) { _, e ->
            viewModelScope.launch {
                if (e == null)
                    _cartProducts.emit(Resource.Success(cartProduct))
                else
                    _cartProducts.emit(Resource.Error(e.message.toString()))
            }
        }
    }

}