package com.elvin.e_commerce.fragments.shopping

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.elvin.e_commerce.R
import com.elvin.e_commerce.adapters.CartProductsAdapter
import com.elvin.e_commerce.databinding.FragmentCartBinding
import com.elvin.e_commerce.utils.Resource
import com.elvin.e_commerce.utils.VerticalItemDecoration
import com.elvin.e_commerce.viewmodel.CartViewModel
import kotlinx.coroutines.flow.collectLatest


class CartFragment: Fragment(R.layout.fragment_cart) {
    private lateinit var binding: FragmentCartBinding
    private val cartAdapter by lazy { CartProductsAdapter() }
    private val viewModel by activityViewModels<CartViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentCartBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupCartRv()

        lifecycleScope.launchWhenStarted {
            viewModel.productsPrice.collectLatest { price ->
                price.let {
                    binding.tvTotalPrice.text = "$ $price"
                }
            }
        }

        lifecycleScope.launchWhenStarted {
            viewModel.cartProducts.collectLatest {
                when (it) {
                    is Resource.Loading -> {
                        binding.progressbarCart.visibility = View.VISIBLE
                    }
                    is Resource.Success -> {
                        binding.progressbarCart.visibility = View.INVISIBLE
                        if(it.data!!.isEmpty()) {
                            showEmptyBox()
                            hideOtherViews()
                        }
                        else{
                            hideEmptyBox()
                            showOtherViews()
                            cartAdapter.differ.submitList(it.data)
                        }

                    }
                    is Resource.Error -> {
                        binding.progressbarCart.visibility = View.INVISIBLE
                        Toast.makeText(requireContext(), it.message.toString(), Toast.LENGTH_SHORT).show()
                    }
                    else -> Unit
                }
            }
        }
    }

    private fun showOtherViews() {
        binding.apply {
            totalBoxContainer.visibility = View.VISIBLE
            buttonCheckout.visibility = View.VISIBLE
            rvCart.visibility = View.VISIBLE
        }
    }

    private fun hideOtherViews() {
        binding.apply {
            totalBoxContainer.visibility = View.GONE
            buttonCheckout.visibility = View.GONE
            rvCart.visibility = View.GONE
        }
    }

    private fun hideEmptyBox() {
        binding.layoutCartEmpty.visibility = View.GONE
    }

    private fun showEmptyBox() {
        binding.layoutCartEmpty.visibility = View.VISIBLE

    }

    private fun setupCartRv() {
        binding.rvCart.apply {
            layoutManager = LinearLayoutManager(requireContext(), RecyclerView.VERTICAL, false)
            adapter = cartAdapter
            addItemDecoration(VerticalItemDecoration())
        }
    }

}