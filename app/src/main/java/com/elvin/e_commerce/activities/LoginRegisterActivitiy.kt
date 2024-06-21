package com.elvin.e_commerce.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
import androidx.lifecycle.ViewModelProvider
import com.elvin.e_commerce.R
import com.elvin.e_commerce.viewmodel.LoginViewModel
import com.elvin.e_commerce.viewmodel.auth.AuthViewModel
import com.elvin.e_commerce.viewmodel.auth.ViewModelProviderFactory
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class LoginRegisterActivitiy : AppCompatActivity() {

    val viewModel by lazy {
        val auth = FirebaseAuth.getInstance()
        val db = FirebaseFirestore.getInstance()
        val viewModelFactory = ViewModelProviderFactory(auth, db)
        ViewModelProvider(this,viewModelFactory)[AuthViewModel::class.java]
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login_register)
    }
}