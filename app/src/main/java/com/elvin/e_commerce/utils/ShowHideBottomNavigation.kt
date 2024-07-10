package com.elvin.e_commerce.utils

import android.view.View
import androidx.fragment.app.Fragment
import com.elvin.e_commerce.R
import com.elvin.e_commerce.activities.ShoppingActivity
import com.google.android.material.bottomnavigation.BottomNavigationView

fun Fragment.showBottomNavigatonView(){
    val bottomNavView = (activity as ShoppingActivity).findViewById<BottomNavigationView>(
        R.id.bottomNavigation
    )
    bottomNavView.visibility = View.VISIBLE
}

fun Fragment.hideBottomNavigatonView(){
    val bottomNavView = (activity as ShoppingActivity).findViewById<BottomNavigationView>(
        R.id.bottomNavigation
    )
    bottomNavView.visibility = View.GONE
}