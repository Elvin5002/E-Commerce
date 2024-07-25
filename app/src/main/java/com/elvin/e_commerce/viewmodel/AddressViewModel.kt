package com.elvin.e_commerce.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.elvin.e_commerce.data.Address
import com.elvin.e_commerce.utils.Resource
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AddressViewModel @Inject constructor(
    private val firestore: FirebaseFirestore,
    private val auth: FirebaseAuth
):ViewModel() {

    private val _addNewAddress = MutableStateFlow<Resource<Address>>(Resource.Unspecified())
    val addNewAddress = _addNewAddress.asStateFlow()

    private val _error = MutableSharedFlow<String>()
    val error = _error.asSharedFlow()

     fun addAddress(address: Address){
        viewModelScope.launch {
            _addNewAddress.emit(Resource.Loading())
        }
        val validateInputs = validateInputs(address)
        if (validateInputs) {
            firestore.collection("user").document(auth.uid!!).collection("address").document()
                .set(address).addOnSuccessListener {
                    viewModelScope.launch {
                        _addNewAddress.emit(Resource.Success(address))
                    }

                }.addOnFailureListener {
                    viewModelScope.launch {
                        _addNewAddress.emit(Resource.Error(it.message.toString()))
                    }

                }
        }else{
            viewModelScope.launch {
                _error.emit("Please fill all fields")
            }
        }
    }

    private fun validateInputs(address: Address): Boolean {
        return address.addressTitle.trim().isNotEmpty() && address.fullName.trim().isNotEmpty()
                && address.street.trim().isNotEmpty() && address.city.trim().isNotEmpty() &&
                address.state.trim().isNotEmpty() && address.phone.trim().isNotEmpty()
    }
}