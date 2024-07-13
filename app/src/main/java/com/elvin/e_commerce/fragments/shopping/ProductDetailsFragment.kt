package com.elvin.e_commerce.fragments.shopping

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.elvin.e_commerce.R
import com.elvin.e_commerce.activities.ShoppingActivity
import com.elvin.e_commerce.adapters.ColorsAdapter
import com.elvin.e_commerce.adapters.SizesAdapter
import com.elvin.e_commerce.adapters.ViewPagerImages
import com.elvin.e_commerce.data.CartProduct
import com.elvin.e_commerce.databinding.FragmentProductDetailsBinding
import com.elvin.e_commerce.utils.Resource
import com.elvin.e_commerce.utils.hideBottomNavigatonView
import com.elvin.e_commerce.viewmodel.CartDetailsViewModel
import com.google.android.material.bottomnavigation.BottomNavigationView
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest

@AndroidEntryPoint
class ProductDetailsFragment: Fragment(R.layout.fragment_product_details) {
    private val args by navArgs<ProductDetailsFragmentArgs>()
    private lateinit var binding: FragmentProductDetailsBinding
    private val viewPagerAdapter by lazy { ViewPagerImages() }
    private val sizesAdapter by lazy { SizesAdapter() }
    private val colorsAdapter by lazy { ColorsAdapter() }
    private var selectedSize: String? = null
    private var selectedColor: Int? = null
    private val viewModel by viewModels<CartDetailsViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        hideBottomNavigatonView()
        binding = FragmentProductDetailsBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val product = args.product

        setupViewPager()
        setupSizesRv()
        setupColorsRv()

        binding.imgClose.setOnClickListener {
            findNavController().navigateUp()
        }

        sizesAdapter.onItemClick = {
            selectedSize = it
        }

        colorsAdapter.onItemClick = {
            selectedColor = it
        }

        binding.buttonAddToCard.setOnClickListener{
            viewModel.addUpdateCartProduct(CartProduct(product, 1, selectedSize, selectedColor))
        }

        lifecycleScope.launchWhenStarted {
            viewModel.cartProduct.collectLatest {
                when(it){
                    is Resource.Loading -> {
                        binding.buttonAddToCard.startAnimation()
                    }
                    is Resource.Success -> {
                        binding.buttonAddToCard.revertAnimation()
                        binding.buttonAddToCard.setBackgroundColor(resources.getColor(R.color.g_blue_gray200))
                    }
                    is Resource.Error -> {
                        binding.buttonAddToCard.revertAnimation()
                        Toast.makeText(requireContext(), it.message, Toast.LENGTH_SHORT).show()
                    }
                    else -> Unit
                }
            }
        }

        binding.apply {
            tvProductName.text = product.name
            tvProductPrice.text = "$ ${product.price}"
            tvProductDesc.text = product.description

            if (product.colors.isNullOrEmpty())
                tvProductColor.visibility = View.GONE

            if (product.sizes.isNullOrEmpty())
                tvProductSize.visibility = View.GONE
        }

        viewPagerAdapter.differ.submitList(product.images)
        product.sizes?.let { sizesAdapter.differ.submitList(it) }
        product.colors?.let { colorsAdapter.differ.submitList(it) }

    }

    private fun setupSizesRv() {
        binding.rvSize.apply {
            adapter = sizesAdapter
            layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        }
    }

    private fun setupColorsRv() {
        binding.rvColors.apply {
            adapter = colorsAdapter
            layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        }
    }

    private fun setupViewPager() {
        binding.viewPagerProduct.apply {
            adapter = viewPagerAdapter
        }
    }
}