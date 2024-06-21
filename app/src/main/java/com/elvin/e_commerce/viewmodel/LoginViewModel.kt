package com.elvin.e_commerce.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.elvin.e_commerce.utils.Resource
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import java.lang.reflect.Constructor
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val firebaseAuth: FirebaseAuth
): ViewModel(){
    private val _login = MutableSharedFlow<Resource<String>>()
    val login = _login.asSharedFlow()

    private val _resetPassword = MutableSharedFlow<Resource<String>>()
    val resetPassword = _resetPassword.asSharedFlow()

    fun login(email: String, password: String){
        viewModelScope.launch { _login.emit(Resource.Loading()) }
        firebaseAuth.signInWithEmailAndPassword(
            email, password
        ).addOnSuccessListener {
            viewModelScope.launch {
                it.user?.let{
                    _login.emit(Resource.Success(it.toString()))
                }
            }
        }.addOnFailureListener {
            viewModelScope.launch {
                _login.emit(Resource.Error(it.message.toString()))
            }
        }
    }

    fun resetPassword(email: String){
        viewModelScope.launch {
            _resetPassword.emit(Resource.Loading())
        }
        firebaseAuth
            .sendPasswordResetEmail(email)
            .addOnSuccessListener {
                viewModelScope.launch{
                    _resetPassword.emit(Resource.Success(email))
                }
            }.addOnFailureListener {
                viewModelScope.launch{
                    _resetPassword.emit(Resource.Error(it.message.toString()))
                }
            }
    }

    fun isUserSignedIn() : Boolean {
        if (FirebaseAuth.getInstance().currentUser?.uid != null)
            return true
        return false

    }


}