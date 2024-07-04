package com.elvin.e_commerce.fragments.applunch

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.elvin.e_commerce.R
import com.elvin.e_commerce.activities.LoginRegisterActivitiy
import com.elvin.e_commerce.activities.ShoppingActivity
import com.elvin.e_commerce.databinding.FragmentSplashScreenBinding
import com.elvin.e_commerce.viewmodel.LoginViewModel

class SplashScreen : Fragment() {

    private lateinit var binding: FragmentSplashScreenBinding
    //private val viewModel by viewModels<LoginViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentSplashScreenBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val viewModel = (activity as LoginRegisterActivitiy).viewModel

        val isUserSignedIn = viewModel.isUserSignedIn()
        println("isUserSignedIn: $isUserSignedIn")
        if (isUserSignedIn) {
            val intent = Intent(requireActivity(), ShoppingActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)
            Handler().postDelayed({
                startActivity(intent)
            }, 1500)
        } else
            Handler().postDelayed({
                findNavController().navigate(R.id.action_splashScreen_to_introductionFragment)
            }, 1500)

    }

}