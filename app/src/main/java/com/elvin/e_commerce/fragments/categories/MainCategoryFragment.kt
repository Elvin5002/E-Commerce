package com.elvin.e_commerce.fragments.categories

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.widget.NestedScrollView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.elvin.e_commerce.R
import com.elvin.e_commerce.adapters.BestDealsAdapter
import com.elvin.e_commerce.adapters.BestProductAdapter
import com.elvin.e_commerce.adapters.SpecialProductsAdapter
import com.elvin.e_commerce.databinding.FragmentMainCategoryBinding
import com.elvin.e_commerce.utils.Resource
import com.elvin.e_commerce.viewmodel.MainCategoryViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
private val TAG = "MainCategoryFragment"

@AndroidEntryPoint
class MainCategoryFragment: Fragment(R.layout.fragment_main_category){

    private lateinit var binding: FragmentMainCategoryBinding
    private lateinit var specialProductsAdapter: SpecialProductsAdapter
    private lateinit var bestDealsAdapter: BestDealsAdapter
    private lateinit var bestProductAdapter: BestProductAdapter
    private val viewModel by viewModels<MainCategoryViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentMainCategoryBinding.inflate(inflater)
        return binding.root
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupSpecialProductsRv()
        setupBestDealsRv()
        setupBestProductsRv()

        lifecycleScope.launchWhenStarted {
            viewModel.specialProducts.collectLatest {
                when (it) {
                    is Resource.Loading -> {
                        showLoading()
                    }
                    is Resource.Success -> {
                        specialProductsAdapter.differ.submitList(it.data)
                        hideLoading()
                    }
                    is Resource.Error -> {
                        hideLoading()
                        Log.e(TAG, it.message.toString())
                        Toast.makeText(requireContext(), it.message, Toast.LENGTH_SHORT).show()
                    }
                    else -> Unit
                }
            }
        }

        lifecycleScope.launchWhenStarted {
            viewModel.bestDeals.collectLatest {
                when (it) {
                    is Resource.Loading -> {
                        showLoading()
                    }
                    is Resource.Success -> {
                        bestDealsAdapter.differ.submitList(it.data)
                        hideLoading()
                    }
                    is Resource.Error -> {
                        hideLoading()
                        Log.e(TAG, it.message.toString())
                        Toast.makeText(requireContext(), it.message, Toast.LENGTH_SHORT).show()
                    }
                    else -> Unit
                }
            }
        }

        lifecycleScope.launchWhenStarted {
            viewModel.bestProducts.collectLatest {
                when (it) {
                    is Resource.Loading -> {
                        binding.bestProductsProgressBar.visibility = View.VISIBLE
                    }
                    is Resource.Success -> {
                        bestProductAdapter.differ.submitList(it.data)
                        binding.bestProductsProgressBar.visibility = View.GONE

                    }
                    is Resource.Error -> {
                        Log.e(TAG, it.message.toString())
                        Toast.makeText(requireContext(), it.message, Toast.LENGTH_SHORT).show()
                        binding.bestProductsProgressBar.visibility = View.GONE
                    }
                    else -> Unit
                }
            }
        }

        binding.nestedScrollViewMain.setOnScrollChangeListener(NestedScrollView.OnScrollChangeListener { v, _, scrollY, _, _ ->
            if(v.getChildAt(0).bottom <= v.height + scrollY){
                viewModel.fetchBestProducts()
            }
        })
        
    }

    private fun hideLoading() {
        binding.mainCategoryProgressBar.visibility = View.GONE
    }

    private fun showLoading() {
        binding.mainCategoryProgressBar.visibility = View.VISIBLE
    }

    private fun setupSpecialProductsRv() {
        specialProductsAdapter = SpecialProductsAdapter()
        binding.rvSpecialProducts.apply {
            layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
            adapter = specialProductsAdapter
        }
    }

    private fun setupBestProductsRv() {
        bestProductAdapter = BestProductAdapter()
        binding.rvBestProducts.apply {
            layoutManager = GridLayoutManager(requireContext(), 2, GridLayoutManager.VERTICAL, false)
            adapter = bestProductAdapter
        }
    }

    private fun setupBestDealsRv(){
        bestDealsAdapter = BestDealsAdapter()
        binding.rvBestDealProducts.apply {
            layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
            adapter = bestDealsAdapter
        }
    }

}