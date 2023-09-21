package com.elvin.e_commerce.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.elvin.e_commerce.R
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class LoginRegisterActivitiy : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login_register)
    }
}