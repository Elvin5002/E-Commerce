package com.elvin.e_commerce.fragments.shopping

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.elvin.e_commerce.R
import com.elvin.e_commerce.activities.ShoppingActivity
import com.elvin.e_commerce.adapters.ColorsAdapter
import com.elvin.e_commerce.adapters.SizesAdapter
import com.elvin.e_commerce.adapters.ViewPagerImages
import com.elvin.e_commerce.databinding.FragmentProductDetailsBinding
import com.elvin.e_commerce.utils.hideBottomNavigatonView
import com.google.android.material.bottomnavigation.BottomNavigationView

class ProductDetailsFragment: Fragment(R.layout.fragment_product_details) {
    private val args by navArgs<ProductDetailsFragmentArgs>()
    private lateinit var binding: FragmentProductDetailsBinding
    private val viewPagerAdapter by lazy { ViewPagerImages() }
    private val sizesAdapter by lazy { SizesAdapter() }
    private val colorsAdapter by lazy { ColorsAdapter() }

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